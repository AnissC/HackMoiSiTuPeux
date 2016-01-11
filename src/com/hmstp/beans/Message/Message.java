package com.hmstp.beans.Message;

import java.net.Socket;

public class Message implements java.io.Serializable {

    private Socket socket;
    private String message;

    public Message(){
        this.message = "test";
    }


    public Socket getSocket() {
        return socket;
    }
    public String getMessage() {
        return message;
    }

}
