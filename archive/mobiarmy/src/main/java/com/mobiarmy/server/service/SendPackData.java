package com.mobiarmy.server.service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mobiarmy.server.Util;
import com.mobiarmy.server.network.Message;
import com.mobiarmy.server.network.Session;

public class SendPackData {

    private static final Logger LOGGER = Logger.getLogger(SendPackData.class.getName());

    private final byte[] iconData;
    private final byte[] playerData;
    private final byte[] tileData;
    private final byte[] valuesData;

    private byte currentVersion = 1;

    public static void sendPack(Session session, int packType) {
        SendPackData instance = session.sendPackData;
        if (instance == null) return;
        byte[] data = setDataByTypeStatic((byte) packType, instance);
        if (data == null) return;
        try {
            Message m = new Message((byte) 90);
            m.writer().writeByte((byte) packType);
            m.writer().writeByte(instance.currentVersion);
            m.writer().writeShort(data.length);
            m.writer().write(data);
            m.writer().flush();
            session.push(m);
            LOGGER.info("Auto-sent pack type=" + packType + " (" + data.length + " bytes)");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "sendPack error for type=" + packType, e);
        }
    }

    public SendPackData() {
        tileData = Util.readAllFile("cache/tiledata");
        iconData = Util.readAllFile("cache/icondata");
        valuesData = Util.readAllFile("cache/valuesdata");
        playerData = Util.readAllFile("cache/playerdata");
        LOGGER.info("Cache data loaded successfully.");
    }

    public void sendPackData(Session session, Message requestMessage) {
        try {
            if (requestMessage.reader().available() <= 0) {
                LOGGER.warning("Received empty message, cannot read type.");
                return;
            }

            byte type = requestMessage.reader().readByte();
            byte[] currentData = setDataByType(type);

            if (currentData == null) {
                LOGGER.log(Level.WARNING, "No data found for type: {0}", type);
                return;
            }

            int length = currentData.length;
            if (length > 65535) {
                LOGGER.log(Level.SEVERE, "Data too large to fit in unsigned short: {0}", length);
                return;
            }

            Message responseMessage = new Message(90);
            responseMessage.writer().writeByte(type);
            responseMessage.writer().writeByte(currentVersion);
            responseMessage.writer().writeShort(length);
            // writeUnsignedShort(responseMessage, length);
            responseMessage.writer().write(currentData);

            session.push(responseMessage);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while sending pack data", e);
        }
    }

    /**
     * Ghi một giá trị unsigned short (0–65535) vào message.
     */
    private void writeUnsignedShort(Message message, int value) throws IOException {
        message.writer().writeByte((value >>> 8) & 0xFF); // byte cao
        message.writer().writeByte(value & 0xFF);         // byte thấp
    }

    private byte[] setDataByType(byte type) {
        return setDataByTypeStatic(type, this);
    }

    private static byte[] setDataByTypeStatic(byte type, SendPackData instance) {
        switch (type) {
            case 0 -> { instance.currentVersion = 10; return instance.tileData; }
            case 1 -> { instance.currentVersion = 8;  return instance.iconData; }
            case 2 -> { instance.currentVersion = 5;  return instance.valuesData; }
            case 3 -> { instance.currentVersion = 7;  return instance.playerData; }
            default -> { return null; }
        }
    }
}
