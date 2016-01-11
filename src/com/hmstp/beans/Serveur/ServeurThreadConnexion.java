package com.hmstp.beans.Serveur;

import com.hmstp.beans.Message.Message;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServeurThreadConnexion {
    private static ArrayList<Message> listMessagesRecu;
    private static ArrayList<Message> listMessagesEnvoyer;

    private final static String CREER_COMPTE="CREER_COMPTE";
    //Client -> serveur : identifiant + mot de passe
    public final static String CONNEXION = "CONNEXION";
    //Sinon connexion client ->serveur : identifiant + mot de passe

    private final static String CONNEXION_OK="CONNEXION_OK";
    //Serveur->Client
    private final static String CONNEXION_KO="CONNEXION_KO";
    //Serveur->Client :

    private final static String RECONNEXION="RECONNEXION";
    //Client->Serveur : une fois que le message EN_PARTIE reçu

    private final static String EN_PARTIE="EN_PARTIE";
    //Serveur->Client : pour génerer le bouton de reconnexion
    private final static String PAS_EN_PARTIE="PAS_EN_PARTIE";
    //Serveur->Client : dire que le client n'est pas en partie
    private final static String PARTIE_TROUVEE="PARTIE_TROUVEE";
    //Serveur->Client : échange des adresses ip des clients déja dans une partie qui n'as pas encore commencé
    private final static String CREER_PARTIE="CREER_PARTIE";
    //Serveur->Client : création de la salle d'attente si pas de partie correspondante trouvée
    private final static String COMMENCER_PARTIE="COMMENCER_PARTIE";
    //Serveur-> Client : lorsque le nombre de joueurs requis est atteint, message d'information du début de partie
    private final static String PARTIE_FINIE="PARTIE_FINIE";
    // N*Clients-> serveur : envoie le gagnant de la partie, ( celui a 10 pts first)

    private final static String JOUEUR_PERDU="JOUEUR_PERDU";
    //Client -> Serveur : informe que ce joueur a perdu lors de la partie disputée
    private final static String NOUVEAU_JOUEUR="NOUVEAU_JOUEUR";
    // Serveur -> N*Clients  : informe q'un nouveau joueur a rejoint le lobby en attendant le début de la partie
    // ou qu'un joueur qui s'est decconecté s'est reconnecté à la partie
    private final static String NB_JOUEURS="NB_JOUEURS";
    //Client -> Serveur : le client désir trouver une partie avec NB Joueurs


    public void EnvoiMessage(Socket c, Message m) throws Exception{
        ObjectOutputStream out = new ObjectOutputStream(c.getOutputStream());
        out.flush();
        out.writeObject(m);
        out.flush();
    }


    public static void main (String[] args) throws Exception {
        ServerSocket s= new ServerSocket(8080);
        Socket c= s.accept();
        ServeurThreadEcriture serveurEcriture = new ServeurThreadEcriture(listMessagesEnvoyer);
        serveurEcriture.run();
        ServeurThreadEcoute serveurEcoute = new ServeurThreadEcoute(listMessagesRecu);
        serveurEcoute.run();



    }
}
