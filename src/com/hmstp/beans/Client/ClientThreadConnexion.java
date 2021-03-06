package com.hmstp.beans.Client;

import com.hmstp.beans.Message.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThreadConnexion extends Thread{

    private ArrayList<Lettre> listMessagesRecu;
    private ArrayList<Lettre> listMessagesEnvoyer;
    private Client client;

    public ClientThreadConnexion(ArrayList<Lettre> mR, ArrayList<Lettre> mE, Client client){
        this.listMessagesRecu = mR;
        this.listMessagesEnvoyer = mE;
        this.client = client;
    }

    @Override
    public void run(){
        try {
            ServerSocket s  = new ServerSocket(Client.port);
            while (true){
                Socket c= s.accept();
                ClientThreadEcriture serveurEcriture = new ClientThreadEcriture(listMessagesEnvoyer, c);
                serveurEcriture.start();
                ClientThreadEcoute serveurEcoute = new ClientThreadEcoute(listMessagesRecu, c, client);
                serveurEcoute.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
