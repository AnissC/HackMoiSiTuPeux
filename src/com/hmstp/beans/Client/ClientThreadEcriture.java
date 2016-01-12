package com.hmstp.beans.Client;


import com.hmstp.beans.Message.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ClientThreadEcriture extends Thread{
    private ArrayList<Message> listMessagesEnvoyer;

    public ClientThreadEcriture(ArrayList<Message> l){
        this.listMessagesEnvoyer = l;
    }
    public void message()throws IOException {
        ObjectOutputStream ob = null;
        Message m = null;

        while (!this.isInterrupted()){
            synchronized (this.listMessagesEnvoyer) {
                if (!listMessagesEnvoyer.isEmpty()){
                    m = listMessagesEnvoyer.remove(0);
                    ob = new ObjectOutputStream(m.getSocket().getOutputStream());
                    ob.flush();
                    ob.writeObject(m);
                }
            }
        }

        ob.close();
    }

    @Override
    public void run(){
        try {
            this.message();
        }catch (IOException e){
            System.err.println("Erreur Client : Client Thread Ecriture");
        }
    }
}
