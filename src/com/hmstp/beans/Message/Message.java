package com.hmstp.beans.Message;

import java.net.Socket;

public class Message implements java.io.Serializable {

    private String message;

    public Message(String m){
        this.message = m;
    }

    public String getMessage() {
        return message;
    }

}
