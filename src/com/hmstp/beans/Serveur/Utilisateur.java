package com.hmstp.beans.Serveur;

import java.net.Socket;

public class Utilisateur {
    private Socket socket;
    private String nom;

    public Utilisateur(Socket s, String nom){
        this.socket = s;
        this.nom = nom;
    }
}
