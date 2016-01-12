package com.hmstp.beans.Jeu;


import java.net.Socket;

public class Joueur extends Participant{
		private Socket sock;


	public Joueur(Socket so, String n){
		super(n);
		this.sock = so;
	}

	public void choixAction() {
		this.getRole().choixAction();
	}

	@Override
	public boolean isRemplacant() {
		return false;
	}

}
