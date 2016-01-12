package com.hmstp.beans.Message;


import java.net.Socket;

public class MessageJoueur extends Message {

    private String joueur;
    private String nom;

    public MessageJoueur(String j, String n, Socket s, String m){
        super(s, m);
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
