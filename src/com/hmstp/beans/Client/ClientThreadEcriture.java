package com.hmstp.beans.Client;


import com.hmstp.beans.Message.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThreadEcriture extends Thread{
    private ArrayList<Message> listMessagesEnvoyer;
    private Socket socket;

    public ClientThreadEcriture(ArrayList<Message> l, Socket s){
        this.listMessagesEnvoyer = l;
        this.socket = s;
    }

    public void message()throws IOException {
        ObjectOutputStream ob = new ObjectOutputStream(socket.getOutputStream());
        ob.flush();
        Message m = null;
        int i;

        while (!this.isInterrupted()){
            synchronized (this.listMessagesEnvoyer) {
                if (!listMessagesEnvoyer.isEmpty()){
                    i = 0;
                    while(listMessagesEnvoyer.get(i) != null) {
                        if(listMessagesEnvoyer.get(i).getSocket() == this.socket) {
                            m = listMessagesEnvoyer.remove(i);
                            ob.writeObject(m);
                        }
                        i++;
                    }
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
