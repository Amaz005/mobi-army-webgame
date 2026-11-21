package com.mobiarmy.server.service;

import java.io.IOException;

import com.mobiarmy.server.network.Message;
import com.mobiarmy.server.network.Session;

public class PopupService {
    public static void sendThongBao(Session session, String text) {
        Message m = new Message(4);
        try {
            m.writer().writeUTF(text);
        } catch (IOException e) {
        }
        session.push(m);
    }
}
