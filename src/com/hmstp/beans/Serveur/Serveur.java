package com.hmstp.beans.Serveur;

import com.hmstp.beans.Message.*;

import java.io.FileOutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Serveur {


    private static ArrayList<Lettre> listMessagesRecu = new ArrayList<>();
    private static ArrayList<Lettre> listMessagesEnvoyer = new ArrayList<>();

    private static final String SQL_CREER_COMPTE = "INSERT INTO joueur (pseudo, motdepasse, gagne, perdu) VALUES (?, ?, 0, 0)";
    private static final String SQL_CONNEXION = "SELECT * FROM joueur WHERE pseudo = ? AND motdepasse = ?";
    private static final String SQL_TEST_COMPTE = "SELECT * FROM joueur WHERE pseudo = ?";


    MysqlConnect msql = MysqlConnect.getDbCon();
    public boolean ajouterUtilisateur(MessageCompte mc) {
        try {
            PreparedStatement preparedStatement = msql.conn.prepareStatement(SQL_CREER_COMPTE);
            preparedStatement.setString(1, mc.getIdentifiant());
            preparedStatement.setString(2, mc.getMotdepasse());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            System.err.println(e.toString());
        }
        return true;
    }
    public boolean seConnecter(MessageCompte mc){
        boolean exist = false;
        try {
            PreparedStatement preparedStatement = msql.conn.prepareStatement(SQL_CONNEXION);
            preparedStatement.setString(1,mc.getIdentifiant());
            preparedStatement.setString(2,mc.getMotdepasse());
            ResultSet rs = preparedStatement.executeQuery();
            exist = rs.next();
        }catch (SQLException e){
            System.err.println(e.toString());
        }
        return exist;
    }
    public boolean testCompte(MessageCompte mc){
        boolean exist = false;
        try {
            PreparedStatement preparedStatement = msql.conn.prepareStatement(SQL_TEST_COMPTE);
            preparedStatement.setString(1,mc.getIdentifiant());
            ResultSet rs = preparedStatement.executeQuery();
            exist = rs.next();
        }catch (SQLException e){
            System.err.println(e.toString());
        }
        return exist;
    }

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


    public static void message(Lettre msg){
        synchronized (listMessagesEnvoyer){
            listMessagesEnvoyer.add(msg);
        }
    }

    public void gestionMessage() throws Exception{
        Message m = null;
        Socket clientSocket = null;
        FileOutputStream logs = new FileOutputStream("logs.txt");
        GestionPartie gp = new GestionPartie();
        while (true){
            synchronized (Serveur.listMessagesRecu) {
                if (!Serveur.listMessagesRecu.isEmpty()){
                    clientSocket = listMessagesRecu.get(0).getSocket();
                    m = listMessagesRecu.remove(0).getMessage();
                    System.out.println("Adresse IP : " + clientSocket.getInetAddress() + " Action : " + m.getMessage() + "\n");
                    String s = "Adresse IP : " + clientSocket.getInetAddress() + " Action : " + m.getMessage() + "\n";
                    byte[] contentInBytes = s.getBytes();
                    logs.write(contentInBytes);
                }
            }
            if (m != null) {
                switch (m.getMessage()) {
                    case Serveur.CREER_COMPTE:
                        MessageCompte mC = (MessageCompte) m;
                        if (!testCompte(mC)){
                            ajouterUtilisateur(mC);
                            m = new Message(INSCRIPTION_OK);
                            listMessagesEnvoyer.add(new Lettre(m, clientSocket));
                        }
                        else {
                            m = new Message(INSCRIPTION_KO);
                            listMessagesEnvoyer.add(new Lettre(m, clientSocket));
                        }
                        break;
                    case Serveur.CONNEXION:
                        MessageCompte mC1 = (MessageCompte)m;
                        if (seConnecter(mC1)){
                            m = new Message(CONNEXION_OK);
                            listMessagesEnvoyer.add(new Lettre(m, clientSocket));
                            System.out.println("Adresse IP : " + clientSocket.getInetAddress() + " Action : " + m.getMessage() + "\n");
                            String s = "Adresse IP : " + clientSocket.getInetAddress() + " Action : " + m.getMessage() + "\n";
                            byte[] contentInBytes = s.getBytes();
                            logs.write(contentInBytes);
                            if(gp.trouverJoueurListes(mC1.getIdentifiant()) != null){
                                m = new Message(EN_PARTIE);
                                listMessagesEnvoyer.add(new Lettre(m, clientSocket));
                            }
                            else{
                                m = new Message(PAS_EN_PARTIE);
                                listMessagesEnvoyer.add(new Lettre(m, clientSocket));
                            }
                        }
                        else{
                            listMessagesEnvoyer.add(new Lettre(new Message(CONNEXION_KO),clientSocket));
                            System.out.println("Adresse IP : " + clientSocket.getInetAddress() + " Action : " + m.getMessage() + "\n");
                            String s = "Adresse IP : " + clientSocket.getInetAddress() + " Action : " + m.getMessage() + "\n";
                            byte[] contentInBytes = s.getBytes();
                            logs.write(contentInBytes);
                        }
                        break;
                    case Serveur.RECONNEXION:
                        MessageJoueur Mj = (MessageJoueur) m;
                        gp.reconnexion(Mj.getJoueur(), clientSocket);
                        break;
                    case Serveur.PARTIE_FINIE:
                        MessageJoueur mJ = (MessageJoueur) m;
                        //gp.finirPartie();
                        break;
                    case Serveur.JOUEUR_PERDU:
                        MessageJoueur MJ = (MessageJoueur) m;
                        gp.joueurPerdu(MJ.getNom());
                        break;
                    case Serveur.NB_JOUEURS:
                        int nombreJoueur;
                        MessageChoix mN = (MessageChoix) m;
                        nombreJoueur = mN.getNombre();
                        gp.insererJoueurDansPartie(nombreJoueur, clientSocket,mN.getJoueur());
                        break;
                }
            }
            m=null;
        }
    }


    public static void main(String[] args) throws Exception{

        ServeurThreadConnexion serveurConnexion = new ServeurThreadConnexion(listMessagesRecu, listMessagesEnvoyer);
        serveurConnexion.start();

        Serveur serveur = new Serveur();

        serveur.gestionMessage();

    }
}
