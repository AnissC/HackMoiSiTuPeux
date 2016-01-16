package com.hmstp.beans.Message;

public class MessageChoix extends MessageNombre{
    private String joueur;

    public MessageChoix(String j, int n, String m){
        super(n, m);
        this.joueur = j;
    }

    public String getJoueur() {
        return joueur;
    }
}
