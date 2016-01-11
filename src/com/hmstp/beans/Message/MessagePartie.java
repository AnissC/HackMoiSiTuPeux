package com.hmstp.beans.Message;

import java.net.Socket;
import java.util.ArrayList;

public class MessagePartie extends MessageNombre{

    private ArrayList<MessageJoueur> listJoueur;

    public ArrayList<MessageJoueur> getListJoueur(){
        return listJoueur;
    }
}
