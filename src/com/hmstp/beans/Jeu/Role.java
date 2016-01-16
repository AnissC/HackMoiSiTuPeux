package com.hmstp.beans.Jeu;

import com.hmstp.beans.Client.Client;

public abstract class Role implements java.io.Serializable{

	private boolean choixFait = false;

	private int numero = -1;

	public boolean isChoixFait() {
		return choixFait;
	}

	public void setChoixFait(boolean choixFait) {
		this.choixFait = choixFait;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public void choixAction(Client c){
			c.choixAction(this);
	}

	public abstract void choixAction(int i);

	public abstract int retourneChoix();

	public void remmettreZero(){
		setChoixFait(false);
	}
}