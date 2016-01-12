package com.hmstp.beans.Message;

import java.net.Socket;

public class MessageNombre extends Message{

    private int nombre;

    public MessageNombre(int n, Socket s, String m){
        super(s, m);
        this.nombre = n;
    }

    public int getNombre(){
        return nombre;
    }
}
