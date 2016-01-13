package com.hmstp.beans.Jeu;

public class Hackeur extends Role{
	private int victime;
	private static Hackeur instance = null;
	
	private Hackeur(){
		this.victime = -1;
	}

	public static Hackeur getInstance(){
		synchronized(instance){
			if(instance == null){
				instance = new Hackeur();
			}
		}
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
	}

}
