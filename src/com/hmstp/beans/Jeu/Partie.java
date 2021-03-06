package com.hmstp.beans.Jeu;

/** Cette classe contient toutes les méthodes nécessaires au bon déroulement de la partie*/
import com.hmstp.beans.Client.Client;
import com.hmstp.beans.InterfaceGraphique.IHMJeu;
import com.hmstp.beans.Message.*;

import java.net.Socket;
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
        System.out.println(role);
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

            System.out.println("Je choisis");
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
            System.out.println("L'ordi choisit");
            distributionRoleManche1();
            int f = 0;
            while(f < nbParticipants){
                listParticipant.get(f).getRole().remmettreZero();
                f++;
            }
        }
        else{
            System.out.println("Un joueur choisit");
            client.ihmJeu.setEcranAffichage(IHMJeu.IHM_ENATTENTE);

            while (! tousNontChoisit()) {
                //wait le choix des rôles
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
            System.out.println("Je respecte personne");
        }

        int k = 0;
        while(k < nbParticipants){
            if(listParticipant.get(k).isPerdant()){
                listParticipant.get(k).setPerdant(false);
            }
            k++;
        }
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

        if (((Entreprise) victime.getRole()).getProtection()){
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
        System.out.println("Début tour");

        this.moi.getRole().choixAction(client);

        while (! moi.getRole().isChoixFait()) {
            //wait le choix
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        System.out.println("Choix fait");

        this.envoyerChoix(this.moi.getRole().retourneChoix(), moi.getNom() ,Client.CHOIX_DU_TOUR);
        client.ihmJeu.setEcranAffichage(IHMJeu.IHM_ENATTENTE);

        System.out.println("Choix envoyé");

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

        System.out.println("IA a choisit");

        while (!tousOntChoisit()) {
            //wait la réponse des autres
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        System.out.println("Les joueurs on choisit");

        this.resoudreTour();

        System.out.println("Tour résolu");
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

    public void resetPoint(){
        int i = 0;
        while (i < listParticipant.size()) {
            listParticipant.get(i).setScore(0);
            i++;
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
                else if (listParticipant.get(i).getScore() == max){if(listParticipant.get(i).getRole() == hackeur || listParticipant.get(i).getRole().getNumero() == hackeur.getVictime()){
                        gagnant = listParticipant.get(i).getNom();
                    }
                }
            }
        }
        return gagnant;
    }

    public void miseANiveauRole(Participant p){
        if(!p.isRemplacant()) {
            p.setRole(listRole.get(p.getRole().getNumero()));
            p.getRole().setChoixFait(true);
        }
    }

    public void run() {
        if (client.premier) {
            synchronized (listParticipant) {
                this.distributionRoleManche1();
                this.tour();
            }
        }
        else{
            synchronized (listParticipant) {
                int i = 0;
                while (i < listParticipant.size()) {
                    miseANiveauRole(listParticipant.get(i));
                    i++;
                }
            }
        }

        while(! this.active){
            synchronized (listParticipant) {
                if (client.pasjoueurEnAttente()) {
                    try{
                        listParticipant.wait();
                        Thread.sleep(100);
                        client.joueuraccept();
                    }
                    catch (Exception e) {
                        System.err.println(e);
                    }

                }
                int o = 0;
                while( o < nbParticipants) {
                    System.out.print(listParticipant.get(o).getNom());
                    System.out.print(" ");
                    System.out.print(listParticipant.get(o).getRole().getNumero());
                    System.out.print(" ");
                    System.out.print(listParticipant.get(o).getScore());
                    System.out.print(" ");
                    System.out.print(listParticipant.get(o).isPerdant());
                    System.out.println("");
                    o++;
                }
                this.distributionRoleMancheN();
                this.tour();
            }
        }

        boolean tour_de_chauffe = true;

        while(tour_de_chauffe){
            synchronized (listParticipant) {
            this.distributionRoleManche1();
            this.tour();
            tour_de_chauffe = false;
            }
        }

        this.resetPoint();

        while(pasDeGagnant()){
            synchronized (listParticipant) {
                if (client.pasjoueurEnAttente()) {
                    try{
                        listParticipant.wait();
                        Thread.sleep(100);
                        client.joueuraccept();
                    }
                    catch (Exception e) {
                        System.err.println(e);
                    }
                }
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
