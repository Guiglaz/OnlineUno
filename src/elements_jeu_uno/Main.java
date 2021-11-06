//Class modelisant la main d'un joueur et construtie à partir de la class PaquetCarte.

package elements_jeu_uno;

import java.util.ArrayList;

public class Main extends PaquetCartes{

	
	
	public Main() {
		super();
		this.paquet = new ArrayList<Carte>();
	}

	// Focntion calculant le score d'une main.
	public int score() {
		int points = 0;
		for(Carte carte : this.paquet) {


			if(carte.getColor().equals("Joker")) {
				points += 50;
			}
			else if(carte.getEffet().equals("+")) {
				points += 20;
			}
			else {
				points += carte.getNumber();
			}
		}
		return points;
	}
	
	// Fonction distribuant a partir d'une pioche une main de depart de jeu d'un coup.
	// Pour un jeu parfaitement melanger, ditribuer carte par carte ou 7 carte d'un coup ne change pas.
	// Le plus simple etant de tirer une main d'un coup
//	public void creerMain(Pioche maPioche) {
//		
//		
//		for(int i=0 ; i<7 ; i++) {
//			this.addCarte(maPioche.piocher());
//		}
//		
//	}
	
}

