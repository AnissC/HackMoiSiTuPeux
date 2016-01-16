package com.hmstp.beans.Client;

import com.hmstp.beans.Message.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThreadConnexion extends Thread{

    private ArrayList<Lettre> listMessagesRecu;
    private ArrayList<Lettre> listMessagesEnvoyer;

    public ClientThreadConnexion(ArrayList<Lettre> mR, ArrayList<Lettre> mE){
        this.listMessagesRecu = mR;
        this.listMessagesEnvoyer = mE;
    }

    @Override
    public void run(){
        try {
            System.out.println(Client.port);
            ServerSocket s  = new ServerSocket(Client.port);
            while (true){
                Socket c= s.accept();
                ClientThreadEcriture serveurEcriture = new ClientThreadEcriture(listMessagesEnvoyer, c);
                serveurEcriture.start();
                ClientThreadEcoute serveurEcoute = new ClientThreadEcoute(listMessagesRecu, c);
                serveurEcoute.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
