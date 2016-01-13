package com.hmstp.beans.Serveur;

import java.net.Socket;

public class Utilisateur {
    private Socket socket;
    private String nom;

    public Utilisateur(Socket s, String nom){
        this.socket = s;
        this.nom = nom;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
