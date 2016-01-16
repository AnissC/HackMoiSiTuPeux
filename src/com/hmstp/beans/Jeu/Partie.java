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
    public int perdant;
    private Client client;
    private ArrayList<Integer> listRandom;
    private Joueur moi;
    private ArrayList<Role> listRole = new ArrayList<>();
    private Hackeur hackeur = Hackeur.getInstance();
    private Entreprise e1 = new Entreprise(2, "Moyenne entreprise", 1);
    private Entreprise e2 = new Entreprise(1, "Petite entreprise", 2);
    private Entreprise e3 = new Entreprise(3, "Grande entrepise", 3);
    private Entreprise e4 = new Entreprise(1, "Petite entreprise", 4);
    private Entreprise e5 = new Entreprise(1, "Petite entreprise", 5);

    public Partie(ArrayList<Participant> lp, ArrayList<Lettre> lme, Joueur j, int nb, Client c){
        this.listParticipant = lp;
        this.listMessagesEnvoyer = lme;
        this.nbParticipants = nb;
        this.active = false;
        this.client = c;
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
            Integer j = litRandomTemp.remove((nbParticipants+3+i*3-i)%litRandomTemp.size());
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
            this.listParticipant.get(listRandom.get((3+max())%nbParticipants)).setRole(e3);
            if (this.nbParticipants >= NB5){
                this.listParticipant.get(listRandom.get((4+max())%nbParticipants)).setRole(e4);
                if (this.nbParticipants == NB6) {
                    this.listParticipant.get(listRandom.get((5+max())%nbParticipants)).setRole(e5);
                }
            }
        }
    }

    public void choixDistribution(Participant p, int role) {
        attribuerRole(p,role);
        envoyerChoix(role, p.getNom(), Client.CHOIX_DU_ROLE);
        p.getRole().remmettreZero();
    }

    public void choixDistribution(String no, int role) {
        int i = 0;
            while ((i < listParticipant.size()) && (! listParticipant.get(i).getNom().equals(no))){
                i++;
            }
        Participant p = listParticipant.get(i);
        attribuerRole(p,role);
        p.getRole().remmettreZero();
    }

    public void attribuerRole(Participant p, int role){
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
    }

    public void distributionRoleMancheN(){
        if (moi.isPerdant()){
            client.choixDistibution();

            while (! tousNontChoisit()) {
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
            while (! tousNontChoisit()) {
                //wait le choix des rôles
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
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
        while(i < nbParticipants){
            if (! listRole.get(i).isChoixFait()) {
                return false;
            }
            i++;
        }
        return true;
    }
    public boolean tousNontChoisit(){
        int i = 0;
        while(i < nbParticipants){
            if (listRole.get(i).isChoixFait()) {
                return false;
            }
            i++;
        }
        return true;
    }

    public void envoyerChoix(int choix, String nom, String mess){
        MessageChoix mn = null;
        int i = 0;
        while (i < listParticipant.size()) {
            if (!(listParticipant.get(i).isRemplacant())) {
                mn = new MessageChoix(nom, choix, mess);
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
                    if(!((Entreprise)listParticipant.get(i).getRole()).getProtection()){
                        listParticipant.get(i).changeScore(((Entreprise)listParticipant.get(i).getRole()).getValeur());
                    }
                }
            //}
            i++;
        }
    }

    public void tour() {
        System.out.println(moi);
        System.out.println(client);
        System.out.println(moi.getRole());
        this.moi.getRole().choixAction(client);

        while (! moi.getRole().isChoixFait()) {
            //wait le choix
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        this.envoyerChoix(this.moi.getRole().retourneChoix(),moi.getNom() ,Client.CHOIX_DU_TOUR);

        int i = 0;
        int temp;
        while (i < listParticipant.size()) {
            if (listParticipant.get(i).isRemplacant()) {
                if (listParticipant.get(i).getRole() instanceof Entreprise) {
                    int l = listRandom.get(max()%nbParticipants);
                    l = l%2;
                    listParticipant.get(i).getRole().choixAction(l);
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

        while (!tousOntChoisit()) {
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

    public void run() {
        if (client.premier) {
            synchronized (listParticipant) {
                this.distributionRoleManche1();
                this.tour();
            }
        }

        while(! this.active){
            synchronized (listParticipant) {
                while (client.pasjoueurEnAttente()) {
                    try{
                        System.out.println("TOTO");
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
                while (client.pasjoueurEnAttente()) {
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
            this.listMessagesEnvoyer.add(new Lettre(mj, client.serveur));
        }
    }
}
