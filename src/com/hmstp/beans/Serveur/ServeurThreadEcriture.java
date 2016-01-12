package com.hmstp.beans.Serveur;


import com.hmstp.beans.Message.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;

public class ServeurThreadEcriture extends Thread {
    public ArrayList<Message> listMessagesEnvoyer;

    public ServeurThreadEcriture(ArrayList <Message> list){
        listMessagesEnvoyer = list;
    }

    public void message () throws IOException{
        ObjectOutputStream ob = null;
        Message m = null;

        while(!this.isInterrupted()){
            synchronized (this.listMessagesEnvoyer) {
                if (!listMessagesEnvoyer.isEmpty()) {
                    m = listMessagesEnvoyer.remove(0);
                    ob = new ObjectOutputStream(m.getSocket().getOutputStream());
                    ob.writeObject(m);
                }
            }

        }

    }
    @Override
    public void run(){
        try {
            this.message();
        }catch (IOException e){
            System.err.println("Erreur Serveur : Serveur Thread Ecriture");
        }
    }


}
