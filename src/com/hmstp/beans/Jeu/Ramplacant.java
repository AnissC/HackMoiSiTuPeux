package com.hmstp.beans.Jeu;


public class Ramplacant extends Participant{

    public Ramplacant(String n){
        super(n);
    }

    @Override
    public boolean isRemplacant() {
        return true;
    }
}
