package com.hmstp.beans.Client;

import com.hmstp.beans.Jeu.*;
import com.hmstp.beans.Message.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class Client{

    private static ArrayList<Message> listMessagesRecu;
    private static ArrayList<Message> listMessagesEnvoyer;
    private static ArrayList<Participant> listParticipant;
    private static int nbjoueur = 0;
    private static String adresseIP = "132.227.125.85";
    private static final String CREER_COMPTE = "CREER_COMPTE";
    // Client -> Serveur, identifiant, mot de passe.
    public final static String CONNEXION = "CONNEXION";
    // Sinon connexion : Client -> Serveur, identifiant, mot de passe.
    private static final String CONNEXION_OK = "CONNEXION_OK";
    // Serveur -> Client connexion réussie
    private static final String CONNEXION_KO = "CONNEXION_KO";
    // Serveur -> Client connexion échouée
    private static final String RECONNEXION = "RECONNEXION";
    // Client -> Serveur acceptation de reprise de partie
    private static final String EN_PARTIE = "EN_PARTIE";
    // Serveur -> Client propose le bouton reconnexion (après connexion réussie)
    private static final String PAS_EN_PARTIE = "PAS_EN_PARTIE";
    // Serveur -> Client le joueur n'est pas en partie (après connexion réussie)
    private static final String PARTIE_TROUVE = "PARTIE_TROUVE";
    // Serveur -> Client le client reçoit ensuite le nombre de joueurs présent puis liste des joueurs de la partie
    private static final String CREER_PARTIE= "CREER_PARTIE";
    // Serveur -> Client le client est le seul sur sa partie et dois la mettre en place
    private static final String COMMENCER_PARTIE= "COMMENCER_PARTIE";
    // Serveur -> Client envoie un message à tout les participants pour qu'ils commencent la partie
    private static final String PARTIE_FINIE = "PARTIE_FINIE";
    // Client -> Serveur envoie quand la partie est finit et envoie ensuite le gagnant
    private static final String JOUEUR_PERDU = "JOUEUR_PERDU";
    // Client -> Serveur informe de la perte d'un joueur et envoie le disparu
    private static final String NOUVEAU_JOUEUR = "NOUVEAU_JOUEUR";
    // Serveur -> Client informe de l'arrivé d'un nouveau joueur ou d'un joueur se reconnectant
    private static final String NB_JOUEURS = "NB_JOUEURS";
    // Client -> Serveur envoie le nombre de joueurs souhaité pour la partie

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
            synchronized (Client.listMessagesRecu) {
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
                        synchronized (Client.listParticipant) {
                            while (!(mP.getListJoueur().isEmpty())) {
                                mJ = mP.getListJoueur().remove(0);
                                listParticipant.add(new Joueur(Client.connexion(mJ.getJoueur()), mJ.getNom()));
                                k++;
                            }
                            while(k < nbjoueur){
                                listParticipant.add(new Ramplacant("Vide"));
                                k++;
                            }
                        }
                        break;
                    case Client.CREER_PARTIE:
                        MessageJoueur mj = (MessageJoueur) m;
                        synchronized (Client.listParticipant) {
                            listParticipant.add(new Joueur(null, mj.getNom()));
                            int j = 1;
                            while (j < nbjoueur) {
                                listParticipant.add(new Ramplacant("Vide"));
                                j++;
                            }
                        }
                        break;
                    case Client.COMMENCER_PARTIE:
                        // Lance la partie
                        break;
                    case Client.NOUVEAU_JOUEUR:
                        MessageJoueur mej = (MessageJoueur) m;
                        synchronized (Client.listParticipant) {
                            ServerSocket ss = new ServerSocket(8080);
                            Socket sc = ss.accept();
                            while(true)
                            listParticipant.get() = new Joueur(sc, mej.getNom());
                        }
                        break;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception{
        Socket c = Client.connexion(adresseIP);
        ClientThreadEcoute clientEcoute = new ClientThreadEcoute(listMessagesRecu);
        clientEcoute.run();
        ClientThreadEcriture clientEcriture = new ClientThreadEcriture(listMessagesEnvoyer);
        clientEcriture.run();
        Client.gestionMessage();
    }
}
