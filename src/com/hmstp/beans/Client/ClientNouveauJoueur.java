package com.hmstp.beans.Client;


import com.hmstp.beans.Jeu.*;
import com.hmstp.beans.Message.*;

import java.util.ArrayList;

public class ClientNouveauJoueur extends Thread{

    private ArrayList<NouveauJoueur> listMessagesNouveauJoueur;

    public ClientNouveauJoueur(ArrayList<NouveauJoueur> listMessagesNouveauJoueur ){
        this.listMessagesNouveauJoueur = listMessagesNouveauJoueur;
    }

    public void run(){
        int h = 0;
        NouveauJoueur nj;
        Joueur j;
        ArrayList<Participant> listParticipant;

        while (!this.isInterrupted()){
            if (!listMessagesNouveauJoueur.isEmpty()){
                synchronized (listMessagesNouveauJoueur.get(0).getListParticipant()) {
                    nj = listMessagesNouveauJoueur.remove(0);
                    listParticipant = nj.getListParticipant();
                    while ((h < nj.getNbjoueur()) && (!listParticipant.get(h).isRemplacant() || (listParticipant.get(h).getNom().equals(nj.getMej().getNom())))) {
                        h++;
                    }
                    j = new Joueur(nj.getSocketclient(), nj.getMej().getNom());
                    j.setScore(listParticipant.get(h).getScore());
                    j.setRole(listParticipant.get(h).getRole());
                    j.setPerdant(listParticipant.get(h).isPerdant());
                    listParticipant.set(h, j);
                    nj.getClient().message(new Lettre(new MessageList(nj.getPartie().perdant, Client.LIST, listParticipant), j.getSock()));
                    listParticipant.notify();

                }
            }
        }
    }
}
