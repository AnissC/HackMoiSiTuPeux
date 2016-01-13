package com.hmstp.beans.Serveur;

import java.net.Socket;
import java.util.ArrayList;

public class GestionPartie {
    private ArrayList<Utilisateur> lobby[];
    private static final int JOUEUR_MAX = 6;
    private static final int JOUEUR_MIN = 3;
    private ArrayList<ArrayList<Utilisateur>> listePartie;

    public GestionPartie(){
        for (int i = 0; i<=JOUEUR_MAX-JOUEUR_MIN;i++){
            lobby[i] = new ArrayList<>();
        }
        listePartie = new ArrayList<>();
    }

    public void insererJoueurDansPartie(int nombreJoueur, Socket socket, String nomJoueur){
        int caseLobby = nombreJoueur-JOUEUR_MIN;
        lobby[caseLobby].add(new Utilisateur(socket,nomJoueur));
        //Prévenir les joueurs de la partie
        if (lobby[caseLobby].size() >= caseLobby){
            listePartie.add(lobby[caseLobby]);
            //Prévenir que la partie commence
            lobby[caseLobby] = new ArrayList<>();
        }
    }
}
