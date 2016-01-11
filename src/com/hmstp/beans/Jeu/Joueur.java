package com.hmstp.beans.Jeu;

import java.net.Socket;

public class Joueur extends Participant{
		private Socket sock;


	public Joueur(Socket s, String n){
		super(n);
		this.sock = s;
	}

	public void choixAction() {
		this.getRole().choixAction();
	}
}
