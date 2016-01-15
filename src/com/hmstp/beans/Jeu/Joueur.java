package com.hmstp.beans.Jeu;


import com.hmstp.beans.Client.Client;

import java.net.Socket;

public class Joueur extends Participant{
	private Socket sock;


	public Joueur(Socket so, String n){
		super(n);
		this.sock = so;
	}

	public Socket getSock() {
		return sock;
	}


	public void choixAction(Client c) {
		this.getRole().choixAction(c);
	}

	@Override
	public boolean isRemplacant() {
		return false;
	}

}
