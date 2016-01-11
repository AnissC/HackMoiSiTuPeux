package com.hmstp.beans.Jeu;

public final class Hackeur extends Role{
	private Joueur victime;
	private static Hackeur instance =null;
	
	private Hackeur(){
		this.victime=null;
	}
	public final static Hackeur getInstance(){
		if(Hackeur.instance == null){
			synchronized(Hackeur.class){
				if(Hackeur.class == null){
					Hackeur.instance = new Hackeur();
				}
			}
		}
		return Hackeur.instance;
	}
	
	public void getVictime(Joueur jj){
		this.victime=jj;
	}
	
	@Override
	public void choixAction(Joueur j){
		getVictime(j);
	}
	@Override
	public void choixAction() {
		
	}
}
