package com.mobiarmy.portal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiarmy.server.Accounts;
import com.mobiarmy.server.GameTickets;
import com.mobiarmy.server.PasswordHash;
import com.mobiarmy.server.Registration;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.IntSupplier;

/**
 * The portal's JSON API (specs/004-game-portal/contracts/portal-api.md), on the JDK's built-in HTTP
 * server. Listens on an internal port only; browsers reach it through nginx at /api/.
 */
public final class PortalServer {
    static final int MAX_BODY = 4096;
    static final String COOKIE = "portal_session";

    private static final ObjectMapper JSON = new ObjectMapper();
    /** Verified when the login name doesn't exist, so a miss costs as much as a wrong password. */
    private static final String DUMMY_HASH = PasswordHash.hash("khongtontai0");

    private final PortalTokens tokens;
    private final LoginThrottle throttle;
    private final Ranking ranking;
    private final IntSupplier onlineCount;
    private final Clock clock;
    private final boolean secureCookie;
    private HttpServer http;
    private ExecutorService executor;

    public PortalServer(PortalTokens tokens, LoginThrottle throttle, Ranking ranking,
                        IntSupplier onlineCount, Clock clock, boolean secureCookie) {
        this.tokens = tokens;
        this.throttle = throttle;
        this.ranking = ranking;
        this.onlineCount = onlineCount;
        this.clock = clock;
        this.secureCookie = secureCookie;
    }

    public void start(int port) throws IOException {
        http = HttpServer.create(new InetSocketAddress(port), 0);
        executor = Executors.newFixedThreadPool(8);
        http.setExecutor(executor);
        http.createContext("/", this::handle);
        http.start();
    }

    public int port() {
        return http.getAddress().getPort();
    }

    public void stop() {
        if (http != null) http.stop(0);
        if (executor != null) executor.shutdownNow();
    }

    /** A handled request ends by throwing this, carrying the response. */
    private static final class Reply extends Exception {
        final int status;
        final Object body;
        final String cookie;

        Reply(int status, Object body, String cookie) {
            super(null, null, false, false);
            this.status = status;
            this.body = body;
            this.cookie = cookie;
        }
    }

    private static Reply error(int status, String code, String message) {
        Map<String, String> b = new LinkedHashMap<>();
        b.put("error", code);
        if (message != null) b.put("message", message);
        return new Reply(status, b, null);
    }

    private static final String MSG_BAD_CREDENTIALS = "Sai tài khoản hoặc mật khẩu.";
    private static final String MSG_TOO_MANY = "Bạn thử quá nhiều lần, vui lòng đợi rồi thử lại.";

    private void handle(HttpExchange ex) throws IOException {
        Reply reply;
        try {
            route(ex);
            reply = error(500, "server_error", null); // route always throws a Reply
        } catch (Reply r) {
            reply = r;
        } catch (Exception e) {
            System.err.println("[Portal] " + ex.getRequestMethod() + " " + ex.getRequestURI().getPath() + " failed:");
            e.printStackTrace();
            reply = error(500, "server_error", "Lỗi máy chủ, vui lòng thử lại.");
        }
        send(ex, reply);
    }

    private void route(HttpExchange ex) throws Exception {
        String path = ex.getRequestURI().getPath();
        String method = ex.getRequestMethod();
        switch (path) {
            case "/api/register" -> { requirePost(ex); register(ex); }
            case "/api/login" -> { requirePost(ex); login(ex); }
            case "/api/logout" -> { requirePost(ex); logout(ex); }
            case "/api/password" -> { requirePost(ex); password(ex); }
            case "/api/me" -> { requireGet(method); me(ex); }
            case "/api/mobiarmy/status" -> { requireGet(method); status(); }
            case "/api/mobiarmy/ranking" -> { requireGet(method); ranking(); }
            case "/api/mobiarmy/ticket" -> { requirePost(ex); ticket(ex); }
            default -> throw error(404, "not_found", null);
        }
    }

    private static void requireGet(String method) throws Reply {
        if (!"GET".equals(method)) throw error(405, "method_not_allowed", null);
    }

    /** POST + JSON + same-origin (CSRF guard, contracts/portal-api.md). */
    private static void requirePost(HttpExchange ex) throws Reply {
        if (!"POST".equals(ex.getRequestMethod())) throw error(405, "method_not_allowed", null);
        String type = ex.getRequestHeaders().getFirst("Content-Type");
        String origin = ex.getRequestHeaders().getFirst("Origin");
        String host = ex.getRequestHeaders().getFirst("Host");
        if (type == null || !type.toLowerCase().startsWith("application/json") || origin == null || host == null) {
            throw error(403, "forbidden", null);
        }
        try {
            if (!host.equalsIgnoreCase(URI.create(origin).getRawAuthority())) throw error(403, "forbidden", null);
        } catch (IllegalArgumentException e) {
            throw error(403, "forbidden", null);
        }
    }

    private static JsonNode body(HttpExchange ex) throws Reply {
        byte[] raw;
        try (InputStream in = ex.getRequestBody()) {
            raw = in.readNBytes(MAX_BODY + 1);
        } catch (IOException e) {
            throw error(400, "bad_request", null);
        }
        if (raw.length > MAX_BODY) throw error(400, "bad_request", null);
        try {
            JsonNode n = JSON.readTree(raw);
            if (n == null || !n.isObject()) throw error(400, "bad_request", null);
            return n;
        } catch (IOException e) {
            throw error(400, "bad_request", null);
        }
    }

    private static String text(JsonNode body, String field) {
        JsonNode n = body.get(field);
        return n != null && n.isTextual() ? n.asText() : null;
    }

    private static String visitorIp(HttpExchange ex) {
        String ip = ex.getRequestHeaders().getFirst("X-Real-IP");
        return ip != null ? ip : ex.getRemoteAddress().getAddress().getHostAddress();
    }

    private static String cookieToken(HttpExchange ex) {
        String header = ex.getRequestHeaders().getFirst("Cookie");
        if (header == null) return null;
        for (String part : header.split(";")) {
            String p = part.trim();
            if (p.startsWith(COOKIE + "=")) return p.substring(COOKIE.length() + 1);
        }
        return null;
    }

    private String sessionCookie(String token) {
        return COOKIE + "=" + token + "; HttpOnly; SameSite=Strict; Path=/api; Max-Age="
                + PortalTokens.MAX_AGE.toSeconds() + (secureCookie ? "; Secure" : "");
    }

    private String clearedCookie() {
        return COOKIE + "=; HttpOnly; SameSite=Strict; Path=/api; Max-Age=0" + (secureCookie ? "; Secure" : "");
    }

    /** Attribute holding a renewed session cookie for this response (idle timeout slides on use). */
    private static final String RENEWED = "portal.renewedCookie";

    /** The verified session, or null; renews the cookie when the token is getting old. */
    private PortalTokens.Session session(HttpExchange ex) throws Exception {
        PortalTokens.Session s = tokens.verify(cookieToken(ex));
        if (s != null && s.refresh) ex.setAttribute(RENEWED, sessionCookie(tokens.reissue(s, null)));
        return s;
    }

    /** Token for a fresh login, bound to the password as stored now (after any re-hash). */
    private String loginCookie(int accountId) throws Exception {
        Accounts.Row acc = Accounts.findById(accountId);
        return sessionCookie(tokens.issue(acc.id(), acc.username(), acc.password()));
    }

    private static Map<String, Object> user(String username) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("username", username);
        return m;
    }

    // --- accounts (US2) ---

    private void register(HttpExchange ex) throws Exception {
        JsonNode b = body(ex);
        String name = Registration.normalize(text(b, "username"));
        String pass = text(b, "password");
        if (!Registration.isValidPortalUsername(name)) {
            throw error(400, "invalid_username", "Tên đăng nhập 3–20 ký tự, chỉ gồm chữ thường a–z và số.");
        }
        if (!Registration.isValidPortalPassword(pass)) {
            throw error(400, "invalid_password", "Mật khẩu 6–50 ký tự, chỉ gồm chữ và số.");
        }
        String ip = visitorIp(ex);
        if (!throttle.allowRegistration(ip)) throw error(429, "too_many", MSG_TOO_MANY);
        if (!Registration.create(name, pass)) {
            throw error(409, "username_taken", "Tên đăng nhập đã có người dùng.");
        }
        throttle.recordRegistration(ip);
        Accounts.Row acc = Accounts.find(name);
        System.out.println("[Portal] registered account " + acc.id() + " " + acc.username());
        throw new Reply(201, user(acc.username()), loginCookie(acc.id()));
    }

    private void login(HttpExchange ex) throws Exception {
        JsonNode b = body(ex);
        String name = Registration.normalize(text(b, "username"));
        String pass = text(b, "password");
        if (throttle.isLocked(name)) throw error(429, "too_many", MSG_TOO_MANY);
        Accounts.Row acc = name.isEmpty() || pass == null ? null : Accounts.find(name);
        boolean ok;
        if (acc == null) {
            PasswordHash.verify(pass == null ? "" : pass, DUMMY_HASH);
            ok = false;
        } else {
            ok = Accounts.checkPassword(acc.password(), pass);
        }
        if (!ok) {
            if (throttle.recordFailure(name)) {
                System.out.println("[Portal] login locked for name " + name + " from " + visitorIp(ex));
            }
            throw error(401, "bad_credentials", MSG_BAD_CREDENTIALS);
        }
        throttle.recordSuccess(name);
        Accounts.rehashIfLegacy(acc.id(), acc.password(), pass);
        throw new Reply(200, user(acc.username()), loginCookie(acc.id()));
    }

    private void logout(HttpExchange ex) throws Exception {
        tokens.revoke(tokens.verify(cookieToken(ex)));
        throw new Reply(204, null, clearedCookie());
    }

    private void me(HttpExchange ex) throws Exception {
        PortalTokens.Session s = session(ex);
        if (s == null) throw error(401, "not_logged_in", null);
        throw new Reply(200, user(s.username), null);
    }

    private void password(HttpExchange ex) throws Exception {
        PortalTokens.Session s = session(ex);
        if (s == null) throw error(401, "not_logged_in", null);
        JsonNode b = body(ex);
        String current = text(b, "currentPassword");
        String next = text(b, "newPassword");
        if (!Registration.isValidPortalPassword(next)) {
            throw error(400, "invalid_password", "Mật khẩu 6–50 ký tự, chỉ gồm chữ và số.");
        }
        String key = "#" + s.accountId;
        if (throttle.isLocked(key)) throw error(429, "too_many", MSG_TOO_MANY);
        Accounts.Row acc = Accounts.findById(s.accountId);
        if (acc == null || !Accounts.checkPassword(acc.password(), current)) {
            throttle.recordFailure(key);
            throw error(401, "bad_credentials", MSG_BAD_CREDENTIALS);
        }
        throttle.recordSuccess(key);
        String hash = PasswordHash.hash(next);
        Accounts.updatePassword(acc.id(), hash);
        // Tokens carry a fingerprint of the stored password: every other session is now invalid,
        // this one gets a token for the new password.
        System.out.println("[Portal] password changed for account " + acc.id());
        throw new Reply(204, null, sessionCookie(tokens.reissue(s, hash)));
    }

    /** One-time ticket the browser client logs in with, so the game doesn't ask again (research D15). */
    private void ticket(HttpExchange ex) throws Exception {
        PortalTokens.Session s = session(ex);
        if (s == null) throw error(401, "not_logged_in", null);
        Map<String, Object> m = user(s.username);
        m.put("ticket", GameTickets.issue(s.accountId));
        throw new Reply(200, m, null);
    }

    // --- live info (US4) ---

    private void status() throws Reply {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("online", onlineCount.getAsInt());
        m.put("takenAt", clock.instant().toString());
        throw new Reply(200, m, null);
    }

    private void ranking() throws Exception {
        Ranking.Snapshot snap = ranking.top();
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("entries", snap.entries());
        m.put("takenAt", snap.takenAt().toString());
        throw new Reply(200, m, null);
    }

    private static void send(HttpExchange ex, Reply r) throws IOException {
        String cookie = r.cookie != null ? r.cookie : (String) ex.getAttribute(RENEWED);
        if (cookie != null) ex.getResponseHeaders().add("Set-Cookie", cookie);
        ex.getResponseHeaders().set("Cache-Control", "no-store");
        if (r.body == null) {
            ex.sendResponseHeaders(r.status, -1);
        } else {
            byte[] out = JSON.writeValueAsBytes(r.body);
            ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            ex.sendResponseHeaders(r.status, out.length);
            try (OutputStream os = ex.getResponseBody()) {
                os.write(out);
            }
        }
        ex.close();
    }

    /** Wires the API to the running game server. */
    public static PortalServer forGameServer(String jwtSecret, int idleMinutes, boolean secureCookie, IntSupplier onlineCount) {
        Clock clock = Clock.systemUTC();
        PortalTokens tokens = new PortalTokens(PortalTokens.secretFrom(jwtSecret), java.time.Duration.ofMinutes(idleMinutes),
                clock, id -> { Accounts.Row a = Accounts.findById(id); return a == null ? null : a.password(); });
        return new PortalServer(tokens,
                new LoginThrottle(clock), new Ranking(clock), onlineCount, clock, secureCookie);
    }
}
