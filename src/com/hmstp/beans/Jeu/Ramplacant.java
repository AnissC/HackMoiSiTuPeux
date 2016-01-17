package com.hmstp.beans.Jeu;

/** Cette classe contient */
public class Ramplacant extends Participant{

    public Ramplacant(String n){
        super(n);
    }

    @Override
    public boolean isRemplacant() {
        return true;
    }
}
