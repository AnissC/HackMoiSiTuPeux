package com.hmstp.beans.Jeu;

import com.hmstp.beans.Client.Client;

public abstract class Role {

	private boolean choixFait = false;

	public boolean isChoixFait() {
		return choixFait;
	}

	public void setChoixFait(boolean choixFait) {
		this.choixFait = choixFait;
	}

	public void choixAction(){
			Client.choixAction(this);
	}

	public abstract void choixAction(int i);

	public abstract int retourneChoix();

	public void remmettreZero(){
		this.choixFait = false;
	}
}