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
        Message m = null;
        Object o = null;
        while (!this.isInterrupted()){
            o = ob.readObject();
            if (o instanceof Message){
                m = (Message) o;
                System.out.println("Je recois : "+ m.getMessage());
                synchronized (this.listMessagesRecu) {
                    this.listMessagesRecu.add(new Lettre(m, socket));
                }
            }
        }
        ob.close();
    }

    @Override
    public void run(){
        try {
            this.reception();
        }catch (IOException e){
            System.err.println(e);;
        }catch (ClassNotFoundException ea){
            System.err.println(ea);
        }
    }
}
