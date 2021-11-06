// Class modelisant une manche de uno avec toutes les composantes de la manche en attribut (pioche, talon, joueurs).

package jeuUno;

import elements_jeu_uno.Pioche;
import elements_jeu_uno.Talon;

public class MancheUno {
	
	private Talon talonManche;
	private Pioche piocheManche;
	private Joueur[] listJoueur;
	
	// Constructeur generant la pioche, les mains de chaque joueurs et le talon.
	public MancheUno(Joueur[] listJoueur, int i) {
		this.listJoueur = listJoueur;
		this.piocheManche = new Pioche(i);
		this.talonManche = new Talon();
		for(int numCarte = 0; numCarte<7; numCarte++) {
			for(Joueur joueur : listJoueur) {
				joueur.getMaMain().addCarte(this.piocheManche.piocher());
			}
		}
		this.talonManche.addCarte(this.piocheManche.piocher());
	}
	
	
	public Talon getTalonManche() {
		return talonManche;
	}
	public void setTalonManche(Talon talonManche) {
		this.talonManche = talonManche;
	}
	public Pioche getPiocheManche() {
		return piocheManche;
	}
	public void setPiocheManche(Pioche piocheManche) {
		this.piocheManche = piocheManche;
	}
	public Joueur[] getListJoueur() {
		return listJoueur;
	}
	public void setListJoueur(Joueur[] listJoueur) {
		this.listJoueur = listJoueur;
	}
	
	// Fonction faisant piocher le joueur place en position "rang" dans l'attribut listJoueur de la class.
	// Si la pioche est vide, cette fonction recree la pioche avec les cartes du talon (sauf celle du dessus).
	public void piocher(int rang) {
		if(this.piocheManche.size() == 0) {
			this.piocheManche.setPaquet(this.talonManche.getPaquet());
			this.talonManche.addCarte(this.piocheManche.getPaquet().get(this.piocheManche.size()-1));
			this.piocheManche.melanger();
		}
		this.listJoueur[rang].getMaMain().addCarte(this.piocheManche.piocher());
	}
	
	// Fonction verifiant si la partie est terminant ou non en verifiant le nombre de carte de chaque joueur.
	public boolean isWin() {
		for(Joueur joueur : this.listJoueur) {
			if(joueur.getMaMain().size()==0) {
				for(Joueur player : this.listJoueur) {
					player.ajoutScore();
				}
				return true;
			}
		}
		return false;
	}
}
