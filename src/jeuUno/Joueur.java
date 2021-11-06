// Class representant le joueur de uno.

package jeuUno;

import elements_jeu_uno.Carte;
import elements_jeu_uno.Main;

public class Joueur {
	
	private String nom;
	private Main maMain;
	private int score;
	private Carte dessusTalon;
	
	public Joueur() {
		this.score = 0;
		this.maMain = new Main();
		this.nom = "";
		this.dessusTalon = new Carte();
	}
	
	public Joueur(String nom) {
		this.score = 0;
		this.maMain = new Main();
		this.nom = nom;
		this.dessusTalon = new Carte();
	}
	
	public Joueur(String nom, Main maMain) {
		super();
		this.nom = nom;
		this.maMain = maMain;
		this.score = 0;
		this.dessusTalon = new Carte();

	}

	
	
	public Carte getDessusTalon() {
		return dessusTalon;
	}

	public void setDessusTalon(Carte dessusTalon) {
		this.dessusTalon = dessusTalon;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Main getMaMain() {
		return maMain;
	}

	public void setMaMain(Main maMain) {
		this.maMain = maMain;
	}

	public int getScore() {
		return score;
	}


	
	public String toString() {
		return this.nom;
	}
	
	
	// Ajoute au score du joueur les points de sa main.
	public void ajoutScore() {
		this.score += this.maMain.score();
	}
	
}
