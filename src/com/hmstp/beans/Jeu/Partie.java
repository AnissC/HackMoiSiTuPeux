package com.hmstp.beans.Jeu;

public class Partie {
    private static final int NB4 = 4;
    private static final int NB5 = 5;
    private static final int NB6 = 6;

    private int nbParticipants;
    private Joueur[] tab;

    private Partie(int x){
        this.nbParticipants=x;
        tab=new Joueur[nbParticipants];
        for(int i=0;i<nbParticipants;i++){
            tab[i]=new Joueur(0);
        }
    }

    private void distributionRoleManche1(){
        this.tab[0].setRole(Hackeur.getInstance());
        this.tab[1].setRole( new Entreprise(2,"Moyenne Entreprise"));
        this.tab[2].setRole(new Entreprise(1,"PME"));

        if(this.nbParticipants >= 4){
            this.tab[3].setRole( new Entreprise(3, "Entrepise internationalle"));
            if (this.nbParticipants >= 5){
                this.tab[4].setRole( new Entreprise(1, "PME"));
                if (this.nbParticipants == 6) {
                    this.tab[5].setRole( new Entreprise(1, "PME"));
                }
            }
        }
    }

    private void assigneRole(Joueur j, Role r){
        for(int i=0;i<nbParticipants;i++){
            tab[i].setRole(r);
        }
    }
}
