package com.mobiarmy.portal;

import static org.junit.jupiter.api.Assertions.*;

import com.mobiarmy.TestDb;
import com.mobiarmy.TestDb.MutableClock;
import com.mobiarmy.server.Accounts;
import com.mobiarmy.server.PasswordHash;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** contracts/portal-api.md, exercised over HTTP against an in-process server and a SQLite copy of dbarmy.sql. */
class PortalServerTest {
    private final MutableClock clock = new MutableClock();
    private final AtomicInteger online = new AtomicInteger(3);
    private final HttpClient client = HttpClient.newHttpClient();
    private PortalServer server;
    private String base;
    private String host;

    private static final String SECRET = "portal-test-secret-portal-test-secret";

    private PortalServer newServer() {
        PortalTokens tokens = new PortalTokens(PortalTokens.secretFrom(SECRET), Duration.ofMinutes(30), clock,
                id -> { Accounts.Row a = Accounts.findById(id); return a == null ? null : a.password(); });
        return new PortalServer(tokens, new LoginThrottle(clock), new Ranking(clock), online::get, clock, false);
    }

    @BeforeEach
    void start() throws Exception {
        TestDb.fresh();
        server = newServer();
        server.start(0);
        host = "localhost:" + server.port();
        base = "http://" + host;
    }

    @Test
    void loginSurvivesAServerRestart() throws Exception {
        String c = cookie(post("/api/login", "{\"username\":\"a\",\"password\":\"1\"}", null));
        server.stop();
        server = newServer();
        server.start(0);
        base = "http://localhost:" + server.port();
        assertEquals("{\"username\":\"a\"}", get("/api/me", c).body());
    }

    @Test
    void sessionCookieIsRenewedOnUseAndTimesOutWhenIdle() throws Exception {
        String c = cookie(post("/api/login", "{\"username\":\"a\",\"password\":\"1\"}", null));
        clock.advance(Duration.ofMinutes(6));
        HttpResponse<String> r = get("/api/me", c);
        assertEquals(200, r.statusCode());
        String renewed = cookie(r);
        assertNotEquals(c, renewed);
        clock.advance(Duration.ofMinutes(28));
        assertEquals(401, get("/api/me", c).statusCode(), "old cookie idle 34 min");
        assertEquals(200, get("/api/me", renewed).statusCode());
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    private HttpResponse<String> get(String path, String cookie) throws Exception {
        HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(base + path)).GET();
        if (cookie != null) b.header("Cookie", cookie);
        return client.send(b.build(), HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> post(String path, String json, String cookie) throws Exception {
        HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(base + path))
                .header("Content-Type", "application/json")
                .header("Origin", base)
                .POST(HttpRequest.BodyPublishers.ofString(json));
        if (cookie != null) b.header("Cookie", cookie);
        return client.send(b.build(), HttpResponse.BodyHandlers.ofString());
    }

    /** "portal_session=<token>" from Set-Cookie. */
    private static String cookie(HttpResponse<String> r) {
        String set = r.headers().firstValue("Set-Cookie").orElseThrow();
        return set.substring(0, set.indexOf(';'));
    }

    private static void assertNoSecrets(String body) {
        assertFalse(body.contains("password"), body);
        assertFalse(body.contains("pbkdf2"), body);
        assertFalse(body.contains("portal_session"), body);
    }

    // --- generic rules (T004) ---

    @Test
    void unknownPathWrongMethodAndCsrfGuard() throws Exception {
        assertEquals(404, get("/api/nope", null).statusCode());
        assertEquals("{\"error\":\"not_found\"}", get("/api/nope", null).body());
        assertEquals(405, get("/api/login", null).statusCode());

        HttpRequest noType = HttpRequest.newBuilder(URI.create(base + "/api/login")).header("Origin", base)
                .POST(HttpRequest.BodyPublishers.ofString("{}")).build();
        assertEquals(403, client.send(noType, HttpResponse.BodyHandlers.ofString()).statusCode());

        HttpRequest noOrigin = HttpRequest.newBuilder(URI.create(base + "/api/login"))
                .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString("{}")).build();
        assertEquals(403, client.send(noOrigin, HttpResponse.BodyHandlers.ofString()).statusCode());

        HttpRequest evil = HttpRequest.newBuilder(URI.create(base + "/api/login")).header("Origin", "http://evil.example")
                .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString("{}")).build();
        HttpResponse<String> r = client.send(evil, HttpResponse.BodyHandlers.ofString());
        assertEquals(403, r.statusCode());
        assertEquals("{\"error\":\"forbidden\"}", r.body());
    }

    @Test
    void malformedOrOversizedBodies() throws Exception {
        HttpResponse<String> r = post("/api/login", "{not json", null);
        assertEquals(400, r.statusCode());
        assertEquals("{\"error\":\"bad_request\"}", r.body());
        assertEquals(400, post("/api/login", "[1,2]", null).statusCode());
        assertEquals(400, post("/api/login", "{\"username\":\"" + "a".repeat(5000) + "\"}", null).statusCode());
    }

    // --- accounts (T022) ---

    @Test
    void registerThenLoginEverywhere() throws Exception {
        HttpResponse<String> r = post("/api/register", "{\"username\":\" TestUser1 \",\"password\":\"abc123\"}", null);
        assertEquals(201, r.statusCode(), r.body());
        assertEquals("{\"username\":\"testuser1\"}", r.body());
        String set = r.headers().firstValue("Set-Cookie").orElseThrow();
        assertTrue(set.contains("HttpOnly") && set.contains("SameSite=Strict") && set.contains("Path=/api"), set);
        assertFalse(set.contains("Secure"));
        String c = cookie(r);

        assertEquals("{\"username\":\"testuser1\"}", get("/api/me", c).body());
        // Same credentials as the game login uses (FR-016).
        assertTrue(Accounts.checkPassword(Accounts.find("testuser1").password(), "abc123"));
        assertFalse(PasswordHash.isLegacy(Accounts.find("testuser1").password()));

        HttpResponse<String> dup = post("/api/register", "{\"username\":\"testuser1\",\"password\":\"abc123\"}", null);
        assertEquals(409, dup.statusCode());
        assertTrue(dup.body().contains("username_taken"));

        HttpResponse<String> shortName = post("/api/register", "{\"username\":\"ab\",\"password\":\"abc123\"}", null);
        assertEquals(400, shortName.statusCode());
        assertTrue(shortName.body().contains("invalid_username") && shortName.body().contains("3–20"), shortName.body());
        assertTrue(post("/api/register", "{\"username\":\"abcd\",\"password\":\"12345\"}", null).body().contains("invalid_password"));

        assertEquals(204, post("/api/logout", "{}", c).statusCode());
        assertEquals(401, get("/api/me", c).statusCode());
    }

    @Test
    void loginFailuresAreIdenticalAndThrottled() throws Exception {
        HttpResponse<String> ok = post("/api/login", "{\"username\":\"nguoiduatin\",\"password\":\"3\"}", null);
        assertEquals(200, ok.statusCode(), "existing account works without signing up");
        assertNoSecrets(ok.body());

        HttpResponse<String> wrong = post("/api/login", "{\"username\":\"nguoiduatin\",\"password\":\"4\"}", null);
        HttpResponse<String> missing = post("/api/login", "{\"username\":\"nosuchuser\",\"password\":\"4\"}", null);
        assertEquals(401, wrong.statusCode());
        assertEquals(wrong.statusCode(), missing.statusCode());
        assertEquals(wrong.body(), missing.body());

        for (int i = 0; i < 4; i++) post("/api/login", "{\"username\":\"nguoiduatin\",\"password\":\"4\"}", null);
        HttpResponse<String> locked = post("/api/login", "{\"username\":\"nguoiduatin\",\"password\":\"3\"}", null);
        assertEquals(429, locked.statusCode());
        assertTrue(locked.body().contains("too_many"));
        assertEquals(200, post("/api/login", "{\"username\":\"a\",\"password\":\"1\"}", null).statusCode(), "other names unaffected");
    }

    @Test
    void legacyPasswordIsRehashedOnPortalLogin() throws Exception {
        TestDb.exec("INSERT INTO account (id, username, password) VALUES (60, 'oldie', 'pw60')");
        assertEquals(200, post("/api/login", "{\"username\":\"oldie\",\"password\":\"pw60\"}", null).statusCode());
        assertFalse(PasswordHash.isLegacy(Accounts.find("oldie").password()));
        assertEquals(200, post("/api/login", "{\"username\":\"oldie\",\"password\":\"pw60\"}", null).statusCode());
    }

    @Test
    void passwordChange() throws Exception {
        String c1 = cookie(post("/api/register", "{\"username\":\"testuser1\",\"password\":\"abc123\"}", null));
        String c2 = cookie(post("/api/login", "{\"username\":\"testuser1\",\"password\":\"abc123\"}", null));

        assertEquals(401, post("/api/password", "{\"currentPassword\":\"abc123\",\"newPassword\":\"xyz789\"}", null).statusCode());
        HttpResponse<String> bad = post("/api/password", "{\"currentPassword\":\"nope12\",\"newPassword\":\"xyz789\"}", c1);
        assertEquals(401, bad.statusCode());
        assertTrue(bad.body().contains("bad_credentials"));
        assertEquals(400, post("/api/password", "{\"currentPassword\":\"abc123\",\"newPassword\":\"x\"}", c1).statusCode());

        HttpResponse<String> changed = post("/api/password", "{\"currentPassword\":\"abc123\",\"newPassword\":\"xyz789\"}", c1);
        assertEquals(204, changed.statusCode());
        String c1new = cookie(changed);
        String stored = Accounts.find("testuser1").password();
        assertTrue(Accounts.checkPassword(stored, "xyz789"));
        assertFalse(Accounts.checkPassword(stored, "abc123"));
        assertEquals(200, get("/api/me", c1new).statusCode(), "current session kept, with a new cookie");
        assertEquals(401, get("/api/me", c1).statusCode(), "its old cookie is tied to the old password");
        assertEquals(401, get("/api/me", c2).statusCode(), "other sessions ended");
    }

    // --- game ticket (research D15) ---

    @Test
    void ticketNeedsAPortalSessionAndLogsInOnce() throws Exception {
        assertEquals(401, post("/api/mobiarmy/ticket", "{}", null).statusCode());
        assertEquals(405, get("/api/mobiarmy/ticket", null).statusCode());
        String c = cookie(post("/api/login", "{\"username\":\"nguoiduatin\",\"password\":\"3\"}", null));
        HttpResponse<String> r = post("/api/mobiarmy/ticket", "{}", c);
        assertEquals(200, r.statusCode());
        assertTrue(r.body().startsWith("{\"username\":\"nguoiduatin\",\"ticket\":\""), r.body());
        String ticket = r.body().replaceAll(".*\"ticket\":\"([A-Za-z0-9]+)\".*", "$1");
        Accounts.Row acc = Accounts.find("nguoiduatin");
        assertTrue(Accounts.checkLogin(acc.id(), acc.password(), ticket));
        assertFalse(Accounts.checkLogin(acc.id(), acc.password(), ticket));
    }

    // --- live info (T041) ---

    @Test
    void statusAndRanking() throws Exception {
        HttpResponse<String> s = get("/api/mobiarmy/status", null);
        assertEquals(200, s.statusCode());
        assertEquals("{\"online\":3,\"takenAt\":\"2026-11-22T10:00:00Z\"}", s.body());
        online.set(5);
        assertTrue(get("/api/mobiarmy/status", null).body().contains("\"online\":5"));

        HttpResponse<String> rk = get("/api/mobiarmy/ranking", null);
        assertEquals(200, rk.statusCode());
        assertEquals("{\"entries\":[{\"rank\":1,\"name\":\"a\",\"level\":1,\"exp\":0}],\"takenAt\":\"2026-11-22T10:00:00Z\"}", rk.body());
        assertNoSecrets(rk.body());
    }
}
