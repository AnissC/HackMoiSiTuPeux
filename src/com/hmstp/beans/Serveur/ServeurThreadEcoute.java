package com.hmstp.beans.Serveur;

import com.hmstp.beans.Message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServeurThreadEcoute extends Thread{

    private Socket socket;
    private ArrayList<Message> listMessagesRecu;

    public ServeurThreadEcoute(ArrayList<Message> l, Socket s){
        this.listMessagesRecu = l;
        this.socket = s;
    }


    public void reception()throws IOException, ClassNotFoundException {
        ObjectInputStream ob = null;

        while (!this.isInterrupted()){
            synchronized (this.listMessagesRecu) {
                ob = new ObjectInputStream(socket.getInputStream());
                Message m = (Message) ob.readObject();
                this.listMessagesRecu.add(m);
            }
        }
        ob.close();

    }

    @Override
    public void run(){
        try {
            this.reception();
        }catch (IOException e){
            System.err.println("Erreur serveur : Serveur Thread Ecoute");
        }catch (ClassNotFoundException ea){
            System.err.println("Erreur serveur : Serveur Thread Ecoute");
        }
    }
}
