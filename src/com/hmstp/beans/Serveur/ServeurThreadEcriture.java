package com.hmstp.beans.Serveur;


import com.hmstp.beans.Message.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;

public class ServeurThreadEcriture extends Thread {
    private ArrayList<Message> listMessagesEnvoyer;
    private Socket socket;

    public ServeurThreadEcriture(ArrayList <Message> list, Socket s){
        this.listMessagesEnvoyer = list;
        this.socket = s;
    }

    public void message () throws IOException{
        ObjectOutputStream ob = null;
        Message m = null;
        int i;

        while(!this.isInterrupted()){
            synchronized (this.listMessagesEnvoyer) {
                if (!listMessagesEnvoyer.isEmpty()) {
                    i = 0;
                    while(listMessagesEnvoyer.get(i) != null){
                        if(listMessagesEnvoyer.get(i).getSocket() == socket) {
                            m = listMessagesEnvoyer.remove(i);
                            ob = new ObjectOutputStream(m.getSocket().getOutputStream());
                            ob.flush();
                            ob.writeObject(m);
                        }
                        i++;
                    }
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
