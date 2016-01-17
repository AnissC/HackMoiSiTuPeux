package com.hmstp.beans.Client;

import com.hmstp.beans.Jeu.*;
import java.util.ArrayList;

public class ClientLancerPartie extends Thread{

    private Partie partie;
    private ArrayList<Participant> listParticipant;

    public ClientLancerPartie(Partie partie, ArrayList<Participant> listParticipant){
        this.partie = partie;
        this.listParticipant = listParticipant;
    }


    public void run() {
        synchronized (listParticipant) {
            partie.setActive(true);
        }
    }
}
