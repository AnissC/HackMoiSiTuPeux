package com.hmstp.beans.Client;

import com.hmstp.beans.InterfaceGraphique.IHMClient;
import com.hmstp.beans.InterfaceGraphique.IHMJeu;
import com.hmstp.beans.Jeu.*;
import com.hmstp.beans.Message.*;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
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
    private static String nom;
    private static Joueur moi;
    private static Partie partie;
    private static String adresseIP = "132.227.125.85";

    public static Socket serveur;

    public static final String CREER_COMPTE = "CREER_COMPTE";
    // Client -> Serveur, identifiant, mot de passe.
    public final static String INSCRIPTION_OK = "INSCRIPTION_OK";
    // Serveur -> Client
    public final static String INSCRIPTION_KO = "INSCRIPTION_KO";
    // Serveur -> Client
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
    // Client -> Client envoie choix avec un MessageNombre

    private static Socket connexion(String ad) {
        Socket c = null;

        while (c == null){
            try {
                c =  new Socket(ad, 8080);
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
        //Condition = bouton quitter
        while(true){
            synchronized (listMessagesRecu) {
                if (!Client.listMessagesRecu.isEmpty()) {
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
                        nbjoueur = 5;
                        break;
                    case Client.PARTIE_TROUVE:
                        MessagePartie mP = (MessagePartie) m;
                        MessageJoueur mJ;
                        int k = 0;
                        synchronized (listParticipant) {
                            while (!(mP.getListJoueur().isEmpty())) {
                                mJ = mP.getListJoueur().remove(0);
                                if (mJ.getJoueur().equals(Client.nom)){
                                    moi = new Joueur(null, mJ.getNom());
                                    listParticipant.add(moi);
                                    Client.partie = new Partie(listParticipant, listMessagesEnvoyer, moi);
                                    Client.partie.run();
                                }
                                else {
                                    Socket c  = Client.connexion(mJ.getJoueur());
                                    listParticipant.add(new Joueur(c, mJ.getNom()));
                                    ClientThreadEcoute clientEcoute = new ClientThreadEcoute(listMessagesRecu, c);
                                    clientEcoute.start();
                                    ClientThreadEcriture clientEcriture = new ClientThreadEcriture(listMessagesEnvoyer, c);
                                    clientEcriture.start();
                                }
                                k++;
                            }
                            while(k < nbjoueur){
                                listParticipant.add(new Ramplacant("Ordinateur"));
                                k++;
                            }
                        }
                        Client.lancerJeu();
                        break;
                    case Client.CREER_PARTIE:
                        MessageJoueur mj = (MessageJoueur) m;
                        synchronized (listParticipant) {
                            moi = new Joueur(null, mj.getNom());
                            listParticipant.add(moi);
                            int j = 1;
                            while (j < nbjoueur) {
                                listParticipant.add(new Ramplacant("Ordinateur"));
                                j++;
                            }
                            Client.partie = new Partie(listParticipant, listMessagesEnvoyer, moi);
                            Client.partie.run();
                        }
                        Client.lancerJeu();
                        break;
                    case Client.COMMENCER_PARTIE:
                        partie.setActive(true);
                        break;
                    case Client.NOUVEAU_JOUEUR:
                        MessageJoueur mej = (MessageJoueur) m;
                        ServerSocket ss = new ServerSocket(8080);
                        Socket sc = ss.accept();
                        ClientThreadEcoute clientEcoute = new ClientThreadEcoute(listMessagesRecu, sc);
                        clientEcoute.start();
                        ClientThreadEcriture clientEcriture = new ClientThreadEcriture(listMessagesEnvoyer, sc);
                        clientEcriture.start();
                        int h = 0;
                        synchronized (listParticipant) {
                            while((h < nbjoueur) && (! listParticipant.get(h).isRemplacant() || (listParticipant.get(h).getNom().equals(mej.getJoueur())))) {
                                h++;
                            }
                            Joueur j = new Joueur(sc, mej.getNom());
                            j.setScore(listParticipant.get(h).getScore());
                            listParticipant.set(h, j);
                        }
                        break;
                    case Client.CHOIX_DU_TOUR:
                        MessageChoix mC = (MessageChoix) m;
                        int o = 0;
                        synchronized (listParticipant) {
                            while ((o < nbjoueur) && (listParticipant.get(o).getNom().equals(mC.getJoueur()))) {
                                o++;
                            }
                            listParticipant.get(o).getRole().choixAction(mC.getNombre());
                        }
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

    public static void choixAction(Role r){
        if (r instanceof  Hackeur){
            ihmJeu.setEcranAffichage(IHMJeu.IHM_HACKEUR);
        }
        else{
            ihmJeu.setEcranAffichage(IHMJeu.IHM_ENTREPRISE);
        }
    }

    public static void choixDistibution(Role r){
        if (r instanceof  Hackeur){
            ihmJeu.setEcranAffichage(IHMJeu.IHM_ASSIGNE_ROLE);
        }
        else{
            ihmJeu.setEcranAffichage(((Entreprise)r).getNom());
        }
    }

    public static void choixDistibution(int i, String nom){
        //.........
    }

    public static void choixAction(int i){
        moi.getRole().choixAction(i);
    }


    public static ArrayList<String> classement(){
        int i = 0;
        ArrayList<String> listNom = new ArrayList<>();
        synchronized (listMessagesEnvoyer) {
            while (i < listMessagesEnvoyer.size()) {
                listNom.add(listParticipant.get(i).getNom());
                i++;
            }
        }
        return listNom;
    }

    public static int score(String nom){
        int i = 0;
        synchronized (listMessagesEnvoyer) {
            while ((i < listMessagesEnvoyer.size()) && (listParticipant.get(i).getNom() != nom)){
                i++;
            }
            return listParticipant.get(i).getScore();
        }
    }

    public static Role getRoleParNom(String nom){
        int i = 0;
        synchronized (listMessagesEnvoyer) {
            while ((i < listMessagesEnvoyer.size()) && (listParticipant.get(i).getNom() != nom)){
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
        Client.serveur = Client.connexion(adresseIP);

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

        Client.gestionMessage();

    }
}
