package com.hmstp.beans.Serveur;


import com.hmstp.beans.Message.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class Serveur {


    private static ArrayList<Lettre> listMessagesRecu = new ArrayList<>();
    private static ArrayList<Lettre> listMessagesEnvoyer = new ArrayList<>();

    private static final String SQL_CREER_COMPTE = "INSERT INTO joueur (pseudo, motdepasse) VALUES (?, ?)";
    private static final String SQL_SELECT_IDENTIFIANT = "SELECT pseudo FROM joueur WHERE pseudo = ?";

    MysqlConnect msql = MysqlConnect.getDbCon();
    public boolean ajouterUtilisateur(MessageCompte mc) {
        if (msql == null) {
            return false;
        }
        try {
            System.out.println("Identitifant : " + mc.getIdentifiant() + " Mot de passe : " + mc.getMotdepasse());
            PreparedStatement preparedStatement = MysqlConnect.initialisationRequetePreparee(msql.conn,SQL_CREER_COMPTE,true,mc.getIdentifiant(), mc.getMotdepasse());
            preparedStatement.execute();
        }catch (Exception e){
            System.err.println("erreur dans la requete");
        }
        return true;
    }
    public String requetePreparerSelectPseudo(MessageCompte mc){
        try {
            PreparedStatement statement = MysqlConnect.initialisationRequetePreparee(msql.conn,SQL_SELECT_IDENTIFIANT,true,mc.getIdentifiant());
            statement.executeUpdate();
            return statement.toString();
        }catch (Exception s){
            System.err.println("erreur dans la requete");
        }
        return "";
    }




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



    public void gestionMessage() throws Exception{
        Message m = null;
        while (true){
            synchronized (Serveur.listMessagesRecu) {
                if (!Serveur.listMessagesRecu.isEmpty()){
                    m = listMessagesRecu.remove(0).getMessage();
                    System.out.println(m.getMessage());
                }
            }
            if (m != null) {
                switch (m.getMessage()) {
                    case Serveur.CREER_COMPTE:
                        MessageCompte mC = (MessageCompte) m;
                        ajouterUtilisateur(mC);
                        break;
                    case Serveur.CONNEXION:
                        break;
                    case Serveur.RECONNEXION:
                        break;
                    case Serveur.PARTIE_FINIE:
                        break;
                    case Serveur.JOUEUR_PERDU:
                        break;
                    case Serveur.NB_JOUEURS:
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
