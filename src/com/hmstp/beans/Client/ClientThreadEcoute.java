package com.hmstp.beans.Client;


import com.hmstp.beans.Message.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThreadEcoute extends Thread{
    private ArrayList<Lettre> listMessagesRecu;
    private Socket socket;

    public ClientThreadEcoute(ArrayList<Lettre> l, Socket s){
        this.listMessagesRecu = l;
        this.socket = s;
    }
    public void reception()throws IOException, ClassNotFoundException{
        ObjectInputStream ob = new ObjectInputStream(socket.getInputStream());
        while (!this.isInterrupted()){
            synchronized (this.listMessagesRecu) {
                Message m = (Message) ob.readObject();
                this.listMessagesRecu.add(new Lettre(m, socket));
            }
        }
        ob.close();
    }

    @Override
    public void run(){
        try {
            this.reception();
        }catch (IOException e){
            System.err.println("Erreur Client : Client Thread Ecoute");
        }catch (ClassNotFoundException ea){
            System.err.println("Erreur Client : Client Thread Ecoute");
        }
    }
}
