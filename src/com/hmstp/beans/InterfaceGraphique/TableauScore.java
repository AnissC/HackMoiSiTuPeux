package com.hmstp.beans.InterfaceGraphique;
import com.hmstp.beans.Jeu.Partie;
import com.hmstp.beans.Client.Client;


import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.util.ArrayList;

public class TableauScore extends JFrame{

    public TableauScore(){

        ArrayList<String> noms = new ArrayList<String>();/*Client.classement();*/
        noms.add("Alban");
        noms.add("Khaled");
        noms.add("Anis");
        noms.add("Kaci");
        noms.add("Absou");
        int taille=noms.size();

        Object[][] donnees = new Object[taille][2];
        for(int i=0;i<taille;i++){
            donnees[i][0]=noms.get(i);
            donnees[i][1]=0/*Client.score(noms.get(i))*/;
        }

        String[] entetes = {"Participants","Score"};

        JTable tableauPartie= new JTable(donnees,entetes);
        tableauPartie.setSize(100,250);

       //getContentPane().add(tableauPartie, BorderLayout.CENTER);
       //    pack();

    }

}
