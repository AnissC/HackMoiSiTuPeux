package com.hmstp.beans.Client;

import com.hmstp.beans.InterfaceGraphique.*;
import com.hmstp.beans.Jeu.*;
import com.hmstp.beans.Message.*;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class Client{
    private static ArrayList<Lettre> listMessagesRecu = new ArrayList<>();
    private static ArrayList<Lettre> listMessagesEnvoyer = new ArrayList<>();
    private static ArrayList<Participant> listParticipant = new ArrayList<>();
    private static IHMClient ihmClient = new IHMClient();
    private static IHMJeu ihmJeu = new IHMJeu();
    private static int nbjoueur = 0;
    private static int joueurEnAttente = 0;
    private static int partieEnAttente = 0;
    private static String nom;
    private static Joueur moi;
    private static Partie partie;
    public static boolean premier = false;

    //public static String adresseIP = "169.254.129.165";
    public static String adresseIP = "132.227.125.85";
    public static int port = 1180;
    public static Socket serveur;

    public static final String CREER_COMPTE = "CREER_COMPTE";
    // Client -> Serveur, identifiant, mot de passe.
    public final static String INSCRIPTION_OK = "INSCRIPTION_OK";
    // Serveur -> Client L'inscription du client s'est bien passer
    public final static String INSCRIPTION_KO = "INSCRIPTION_KO";
    // Serveur -> Client L'inscription du client s'est mal passer
    public final static String CONNEXION = "CONNEXION";
    // Sinon connexion : Client -> Serveur, identifiant, mot de passe.
    public static final String CONNEXION_OK = "CONNEXION_OK";
    // Serveur -> Client connexion réussie
    public static final String CONNEXION_KO = "CONNEXION_KO";
    // Serveur -> Client connexion échouée
    public static final String RECONNEXION = "RECONNEXION";
    // Client -> Serveur acceptation de reprise de partie
    public static final String EN_PARTIE = "EN_PARTIE";
    // Serveur -> Client propose le bouton reconnexion (après connexion réussie)
    public static final String PAS_EN_PARTIE = "PAS_EN_PARTIE";
    // Serveur -> Client le joueur n'est pas en partie (après connexion réussie)
    public static final String PARTIE_TROUVE = "PARTIE_TROUVE";
    // Serveur -> Client le client reçoit ensuite le nombre de joueurs présent puis liste des joueurs de la partie
    public static final String CREER_PARTIE= "CREER_PARTIE";
    // Serveur -> Client le client est le seul sur sa partie et dois la mettre en place
    public static final String COMMENCER_PARTIE= "COMMENCER_PARTIE";
    // Serveur -> Client envoie un message à tout les participants pour qu'ils commencent la partie
    public static final String PARTIE_FINIE = "PARTIE_FINIE";
    // Client -> Serveur envoie quand la partie est finit et envoie ensuite le gagnant
    public static final String JOUEUR_PERDU = "JOUEUR_PERDU";
    // Client -> Serveur informe de la perte d'un joueur et envoie le disparu
    public static final String NOUVEAU_JOUEUR = "NOUVEAU_JOUEUR";
    // Serveur -> Client informe de l'arrivé d'un nouveau joueur ou d'un joueur se reconnectant
    public static final String NB_JOUEURS = "NB_JOUEURS";
    // Client -> Serveur envoie le nombre de joueurs souhaité pour la partie

    public static final String CHOIX_DU_TOUR = "CHOIX_DU_TOUR";
    // Client -> Client envoie choix avec un MessageChoix
    public static final String CHOIX_DU_ROLE = "CHOIX_DU_ROLE";
    // Client -> Client envoie choix avec un MessageChoix
    public static final String ROLE = "ROLE";
    // Client -> Client envoie choix avec un MessageChoix

    private static Socket connexion(String ad, int p) {
        Socket c = null;

        while (c == null){
            try {
                c =  new Socket(ad, p);
            } catch (UnknownHostException e) {
                System.err.println("Nom d'hôte non trouvé");
                e.printStackTrace();
                System.exit(-1);
            } catch (IOException e) {
                try {
                    Thread.sleep(4200);
                } catch (InterruptedException e1) {
                    System.err.println("Fonction sleep ne marche plus");
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        return c;
    }

    private static void gestionMessage() throws Exception{
        Message m = null;
        Socket socketclient = null;
        //Condition = bouton quitter
        while(true){
            synchronized (listMessagesRecu) {
                if (!Client.listMessagesRecu.isEmpty()) {
                    socketclient = Client.listMessagesRecu.get(0).getSocket();
                    m = Client.listMessagesRecu.remove(0).getMessage();
                }
            }
            if(m != null){
                switch (m.getMessage()) {
                    case Client.CONNEXION_OK:
                        ihmClient.setEcranAffichage(IHMClient.IHM_MENU);
                        break;
                    case Client.CONNEXION_KO:
                        ihmClient.setEcranAffichage(IHMClient.IHM_CONNEXION);
                        break;
                    case Client.INSCRIPTION_OK:
                        ihmClient.setEcranAffichage(IHMClient.IHM_CONNEXION);
                        break;
                    case Client.INSCRIPTION_KO:
                        ihmClient.setEcranAffichage(IHMClient.IHM_INSCRIPTION);
                        break;
                    case Client.EN_PARTIE:
                        ihmClient.setEcranAffichage(IHMClient.IHM_RECONNEXION);
                        break;
                    case Client.PAS_EN_PARTIE:
                        ihmClient.setEcranAffichage(IHMClient.IHM_MENU);
                        // Choix interface graphique
                        break;
                    case Client.PARTIE_TROUVE:
                        MessagePartie mP = (MessagePartie) m;
                        MessageJoueur mJrecu;
                        MessageJoueur mJenvoyer;
                        int k = 0;

                        while (!(mP.getListJoueur().isEmpty())) {
                            mJrecu = mP.getListJoueur().remove(0);
                            if (mJrecu.getNom().equals(Client.nom)){
                                moi = new Joueur(null, mJrecu.getNom());
                                synchronized (listParticipant) {
                                    listParticipant.add(moi);
                                    Client.partie = new Partie(listParticipant, listMessagesEnvoyer, moi);
                                }
                            }
                            else {
                                Socket autreC  = Client.connexion(mJrecu.getJoueur().substring(1), port);
                                synchronized (listParticipant) {
                                    listParticipant.add(new Joueur(autreC, mJrecu.getNom()));
                                    mJenvoyer = new MessageJoueur(null, nom, Client.NOUVEAU_JOUEUR);
                                    Client.message(new Lettre(mJenvoyer,autreC));
                                }
                            }
                            k++;
                        }
                        while(k < nbjoueur){
                            synchronized (listParticipant) {
                                listParticipant.add(new Ramplacant("Ordinateur" + k));
                            }
                            k++;
                        }
                        Client.lancerJeu();
                        break;
                    case Client.ROLE:
                        MessageChoix MC = (MessageChoix) m;
                        choixDistribution(moi.getNom(), MC.getNombre());
                        Client.partie.start();
                        break;
                    case Client.CREER_PARTIE:
                        MessageJoueur mj = (MessageJoueur) m;
                        premier = true;
                        synchronized (listParticipant) {
                            moi = new Joueur(null, mj.getNom());
                            listParticipant.add(moi);
                            Client.partie = new Partie(listParticipant, listMessagesEnvoyer, moi);
                            int j = 1;
                            while (j < nbjoueur) {
                                listParticipant.add(new Ramplacant("Ordinateur" + j));
                                j++;
                            }
                        }
                        Client.lancerJeu();
                        Thread.sleep(1000);
                        Client.partie.start();
                        break;
                    case Client.COMMENCER_PARTIE:
                        MessagePartie mpSyn = (MessagePartie) m;
                        partieEnAttente++;
                        synchronized (listParticipant) {
                            if (joueurEnAttente > 0) {
                                try{
                                    synchronized (partie) {
                                        partie.wait();
                                    }
                                }
                                catch (Exception e) {
                                    System.err.println(e);
                                }

                            }
                        }
                        synchronized (listParticipant) {
                            //classement(mpSyn);

                            partieEnAttente--;
                            listParticipant.notify();
                        }
                        partie.setActive(true);
                        break;
                    case Client.NOUVEAU_JOUEUR:
                        MessageJoueur mej = (MessageJoueur) m;
                        joueurEnAttente++;
                        System.out.println("avant" + joueurEnAttente);
                        int h = 0;
                        synchronized (listParticipant) {
                            while((h < nbjoueur) && (! listParticipant.get(h).isRemplacant() || (listParticipant.get(h).getNom().equals(mej.getNom())))) {
                                h++;
                            }
                            Joueur j = new Joueur(socketclient, mej.getNom());
                            j.setScore(listParticipant.get(h).getScore());
                            j.setRole(listParticipant.get(h).getRole());
                            listParticipant.set(h, j);
                            message(new Lettre(new MessageChoix(j.getNom(),j.getRole().getNumero(),Client.ROLE), j.getSock()));

                            joueurEnAttente--;
                            System.out.println("après" + joueurEnAttente);
                            listParticipant.notify();
                        }
                        break;
                    case Client.CHOIX_DU_TOUR:
                        MessageChoix mC = (MessageChoix) m;
                        int o = 0;
                        while ((o < listParticipant.size()) && (! listParticipant.get(o).getNom().equals(mC.getJoueur()))){
                            o++;
                        }
                        listParticipant.get(o).getRole().choixAction(mC.getNombre());
                        break;
                    case Client.CHOIX_DU_ROLE:
                        MessageChoix mc = (MessageChoix) m;
                        choixDistribution(mc.getJoueur(),mc.getNombre());
                        break;
                }
            }
            m = null;
        }
    }

    public static void message(Lettre msg){
        synchronized (listMessagesEnvoyer){
            listMessagesEnvoyer.add(msg);
        }
    }

    public static boolean pasjoueurEnAttente(){
        return ((joueurEnAttente > 0) && (partieEnAttente > 0)) ;
    }


    public static String getNom(){
        return nom;
    }

    public static void setNom(String n){
        Client.nom = n;
    }

    public static void setNbjoueur(int n){
        Client.nbjoueur = n;
    }

    public static int getNbjoueur() {
        return nbjoueur;
    }

    public static void choixAction(Role r){
        if (r instanceof  Hackeur){
            ihmJeu.setEcranAffichage(IHMJeu.IHM_HACKEUR);
        }
        else{
            ihmJeu.setEcranAffichage(IHMJeu.IHM_ENTREPRISE);
        }
    }

    public static void choixDistibution(){
        ihmJeu.setEcranAffichage(IHMJeu.IHM_ASSIGNE_ROLE);
    }

    public static void choixDistribution(Participant p, int role){
        partie.choixDistribution(p, role);
    }
    public static void choixDistribution(String p, int role){
        partie.choixDistribution(p, role);
    }

    public static void choixAction(int i){
        moi.getRole().choixAction(i);
    }

    public static ArrayList<Participant> setListParticipant(){
        return listParticipant;
    }

    /*public static ArrayList<Participant> classement(MessagePartie mpSyn){
        ArrayList<Participant> listTemp = new ArrayList<>();
        MessageJoueur MJ;
        int i = 0;
        int j = 0;

        synchronized (listParticipant) {
            while(i < nbjoueur){
                MJ = mpSyn.getListJoueur().get(i);
                while ((j < listParticipant.size()) && (! listParticipant.get(j).getNom().equals(MJ.getNom()))){
                    j++;
                }
                listTemp.add(listParticipant.remove(j));
                j = 0 ;
                i++;
            }
            while(i > 0) {
                listParticipant.add(listTemp.remove(0));
                i--;
            }
            return listTemp;
        }
    }*/

    public static int score(String no){
        int i = 0;
        synchronized (listParticipant) {
            while ((i < listParticipant.size()) && (! listParticipant.get(i).getNom().equals(no))){
                i++;
            }
            return listParticipant.get(i).getScore();
        }
    }

    public static Role getRoleParNom(String no){
        int i = 0;
        synchronized (listParticipant) {
            while ((i < listParticipant.size()) && (! listParticipant.get(i).getNom().equals(no))){
                i++;
            }
            return listParticipant.get(i).getRole();
        }
    }

    public static void lancerJeu(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ihmJeu.go();
            }
        });
    }

    public static void main(String[] args) throws Exception{
        Client.serveur = Client.connexion(adresseIP, 8080);

        ClientThreadEcoute clientEcoute = new ClientThreadEcoute(listMessagesRecu, serveur);
        clientEcoute.start();
        ClientThreadEcriture clientEcriture = new ClientThreadEcriture(listMessagesEnvoyer, serveur);
        clientEcriture.start();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ihmClient.go();
            }
        });

        ClientThreadConnexion serveurConnexion = new ClientThreadConnexion(listMessagesRecu, listMessagesEnvoyer);
        serveurConnexion.start();

        Client.gestionMessage();

    }
}
