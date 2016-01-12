package com.hmstp.beans.Message;

import java.net.Socket;

public class MessageChoix extends MessageNombre{
    private String joueur;

    public MessageChoix(String j, int n, Socket s, String m){
        super(n, s, m);
        this.joueur = j;
    }

    public String getJoueur() {
        return joueur;
    }
}
