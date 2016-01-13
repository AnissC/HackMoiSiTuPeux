package com.hmstp.beans.Message;

import java.net.Socket;

public class MessageNombre extends Message{

    private int nombre;

    public MessageNombre(int n,String m){
        super(m);
        this.nombre = n;
    }

    public int getNombre(){
        return nombre;
    }
}
