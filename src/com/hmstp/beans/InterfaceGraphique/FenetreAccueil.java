package com.hmstp.beans.InterfaceGraphique;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FenetreAccueil extends JPanel{
    private JFrame frameAccueil;
    private JPanel panelAccueil;

    private JPanel panelPartieA3;
    private JButton boutonPartieA3;

    private JPanel panelPartieA4;
    private JButton boutonPartieA4;

    private JPanel panelPartieA5;
    private JButton boutonPartieA5;

    private JPanel panelPartieA6;
    private JButton  boutonPartieA6;

    private JPanel panelBoutonQuitter;
    private JButton boutonQuitter;

    public void vamos(){
        frameAccueil = new JFrame("Hack Moi Si Tu Peux !");
        panelAccueil = new FenetreAccueil();
        panelAccueil.setLayout(null);

        frameAccueil.add(panelAccueil);
        frameAccueil.setSize(900,600);
        frameAccueil.setLocationRelativeTo(null);
        frameAccueil.setVisible(true);
        frameAccueil.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //==============Bouton à 3==============//
        panelPartieA3 = new JPanel();
        panelPartieA3.setOpaque(false);
        panelPartieA3.setBounds(350,100,200,75);

        boutonPartieA3 = new JButton("Rejoindre une partie à 3");
        panelPartieA3.add(boutonPartieA3);
        System.out.println("Partie a 3 done");

        //==============Bouton à 4==============//
        panelPartieA4 = new JPanel();
        panelPartieA4.setOpaque(false);
        panelPartieA4.setBounds(350,200,200,75);

        boutonPartieA4 = new JButton("Rejoindre une partie a 4");
        panelPartieA4.add(boutonPartieA4);
        System.out.println("Partie a 4 done");

        //==============Bouton à 5==============//
        panelPartieA5 = new JPanel();
        panelPartieA5.setOpaque(false);
        panelPartieA5.setBounds(350,300,200,75);

        boutonPartieA5 = new JButton("Rejoindre une partie a 5");
        panelPartieA5.add(boutonPartieA5);
        System.out.println("Partie a 5 done");

        //==============Bouton à 6===============//
        panelPartieA6 = new JPanel();
        panelPartieA6.setOpaque(false);
        panelPartieA6.setBounds(350,400,200,75);

        boutonPartieA6 = new JButton("Rejoindre une partie a 6");
        panelPartieA6.add(boutonPartieA6);
        System.out.println("Partie a 6 done");

        /*===============/Quitter/==============*/

        //Panel correspondant au bouton Quitter
        panelBoutonQuitter = new JPanel();
        panelBoutonQuitter.setOpaque(false);
        panelBoutonQuitter.setBounds(750,500,100,50);


        boutonQuitter = new JButton("Quitter");
        panelBoutonQuitter.add(boutonQuitter);
        System.out.println("Bouton Quitter Done");

        //================Ajout Panel===============/
        panelAccueil.add(panelPartieA3);
        panelAccueil.add(panelPartieA4);
        panelAccueil.add(panelPartieA5);
        panelAccueil.add(panelPartieA6);
        panelAccueil.add(panelBoutonQuitter);
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
                FenetreAccueil fenetre = new FenetreAccueil();
                fenetre.vamos();
            }
        });
    }


}
