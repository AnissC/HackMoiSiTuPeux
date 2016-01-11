package com.hmstp.beans.Message;

import java.net.Socket;

public class MessageJoueur extends Message {

    private Socket joueur;
    private String nom;

    public Socket getJoueur(){
        return joueur;
    }
    public String getNom(){
        return nom;
    }
}
