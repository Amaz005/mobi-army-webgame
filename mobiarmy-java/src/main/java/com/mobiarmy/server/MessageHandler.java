package com.mobiarmy.server;

import com.mobiarmy.network.IMessageHandler;
import com.mobiarmy.network.Message;
import java.util.HashSet;
import java.util.Set;

public final class MessageHandler implements IMessageHandler {
    private final ClientEntry client;

    private static final Set<Integer> BLOCKED_WHILE_FIGHTING = new HashSet<>();
    static {
        BLOCKED_WHILE_FIGHTING.add(6);
        BLOCKED_WHILE_FIGHTING.add(7);
        BLOCKED_WHILE_FIGHTING.add(8);
        BLOCKED_WHILE_FIGHTING.add(19);
        BLOCKED_WHILE_FIGHTING.add(56);
        BLOCKED_WHILE_FIGHTING.add(69);
        BLOCKED_WHILE_FIGHTING.add(75);
        BLOCKED_WHILE_FIGHTING.add(78);
    }

    public MessageHandler(ClientEntry client) {
        this.client = client;
    }

    @Override
    public void onMessage(Message mss) {
        if (mss == null) return;
        try {
            if (client.user != null && fighting() && BLOCKED_WHILE_FIGHTING.contains((int) mss.getCommand())) {
                System.out.println("Disconnect: user=" + client.user.getUserName() + " gửi cmd " + mss.getCommand() + " khi đang trong trận");
                client.close();
                return;
            }

            switch (mss.getCommand()) {
                case -27: client.hansakeMessage(); break;
                case -28:
                    ServerManager.requestEmptyRoomMessage(client.user, mss);
                    break;
                case -25:
                    // if (waiting()) client.user.giaHanMessage(mss);
                    break;
                case -17:
                    // client.user.giftAfterFight(mss);
                    break;
                case -6:
                    if (waiting()) client.user.luyentap.luyenTapMessage(client.user);
                    break;
                case -4:
                    client.closeMessage();
                    break;
                case 1:
                    if (client.user == null) client.loginMessage(mss);
                    break;
                case 5:
                    // ServerManager.userSendMessage(client.user, mss);
                    break;
                case 6:
                    if (waiting()) ServerManager.getRoomsMessage(client.user);
                    break;
                case 7:
                    if (waiting()) ServerManager.getRoomNumberMessage(client.user, mss);
                    break;
                case 8:
                    if (waiting()) ServerManager.joinRegionMessage(client.user, mss);
                    break;
                case 9:
                    // if (fighting()) client.user.fight.chatMessage(client.user, mss);
                    if (waitFight()) client.user.getFightWait().chatMessage(client.user, mss);
                    break;
                case 11:
                    if (waitFight()) client.user.getFightWait().kickMessage(client.user, mss);
                    break;
                case 15:
                    // if (fighting()) client.user.fight.leave(client.user);
                    if (waitFight()) client.user.getFightWait().leave(client.user);
                    break;
                case 16:
                    if (waitFight()) client.user.getFightWait().readyMessage(client.user, mss);
                    break;
                case 17:
                    // if (waiting()) client.user.hopNgocMessage(mss);
                    break;
                case 18:
                    if (waitFight()) client.user.getFightWait().setPassMessage(client.user, mss);
                    break;
                case 19:
                    if (waitFight()) client.user.getFightWait().setMoneyMessage(client.user, mss);
                    break;
                case 20:
                    if (waitFight()) client.user.getFightWait().startGameMessage(client);
                    break;
                case 21:
                    if (fighting()) client.user.fight.changeLocationMessage(client.user, mss);
                    break;
                case 22:
                case 84:
                    if (fighting()) client.user.fight.shootMessage(client.user, mss);
                    break;
                case 23:
                    // if (fighting()) client.user.notifyNetWaitMessage();
                    break;
                case 26:
                    if (fighting()) client.user.fight.useItemMessage(client.user, mss);
                    break;
                case 28:
                    if (waiting()) ServerManager.enterShortPlayMessage(client.user, mss);
                    break;
                case 29:
                    // client.user.viewFriendsMessage();
                    break;
                case 30:
                    // ServerManager.bangXhMessage(client.user, mss, false);
                    break;
                case 31:
                    // ServerManager.bangXhMessage(client.user, mss, true);
                    break;
                case 32:
                    // client.user.addFriendsMessage(mss);
                    break;
                case 33:
                    // client.user.deleteFriendsMessage(mss);
                    break;
                case 34:
                    // client.user.viewTtMessage(mss);
                    break;
                case 35:
                    if (client.user == null) client.registerMessage(mss);
                    break;
                case 36:
                    // client.user.findUserMessage(mss);
                    break;
                case 49:
                    if (fighting()) client.user.fight.boLuotMessage(client.user);
                    break;
                case 53:
                    if (fighting()) client.user.fight.changeLocationMessage(client.user, mss);
                    break;
                case 54:
                    if (waitFight()) client.user.getFightWait().setNameMessage(client.user, mss);
                    break;
                case 56:
                    if (waitFight()) client.user.getFightWait().setMaxPlayerMessage(client.user, mss);
                    break;
                case 68:
                    if (waitFight()) client.user.getFightWait().setItemMessage(client.user, mss);
                    break;
                case 69:
                    if (waiting() || waitFight()) client.user.selectNvMessage(mss);
                    break;
                case 71:
                    if (waitFight()) client.user.getFightWait().doiPheMessage(client.user, mss);
                    break;
                case 72:
                    // if (waiting() || waitFight()) client.user.buyItemMessage(mss);
                    break;
                case 74:
                    if (waiting() || waitFight()) client.user.buyNvMessage(mss);
                    break;
                case 75:
                    if (waitFight()) client.user.getFightWait().setMapMessage(client.user, mss);
                    break;
                case 77:
                    // ServerManager.napTheMessage(client.user, mss);
                    break;
                case 78:
                    if (waitFight()) client.user.getFightWait().findPlayerMessage(client.user, mss);
                    break;
                case 79:
                    if (fighting()) client.user.fight.removeBullMessage(client.user, mss);
                    break;
                case 81:
                    // if (waiting()) client.user.changePassMessage(mss);
                    break;
                case 83:
                    if (waiting() || fighting()) client.user.luyentap.startLuyenTapMessage(client.user, mss);
                    break;
                case 90:
                    ServerManager.getPackMessage(client.user, mss);
                    break;
                case 110:
                    // if (waiting()) client.user.quaysoMessage(mss);
                    break;
                case 114:
                    client.platform = mss.readUTF();
                    break;
                case 120:
                    // ServerManager.getBigIconMessage(client, mss);
                    break;
                case 121:
                    client.regMessage(mss);
                    break;
                case 122:
                    // ServerManager.napTienMessage(client.user, mss);
                    break;
                case 126:
                    // ServerManager.getMaterialIconMessage(client, mss);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean waiting() { return client.user != null && client.user.getState() == User.State.Waiting; }
    private boolean fighting() { return client.user != null && client.user.getState() == User.State.Fighting; }
    private boolean waitFight() { return client.user != null && client.user.getState() == User.State.WaitFight; }

    @Override public void onConnectionFail() { }
    @Override public void onDisconnected() { }
    @Override public void onConnectOK() { }
}
