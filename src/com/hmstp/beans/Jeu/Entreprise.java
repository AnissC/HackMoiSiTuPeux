package com.hmstp.beans.Jeu;

public class Entreprise extends Role {
	public int valeur;
	public String nom;
	public boolean protection;
	
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

	public int choixAction(){
		// appel choix action graphique
		System.out.println("J'ai choisi mon action");
		this.setChoixFait(true);
		return -1;
	}

}
