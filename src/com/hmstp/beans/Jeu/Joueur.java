package com.hmstp.beans.Jeu;

public class Joueur {
	private Role role;
	private int score;
	
	public Joueur(Role R,int s){
		this.role = R;
		this.score = s;
	}
	
	public void choixAction(){
		role.choixAction();
	}
}
