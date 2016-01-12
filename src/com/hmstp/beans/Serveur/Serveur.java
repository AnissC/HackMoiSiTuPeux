package com.hmstp.beans.Serveur;


import com.hmstp.beans.Message.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class Serveur {


    MysqlConnect msql = MysqlConnect.getDbCon();

    private static ArrayList<Message> listMessagesRecu;
    private static ArrayList<Message> listMessagesEnvoyer;

    private static final String SQL_CREER_COMPTE = "INSERT INTO user (pseudo, motdepasse) VALUES (?, ?)";
    private static final String SQL_SELECT_IDENTIFIANT = "SELECT pseudo FROM user WHERE pseudo = ?";

    public void requetePreparerInsert(MessageCompte mc){
        try{
            PreparedStatement statement = msql.initialisationRequetePreparee(msql.conn,SQL_CREER_COMPTE,true,mc.getIdentifiant(),mc.getMotdepasse());
            System.out.println(mc.getIdentifiant() + mc.getMotdepasse());
            statement.executeUpdate();

        }catch(Exception s){
            System.err.println("erreur dans la requete");
        }
    }
    public String requetePreparerSelectPseudo(MessageCompte mc){
        try {
            PreparedStatement statement = msql.initialisationRequetePreparee(msql.conn,SQL_SELECT_IDENTIFIANT,true,mc.getIdentifiant());
            statement.executeUpdate();
            return statement.toString();
        }catch (Exception s){
            System.err.println("erreur dans la requete");
        }
        return "";
    }

    public String requetePreparerSelectMdp(MessageCompte mc){
        try {
            PreparedStatement statement = msql.initialisationRequetePreparee(msql.conn,SQL_SELECT_IDENTIFIANT,true,mc.getMotdepasse());
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
                   m = listMessagesEnvoyer.remove(0);
                }
            }
            if (m != null) {
                MessageCompte mC = (MessageCompte) m;
                switch (m.getMessage()) {
                    case Serveur.CREER_COMPTE:
                        requetePreparerInsert(mC);
                        break;
                    case Serveur.CONNEXION:
                        String mdp = requetePreparerSelectMdp(mC);
                        String pseudo = requetePreparerSelectPseudo(mC);
                        if (pseudo == mC.getIdentifiant() && mdp == mC.getMotdepasse()) {
                            System.out.println("connexion ok");
                        }else {
                            System.out.println("connexion ko");
                        }
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
        }

    }


    public static void main(String[] args) throws Exception{

        ServeurThreadEcriture serveurEcriture = new ServeurThreadEcriture(listMessagesEnvoyer);
        serveurEcriture.run();
        ServeurThreadEcoute serveurEcoute = new ServeurThreadEcoute(listMessagesRecu);
        serveurEcoute.run();
        ServeurThreadConnexion serveurConnexion = new ServeurThreadConnexion();
        serveurConnexion.run();

        System.out.println("TOTOTOT");
    }
}
