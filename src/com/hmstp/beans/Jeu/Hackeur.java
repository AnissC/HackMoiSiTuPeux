package com.hmstp.beans.Jeu;

public final class Hackeur extends Role{
	private Joueur victime;
	private static Hackeur instance = null;
	
	private Hackeur(){
		this.victime = null;
	}

	public static Hackeur getInstance(){
		synchronized(instance){
			if(instance == null){
				instance = new Hackeur();
			}
		}
		return instance;
	}
	
	public void getVictime(Joueur j){
		this.victime=j;
	}
	
	@Override
	public void choixAction(Joueur j){
		getVictime(j);
	}
	@Override
	public void choixAction() {
		
	}
}
