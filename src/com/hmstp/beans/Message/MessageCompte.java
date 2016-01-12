package com.hmstp.beans.Message;

import java.net.Socket;

public class MessageCompte extends Message{

    private String identifiant;
    private String motdepasse;

    public MessageCompte(String id, String mdp, Socket s, String m){
        super(s, m);
        this.identifiant = id;
        this.motdepasse = mdp;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public String getMotdepasse() {
        return motdepasse;
    }
}
