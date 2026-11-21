package com.mobiarmy.server.network;

import com.mobiarmy.server.service.CharacterService;
import com.mobiarmy.server.service.LoginService;
import com.mobiarmy.server.service.Ping;

public class MessageHandle implements IMessageHandle {

    private final Session session;

    public MessageHandle(Session session) {
        this.session = session;
    }

    @Override
    public void onMessage(Message m) {
        byte cmd = m.command;
        byte[] payload = (m == null) ? new byte[0] : m.getData();
        System.out.println("[Handler] recv cmd=" + (cmd & 0xff) + " (0x" + String.format("%02X", cmd & 0xff) + ") data_len=" + payload.length);
        try {
            switch (cmd) {
                // ===== Login / disconnect =====
                case 1:
                    LoginService login = new LoginService(session, m);
                    login.login();
                    break;

                case -27:
                    session.sendSessionKey();
                    break;

                // ===== Ping / pong =====
                case 42:
                case 58:
                case 127:
                    Ping.send(session);
                    break;

                // ===== Server-driven menu flow =====
                case 6:
                    // Room list + NV picker init
                    if (session.player != null) {
                        CharacterService.sendRoomList(session);
                    }
                    break;
                case 7:
                    CharacterService.sendRegionList(session);
                    break;
                case 8:
                    CharacterService.handleJoinRegion(session, m);
                    break;
                case 29:
                    CharacterService.sendItemList(session);
                    break;
                case 30:
                    CharacterService.sendCategorizedItems(session, m);
                    break;
                case 64:
                    // NVData — allocates picker objects on client
                    if (session.player != null) {
                        CharacterService.sendNVData(session, session.player);
                    }
                    break;
                case 67:
                    // Gun response — echo
                    break;
                case 69:
                    // GunRequest — treat bytes 0..8 as NV slot picks
                    CharacterService.handleGunRequest(session, m);
                    break;
                case 75:
                    // Client never sends cmd 75 (server→client only)
                    System.out.println("[Recv] unexpected cmd=75 from " + (session.player != null ? session.player.name : "unknown"));
                    break;
                case 76:
                    CharacterService.sendBoard76(session);
                    break;
                case 88:
                    CharacterService.sendEntryList(session);
                    break;

                // ===== Character selection =====
                case 26:
                    // Buy a character (mua nhân vật)
                    if (session.player != null) {
                        CharacterService.handleMuaNhanVat(session, m);
                    }
                    break;
                case 28:
                    // Confirm character pick from menu → START_ARMY
                    if (session.player != null) {
                        int pickIdx = (payload.length > 0) ? (payload[0] & 0xff) : -1;
                        System.out.println("[Handler] cmd=28 char-pick index=" + pickIdx);
                        CharacterService.handleStartArmy(session, m);
                    }
                    break;
                case 31:
                    // Select a character (client→server)
                    if (session.player != null) {
                        CharacterService.handleSelectChar(session, m);
                    }
                    break;

                // ===== Game action =====
                case 2:
                    System.out.println("[GameAction] cmd=2 from " + (session.player != null ? session.player.name : "unknown"));
                    break;

                // ===== SENDPACK =====
                case 90:
                    session.sendPackData.sendPackData(session, m);
                    break;

                // ===== Echo unknown commands for debugging =====
                default:
                    System.out.println("[Handler] Unknown cmd: " + cmd + " (0x" + String.format("%02X", cmd & 0xff) + ")");
                    break;
            }
        } catch (Exception ex) {
            System.out.println("[Handler] Exception cmd=" + cmd + ": " + ex);
            ex.printStackTrace();
        }
    }

    @Override
    public void onConnected() {
    }

    @Override
    public void onDisconnected() {
        System.out.println("disconnected: " + (session.player != null ? session.player.name : "unknown"));
    }

}
