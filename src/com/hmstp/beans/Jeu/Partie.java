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
    private int perdant;
    private ArrayList<Integer> listRandom;
    private Joueur moi;
    private ArrayList<Role> listRole = new ArrayList<>();
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
        this.initRandom();

        listRole.add(hackeur);
        listRole.add(e1);
        listRole.add(e2);
        listRole.add(e3);
        listRole.add(e4);
        listRole.add(e5);

    }

    public void initRandom() {
        int i = 0;
        listRandom = new ArrayList<>();
        ArrayList<Integer>litRandomTemp = new ArrayList<>();

        while(i < nbParticipants){
            litRandomTemp.add(i);
            i++;
        }

        i=0;
        while(i < nbParticipants){
            //random controlé
            Integer j = litRandomTemp.remove((nbParticipants*2+3+i*3-i)%litRandomTemp.size());
            listRandom.add(j);
            i++;
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void distributionRoleManche1(){
        this.listParticipant.get(listRandom.get((max())%nbParticipants)).setRole(hackeur);
        this.listParticipant.get(listRandom.get((1+max())%nbParticipants)).setRole(e1);
        this.listParticipant.get(listRandom.get((2+max())%nbParticipants)).setRole(e2);

        if(this.nbParticipants >= NB4){
            this.listParticipant.get(listRandom.get(3+max())%nbParticipants).setRole(e3);
            if (this.nbParticipants >= NB5){
                this.listParticipant.get(listRandom.get((4+max())%nbParticipants)).setRole(e4);
                if (this.nbParticipants == NB6) {
                    this.listParticipant.get(listRandom.get((5+max())%nbParticipants)).setRole(e5);
                }
            }
        }
    }

    public void choixDistribution(Participant p, int role) {
        switch (role) {
            case 0 : p.setRole(hackeur);
                break;
            case 1 : p.setRole(e1);
                break;
            case 2 : p.setRole(e2);
                break;
            case 3 : p.setRole(e3);
                break;
            case 4 : p.setRole(e4);
                break;
            case 5 : p.setRole(e5);
                break;
        }
        p.getRole().remmettreZero();
    }

    public void distributionRoleMancheN(){
        if (moi.isPerdant()){
            Client.choixDistibution();

            while (! tousOntChoisit()) {
                //wait le choix des rôles
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        }
        else if (listParticipant.get(perdant).isRemplacant()){
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

        int k = 0;
        while(k < nbParticipants){
            if(listParticipant.get(k).isPerdant()){
                listParticipant.get(k).setPerdant(false);
            }
            k++;
        }
        perdant = -1;
    }

    public boolean tousOntChoisit(){
        int i = 0;
        int taille = 6;
        while(i < taille){
            if (! listRole.get(i).isChoixFait()) {
                return true;
            }
            i++;
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
            perdant = i;
            //if (this.active) {
                hackeur.changeScore(0 - ((Entreprise) victime.getRole()).getValeur());
                victime.changeScore(((Entreprise) victime.getRole()).getValeur());
            //}
        }
        else {
            victime.setPerdant(true);
            perdant = ((Hackeur) hackeur.getRole()).getVictime();
            //if (this.active) {
                victime.changeScore(0 - ((Entreprise) victime.getRole()).getValeur());
                hackeur.changeScore(((Entreprise) victime.getRole()).getValeur());
            //}
        }

        i=0;
        while(i < nbParticipants){
            //if (this.active) {
                if ((listParticipant.get(i) == hackeur) || (listParticipant.get(i) == victime)){
                    // point déjà calculé
                }
                else{
                    listParticipant.get(i).changeScore(((Entreprise)listParticipant.get(i).getRole()).getValeur());
                }
            //}
            i++;
        }
    }

    public void tour() {
        this.moi.getRole().choixAction();

        System.out.println("toto");
        while (! moi.getRole().isChoixFait()) {
            //wait le choix
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        System.out.println("toto");
        this.envoyerChoix(this.moi.getRole().retourneChoix());
        System.out.println("titu");

        int i = 0;
        int temp;
        while (i < listParticipant.size()) {
            if (listParticipant.get(i).isRemplacant()) {
                if (listParticipant.get(i).getRole() instanceof Entreprise) {
                    listParticipant.get(i).getRole().choixAction(listRandom.get(max()%nbParticipants) % 2);
                } else {
                    temp = listRandom.get(max()%nbParticipants);
                    if (temp == i){
                        temp = (temp + 1)%nbParticipants;
                    }
                    listParticipant.get(i).getRole().choixAction(temp);
                }
            }
            i++;
        }

        System.out.println("titu");

        while (tousOntChoisit()) {
            //wait la réponse des autres
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        System.out.println("tata");
        this.resoudreTour();
        System.out.println("tata");
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

    public int max(){
        int i = 0;
        int max= 0;
        synchronized (listParticipant) {
            while (i < listParticipant.size()) {
                if (listParticipant.get(i).getScore() > max) {
                    max = listParticipant.get(i).getScore();
                }
                i++;
            }
        }
        return max;
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
