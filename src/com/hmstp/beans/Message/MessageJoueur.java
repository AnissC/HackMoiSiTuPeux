package com.hmstp.beans.Message;


import java.net.Socket;

public class MessageJoueur extends Message {

    private String joueur;
    private String nom;

    public MessageJoueur(String j, String n, String m){
        super(m);
        this.joueur = j;
        this.nom = n;
    }

    public String getJoueur(){
        return joueur;
    }
    public String getNom(){
        return nom;
    }
}
