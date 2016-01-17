package com.hmstp.beans.Jeu;

public class Hackeur extends Role{
	private int victime;
	private static Hackeur instance = new Hackeur(0);
	
	private Hackeur(int n){
		this.victime = 1;
		this.setNumero(n);
	}

	public static Hackeur getInstance(){
		return instance;
	}

	public int getVictime() {
		return victime;
	}
	public void setVictime(int j){
		this.victime=j;
	}

	public void choixAction(int i){
		this.victime = i;
		this.setChoixFait(true);
	}

	public int retourneChoix(){
		return this.victime;
	}

}
