package com.hmstp.beans.Message;


import java.net.Socket;

public class MessageJoueur extends Message {

    private String joueur;
    private String nom;

    public MessageJoueur(Socket s, String m){
        super(s, m);
    }

    public String getJoueur(){
        return joueur;
    }
    public String getNom(){
        return nom;
    }
}
