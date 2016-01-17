package com.hmstp.beans.Client;

import com.hmstp.beans.Jeu.*;
import com.hmstp.beans.Message.*;

import java.net.Socket;
import java.util.ArrayList;

public class NouveauJoueur {
    private Partie partie;
    private ArrayList<Participant> listParticipant;
    private MessageJoueur mej;
    private int nbjoueur;
    private Socket socketclient;
    private Client client;

    public NouveauJoueur(Partie partie, ArrayList<Participant> listParticipant, MessageJoueur m, int nb, Socket s, Client client){
        this.partie = partie;
        this.listParticipant = listParticipant;
        this.mej = m;
        this.nbjoueur = nb;
        this.socketclient = s;
        this.client = client;
    }

    public ArrayList<Participant> getListParticipant() {
        return listParticipant;
    }

    public Client getClient() {
        return client;
    }

    public int getNbjoueur() {
        return nbjoueur;
    }

    public MessageJoueur getMej() {
        return mej;
    }

    public Partie getPartie() {
        return partie;
    }

    public Socket getSocketclient() {
        return socketclient;
    }
}
