package com.hmstp.beans.InterfaceGraphique;


import com.hmstp.beans.Client.Client;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class IHMJeu extends JPanel{
    public static final String IHM_HACKEUR = "IHM_HACKEUR";
    public static final String IHM_ENTREPRISE = "IHM_ENTREPRISE";
    public static final String IHM_ASSIGNE_ROLE = "IHM_ASSIGNE_ROLE";

    private JFrame frameJeu;
    private JPanel panelJeu;

    private JPanel panelTableau;

    private JPanel panelInfo;

    private JPanel panelBoutons;

    private JPanel panelMoi;
    private JLabel labelMoi;
    private JButton boutonMoi;

    private JPanel panelVictime1;
    private JLabel labelVictime1;
    private JButton boutonVictime1;

    private JPanel panelVictime2;
    private JLabel labelVictime2;
    private JButton boutonVictime2;

    private JPanel panelVictime3;
    private JLabel labelVictime3;
    private JButton boutonVictime3;

    private JPanel panelVictime4;
    private JLabel labelVictime4;
    private JButton boutonVictime4;

    private JPanel panelVictime5;
    private JLabel labelVictime5;
    private JButton boutonVictime5;

    private JPanel panelEconomiser;
    private JLabel labelEconomiser;
    private JButton boutonEconomiser;

    private JPanel panelSeProteger;
    private JLabel labelSeProteger;
    private JButton boutonSeProteger;

    private String ecranAffichage = "";

    public void setEcranAffichage(String ecranAffichage) {
        this.ecranAffichage = ecranAffichage;
        dessine();
    }

    public void go(){
        frameJeu = new JFrame("Hack moi si tu peux");
        panelJeu = new IHMJeu();
        panelJeu.setLayout(null);

        frameJeu.add(panelJeu);
        frameJeu.setSize(900,600);
        frameJeu.setLocationRelativeTo(null);
        frameJeu.setVisible(true);
        frameJeu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //========================Panel Tableau===========================//
        panelTableau = new JPanel();
        panelTableau.setOpaque(true);
        panelTableau.setBounds(50,25,200,200);

        //========================Panel Tchat=============================//
        panelInfo = new JPanel();
        panelInfo.setOpaque(true);
        panelInfo.setBounds(650,25,150,150);

        //========================Panel Bouton============================//
        panelBoutons = new JPanel();
        panelBoutons.setOpaque(true);
        panelBoutons.setBackground(Color.GRAY);
        panelBoutons.setBounds(50,300,800,200);


            //======================Victime 1=============//
        panelVictime1 = new JPanel();
        panelVictime1.setOpaque(false);

        labelVictime1 = new JLabel("Victime 1");


        boutonVictime1 = new JButton("Victime 1");
        panelVictime1.setLayout(new BorderLayout());
        panelVictime1.add(labelVictime1, SpringLayout.NORTH);
        panelVictime1.add(boutonVictime1,SpringLayout.SOUTH);


            //=====================Victime 2==============//
        panelVictime2 = new JPanel();
        panelVictime2.setOpaque(false);

        labelVictime2 = new JLabel("Victime 2");


        boutonVictime2 = new JButton("Victme2");
        panelVictime2.setLayout(new BorderLayout());
        panelVictime2.add(labelVictime2,BorderLayout.NORTH);
        panelVictime2.add(boutonVictime2,BorderLayout.SOUTH);


        //=====================Victime 3==============//
        panelVictime3 = new JPanel();
        panelVictime3.setOpaque(false);

        labelVictime3 = new JLabel("Victime 3");


        boutonVictime3 = new JButton("Victme3");
        panelVictime3.setLayout(new BorderLayout());
        panelVictime3.add(labelVictime3,BorderLayout.NORTH);
        panelVictime3.add(boutonVictime3,BorderLayout.SOUTH);


        //=====================Victime 4==============//
        panelVictime4 = new JPanel();
        panelVictime4.setOpaque(false);

        labelVictime4 = new JLabel("Victime 4");


        boutonVictime4 = new JButton("Victme4");
        panelVictime4.setLayout(new BorderLayout());
        panelVictime4.add(labelVictime4,BorderLayout.NORTH);
        panelVictime4.add(boutonVictime4,BorderLayout.SOUTH);


            //=====================Victime 5==============//
        panelVictime5 = new JPanel();
        panelVictime5.setOpaque(false);

        labelVictime5 = new JLabel("Victime 5");

        boutonVictime5 = new JButton("Victme5");
        panelVictime5.setLayout(new BorderLayout());
        panelVictime5.add(labelVictime5,BorderLayout.NORTH);
        panelVictime5.add(boutonVictime5,BorderLayout.SOUTH);


        //==================Bouton Economiser=============//
        panelEconomiser = new JPanel();
        panelEconomiser.setOpaque(false);

        labelEconomiser = new JLabel("Economiser");


        boutonEconomiser = new JButton("Economiser");
        panelEconomiser.setLayout(new BorderLayout());
        panelEconomiser.add(labelEconomiser,BorderLayout.NORTH);
        panelEconomiser.add(boutonEconomiser,BorderLayout.SOUTH);


        //==============Bouton SeProteger============//
        panelSeProteger = new JPanel();
        panelSeProteger.setOpaque(false);

        labelSeProteger = new JLabel("Se Proteger");


        boutonSeProteger = new JButton("Se Proteger");
        panelSeProteger.setLayout(new BorderLayout());
        panelSeProteger.add(labelSeProteger,BorderLayout.NORTH);
        panelSeProteger.add(boutonSeProteger,BorderLayout.SOUTH);


        //===================Bouton pour me target=================//
        panelMoi = new JPanel();
        panelMoi.setOpaque(false);

        labelMoi = new JLabel("Moi");

        boutonMoi = new JButton("Moi");
        panelMoi.setLayout(new BorderLayout());
        panelMoi.add(labelMoi,BorderLayout.NORTH);
        panelMoi.add(boutonMoi,BorderLayout.SOUTH);


        //=====================Tableau Score =============================//
        ArrayList<String> joueurs = Client.classement();
        Object[][] donnees = new Object[joueurs.size()][3];
        for (int i=0;i<joueurs.size();i++){
            donnees[i][0] = joueurs.get(i);
            donnees[i][1] = Client.getRoleParNom(joueurs.get(i)).toString();
            donnees[i][2] = Client.score(joueurs.get(i));

        }

        String[] entetes = {"Nom", "Role", "Score"};
        JTable tableau= new JTable(donnees,entetes);
        tableau.setSize(200,200);
        tableau.setVisible(true);
        panelTableau.setLayout(new BorderLayout());
        panelTableau.add(tableau.getTableHeader(),BorderLayout.NORTH);
        panelTableau.add(tableau, BorderLayout.CENTER);

        dessine();
    }

    public void dessine(){
        if (ecranAffichage.equals(IHM_ASSIGNE_ROLE)){
            panelBoutons.removeAll();
            panelBoutons.add(panelVictime1);
            panelBoutons.add(panelVictime2);
            panelBoutons.add(panelVictime3);
            panelBoutons.add(panelVictime4);
            panelBoutons.add(panelVictime5);
            panelBoutons.add(panelMoi);
        }else if(ecranAffichage.equals(IHM_HACKEUR)){
            panelBoutons.removeAll();
            panelBoutons.add(panelVictime1);
            panelBoutons.add(panelVictime2);
            panelBoutons.add(panelVictime3);
            panelBoutons.add(panelVictime4);
            panelBoutons.add(panelVictime5);
        }else if (ecranAffichage.equals(IHM_ENTREPRISE)){
            panelBoutons.removeAll();
            panelBoutons.add(labelEconomiser);
            panelBoutons.add(labelSeProteger);
        }


        //=====================Ajout de tous les panels===================//
        panelJeu.add(panelTableau);
        panelJeu.add(panelInfo);
        panelJeu.add(panelBoutons);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        Color color1 = Color.BLUE;
        Color color2 = Color.BLACK;
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                IHMJeu game = new IHMJeu();
                game.go();
            }
        });
    }
}
