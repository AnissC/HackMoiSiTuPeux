package com.hmstp.beans.Message;

import java.net.Socket;

public class Lettre {
    private Message message;
    private Socket socket;

    public Lettre(Message message, Socket socket){
        this.message = message;
        this.socket = socket;
    }

    public Message getMessage() {
        return message;
    }
    public Socket getSocket() {
        return socket;
    }

}
