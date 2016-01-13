package com.hmstp.beans.InterfaceGraphique;

import javax.swing.*;
import java.awt.*;

public class IHMJeu extends JPanel{
    private JFrame frameJeu;//done
    private JPanel panelJeu;//done

    private JPanel panelTableau;//done

    private JPanel panelInfo;//done

    private JPanel panelBoutons;//done

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

    private JPanel panelDormir;
    private JLabel labelDormir;
    private JButton boutonDormir;

    private JPanel panelSeDefendre;
    private JLabel labelSeDefendre;
    private JButton boutonSeDefendre;


    public void start(){
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
        panelBoutons= new JPanel();
        panelBoutons.setOpaque(true);
        panelBoutons.setBackground(Color.GRAY);
        panelBoutons.setBounds(50,300,800,200);

            //======================Victime 1=============//

            panelVictime1 = new JPanel();
            panelVictime1.setOpaque(false);
            labelVictime1 = new JLabel("Victime 1");
            boutonVictime1 = new JButton("Victme1");
            panelVictime1.add(labelVictime1,BorderLayout.NORTH);
            panelVictime1.add(boutonVictime1,BorderLayout.SOUTH);
            //panelVictime1.setBounds(75,325,50,50);
            panelBoutons.add(panelVictime1,BorderLayout.NORTH);

            //=====================Victime 2==============//
            panelVictime2 = new JPanel();
            panelVictime2.setOpaque(false);
            labelVictime2 = new JLabel("Victime 2");
            boutonVictime2 = new JButton("Victme2");
            panelVictime2.add(labelVictime2,BorderLayout.NORTH);
            panelVictime2.add(boutonVictime2,BorderLayout.SOUTH);
            panelVictime2.setBounds(150,325,50,50);
            panelBoutons.add(panelVictime2,BorderLayout.NORTH);


        //=====================Victime 3==============//
            panelVictime3 = new JPanel();
            panelVictime3.setOpaque(false);
            labelVictime3 = new JLabel("Victime 3");
            boutonVictime3 = new JButton("Victme3");
            panelVictime3.add(labelVictime3,BorderLayout.NORTH);
            panelVictime3.add(boutonVictime3,BorderLayout.SOUTH);
            panelBoutons.add(panelVictime3,BorderLayout.NORTH);

        //=====================Victime 4==============//
            panelVictime4 = new JPanel();
            panelVictime4.setOpaque(false);
            labelVictime4 = new JLabel("Victime 4");
            boutonVictime4 = new JButton("Victme4");
            panelVictime4.add(labelVictime4,BorderLayout.NORTH);
            panelVictime4.add(boutonVictime4,BorderLayout.SOUTH);
            panelBoutons.add(panelVictime4,BorderLayout.NORTH);

            //=====================Victime 5==============//
            panelVictime5 = new JPanel();
            panelVictime5.setOpaque(false);
            labelVictime5 = new JLabel("Victime 5");
            boutonVictime5 = new JButton("Victme5");

            panelVictime5.add(labelVictime5,BorderLayout.NORTH);
            panelVictime5.add(boutonVictime5,BorderLayout.SOUTH);
            panelBoutons.add(panelVictime5,BorderLayout.NORTH);


        //==================Bouton Dormir=============//
            panelDormir = new JPanel();
            panelDormir.setOpaque(false);
            labelDormir = new JLabel("Dormir \n");
            boutonDormir = new JButton("Dormir");
            panelDormir.add(labelDormir,BorderLayout.NORTH);
            panelDormir.add(boutonDormir,BorderLayout.SOUTH);
            panelDormir.setBounds(425,375,50,50);
            panelBoutons.add(panelDormir, BorderLayout.SOUTH);


        //==============Bouton se défendre============//
            panelSeDefendre = new JPanel();
            panelSeDefendre.setOpaque(false);
            labelSeDefendre = new JLabel("Se Défendre");
            boutonSeDefendre = new JButton("Se Défendre");
            panelSeDefendre.add(labelSeDefendre,BorderLayout.CENTER);
            panelSeDefendre.add(boutonSeDefendre);
            panelBoutons.add(panelSeDefendre);

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
        Color color1 = Color.ORANGE;
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
                game.start();
            }
        });
    }
}
