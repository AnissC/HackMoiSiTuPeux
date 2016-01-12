package com.hmstp.beans.Message;

import java.net.Socket;

public class MessageCompte extends Message{

    private String identifiant;
    private String motdepasse;

    public MessageCompte(Socket s, String m){
        super(s, m);
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public String getMotdepasse() {
        return motdepasse;
    }
}
