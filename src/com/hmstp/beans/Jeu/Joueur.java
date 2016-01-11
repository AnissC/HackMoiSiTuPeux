package com.hmstp.beans.Jeu;

public class Joueur {
	private Role role;
	private int score;
	private boolean perdant;
	
	public Joueur(int s){
		this.score = s;
		this.perdant=false;
	}

	public void choixAction(){
		role.choixAction();
	}

}
