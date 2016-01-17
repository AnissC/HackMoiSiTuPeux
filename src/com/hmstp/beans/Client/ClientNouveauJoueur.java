package com.hmstp.beans.Client;


import com.hmstp.beans.Jeu.*;
import com.hmstp.beans.Message.*;

import java.net.Socket;
import java.util.ArrayList;

public class ClientNouveauJoueur extends Thread{

    private Partie partie;
    private ArrayList<Participant> listParticipant;
    private MessageJoueur mej;
    private int nbjoueur;
    private Socket socketclient;
    private Client client;

    public ClientNouveauJoueur(Partie partie, ArrayList<Participant> listParticipant, MessageJoueur m, int nb, Socket s, Client client){
        this.partie = partie;
        this.listParticipant = listParticipant;
        this.mej = m;
        this.nbjoueur = nb;
        this.socketclient = s;
        this.client = client;
    }

    public void run(){
        int h = 0;
        synchronized (listParticipant) {
            while ((h < nbjoueur) && (!listParticipant.get(h).isRemplacant() || (listParticipant.get(h).getNom().equals(mej.getNom())))) {
                h++;
            }
            Joueur j = new Joueur(socketclient, mej.getNom());
            j.setScore(listParticipant.get(h).getScore());
            j.setRole(listParticipant.get(h).getRole());
            listParticipant.set(h, j);
            client.message(new Lettre(new MessageList(partie.perdant, Client.LIST, listParticipant), j.getSock()));
            listParticipant.notify();
        }
    }
}
