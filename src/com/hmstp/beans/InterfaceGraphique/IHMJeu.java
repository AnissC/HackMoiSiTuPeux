package com.hmstp.beans.InterfaceGraphique;


import com.hmstp.beans.Client.Client;
import com.hmstp.beans.Jeu.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class IHMJeu extends JPanel{
    public static final String IHM_VIDE= "IHM_VIDE";
    public static final String IHM_HACKEUR = "IHM_HACKEUR";
    public static final String IHM_ENTREPRISE = "IHM_ENTREPRISE";
    public static final String IHM_ASSIGNE_ROLE = "IHM_ASSIGNE_ROLE";
    public static final String IHM_ENATTENTE = "IHM_ENATTENTE";

    private int numJoueur=0;

    private JFrame frameJeu;
    private JPanel panelJeu;

    private JPanel panelTableau;

    private JPanel panelInfo;
    private JLabel labelInfo;

    private JPanel panelBoutons;

    private JPanel panelVictime1;
    private JLabel labelVictime1;
    private JButton boutonVictime1;
    private JLabel labelVictime1Valeur;

    private JPanel panelVictime2;
    private JLabel labelVictime2;
    private JButton boutonVictime2;
    private JLabel labelVictime2Valeur;

    private JPanel panelVictime3;
    private JLabel labelVictime3;
    private JButton boutonVictime3;
    private JLabel labelVictime3Valeur;

    private JPanel panelVictime4;
    private JLabel labelVictime4;
    private JButton boutonVictime4;
    private JLabel labelVictime4Valeur;

    private JPanel panelVictime5;
    private JLabel labelVictime5;
    private JButton boutonVictime5;
    private JLabel labelVictime5Valeur;


    private JPanel panelVictime6;
    private JLabel labelVictime6;
    private JButton boutonVictime6;
    private JLabel labelVictime6Valeur;

    private JPanel panelEconomiser;
    private JLabel labelEconomiser;
    private JButton boutonEconomiser;

    private JPanel panelSeProteger;
    private JLabel labelSeProteger;
    private JButton boutonSeProteger;

    private JPanel panelParticipant1;
    private JLabel labelParticipant1;
    private JButton boutonParticipant1;

    private JPanel panelParticipant2;
    private JLabel labelParticipant2;
    private JButton boutonParticipant2;

    private JPanel panelParticipant3;
    private JLabel labelParticipant3;
    private JButton boutonParticipant3;

    private JPanel panelParticipant4;
    private JLabel labelParticipant4;
    private JButton boutonParticipant4;

    private JPanel panelParticipant5;
    private JLabel labelParticipant5;
    private JButton boutonParticipant5;

    private JPanel panelParticipant6;
    private JLabel labelParticipant6;
    private JButton boutonParticipant6;

    private String ecranAffichage = IHM_VIDE;
    public int suiviAssignationRole =0;
    private Client client;

    public void setClient(Client client){
        this.client = client;
    }


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
        panelTableau.setBounds(50,25,550,200);

        //========================Panel info=============================//
        panelInfo = new JPanel();
        panelInfo.setOpaque(true);
        panelInfo.setBounds(630,25,200,200);

        labelInfo = new JLabel();
        labelInfo.setLayout(new BorderLayout());
        panelInfo.add(labelInfo,BorderLayout.NORTH);

        //========================Panel Bouton============================//
        panelBoutons = new JPanel();
        panelBoutons.setOpaque(true);
        panelBoutons.setBounds(50,300,800,200);

        //==================Bouton Economiser=============//
        panelEconomiser = new JPanel();
        panelEconomiser.setOpaque(false);

        labelEconomiser = new JLabel("Economiser");


        boutonEconomiser = new JButton("Economiser");
        boutonEconomiser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.choixAction(0);
            }
        });
        panelEconomiser.setLayout(new BorderLayout());
        panelEconomiser.add(labelEconomiser,BorderLayout.NORTH);
        panelEconomiser.add(boutonEconomiser,BorderLayout.SOUTH);


        //==============Bouton SeProteger============//
        panelSeProteger = new JPanel();
        panelSeProteger.setOpaque(false);

        labelSeProteger = new JLabel("Se Proteger");

        boutonSeProteger = new JButton("Se Proteger");
        boutonSeProteger.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.choixAction(1);
            }
        });
        panelSeProteger.setLayout(new BorderLayout());
        panelSeProteger.add(labelSeProteger,BorderLayout.NORTH);
        panelSeProteger.add(boutonSeProteger,BorderLayout.SOUTH);

        dessine();
    }


    public void changeLabelInfo(int u){
        switch (u) {
            case 1:
                labelInfo.setText("<html><body>Veuillez choisir <br /> La Moyenne Entreprise</body></html>");
                panelInfo.repaint();
                break;
            case 2:
                labelInfo.setText("<html><body>Veuillez choisir <br /> la Petite Entreprise</body></html>");
                panelInfo.repaint();
                break;
            case 3:
                labelInfo.setText("<html><body>Veuillez choisir <br /> la Grande Entreprise</body></html>");
                panelInfo.repaint();
                break;
            case 4:
                labelInfo.setText("<html><body>Veuillez choisir <br /> la Petite Entreprise</body></html>");
                panelInfo.repaint();
                break;
            case 5:
                labelInfo.setText("<html><body>Veuillez choisir <br /> la Petite Entreprise</body></html>");
                panelInfo.repaint();
                break;
        }
        panelInfo.repaint();
    }
    public void dessine(){
        int max = 0;
        suiviAssignationRole = 0;
        if(! ecranAffichage.equals(IHM_VIDE)) {
            panelTableau.removeAll();
            ArrayList<Participant> joueurs = client.setListParticipant();
            max = joueurs.size();
            numJoueur = 0;
            if (ecranAffichage.equals(IHM_HACKEUR)){
                //======================Victime 1=============//
                panelVictime1 = new JPanel();
                panelVictime1.setOpaque(false);

                labelVictime1 = new JLabel("Victime 1");
                if (!ecranAffichage.equals(IHM_ASSIGNE_ROLE) && client.getRoleParNom(joueurs.get(numJoueur).getNom()) instanceof Hackeur){
                    numJoueur ++;
                }
                labelVictime1Valeur = new JLabel(Integer.toString(numJoueur));
                labelVictime1Valeur.setVisible(false);
                boutonVictime1 = new JButton(joueurs.get(numJoueur).getNom());
                boutonVictime1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        client.choixAction(Integer.parseInt(labelVictime1Valeur.getText()));
                    }
                });
                numJoueur++;
                panelVictime1.setLayout(new BorderLayout());
                panelVictime1.add(labelVictime1, SpringLayout.NORTH);
                panelVictime1.add(boutonVictime1,SpringLayout.SOUTH);
                //=====================Victime 2==============//
                panelVictime2 = new JPanel();
                panelVictime2.setOpaque(false);

                labelVictime2 = new JLabel("Victime 2");

                if (!ecranAffichage.equals(IHM_ASSIGNE_ROLE) && client.getRoleParNom(joueurs.get(numJoueur).getNom()) instanceof Hackeur){
                    numJoueur ++;
                }
                labelVictime2Valeur = new JLabel(Integer.toString(numJoueur));
                labelVictime2Valeur.setVisible(false);
                boutonVictime2 = new JButton(joueurs.get(numJoueur).getNom());
                boutonVictime2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        client.choixAction(Integer.parseInt(labelVictime2Valeur.getText()));
                    }
                });
                numJoueur++;
                panelVictime2.setLayout(new BorderLayout());
                panelVictime2.add(labelVictime2,BorderLayout.NORTH);
                panelVictime2.add(boutonVictime2,BorderLayout.SOUTH);
                if (max > 3){
                    //=====================Victime 3==============//
                    panelVictime3 = new JPanel();
                    panelVictime3.setOpaque(false);
                    labelVictime3 = new JLabel("Victime 3");
                    if (!ecranAffichage.equals(IHM_ASSIGNE_ROLE) && client.getRoleParNom(joueurs.get(numJoueur).getNom()) instanceof Hackeur){
                        numJoueur ++;
                    }
                    labelVictime3Valeur = new JLabel(Integer.toString(numJoueur));
                    labelVictime3Valeur.setVisible(false);
                    boutonVictime3 = new JButton(joueurs.get(numJoueur).getNom());// embeded ligne cancer
                    boutonVictime3.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            client.choixAction(Integer.parseInt(labelVictime3Valeur.getText()));
                        }
                    });
                    numJoueur++;
                    panelVictime3.setLayout(new BorderLayout());
                    panelVictime3.add(labelVictime3,BorderLayout.NORTH);
                    panelVictime3.add(boutonVictime3,BorderLayout.SOUTH);
                    panelVictime3.add(labelVictime3Valeur);
                }
                if (max > 4){
                    //=====================Victime 4==============//
                    panelVictime4 = new JPanel();
                    panelVictime4.setOpaque(false);
                    labelVictime4 = new JLabel("Victime 4");
                    if (!ecranAffichage.equals(IHM_ASSIGNE_ROLE) && client.getRoleParNom(joueurs.get(numJoueur).getNom()) instanceof Hackeur){
                        numJoueur ++;
                    }
                    labelVictime4Valeur = new JLabel(Integer.toString(numJoueur));
                    labelVictime4Valeur.setVisible(false);
                    boutonVictime4 = new JButton(joueurs.get(numJoueur).getNom());
                    boutonVictime4.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            client.choixAction(Integer.parseInt(labelVictime4Valeur.getText()));
                        }
                    });
                    numJoueur++;
                    panelVictime4.setLayout(new BorderLayout());
                    panelVictime4.add(labelVictime4,BorderLayout.NORTH);
                    panelVictime4.add(boutonVictime4,BorderLayout.SOUTH);
                    panelVictime4.add(labelVictime4Valeur);
                }
                if (max > 5){
                    //=====================Victime 5==============//
                    panelVictime5 = new JPanel();
                    panelVictime5.setOpaque(false);
                    labelVictime5 = new JLabel("Victime 5");
                    if (!ecranAffichage.equals(IHM_ASSIGNE_ROLE) && client.getRoleParNom(joueurs.get(numJoueur).getNom()) instanceof Hackeur){
                        numJoueur ++;
                    }
                    labelVictime5Valeur = new JLabel(Integer.toString(numJoueur));
                    labelVictime5Valeur.setVisible(false);
                    boutonVictime5 = new JButton(joueurs.get(numJoueur).getNom());
                    boutonVictime5.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            client.choixAction(Integer.parseInt(labelVictime5Valeur.getText()));
                        }
                    });
                    numJoueur++;
                    panelVictime5.setLayout(new BorderLayout());
                    panelVictime5.add(labelVictime5,BorderLayout.NORTH);
                    panelVictime5.add(boutonVictime5,BorderLayout.SOUTH);
                    panelVictime5.add(labelVictime5Valeur);
                }
            }else if (ecranAffichage.equals(IHM_ASSIGNE_ROLE)){
                //============Bouton Participant 1======================//
                panelParticipant1 = new JPanel();
                panelParticipant1.setOpaque(false);

                labelParticipant1 = new JLabel(joueurs.get(0).getNom());

                boutonParticipant1 = new JButton(joueurs.get(0).getNom());
                boutonParticipant1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        client.choixDistribution(joueurs.get(0),suiviAssignationRole);
                        suiviAssignationRole++;
                        changeLabelInfo(suiviAssignationRole);
                        panelBoutons.remove(panelParticipant1);
                        panelBoutons.repaint();

                    }
                });
                panelParticipant1.setLayout(new BorderLayout());
                panelParticipant1.add(labelParticipant1, BorderLayout.NORTH);
                panelParticipant1.add(boutonParticipant1,BorderLayout.SOUTH);

                //============Bouton Participant 2======================//
                panelParticipant2 = new JPanel();
                panelParticipant2.setOpaque(false);

                labelParticipant2 = new JLabel(joueurs.get(1).getNom());

                boutonParticipant2 = new JButton(joueurs.get(1).getNom());
                boutonParticipant2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        client.choixDistribution(joueurs.get(1),suiviAssignationRole);
                        suiviAssignationRole++;
                        changeLabelInfo(suiviAssignationRole);
                        panelBoutons.remove(panelParticipant2);
                        panelBoutons.repaint();
                    }
                });
                panelParticipant2.setLayout(new BorderLayout());
                panelParticipant2.add(labelParticipant2, BorderLayout.NORTH);
                panelParticipant2.add(boutonParticipant2,BorderLayout.SOUTH);
                if (max > 3) {
                    //============Bouton Participant 3======================//

                    panelParticipant3 = new JPanel();
                    panelParticipant3.setOpaque(false);

                    labelParticipant3 = new JLabel(joueurs.get(3).getNom());

                    boutonParticipant3 = new JButton(joueurs.get(3).getNom());
                    boutonParticipant3.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            client.choixDistribution(joueurs.get(3),suiviAssignationRole);
                            suiviAssignationRole++;
                            changeLabelInfo(suiviAssignationRole);
                            panelBoutons.remove(panelParticipant3);
                            panelBoutons.repaint();
                        }
                    });
                    panelParticipant3.setLayout(new BorderLayout());
                    panelParticipant3.add(labelParticipant3, BorderLayout.NORTH);
                    panelParticipant3.add(boutonParticipant3, BorderLayout.SOUTH);
                }
                //============Bouton Participant 4======================//
                if(max>4){
                    panelParticipant4 = new JPanel();
                    panelParticipant4.setOpaque(false);

                    labelParticipant4 = new JLabel(joueurs.get(4).getNom());

                    boutonParticipant4 = new JButton(joueurs.get(4).getNom());
                    boutonParticipant4.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            client.choixDistribution(joueurs.get(4),suiviAssignationRole);
                            suiviAssignationRole++;
                            changeLabelInfo(suiviAssignationRole);
                            panelBoutons.remove(panelParticipant4);
                            panelBoutons.repaint();
                        }
                    });
                    panelParticipant4.setLayout(new BorderLayout());
                    panelParticipant4.add(labelParticipant4, BorderLayout.NORTH);
                    panelParticipant4.add(boutonParticipant4,BorderLayout.SOUTH);
                }
                //============Bouton Participant 5======================//
                if(max>5) {
                    panelParticipant5 = new JPanel();
                    panelParticipant5.setOpaque(false);

                    labelParticipant5 = new JLabel(joueurs.get(5).getNom());

                    boutonParticipant5 = new JButton(joueurs.get(5).getNom());
                    boutonParticipant5.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            client.choixDistribution(joueurs.get(5), suiviAssignationRole);
                            suiviAssignationRole++;
                            changeLabelInfo(suiviAssignationRole);
                            panelBoutons.remove(panelParticipant5);
                            panelBoutons.repaint();
                        }
                    });
                    panelParticipant5.setLayout(new BorderLayout());
                    panelParticipant5.add(labelParticipant5, BorderLayout.NORTH);
                    panelParticipant5.add(boutonParticipant5, BorderLayout.SOUTH);
                }
                //============Bouton Participant 6======================//
                if(ecranAffichage.equals(IHM_ASSIGNE_ROLE)) {
                    panelParticipant6 = new JPanel();
                    panelParticipant6.setOpaque(false);
                    labelParticipant6 = new JLabel(joueurs.get(2).getNom());
                    boutonParticipant6 = new JButton(joueurs.get(2).getNom());
                    boutonParticipant6.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            client.choixDistribution(joueurs.get(2), suiviAssignationRole);
                            suiviAssignationRole++;
                            changeLabelInfo(suiviAssignationRole);
                            panelBoutons.remove(panelParticipant6);
                            panelBoutons.repaint();
                        }
                    });
                    panelParticipant6.setLayout(new BorderLayout());
                    panelParticipant6.add(labelParticipant6, BorderLayout.NORTH);
                    panelParticipant6.add(boutonParticipant6, BorderLayout.SOUTH);
                }
            }

            //=====================Tableau Score =============================//
            String[][] donnees = new String[joueurs.size()][3];
            for (int i = 0; i < joueurs.size(); i++) {
                donnees[i][0] = joueurs.get(i).getNom();
                if (joueurs.get(i).getRole() instanceof Hackeur) {
                    donnees[i][1] = "Hackeur";
                }
                else{
                    donnees[i][1] = ((Entreprise)joueurs.get(i).getRole()).getNom();
                }
                donnees[i][2] = "" + client.score(joueurs.get(i).getNom());
            }
            if(client.getRoleParNom(client.getNom()) instanceof Hackeur && ecranAffichage.equals(IHM_HACKEUR)){
                labelInfo.setText("<html><body> Vous Ítes Hackeur,<br/>"+" veuillez choisir votre victime</body></html>");
            }else if (ecranAffichage.equals(IHM_ENTREPRISE)){
                labelInfo.setText("<html><body> Vous Ítes une entreprise;<br/>"+" veuillez choisir votre action :<br/> - Èconomiser,<br/> - se protÈger</body>>/html>");
            }else if (ecranAffichage.equals(IHM_ASSIGNE_ROLE)){
                labelInfo.setText("<html><body>Veuillez choisir <br /> le Hacker</body></html>");
            }else if (ecranAffichage.equals(IHM_ENATTENTE)){
                labelInfo.setText("<html><body>En attente d'un joueur</body></html>");
            }
            String[] entetes = {"Nom", "Role", "Score"};
            JTable tableau = new JTable(donnees, entetes);
            tableau.setSize(200, 200);
            tableau.setVisible(true);
            panelTableau.setLayout(new BorderLayout());
            panelTableau.add(tableau.getTableHeader(), BorderLayout.NORTH);
            panelTableau.add(tableau, BorderLayout.CENTER);
            panelJeu.add(panelTableau);
        }

        if (ecranAffichage.equals(IHM_ASSIGNE_ROLE)){
            panelBoutons.removeAll();
            panelBoutons.add(panelParticipant1);
            panelBoutons.add(panelParticipant2);
            panelBoutons.add(panelParticipant6);
            if (max > 3){
                panelBoutons.add(panelParticipant3);

                if (max > 4){
                    panelBoutons.add(panelParticipant4);

                    if (max > 5){
                        panelBoutons.add(panelParticipant5);
                    }
                }
            }
        }else if(ecranAffichage.equals(IHM_HACKEUR)){
            panelBoutons.removeAll();
            panelBoutons.add(panelVictime1);
            panelBoutons.add(panelVictime2);
            if (max > 3){
                panelBoutons.add(panelVictime3);
                if (max > 4){
                    panelBoutons.add(panelVictime4);
                    if (max > 5){
                        panelBoutons.add(panelVictime5);
                    }
                }
            }
        }else if (ecranAffichage.equals(IHM_ENTREPRISE)){
            panelBoutons.removeAll();
            panelBoutons.add(panelEconomiser);
            panelBoutons.add(panelSeProteger);
        }else/*(ecranAffichage.equals(IHM_ENATTENTE))*/{
            panelBoutons.removeAll();
            System.out.println("removeAll - Marche");
        }
        //=====================Ajout de tous les panels===================//
        panelJeu.add(panelInfo);
        panelJeu.add(panelBoutons);

        frameJeu.validate();
        frameJeu.repaint();
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
