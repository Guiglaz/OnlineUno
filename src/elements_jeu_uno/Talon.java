// Class modelisant le talon du jeu comme le paquet de toutes les cartes precedement posees et contruite a partir de la class PaquetCarte.
package elements_jeu_uno;

public class Talon extends PaquetCartes {

	// Fonction renvoyant le talon actuel du jeu.
	public Carte dessusTalon() {
		return this.paquet.get(this.paquet.size()-1);
	}
	
}
