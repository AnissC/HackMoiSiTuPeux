package com.hmstp.beans.Jeu;

public class Partie {
    private static final int NB3 = 3;
    private static final int NB4 = 4;
    private static final int NB5 = 5;
    private static final int NB6 = 6;

    private int nbParticipants;

    private Partie(int x){
        this.nbParticipants=x;
        Joueur[] tab;
        tab=new Joueur[nbParticipants];
        for(int i=0;i<nbParticipants;i++){
            tab[i]=new Joueur(0);
        }
    }

    private void distributionRoleManche1(){
        if(nbParticipants==NB3){
            this.tab[0].role=new Hackeur();
            this.tab[1].role=new Entreprise(2,"Chevreau");
            this.tab[2].role=new Entreprise(1,"Petit Cochon 1");
        }
        if(nbParticipants==NB4){
            this.tab[0].role=new Hackeur();
            this.tab[1].role=new Entreprise(3,"Chaperon Rouge");
            this.tab[2].role=new Entreprise(2,"Chevreau");
            this.tab[3].role=new Entreprise(1,"Petit Cochon 1");
        }
        if(nbParticipants==NB5){
            this.tab[0].role=new Hackeur();
            this.tab[1].role=new Entreprise(3,"Chaperon Rouge");
            this.tab[2].role=new Entreprise(2,"Chevreau");
            this.tab[3].role=new Entreprise(1,"Petit Cochon 1");
            this.tab[4].role=new Entreprise(1,"Petit Cochon 2");
        }
        if(nbParticipants==NB6){
            this.tab[0].role=new Hackeur();
            this.tab[1].role=new Entreprise(3,"Chaperon Rouge");
            this.tab[2].role=new Entreprise(2,"Chevreau");
            this.tab[3].role=new Entreprise(1,"Petit Cochon 1");
            this.tab[4].role=new Entreprise(1,"Petit Cochon 2");
            this.tab[5].role=new Entreprise(1,"Petit Cochon 3");
        }
    }
    private void assigneRole(Joueur j,Role r){
        for(int i=0;i<tab.length();i++){
            tab[i].role=r;
        }
    }
}
