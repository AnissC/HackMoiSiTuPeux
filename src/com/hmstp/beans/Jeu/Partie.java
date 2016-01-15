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
    private ArrayList<Lettre> listMessagesEnvoyer;
    private boolean active;
    private Role tableauRole[] = new Role[6];
    private Joueur moi;
    private Hackeur hackeur = Hackeur.getInstance();
    private Entreprise e1 = new Entreprise(2, "Moyenne entreprise");
    private Entreprise e2 = new Entreprise(1, "Petite entreprise");
    private Entreprise e3 = new Entreprise(3, "Grande entrepise");
    private Entreprise e4 = new Entreprise(1, "Petite entreprise");
    private Entreprise e5 = new Entreprise(1, "Petite entreprise");

    public Partie(ArrayList<Participant> lp, ArrayList<Lettre> lme, Joueur j){
        this.listParticipant = lp;
        this.listMessagesEnvoyer = lme;
        this.nbParticipants = Client.getNbjoueur();
        this.active = false;
        this.moi = j;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void distributionRoleManche1(){
        this.listParticipant.get(2).setRole(hackeur);
        this.listParticipant.get(0).setRole(e1);
        this.listParticipant.get(1).setRole(e2);

        if(this.nbParticipants >= NB4){
            this.listParticipant.get(3).setRole(e3);
            if (this.nbParticipants >= NB5){
                this.listParticipant.get(4).setRole(e4);
                if (this.nbParticipants == NB6) {
                    this.listParticipant.get(5).setRole(e5);
                }
            }
        }
    }

    public void choixDistribution(Participant p, int role) {
        /*int i = 0;
        synchronized (listParticipant) {
            while ((i < listParticipant.size()) && (! listParticipant.get(i).getNom().equals(nom))){
                i++;
            }
            role.remmettreZero();
            listParticipant.get(j).setRole(role);
        }*/
    }

    public void distributionRoleMancheN(){
        if (moi.isPerdant()){
            Client.choixDistibution(hackeur);
            Client.choixDistibution(e1);
            Client.choixDistibution(e2);

            if(this.nbParticipants >= NB4){
                Client.choixDistibution(e3);
                if (this.nbParticipants >= NB5){
                    Client.choixDistibution(e4);
                    if (this.nbParticipants == NB6) {
                        Client.choixDistibution(e5);
                    }
                }
            }

            while (!tousOntChoisit()) {
                //wait le choix des rôles
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.err.println(e);
                }
            }

        }
        else if (listParticipant.get(0).isRemplacant()){
            distributionRoleManche1();
            int i = 0;
            while(i < nbParticipants){
                listParticipant.get(i).getRole().remmettreZero();
                i++;
            }
        }
        else{
            //afficher un message d'attente
        }
    }

    public boolean tousOntChoisit(){
        for (int i = 0; i < nbParticipants; i++) {
            if (! listParticipant.get(i).getRole().isChoixFait()) {
                return true;
            }
        }
        return false;
    }

    public void envoyerChoix(int choix){
        MessageChoix mn = null;
        int i = 0;
        while (i < listParticipant.size()) {
            if (!(listParticipant.get(i).isRemplacant())) {
                mn = new MessageChoix(moi.getNom(), choix, Client.CHOIX_DU_TOUR);
                synchronized (listMessagesEnvoyer) {
                    this.listMessagesEnvoyer.add(new Lettre(mn, ((Joueur) listParticipant.get(i)).getSock()));
                }
            }
            i++;
        }
    }

    public void resoudreTour(){
        int i = 0;
        while (!(listParticipant.get(i).getRole() instanceof Hackeur)) {
            i++;
        }

        Participant hackeur = listParticipant.get(i);
        Participant victime = listParticipant.get(((Hackeur) hackeur.getRole()).getVictime());

        if (((Entreprise)victime.getRole()).getProtection()){
            hackeur.setPerdant(true);
            if (this.active) {
                hackeur.changeScore(0 - ((Entreprise) victime.getRole()).getValeur());
                victime.changeScore(((Entreprise) victime.getRole()).getValeur());
            }
        }
        else {
            victime.setPerdant(true);
            if (this.active) {
                victime.changeScore(0 - ((Entreprise) victime.getRole()).getValeur());
                hackeur.changeScore(((Entreprise) victime.getRole()).getValeur());
            }
        }


        i=0;
        while(i < nbParticipants){
            if (this.active) {
                if ((!(listParticipant.get(i) == hackeur)) || (!(listParticipant.get(i) == victime))){
                    listParticipant.get(i).changeScore(((Entreprise)listParticipant.get(0).getRole()).getValeur());
                }
            }
            i++;
        }
    }

    public int algoIA(int i){
        int n = nbParticipants;
        n = (i+1)%n;
        return n;
    }

    public void tour() {
        this.moi.getRole().choixAction();

        while (!moi.getRole().isChoixFait()) {
            //wait le choix
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        this.envoyerChoix(this.moi.getRole().retourneChoix());

        int i = 0;
        while (i < listParticipant.size()) {
            if (listParticipant.get(i).isRemplacant()) {
                if (listParticipant.get(i).getRole() instanceof Entreprise) {
                    listParticipant.get(i).getRole().choixAction((i + 1) % 2);
                } else {
                    listParticipant.get(i).getRole().choixAction(algoIA(i));
                }
            }
            i++;
        }

        while (tousOntChoisit()) {
            //wait la réponse des autres
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        this.resoudreTour();
    }

    public boolean pasDeGagnant(){
        int i = 0;
        synchronized (listParticipant) {
            while (i < listParticipant.size()) {
                if (listParticipant.get(i).getScore() >= 10) {
                    return false;
                }
            }
            return true;
        }

    }

    public String leGagnant(){
        int i = 0;
        int max= 0;
        String gagnant = null;
        synchronized (listParticipant) {
            while (i < listParticipant.size()) {
                if (listParticipant.get(i).getScore() > max) {
                    max = listParticipant.get(i).getScore();
                    gagnant = listParticipant.get(i).getNom();
                }
            }
        }
        return gagnant;
    }

    public void run(){
        synchronized (listParticipant) {
            this.distributionRoleManche1();
            this.tour();
        }

        while(! this.active){
            synchronized (listParticipant) {
                if (Client.pasjoueurEnAttente()) {
                    try{
                        listParticipant.wait();
                    }
                    catch (Exception e) {
                        System.err.println(e);
                    }

                }
            }
            synchronized (listParticipant) {
                this.distributionRoleMancheN();
                this.tour();
            }
        }

        synchronized (listParticipant) {
            this.distributionRoleManche1();
            this.tour();
        }

        while(pasDeGagnant()){
            synchronized (listParticipant) {
                if (Client.pasjoueurEnAttente()) {
                    try{
                        listParticipant.wait();
                    }
                    catch (Exception e) {
                        System.err.println(e);
                    }

                }
            }
            synchronized (listParticipant) {
                this.distributionRoleMancheN();
                this.tour();
            }
        }

        MessageJoueur mj = new MessageJoueur(null, leGagnant(), Client.PARTIE_FINIE);
        synchronized (listMessagesEnvoyer) {
            this.listMessagesEnvoyer.add(new Lettre(mj, Client.serveur));
        }
    }
}
