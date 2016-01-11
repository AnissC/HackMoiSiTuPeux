package com.hmstp.beans.Jeu;

import java.net.Socket;

public class Joueur {
	private Role role;
	private int score;
	private Socket sock;
	
	public Joueur(Socket s){
		this.score = 0;
		this.sock = s;
	}

	public void choixAction(){
		role.choixAction();
	}

	public void setRole(Role r){
		this.role = r;
	}
}
