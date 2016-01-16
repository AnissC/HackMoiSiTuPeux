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
        for (int i=0;i<lobby[caseLobby].size();i++){
            MessageJoueur mj = new MessageJoueur(lobby[caseLobby].get(i).getSocket().getInetAddress().toString(),lobby[caseLobby].get(i).getNom(), null);
            listeJoueur.add(mj);
        }
        if (lobby[caseLobby].size() == 1){
            Serveur.message(new Lettre(new MessageJoueur(null, nomJoueur, Serveur.CREER_PARTIE), socket));
        }
        else{
            Serveur.message(new Lettre(new MessagePartie(listeJoueur, Serveur.PARTIE_TROUVE), socket));
        }

        if (lobby[caseLobby].size() >= nombreJoueur){
            listePartie.add(lobby[caseLobby]);
            for (int i=0;i<lobby[caseLobby].size();i++) {
                Serveur.message(new Lettre(new MessagePartie(listeJoueur, Serveur.COMMENCER_PARTIE), lobby[caseLobby].get(i).getSocket()));
            }
            lobby[caseLobby] = new ArrayList<>();
        }
    }

    public ArrayList<Utilisateur> trouverJoueurLobby(String nom){
        int k = 0;
        int i = 0;
        ArrayList<Utilisateur> listRetour = null;

        while((k < lobby.length) && !(lobby[k].get(i).getNom().equals(nom))) {
            while ((i < (k + 2)) && !(lobby[k].get(i).getNom().equals(nom))) {
                i++;
            }
            k++;
        }
        if (k < lobby.length && i < lobby[k].size()){
            listRetour = lobby[k];
        }
        return listRetour;
    }

    public ArrayList<Utilisateur> trouverJoueurListes(String nom){
        int k = 0;
        int i = 0;
        ArrayList<Utilisateur> listRetour = null;

        while(k < listePartie.size() && !(listePartie.get(i).get(k).getNom().equals(nom))){
            while (i < listePartie.get(k).size() && !(listePartie.get(i).get(k).getNom().equals(nom))){
                i++;
            }
            k++;
        }

        if (k < listePartie.size() && i < listePartie.get(k).size()){
            listRetour = listePartie.get(i);
        }

        return listRetour;
    }

    public void joueurPerdu(String nomJoueur){
        ArrayList<Utilisateur> listJoueur = null;
        int i = 0;
        int taille;

        listJoueur = trouverJoueurLobby(nomJoueur);
        if (listJoueur != null){
            taille = listJoueur.size();
            while (i < taille && listJoueur.get(i).getNom().equals(nomJoueur)) {
                i++;
            }
            listJoueur.remove(i);
        }
    }

    public void reconnexion(String nom, Socket so){
        ArrayList<Utilisateur>listetemp = trouverJoueurListes(nom);
        ArrayList<MessageJoueur> listeJoueur = new ArrayList<>();
        listetemp = trouverJoueurListes(nom);
        int taille;
        int i = 0;

        if(listetemp != null){
            taille = listetemp.size();
            while(i < taille){
                MessageJoueur mj = new MessageJoueur(listetemp.get(i).getSocket().getInetAddress().toString(),listetemp.get(i).getNom(), null);
                listeJoueur.add(mj);
            }
            Serveur.message(new Lettre(new MessagePartie(listeJoueur, Serveur.PARTIE_TROUVE), so));
        }
    }
}
