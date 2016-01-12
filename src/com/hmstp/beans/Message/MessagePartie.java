package com.hmstp.beans.Message;

import java.net.Socket;
import java.util.ArrayList;

public class MessagePartie extends MessageNombre{

    private ArrayList<MessageJoueur> listJoueur;

    public MessagePartie(int n, Socket s, String m){
        super(n, s, m);
    }

    public ArrayList<MessageJoueur> getListJoueur(){
        return listJoueur;
    }
}
