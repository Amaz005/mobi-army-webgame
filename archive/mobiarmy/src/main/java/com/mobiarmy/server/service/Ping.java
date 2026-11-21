package com.mobiarmy.server.service;

import com.mobiarmy.server.network.Message;
import com.mobiarmy.server.network.Session;


public class Ping {

    public static void send(Session session) {
        Message m = new Message(42);
        session.push(m);
    }
}
