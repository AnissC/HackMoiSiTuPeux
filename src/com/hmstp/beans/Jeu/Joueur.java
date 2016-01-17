package com.hmstp.beans.Jeu;

/** Cette classe permet de récupérer la socket de faire le lien entre la socket et le nom du joueur*/
import com.hmstp.beans.Client.Client;

import java.net.Socket;

public class Joueur extends Participant{
	private transient Socket sock;


	public Joueur(Socket so, String n){
		super(n);
		this.sock = so;
	}

	public Socket getSock() {
		return sock;
	}


	@Override
	public boolean isRemplacant() {
		return false;
	}

}
