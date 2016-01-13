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
    private Joueur moi;

    public Partie(ArrayList<Participant> lp, ArrayList<Lettre> lme, Joueur j){
        this.listParticipant = lp;
        this.listMessagesEnvoyer = lme;
        this.nbParticipants = this.listParticipant.size();
        this.active = false;
        this.moi = j;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setMoi(Joueur moi) {
        this.moi = moi;
    }

    public void distributionRoleManche1(){
        this.listParticipant.get(2).setRole(Hackeur.getInstance());
        this.listParticipant.get(0).setRole(new Entreprise(2, "Moyenne entreprise"));
        this.listParticipant.get(1).setRole(new Entreprise(1, "Petite entreprise"));

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

    public void distributionRoleMancheN(){
        if (moi == listParticipant.get(0)){
            //interface + envoyer aux autres le role chacun (seul les 4 premier nécéssaire)
        }
        else if (listParticipant.get(0).isRemplacant()){
            distributionRoleManche1();
        }
        else{
            //afficher un message d'attente
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
        MessageChoix mn = null;
        int i = 0;
        while (listParticipant.get(i) != null) {
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
        ArrayList<Participant> listTemp = new ArrayList<>();
        int i = 0;
        while (!(listParticipant.get(i).getRole() instanceof Hackeur)) {
            i++;
        }

        Participant victime = listParticipant.remove(((Hackeur) listParticipant.get(i).getRole()).getVictime());
        if (((Entreprise)victime.getRole()).getProtection()){
            listTemp.add(listParticipant.remove(i));
            listTemp.add(victime);
        }
        else {
            listTemp.add(victime);
            listTemp.add(listParticipant.remove(i));
        }

        if (this.active){
            listTemp.get(0).changeScore(0 - ((Entreprise)victime.getRole()).getValeur());
            listTemp.get(1).changeScore(((Entreprise)victime.getRole()).getValeur());
        }

        while(listParticipant.get(0) != null){
            if (this.active) {
                if (!(((Entreprise)listParticipant.get(0).getRole()).getProtection())){
                    listParticipant.get(0).changeScore(((Entreprise)listParticipant.get(0).getRole()).getValeur());
                }
            }
            listTemp.add(listParticipant.remove(0));
        }

        listParticipant = listTemp;
    }

    public int algoIA(int i){
        int n = nbParticipants;
        n = n-(i+1);
        return n;
    }

    public void tour(){
        this.envoyerChoix(this.moi.getRole().choixAction());
        int i = 0;
        while (listParticipant.get(i) != null) {
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
        }
        this.resoudreTour();
    }

    public boolean pasDeGagnant(){
        int i = 0;
        synchronized (listParticipant) {
            while (listParticipant.get(i) != null) {
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
            while (listParticipant.get(i) != null) {
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
