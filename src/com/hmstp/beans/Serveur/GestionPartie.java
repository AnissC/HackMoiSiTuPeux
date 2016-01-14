package com.hmstp.beans.Serveur;

import com.hmstp.beans.Message.*;

import java.net.Socket;
import java.util.ArrayList;

public class GestionPartie {
    private static final int JOUEUR_MAX = 6;
    private static final int JOUEUR_MIN = 3;
    private ArrayList<Utilisateur> lobby[];
    private ArrayList<ArrayList<Utilisateur>> listePartie;

    public GestionPartie(){
        lobby = new ArrayList[JOUEUR_MAX-JOUEUR_MIN + 1];
        for (int i = 0; i<=JOUEUR_MAX-JOUEUR_MIN;i++){
            lobby[i] = new ArrayList<>();
        }
        listePartie = new ArrayList<>();
    }

    public void insererJoueurDansPartie(int nombreJoueur, Socket socket, String nomJoueur){
        ArrayList<MessageJoueur> listeJoueur = new ArrayList<>();
        int caseLobby = nombreJoueur-JOUEUR_MIN;
        lobby[caseLobby].add(new Utilisateur(socket,nomJoueur));
        MessageJoueur mej = new MessageJoueur(socket.getInetAddress().toString(), nomJoueur, Serveur.NOUVEAU_JOUEUR);
        for (int i=0;i<lobby[caseLobby].size();i++){
            MessageJoueur mj = new MessageJoueur(lobby[caseLobby].get(i).getSocket().getInetAddress().toString(),lobby[caseLobby].get(i).getNom(),Serveur.PARTIE_FINIE);
            listeJoueur.add(mj);
            if (! lobby[caseLobby].get(i).getNom().equals(nomJoueur)){
                Serveur.message(new Lettre(mej,lobby[caseLobby].get(i).getSocket()));
            }
        }
        Serveur.message(new Lettre(new MessagePartie(listeJoueur, Serveur.PARTIE_TROUVE), socket));

        /*if (lobby[caseLobby].size() >= caseLobby){
            listePartie.add(lobby[caseLobby]);
            for (int i=0;i<lobby[caseLobby].size();i++) {
                Message me = new Message(Serveur.COMMENCER_PARTIE);
                Serveur.message(new Lettre(me,lobby[caseLobby].get(i).getSocket()));
            }
            lobby[caseLobby] = new ArrayList<>();
        }
        else{

        }*/
    }
}
