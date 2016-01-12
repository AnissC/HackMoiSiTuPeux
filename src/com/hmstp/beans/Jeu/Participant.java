package com.hmstp.beans.Jeu;


public abstract class Participant {
    private Role role;
    private int score;
    private String nom;

    public Participant(String n){
        this.role = null;
        this.score = 0;
        this.nom = n;
    }

    public int getScore() {
        return score;
    }
    public void changeScore(int score) {
        this.score += score;
        if(this.score < 0){
            this.score = 0;
        }
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role r){
        this.role = r;
    }

    public String getNom() {
        return nom;
    }

    public abstract boolean isRemplacant();
}
