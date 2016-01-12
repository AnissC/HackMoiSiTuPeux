package com.hmstp.beans.Client;


import com.hmstp.beans.Message.*;

import java.io.BufferedOutputStream;
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
        ObjectOutputStream ob = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        ob.flush();
        Message m = null;
        int i;
        while (!this.isInterrupted()){
            synchronized (this.listMessagesEnvoyer) {
                if (!listMessagesEnvoyer.isEmpty()){
                    i = 0;
                    while(i < listMessagesEnvoyer.size()) {
                        if(listMessagesEnvoyer.get(i).getSocket() == this.socket) {
                            m = listMessagesEnvoyer.remove(i);
                            System.out.println(m.getMessage());
                            ob.writeObject(m);
                            System.out.println(m.getMessage());
                        }else {
                            i++;
                        }
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
