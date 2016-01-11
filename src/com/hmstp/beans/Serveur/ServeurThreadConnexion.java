package com.hmstp.beans.Serveur;

import com.hmstp.beans.Message.Message;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServeurThreadConnexion extends Thread{

    public void EnvoiMessage(Socket c, Message m) throws Exception{
        ObjectOutputStream out = new ObjectOutputStream(c.getOutputStream());
        out.flush();
        out.writeObject(m);
        out.flush();
    }


    public static void main (String[] args) throws Exception {
        ServerSocket s= new ServerSocket(8080);
        Socket c= s.accept();
    }
}
