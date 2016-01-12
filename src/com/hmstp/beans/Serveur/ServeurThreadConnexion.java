package com.hmstp.beans.Serveur;

import com.hmstp.beans.Message.Message;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServeurThreadConnexion extends Thread{
    @Override
    public void run(){
        try {
            ServerSocket s= new ServerSocket(8080);
            Socket c= s.accept();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
