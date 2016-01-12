package com.hmstp.beans.InterfaceGraphique;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Fenetre extends JPanel {
    private JFrame frame;
    private JPanel panel;
    private JPanel panelPseudo;
    private JLabel labelPseudo;
    private JTextField pseudo;

    private JPanel panelMdp;
    private JLabel labelMdp;
    private JPasswordField mdp;

    private JPanel panelBoutonLogin;
    private JButton boutonLogin;

    private JPanel panelBoutonQuitter;
    private JButton boutonQuitter;

    private JPanel panelBoutonInscription;
    private JButton boutonInscription;
    public void go(){
        frame = new JFrame("Hack Moi Si Tu Peux");;
        panel = new Fenetre();
        panel.setLayout(null);

        frame.add(panel);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        /*================/Pseudo/================*/

        //Panel correspondant au pseudo
        panelPseudo = new JPanel();
        panelPseudo.setOpaque(false);
        panelPseudo.setBounds(325,100,250,65);

        labelPseudo = new JLabel("Pseudo : ");
        labelPseudo.setForeground(Color.WHITE);
        pseudo = new JTextField();
        panelPseudo.setLayout(new BorderLayout());
        panelPseudo.add(labelPseudo,BorderLayout.NORTH);
        panelPseudo.add(pseudo, BorderLayout.SOUTH);



                /*================/Mot de Passe/===============*/

        //Pseudo correspondant au mot de passe
        panelMdp = new JPanel();
        panelMdp.setOpaque(false);
        panelMdp.setBounds(325,250,250,65);

        labelMdp = new JLabel("Mot de Passe : ");
        labelMdp.setForeground(Color.WHITE);
        mdp = new JPasswordField();
        panelMdp.setLayout(new BorderLayout());
        panelMdp.add(labelMdp,BorderLayout.NORTH);
        panelMdp.add(mdp, BorderLayout.SOUTH);

                /*================/Login/================*/


        //Panel correspondant au bouton Login
        panelBoutonLogin = new JPanel();
        panelBoutonLogin.setOpaque(false);
        panelBoutonLogin.setBounds(475,400,100,50);

        boutonLogin = new JButton("Se connecter");
        boutonLogin.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                panel.repaint();
            }
        });
        panelBoutonLogin.add(boutonLogin);

                /*===============/Inscription/================*/

        //Panel correspondant au bouton Inscription
        panelBoutonInscription = new JPanel();
        panelBoutonInscription.setOpaque(false);
        panelBoutonInscription.setBounds(325,400,100,50);

        boutonInscription = new JButton("S'inscrire");
        panelBoutonInscription.add(boutonInscription);

                /*===============/Quitter/==============*/

        //Panel correspondant au bouton Quitter
        panelBoutonQuitter = new JPanel();
        panelBoutonQuitter.setOpaque(false);
        panelBoutonQuitter.setBounds(750,500,100,50);


        boutonQuitter = new JButton("Quitter");
        panelBoutonQuitter.add(boutonQuitter);

                /*======================================*/

        panel.add(panelPseudo);
        panel.add(panelMdp);
        panel.add(panelBoutonInscription);
        panel.add(panelBoutonLogin);
        panel.add(panelBoutonQuitter);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Fenetre fenetre = new Fenetre();
                fenetre.go();
            }
        });
    }
}
