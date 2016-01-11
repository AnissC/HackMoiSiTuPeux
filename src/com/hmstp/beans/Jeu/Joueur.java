package Jeu;

public class Joueur {
	private Role role;
	private int score;
	
	public Joueur(Role R,int score){
		this.role=R;
		this.score=score;
	}
	
	public void choixAction(){
		role.choixAction();
	}
}
