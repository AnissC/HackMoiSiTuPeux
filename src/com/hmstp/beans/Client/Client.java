package com.hmstp.beans.Client;

import com.hmstp.beans.InterfaceGraphique.*;
import com.hmstp.beans.Jeu.*;
import com.hmstp.beans.Message.*;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/** Cette classe qui permet de gérer les clients qui se connecte au sereur.*/
public class Client{
    private ArrayList<Lettre> listMessagesRecu = new ArrayList<>();
    private ArrayList<NouveauJoueur> listMessagesNouveauJoueur = new ArrayList<>();
    private ArrayList<Lettre> listMessagesEnvoyer = new ArrayList<>();
    private ArrayList<Participant> listParticipant = new ArrayList<>();
    private IHMClient ihmClient = new IHMClient();
    public  IHMJeu ihmJeu = new IHMJeu();
    private int nbjoueur = 0;
    private int joueurEnAttente = 0;
    private int partieEnAttente = 0;
    private String nom;
    private Joueur moi;
    private boolean partieInit = false;
    private Partie partie;
    public  boolean premier = false;

    public static String adresseIP = "192.168.43.37";
    //public static String adresseIP = "132.227.125.85";
    public static int port = 1180;
    public  Socket serveur;

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
    public static final String LIST = "LIST";
    // Client -> Client envoie choix avec un MessageList
    /** Cette méthode permet  de connecter le client au sereur, et l'avertit si jamais le serveur n'est pas opérationnel
     */
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
    /** Cette méthode gère les messages des clients*/
    private  void gestionMessage() throws Exception{
        Message m = null;
        Socket socketclient = null;
        //Condition = bouton quitter
        while(true){
            synchronized (listMessagesRecu) {
                if (!listMessagesRecu.isEmpty()) {
                    socketclient = listMessagesRecu.get(0).getSocket();
                    m = listMessagesRecu.remove(0).getMessage();
                }
            }
            if(m != null){
                switch (m.getMessage()) {
                    case Client.CONNEXION_OK:
                        ihmClient.setEcranAffichage(IHMClient.IHM_CONNEXION);
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
                        break;
                    case Client.PARTIE_TROUVE:
                        MessagePartie mP = (MessagePartie) m;
                        MessageJoueur mJrecu;
                        MessageJoueur mJenvoyer;
                        int k = 0;

                        while (!(mP.getListJoueur().isEmpty())) {
                            mJrecu = mP.getListJoueur().remove(0);
                            if (mJrecu.getNom().equals(nom)) {
                                moi = new Joueur(null, mJrecu.getNom());
                                synchronized (listParticipant) {
                                    listParticipant.add(moi);
                                }
                            } else {
                                Socket autreC = Client.connexion(mJrecu.getJoueur().substring(1), port);
                                ClientThreadEcoute clientEcoute = new ClientThreadEcoute(listMessagesRecu, autreC, this);
                                clientEcoute.start();
                                ClientThreadEcriture clientEcriture = new ClientThreadEcriture(listMessagesEnvoyer, autreC);
                                clientEcriture.start();
                                synchronized (listParticipant) {
                                    listParticipant.add(new Joueur(autreC, mJrecu.getNom()));
                                    mJenvoyer = new MessageJoueur(null, nom, Client.NOUVEAU_JOUEUR);
                                    message(new Lettre(mJenvoyer, autreC));
                                }
                            }
                            k++;
                        }
                        while (k < nbjoueur) {
                            synchronized (listParticipant) {
                                listParticipant.add(new Ramplacant("Ordinateur" + k));
                            }
                            k++;
                        }
                        lancerJeu();
                        break;
                    case Client.LIST:
                        MessageList mL = (MessageList) m;
                        if (! partieInit){
                            synchronized (listParticipant) {
                                int o = 0;
                                while (o < nbjoueur) {
                                    if (listParticipant.get(o).getNom().equals(moi.getNom())) {
                                        moi.setRole(mL.getListMessageParticipant().get(o).getRole());
                                        moi.setScore(mL.getListMessageParticipant().get(o).getScore());
                                        moi.setPerdant(o == mL.getNombre());
                                        listParticipant.set(o, moi);
                                    } else {
                                        listParticipant.get(o).setPerdant(o == mL.getNombre());
                                        listParticipant.get(o).setRole(mL.getListMessageParticipant().get(o).getRole());
                                        listParticipant.get(o).setScore(mL.getListMessageParticipant().get(o).getScore());
                                    }
                                    o++;
                                }
                                partie = new Partie(listParticipant, listMessagesEnvoyer, moi, nbjoueur, this);
                                partie.perdant = mL.getNombre();
                            }
                            partie.start();
                            partieInit = true;
                        }
                        break;
                    case Client.CREER_PARTIE:
                        MessageJoueur mj = (MessageJoueur) m;
                        premier = true;
                        synchronized (listParticipant) {
                            moi = new Joueur(null, mj.getNom());
                            listParticipant.add(moi);
                            partie = new Partie(listParticipant, listMessagesEnvoyer, moi, nbjoueur, this);
                            int j = 1;
                            while (j < nbjoueur) {
                                listParticipant.add(new Ramplacant("Ordinateur" + j));
                                j++;
                            }
                        }
                        lancerJeu();
                        Thread.sleep(1000);
                        partie.start();
                        partieInit = true;
                        break;
                    case Client.COMMENCER_PARTIE:
                        MessagePartie mpSyn = (MessagePartie) m;
                        if (partieInit) {
                            partieEnAttente++;
                            ClientLancerPartie clp = new ClientLancerPartie(partie, listParticipant);
                            //clp.start();
                        }
                        else{
                            synchronized (listMessagesRecu) {
                                listMessagesRecu.add(new Lettre(mpSyn, socketclient));
                            }
                        }
                        break;
                    case Client.NOUVEAU_JOUEUR:
                        MessageJoueur mej = (MessageJoueur) m;
                        if (partieInit) {
                            joueurEnAttente++;
                            synchronized (listMessagesNouveauJoueur) {
                                listMessagesNouveauJoueur.add(new NouveauJoueur(partie, listParticipant, mej, nbjoueur, socketclient, this));
                            }
                        }
                        else{
                            synchronized (listMessagesRecu) {
                                listMessagesRecu.add(new Lettre(mej, socketclient));
                            }
                        }
                        break;
                    case Client.CHOIX_DU_TOUR:
                        MessageChoix mC = (MessageChoix) m;
                        System.out.println("Choix :" + mC.getNombre());
                        getRoleParNom(mC.getJoueur()).choixAction(mC.getNombre());
                        break;
                    case Client.CHOIX_DU_ROLE:
                        MessageChoix mc = (MessageChoix) m;
                        choixDistribution(mc.getJoueur(), mc.getNombre());
                        break;
                }
            }
            m = null;
        }
    }
    /** Cette méthode initialise le message et l'ajoute à la liste des messages qui seront envoyés*/
    public  void message(Lettre msg){
        synchronized (listMessagesEnvoyer){
            listMessagesEnvoyer.add(msg);
        }
    }

    public  boolean pasjoueurEnAttente(){
        return ((joueurEnAttente > 0) || (partieEnAttente > 0));
    }

    public  void joueuraccept(){
        joueurEnAttente = 0;
        partieEnAttente = 0;
    }


    public  String getNom(){
        return nom;
    }

    public  void setNom(String n){
        nom = n;
    }

    public  void setNbjoueur(int n){
        nbjoueur = n;
    }

    public  int getNbjoueur() {
        return nbjoueur;
    }
    /** Cette méthode permet d'initialiser l'interface graphique selon le role du client*/
    public  void choixAction(Role r){
        if (r instanceof  Hackeur){
            ihmJeu.setEcranAffichage(IHMJeu.IHM_HACKEUR);
        }
        else{
            ihmJeu.setEcranAffichage(IHMJeu.IHM_ENTREPRISE);
        }
    }
    /** Cette méthode fait appel à l'interface graphique choix role lorsque le joueur doit assigner les roles*/
    public  void choixDistibution(){
        ihmJeu.setEcranAffichage(IHMJeu.IHM_ASSIGNE_ROLE);
    }
    /** Cette méthode prend en paramètre un participant et un role et assigne à ce participant le role en paramètre*/
    public  void choixDistribution(Participant p, int role){
        partie.choixDistribution(p, role);
    }

    public  void choixDistribution(String p, int role){
        partie.choixDistribution(p, role);
    }

    public  void choixAction(int i){
        moi.getRole().choixAction(i);
    }

    public void joueurParti(int joueur){
        Participant p = new Ramplacant(listParticipant.get(joueur).getNom());
        p.setScore(listParticipant.get(joueur).getScore());
        p.setRole(listParticipant.get(joueur).getRole());
        listParticipant.set(joueur, p);
        if(partieInit) {
            p.getRole().choixAction(1);
        }
        message(new Lettre(new MessageJoueur(null, p.getNom(), Client.JOUEUR_PERDU),this.serveur));
    }
    /** En cas de déconnexion d'un joueur, fait appel à la méthode prenant en paramètre  un entier pour le remplacer
     * par une intelligence artificielle et envoie un message au serveur pour le prévenir de la déconnexion du joueur
     * à qui la socket a été assignée*/
    public void joueurParti(Socket socket){
        int i = 0;
        while (i < listParticipant.size()) {
            if ((!listParticipant.get(i).isRemplacant()) && (((Joueur)listParticipant.get(i)).getSock() == socket)) {
                this.joueurParti(i);
                return;
            }
            i++;
        }
    }

    public  ArrayList<Participant> setListParticipant(){
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

    public  int score(String no){
        int i = 0;
        synchronized (listParticipant) {
            while ((i < listParticipant.size()) && (! listParticipant.get(i).getNom().equals(no))){
                i++;
            }
            return listParticipant.get(i).getScore();
        }
    }

    public  Role getRoleParNom(String no){
        int i = 0;
        while ((i < listParticipant.size()) && (! listParticipant.get(i).getNom().equals(no))){
            i++;
        }
        if(i == listParticipant.size()) {
            return null;
        }
        else{

            return listParticipant.get(i).getRole();
        }
    }

    public  void lancerJeu(){
        Client clienttemp = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ihmJeu.go();
                ihmJeu.setClient(clienttemp);
            }
        });
    }


    public static void main(String[] args) throws Exception{
        Client client = new Client();
        client.serveur = Client.connexion(adresseIP, 8081);

        ClientThreadEcoute clientEcoute = new ClientThreadEcoute(client.listMessagesRecu, client.serveur, client);
        clientEcoute.start();
        ClientThreadEcriture clientEcriture = new ClientThreadEcriture(client.listMessagesEnvoyer, client.serveur);
        clientEcriture.start();
        ClientNouveauJoueur cnj = new ClientNouveauJoueur(client.listMessagesNouveauJoueur);
        cnj.start();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                client.ihmClient.go();
                client.ihmClient.setClient(client);
            }
        });

        ClientThreadConnexion serveurConnexion = new ClientThreadConnexion(client.listMessagesRecu, client.listMessagesEnvoyer, client);
        serveurConnexion.start();

        client.gestionMessage();
    }
}
