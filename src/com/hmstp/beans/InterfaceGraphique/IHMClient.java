package com.hmstp.beans.InterfaceGraphique;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IHMClient extends JPanel {
    private JFrame frame;
    private JPanel panel;
    private JPanel panelPseudo;
    private JLabel labelPseudo;
    private JTextField pseudo;

    private JPanel panelMdp;
    private JLabel labelMdp;
    private JPasswordField mdp;

    private JPanel panelMdpConfirme;
    private JLabel labelMdpConfirme;
    private JPasswordField mdpConfirme;

    private JPanel panelBoutonLogin;
    private JButton boutonLogin;

    private JPanel panelBoutonReconnexion;
    private JLabel labelBoutonReconnexion;
    private JButton boutonReconnexion;

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

    private JPanel panelBoutonInscription;
    private JButton boutonInscription;

    private JPanel panelBoutonAnnuler;
    private JButton boutonAnnuler;

    private String ecranAffichage = "connexion";


    public void go(){
        frame = new JFrame("Hack Moi Si Tu Peux !");
        panel = new IHMClient();
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
        panelPseudo.setBounds(325,50,250,65);

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
        panelMdp.setBounds(325,150,250,65);

        labelMdp = new JLabel("Mot de Passe : ");
        labelMdp.setForeground(Color.WHITE);
        mdp = new JPasswordField();
        panelMdp.setLayout(new BorderLayout());
        panelMdp.add(labelMdp,BorderLayout.NORTH);
        panelMdp.add(mdp, BorderLayout.SOUTH);

        panelMdpConfirme = new JPanel();
        panelMdpConfirme.setOpaque(false);
        panelMdpConfirme.setBounds(325,250,250,65);

        labelMdpConfirme = new JLabel("Confirmation de mot de passe : ");
        labelMdpConfirme.setForeground(Color.WHITE);
        mdpConfirme = new JPasswordField();
        panelMdpConfirme.setLayout(new BorderLayout());
        panelMdpConfirme.add(labelMdpConfirme,BorderLayout.NORTH);
        panelMdpConfirme.add(mdpConfirme, BorderLayout.SOUTH);


        //==============Bouton à 3==============//
        panelPartieA3 = new JPanel();
        panelPartieA3.setOpaque(false);
        panelPartieA3.setBounds(350,100,200,75);

        boutonPartieA3 = new JButton("Rejoindre une partie à 3");
        panelPartieA3.add(boutonPartieA3);

        //==============Bouton à 4==============//
        panelPartieA4 = new JPanel();
        panelPartieA4.setOpaque(false);
        panelPartieA4.setBounds(350,200,200,75);

        boutonPartieA4 = new JButton("Rejoindre une partie a 4");
        panelPartieA4.add(boutonPartieA4);

        //==============Bouton à 5==============//
        panelPartieA5 = new JPanel();
        panelPartieA5.setOpaque(false);
        panelPartieA5.setBounds(350,300,200,75);

        boutonPartieA5 = new JButton("Rejoindre une partie a 5");
        panelPartieA5.add(boutonPartieA5);

        //==============Bouton à 6===============//
        panelPartieA6 = new JPanel();
        panelPartieA6.setOpaque(false);
        panelPartieA6.setBounds(350,400,200,75);

        boutonPartieA6 = new JButton("Rejoindre une partie a 6");
        panelPartieA6.add(boutonPartieA6);


        /*================/Login/================*/


        //Panel correspondant au bouton Login
        panelBoutonLogin = new JPanel();
        panelBoutonLogin.setOpaque(false);
        panelBoutonLogin.setBounds(475,400,100,50);

        boutonLogin = new JButton("Se connecter");
        boutonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ecranAffichage = "menu";
                dessine();
            }
        });
        panelBoutonLogin.add(boutonLogin);

        /*================/Annuler/================*/


        //Panel correspondant au bouton Login
        panelBoutonAnnuler= new JPanel();
        panelBoutonAnnuler.setOpaque(false);
        panelBoutonAnnuler.setBounds(475,400,100,50);

        boutonAnnuler = new JButton("Annuler");
        boutonAnnuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ecranAffichage = "annuler";
                dessine();
            }
        });
        panelBoutonAnnuler.add(boutonAnnuler);

        //===========Bouton Reconnexion==================//
        panelBoutonReconnexion = new JPanel();
        panelBoutonReconnexion.setOpaque(false);
        panelBoutonReconnexion.setBounds(250,250,500,100);

        labelBoutonReconnexion = new JLabel("Votre partie est en cours... veuillez appuyez sur le bouton reconnexion");
        labelBoutonReconnexion.setForeground(Color.WHITE);

        boutonReconnexion = new JButton("Reconnexion");
        panelBoutonReconnexion.add(labelBoutonReconnexion, BorderLayout.NORTH);
        panelBoutonReconnexion.add(boutonReconnexion, BorderLayout.SOUTH);

        /*===============/Inscription/================*/

        //Panel correspondant au bouton Inscription
        panelBoutonInscription = new JPanel();
        panelBoutonInscription.setOpaque(false);
        panelBoutonInscription.setBounds(325,400,100,50);


        boutonInscription = new JButton("S'inscrire");
        boutonInscription.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ecranAffichage = "inscription";
                dessine();
            }
        });
        panelBoutonInscription.add(boutonInscription);

        /*===============/Quitter/==============*/

        //Panel correspondant au bouton Quitter
        panelBoutonQuitter = new JPanel();
        panelBoutonQuitter.setOpaque(false);
        panelBoutonQuitter.setBounds(750,500,100,50);


        boutonQuitter = new JButton("Quitter");
        boutonQuitter.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                frame.dispose();
            }
        });
        panelBoutonQuitter.add(boutonQuitter);

        /*======================================*/
        dessine();
    }
    public void dessine(){
        if (ecranAffichage.equals("connexion")){
            panel.add(panelPseudo);
            panel.add(panelMdp);
            panel.add(panelBoutonInscription);
            panel.add(panelBoutonLogin);
            panel.add(panelBoutonQuitter);
        }else if(ecranAffichage.equals("inscription")){
            panel.remove(panelBoutonLogin);
            panel.add(panelPseudo);
            panel.add(panelMdp);
            panel.add(panelMdpConfirme);
            panel.add(panelBoutonInscription);
            panel.add(panelBoutonAnnuler);
            panel.add(panelBoutonQuitter);
        }else if (ecranAffichage.equals("annuler")){
            panel.remove(panelMdpConfirme);
            panel.remove(panelBoutonAnnuler);
            panel.add(panelPseudo);
            panel.add(panelMdp);
            panel.add(panelBoutonInscription);
            panel.add(panelBoutonLogin);
            panel.add(panelBoutonQuitter);
        }else if (ecranAffichage.equals("inscrire")){
            panel.remove(panelPseudo);
            panel.remove(panelMdp);
            panel.remove(panelBoutonInscription);
            panel.remove(panelBoutonLogin);
            panel.add(panelBoutonQuitter);
            panel.add(panelBoutonReconnexion);
        }else if (ecranAffichage.equals("menu")){
            panel.add(panelBoutonQuitter);
            panel.add(panelPartieA3);
            panel.add(panelPartieA4);
            panel.add(panelPartieA5);
            panel.add(panelPartieA6);
            panel.remove(panelPseudo);
            panel.remove(panelMdp);
            panel.remove(panelBoutonInscription);
            panel.remove(panelBoutonLogin);
        }
        frame.validate();
        frame.repaint();
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
                IHMClient ihmClient = new IHMClient();
                ihmClient.go();
            }
        });
    }
}
