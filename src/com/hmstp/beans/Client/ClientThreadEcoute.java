package com.hmstp.beans.Client;


import com.hmstp.beans.Message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ClientThreadEcoute extends Thread{
    private ArrayList<Message> listMessagesRecu;

    public ClientThreadEcoute(ArrayList<Message> l){
        this.listMessagesRecu = l;
    }

    public void reception()throws IOException, ClassNotFoundException{
        ObjectInputStream ob = null;

        while (!this.isInterrupted()){
            synchronized (this.listMessagesRecu) {
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
            System.err.println("Erreur Client : Client Thread Ecoute");
        }catch (ClassNotFoundException ea){
            System.err.println("Erreur Client : Client Thread Ecoute");
        }
    }
}
