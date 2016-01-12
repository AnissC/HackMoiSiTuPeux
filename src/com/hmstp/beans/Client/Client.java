package com.hmstp.beans.Client;

import com.hmstp.beans.InterfaceGraphique.IHMClient;
import com.hmstp.beans.Jeu.*;
import com.hmstp.beans.Message.*;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class Client{

    private static ArrayList<Message> listMessagesRecu = new ArrayList<>();
    private static ArrayList<Message> listMessagesEnvoyer = new ArrayList<>();
    private static ArrayList<Participant> listParticipant = new ArrayList<>();
    private static int nbjoueur = 0;
    private static String nom;
    private static Partie partie;
    private static String adresseIP = "localhost";

    public static Socket serveur;

    public static final String CREER_COMPTE = "CREER_COMPTE";
    // Client -> Serveur, identifiant, mot de passe.
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
                    m = Client.listMessagesRecu.remove(0);
                }
            }
            if(m != null){
                switch (m.getMessage()) {
                    case Client.CONNEXION_OK:
                        // Affiche un message de bienvenue
                        break;
                    case Client.CONNEXION_KO:
                        // Affiche un message d'erreur
                        break;
                    case Client.EN_PARTIE:
                        // Affiche le bouton reconnexion
                        break;
                    case Client.PAS_EN_PARTIE:
                        // Affiche le menu
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
                                    Joueur jm = new Joueur(null, mJ.getNom());
                                    listParticipant.add(jm);
                                    Client.partie.setMoi(jm);
                                }
                                else {
                                    listParticipant.add(new Joueur(Client.connexion(mJ.getJoueur()), mJ.getNom()));
                                }
                                k++;
                            }
                            while(k < nbjoueur){
                                listParticipant.add(new Ramplacant("Ordinateur"));
                                k++;
                            }
                        }
                        break;
                    case Client.CREER_PARTIE:
                        MessageJoueur mj = (MessageJoueur) m;
                        synchronized (listParticipant) {
                            Joueur jM = new Joueur(null, mj.getNom());
                            listParticipant.add(jM);
                            int j = 1;
                            while (j < nbjoueur) {
                                listParticipant.add(new Ramplacant("Ordinateur"));
                                j++;
                            }
                            Client.partie = new Partie(listParticipant, listMessagesEnvoyer, jM);
                            Client.partie.run();
                        }
                        break;
                    case Client.COMMENCER_PARTIE:
                        partie.setActive(true);
                        break;
                    case Client.NOUVEAU_JOUEUR:
                        MessageJoueur mej = (MessageJoueur) m;
                        synchronized (listParticipant) {
                            ServerSocket ss = new ServerSocket(8080);
                            Socket sc = ss.accept();
                            int h = 0;
                            while((h < nbjoueur) && (! listParticipant.get(h).isRemplacant() || (listParticipant.get(h).getNom().equals(mej.getJoueur())))) {
                                h++;
                            }
                            listParticipant.set(h, new Joueur(sc, mej.getNom()));
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
        }
    }

    public static void message(Message msg){
        synchronized (listMessagesEnvoyer){
            listMessagesEnvoyer.add(msg);
        }
    }
    public static void main(String[] args) throws Exception{
        Client.serveur = Client.connexion(adresseIP);
        ClientThreadEcoute clientEcoute = new ClientThreadEcoute(listMessagesRecu);
        clientEcoute.run();
        ClientThreadEcriture clientEcriture = new ClientThreadEcriture(listMessagesEnvoyer);
        clientEcriture.run();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                IHMClient ihmClient = new IHMClient();
                ihmClient.go();
            }
        });

        Client.gestionMessage();

    }
}
