// Class modelisant la pioche et construite avec la class PaquetCarte.

package elements_jeu_uno;

import java.util.ArrayList;

public class Pioche extends PaquetCartes{
	
	// Constructeur generant directement un jeu complet de uno et melange le paquet ou un paquet précis selectionné par le parramètre i en entree
	public Pioche(int i) {
		super();
		if(i==0) {
			this.paquet = genJeuComplet();
			this.melanger();
		}
		else if(i==1) {
			this.paquet = new ArrayList<Carte>();
			for (int j=0;j<84; j++) {
				this.paquet.add(new Carte(2,"rouge"));
			}
		}
		else if(i == 2) {
			String[] piocheDeck = new String("7-rouge 4-bleu 2-rouge +2-jaune 6-rouge 6-vert 8-jaune 7-jaune 4-bleu +2-rouge 3-jaune +2-vert 1-jaune 7-vert 1-bleu +2-rouge 4-rouge 5-vert 2-vert 5-jaune 4-vert 5-rouge 4-vert 7-bleu 6-vert 6-bleu 9-bleu 2-jaune 8-vert 5-bleu 3-jaune 9-rouge 1-rouge0-jaune 0-vert 0-bleu 7-bleu 6-rouge 2-vert 9-jaune 5-jaune 2-bleu +2-vert 5-vert 1-vert 6-bleu 2-rouge 1-rouge 2-bleu +2-bleu 2-jaune 1-vert 8-rouge 9-vert 1-jaune 3-bleu 4-jaune 5-bleu 3-vert 4-jaune 8-vert 3-vert 7-jaune 9-bleu 7-vert 0-rouge 3-rouge 4-rouge 5-rouge 9-jaune 8-rouge 3-rouge 8-bleu 9-rouge 6-jaune 3-bleu 8-bleu 8-jaune +2-jaune 9-vert 1-bleu 7-rouge 6-jaune +2-bleu").split(" ");
		
			this.paquet = new ArrayList<Carte>();
			for(String carte : piocheDeck) {
				this.paquet.add(Carte.string2Carte(carte));
			}
		}
		else {
			this.paquet = genJeuComplet();
			this.melanger();
		}
		
	}
	
	

	// Fonction utilise dans le constructeur generant un paquet complet.
	private static ArrayList<Carte> genJeuComplet(){
		
		ArrayList<Carte> jeuComplet = new ArrayList<Carte>();
		ArrayList<String> couleurs = new ArrayList<String>();
		
		couleurs.add("rouge");
		couleurs.add("vert");
		couleurs.add("bleu");
		couleurs.add("jaune");
		
		for(String couleur : couleurs) {
			
			jeuComplet.add(new Carte(0, couleur));
			jeuComplet.add(new Carte("+",2, couleur));
			jeuComplet.add(new Carte("+",2, couleur));

			for(int num = 1; num<=9; num++) {
				jeuComplet.add(new Carte(num,couleur));
				jeuComplet.add(new Carte(num,couleur));
			}
		}
		return jeuComplet ;
	}
	
	// Fonction prennant la carte de dessus de la pioche simulant l'action de piocher.
	public Carte piocher() {
		return this.prendreCarte(0);
	}
}
