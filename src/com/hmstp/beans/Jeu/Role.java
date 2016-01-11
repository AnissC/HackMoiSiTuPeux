package com.hmstp.beans.Jeu;

public abstract class Role {

	private boolean choixFait = false;

	public boolean isChoixFait() {
		return choixFait;
	}
	public void setChoixFait(boolean choixFait) {
		this.choixFait = choixFait;
	}

	public abstract void choixAction();
}