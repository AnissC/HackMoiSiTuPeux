package com.hmstp.beans.Serveur;

import com.hmstp.beans.Message.Message;

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServeurThreadConnexion extends Thread{

    private ArrayList<Message> listMessagesRecu;
    private ArrayList<Message> listMessagesEnvoyer;

    public ServeurThreadConnexion(ArrayList<Message> mR, ArrayList<Message> mE){
        this.listMessagesRecu = mR;
        this.listMessagesEnvoyer = mE;
    }

    @Override
    public void run(){
        try {
            ServerSocket s= new ServerSocket(8080);
            while (true){
                Socket c= s.accept();
                ServeurThreadEcriture serveurEcriture = new ServeurThreadEcriture(listMessagesEnvoyer, c);
                serveurEcriture.start();
                ServeurThreadEcoute serveurEcoute = new ServeurThreadEcoute(listMessagesRecu, c);
                serveurEcoute.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
