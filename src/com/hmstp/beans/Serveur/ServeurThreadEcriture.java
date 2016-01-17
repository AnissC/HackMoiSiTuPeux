package com.hmstp.beans.Serveur;


import com.hmstp.beans.Message.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServeurThreadEcriture extends Thread {
    private ArrayList<Lettre> listMessagesEnvoyer;
    private Socket socket;

    public ServeurThreadEcriture(ArrayList <Lettre> list, Socket s){
        this.listMessagesEnvoyer = list;
        this.socket = s;
    }

    public void message () throws IOException{
        ObjectOutputStream ob = new ObjectOutputStream(socket.getOutputStream());
        FileOutputStream logs = new FileOutputStream("logs.txt");
        ob.flush();
        Message m = null;
        int i;
        while(!this.isInterrupted()){
            synchronized (this.listMessagesEnvoyer) {
                if (!listMessagesEnvoyer.isEmpty()) {
                    i = 0;
                    while(i < listMessagesEnvoyer.size()){
                        if(listMessagesEnvoyer.get(i).getSocket() == socket) {
                            m = listMessagesEnvoyer.remove(i).getMessage();
                            ob.writeObject(m);
                            ob.flush();
                            System.out.println("Reçu : Adresse IP : " + socket.getInetAddress() + " Action : " + m.getMessage() + "\n");
                            String s = "Reçu : Adresse IP : " + socket.getInetAddress() + " Action : " + m.getMessage() + "\n";
                            byte[] contentInBytes = s.getBytes();
                            logs.write(contentInBytes);
                        }else {
                            i++;
                        }

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
