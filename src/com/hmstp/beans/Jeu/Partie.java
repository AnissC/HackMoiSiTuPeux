package com.hmstp.beans.Jeu;

import java.util.ArrayList;

public class Partie extends Thread{
    private static final int NB4 = 4;
    private static final int NB5 = 5;
    private static final int NB6 = 6;

    private int nbParticipants;
    private ArrayList<Joueur> listJoueur;
    private boolean active;
    private int moi;

    private Partie(ArrayList<Joueur> lj, int numero){
        this.listJoueur = lj;
        this.nbParticipants = this.listJoueur.size();
        this.active = false;
        this.moi = numero;
    }

    public void distributionRoleManche1(){
        this.listJoueur.get(0).setRole(Hackeur.getInstance());
        this.listJoueur.get(1).setRole(new Entreprise(2, "Moyenne entreprise"));
        this.listJoueur.get(2).setRole(new Entreprise(1, "Petite entreprise"));

        if(this.nbParticipants >= NB4){
            this.listJoueur.get(3).setRole( new Entreprise(3, "Grande entrepise"));
            if (this.nbParticipants >= NB5){
                this.listJoueur.get(4).setRole( new Entreprise(1, "Petite entreprise"));
                if (this.nbParticipants == NB6) {
                    this.listJoueur.get(5).setRole( new Entreprise(1, "Petite entreprise"));
                }
            }
        }
    }

    public boolean manqueUnJoueur(){
        for (int i = 0; i < nbParticipants; i++) {
            if (listJoueur.get(i).getRole().isChoixFait()) {
                return true;
            }
        }
        return false;
    }

    public void tour(){
        listJoueur.get(this.moi).choixAction();
        while(manqueUnJoueur()){
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
