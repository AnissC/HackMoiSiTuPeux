package com.hmstp.beans.Serveur;

import java.util.ArrayList;

public class GestionPartie {
    private Utilisateur partieA3[];
    private Utilisateur partieA4[];
    private Utilisateur partieA5[];
    private Utilisateur partieA6[];

    private ArrayList<Utilisateur> listeUtilisateur3;
    private ArrayList<Utilisateur> listeUtilisateur4;
    private ArrayList<Utilisateur> listeUtilisateur5;
    private ArrayList<Utilisateur> listeUtilisateur6;

    public GestionPartie(){
        this.partieA3 = new Utilisateur[3];
        this.partieA4 = new Utilisateur[4];
        this.partieA5 = new Utilisateur[5];
        this.partieA6 = new Utilisateur[6];

        listeUtilisateur3 = new ArrayList<>();
        listeUtilisateur4 = new ArrayList<>();
        listeUtilisateur5 = new ArrayList<>();
        listeUtilisateur6 = new ArrayList<>();



    }


}
