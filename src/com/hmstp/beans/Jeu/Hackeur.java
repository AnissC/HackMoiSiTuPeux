package com.hmstp.beans.Jeu;

public class Hackeur extends Role{
	private Joueur victime;
	private static Hackeur instance = null;
	
	public Hackeur(){
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

	public void choixAction(){
		//appel graphique d l'action'
		System.out.println("J'ai choisi mon action !");
	}

}
