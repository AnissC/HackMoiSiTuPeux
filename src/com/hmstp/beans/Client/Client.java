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
    private  ArrayList<Lettre> listMessagesRecu = new ArrayList<>();
    private  ArrayList<Lettre> listMessagesEnvoyer = new ArrayList<>();
    private  ArrayList<Participant> listParticipant = new ArrayList<>();
    private  IHMClient ihmClient = new IHMClient();
    public  IHMJeu ihmJeu = new IHMJeu();
    private  int nbjoueur = 0;
    private  int joueurEnAttente = 0;
    private  int partieEnAttente = 0;
    private  String nom;
    private  Joueur moi;
    private boolean partieInit = false;
    private  Partie partie;
    public  boolean premier = false;

    public static String adresseIP = "192.168.0.20";
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
    // Permet de réaliser la connexion
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
                                        System.out.println("moi");
                                        moi.setRole(mL.getListMessageParticipant().get(o).getRole());
                                        listParticipant.set(o, moi);
                                    } else {
                                        listParticipant.get(o).setPerdant(mL.getListMessageParticipant().get(o).isPerdant());
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
                        int h = 0;
                        if (partieInit) {
                            joueurEnAttente++;
                            System.out.println("avant" + joueurEnAttente);
                            synchronized (listParticipant) {
                                while ((h < nbjoueur) && (!listParticipant.get(h).isRemplacant() || (listParticipant.get(h).getNom().equals(mej.getNom())))) {
                                    h++;
                                }
                                Joueur j = new Joueur(socketclient, mej.getNom());
                                j.setScore(listParticipant.get(h).getScore());
                                j.setRole(listParticipant.get(h).getRole());
                                listParticipant.set(h, j);
                                message(new Lettre(new MessageList(partie.perdant, Client.LIST, listParticipant), j.getSock()));

                                joueurEnAttente--;
                                System.out.println("après" + joueurEnAttente);
                                listParticipant.notify();
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
    // Permet de lire les messages (client ou serveur)
    public  void message(Lettre msg){
        synchronized (listMessagesEnvoyer){
            listMessagesEnvoyer.add(msg);
        }
    }
    // Permet d'àjouter un message à la liste de message après la synchronisation de celle-ci
    public  boolean pasjoueurEnAttente(){
        return ((joueurEnAttente > 0) || (partieEnAttente > 0));
    }
    // Permet de vérifier si il y a des joueurs qui sont en attente ou des parties qui sont en hold

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

    public  void choixAction(Role r){
        if (r instanceof  Hackeur){
            ihmJeu.setEcranAffichage(IHMJeu.IHM_HACKEUR);
        }
        else{
            ihmJeu.setEcranAffichage(IHMJeu.IHM_ENTREPRISE);
        }
    }
    // Permet de faire appel aux méthodes choix action de entreprise ou hackeur
    public  void choixDistibution(){
        ihmJeu.setEcranAffichage(IHMJeu.IHM_ASSIGNE_ROLE);
    }
    // Fait appel à l'interface de distribution des roles
    public  void choixDistribution(Participant p, int role){
        partie.choixDistribution(p, role);
    }
    // Permet d'assigner à chaque participant un role, l'assignation est aidée par le tableau info qui indique quel role
    // doit être distribué au moment donné
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
        return listParticipant.get(i).getRole();
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
