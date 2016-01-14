package com.hmstp.beans.Message;

import java.net.Socket;
import java.util.ArrayList;

public class MessagePartie extends Message{

    private ArrayList<MessageJoueur> listJoueur;

    public MessagePartie(ArrayList<MessageJoueur> listJoueur, String m){
        super(m);
        this.listJoueur = listJoueur;
    }

    public ArrayList<MessageJoueur> getListJoueur(){
        return listJoueur;
    }
}
