package com.hmstp.beans.Jeu;

import com.hmstp.beans.Client.Client;

public class Entreprise extends Role {
	private int valeur;
	private String nom;
	private boolean protection;
	
	public Entreprise(int valeur, String nom){
		this.valeur=valeur;
		this.nom=nom;
		this.protection=false;
	}
	
	public int getValeur(){
		return this.valeur;
	}
	
	public void setValeur(int x){
		this.valeur=x;
	}
	
	public String getNom(){
		return this.nom;
	}
	// pas besoin de set pour le nom, il est définitif
	
	public boolean getProtection(){
		return this.protection;
	}
	
	public void setProtection(boolean b){
		this.protection=b;
	}

	public void économiser(){
		setProtection(false);
	}//le joueur décide d'économiser et n'est donc plus protégé, équivalent dormir
	
	public void cyberProtection(){
		setProtection(true);
	}// le joueur investi pour la protection et se protège, équivalent mettre un piège


	public void choixAction(int i){
		if (i == 0){
			this.protection = false;
			this.setChoixFait(true);
		}
		else if (i == 1){
			this.protection = true;
			this.setChoixFait(true);
		}
	}

	public int retourneChoix(){
		if (this.protection){
			return 0;
		}
		else{
			return 1;
		}
	}
}
