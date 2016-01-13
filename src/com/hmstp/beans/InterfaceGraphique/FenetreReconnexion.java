package com.hmstp.beans.InterfaceGraphique;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FenetreReconnexion extends JPanel{
    private JFrame frameReconnexion;
    private JPanel panelReconnexion;

    private JPanel panelBoutonReconnexion;
    private JLabel labelBoutonReconnexion;
    private JButton boutonReconnexion;

    private JPanel panelBoutonQuitter;
    private JButton boutonQuitter;

    public void halommou(){
        frameReconnexion = new JFrame("Hack Moi Si Tu Peux");
        panelReconnexion = new FenetreReconnexion();
        panelReconnexion.setLayout(null);

        frameReconnexion.add(panelReconnexion);
        frameReconnexion.setSize(900,600);
        frameReconnexion.setLocationRelativeTo(null);
        frameReconnexion.setVisible(true);
        frameReconnexion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //===========Bouton Reconnexion==================//
        panelBoutonReconnexion = new JPanel();
        panelBoutonReconnexion.setOpaque(false);
        panelBoutonReconnexion.setBounds(400,250,100,100);

        labelBoutonReconnexion = new JLabel("Votre partie est en cours,.\n veuillez appuyez sur le bouton reconnexion");
        labelBoutonReconnexion.setForeground(Color.WHITE);

        boutonReconnexion = new JButton("Reconnexion");
        panelBoutonReconnexion.add(boutonReconnexion);
        /*===============/Quitter/==============*/

        //Panel correspondant au bouton Quitter
        panelBoutonQuitter = new JPanel();
        panelBoutonQuitter.setOpaque(false);
        panelBoutonQuitter.setBounds(750,500,100,50);


        boutonQuitter = new JButton("Quitter");
        panelBoutonQuitter.add(boutonQuitter);


        //==================Ajout des panels============//
        panelReconnexion.add(panelBoutonReconnexion);
        panelReconnexion.add(panelBoutonQuitter);
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
            public void run(){
                FenetreReconnexion window = new FenetreReconnexion();
                window.halommou();
            }
        });
    }
}
