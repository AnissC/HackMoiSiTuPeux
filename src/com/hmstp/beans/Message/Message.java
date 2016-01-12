package com.hmstp.beans.Message;

import java.net.Socket;

public class Message implements java.io.Serializable {

    private transient Socket socket;
    private String message;

    public Message(Socket s, String m){
        this.socket = s;
        this.message = m;
    }


    public Socket getSocket() {
        return socket;
    }
    public String getMessage() {
        return message;
    }

}
