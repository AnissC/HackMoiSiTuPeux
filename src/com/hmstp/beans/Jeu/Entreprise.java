package com.hmstp.beans.Jeu;

import com.hmstp.beans.Client.Client;
/** Cette classe représente les 3 roles des victimes :
 * - une grande entreprise vaut 3 points, équivalent au chaperon rouge;
 * - une moyenne entreprise vaut  2 points, équivalent au chevreau;
 * - une petite entreprise vaut  1 point, équivalent au petit cochon.*/

public class Entreprise extends Role {
	private int valeur;
	private String nom;
	private boolean protection;
	
	public Entreprise(int valeur, String nom, int n){
		this.valeur=valeur;
		this.nom=nom;
		this.protection=true;
		this.setNumero(n);
	}
	
	public int getValeur(){
		return this.valeur;
	}
	
	public String getNom(){
		return this.nom;
	}
	// pas besoin de set pour le nom, il est définitif

	public boolean getProtection(){
		boolean p;
		synchronized (this) {
			p = this.protection;
		}
		return p;
	}


	public void choixAction(int i){
		if (i == 0){
			this.protection = false;
			this.setChoixFait(true);
		}
		else{
			this.protection = true;
			this.setChoixFait(true);
		}
	}

	public int retourneChoix(){
		if (this.protection) {
			return 1;
		} else {
			return 0;
		}
	}
}
