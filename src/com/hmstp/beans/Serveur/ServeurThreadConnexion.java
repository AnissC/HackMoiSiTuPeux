package com.hmstp.beans.Serveur;

import java.net.ServerSocket;
import java.net.Socket;

public class ServeurThreadConnexion extends Thread{
    @Override
    public void run(){
        try {
            ServerSocket s= new ServerSocket(8080);
            while (true){
                Socket c= s.accept();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
