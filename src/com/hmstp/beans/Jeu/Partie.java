package com.hmstp.beans.Jeu;

import com.hmstp.beans.Client.Client;
import com.hmstp.beans.Message.*;

import java.util.ArrayList;

public class Partie extends Thread{
    private static final int NB4 = 4;
    private static final int NB5 = 5;
    private static final int NB6 = 6;

    private int nbParticipants;
    private ArrayList<Participant> listParticipant;
    private ArrayList<Message> listMessagesEnvoyer;
    private boolean active;
    private int moi;

    public Partie(ArrayList<Participant> lp, int numero){
        this.listParticipant = lp;
        this.nbParticipants = this.listParticipant.size();
        this.active = false;
        this.moi = numero;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setMoi(int moi) {
        this.moi = moi;
    }

    public void distributionRoleManche1(){
        this.listParticipant.get(0).setRole(Hackeur.getInstance());
        this.listParticipant.get(1).setRole(new Entreprise(2, "Moyenne entreprise"));
        this.listParticipant.get(2).setRole(new Entreprise(1, "Petite entreprise"));

        if(this.nbParticipants >= NB4){
            this.listParticipant.get(3).setRole( new Entreprise(3, "Grande entrepise"));
            if (this.nbParticipants >= NB5){
                this.listParticipant.get(4).setRole( new Entreprise(1, "Petite entreprise"));
                if (this.nbParticipants == NB6) {
                    this.listParticipant.get(5).setRole( new Entreprise(1, "Petite entreprise"));
                }
            }
        }
    }

    public boolean tousOntChoisit(){
        for (int i = 0; i < nbParticipants; i++) {
            if (listParticipant.get(i).getRole().isChoixFait()) {
                return true;
            }
        }
        return false;
    }

    public void envoyerChoix(int choix){
        MessageNombre mn = null;
        int i = 0;
        while(listParticipant.get(i) != null){
            if(listParticipant.get(i).isRemplacant()) {
                mn = new MessageNombre(choix, ((Joueur)listParticipant.get(i)).getSock(), Client.CHOIX_DU_TOUR);
                this.listMessagesEnvoyer.add(mn);
                i++;
            }
        }
    }

    public void tour(){
        this.envoyerChoix(listParticipant.get(this.moi).getRole().choixAction());
        while(tousOntChoisit()){
            //wait la réponse des autres
        }

    }

    public void run(){
        this.distributionRoleManche1();
        while(! this.active){
            this.tour();
        }

    }
}
