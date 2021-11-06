// Class utilise pour definir le talon, la pioche, et la main.


package elements_jeu_uno;

import java.util.ArrayList;
import java.util.Collections;




public class PaquetCartes {

	protected ArrayList<Carte> paquet;

	
	public PaquetCartes() {
		super();
		this.paquet = new ArrayList<Carte>();
	}

	
	// Fonction melangeant le paquet associe.
	public void melanger() {
		Collections.shuffle(this.paquet);		
	}
	
	// Fonction renvoyant la carte n du paquet et la retirant de celui-ci.
	public Carte prendreCarte(int n) {
		Carte carte = paquet.get(n);
		this.paquet.remove(n);
		
		return carte;	
	}
	
	// Fonction qui ajoute la carte donnee au paquet vise.
	public void addCarte(Carte maCarte) {
		this.paquet.add(maCarte);
	}
	
	// Fonction retirant la carte entree si celle-ci est dans le paquet de carte puis renvoie true, renvoie false si la carte n'est pas dans le paquet.
	public boolean removeCarte(Carte maCarte) {
		for(int i=0; i<this.size(); i++) {
			if(maCarte.equals(this.paquet.get(i))) {
				this.paquet.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Carte> getPaquet() {
		return paquet;
	}

	public void setPaquet(ArrayList<Carte> paquet) {
		this.paquet = paquet;
	}

	public String toString() {
		
		String paquetCarte = new String();
		
		for(int i=0; i<this.paquet.size() ; i++) {
			paquetCarte += this.paquet.get(i) + "\n";
		}
		return paquetCarte;
	}
	
	//Renvoie la taille du paquet de la classe/sous-classe 
	public int size() {
		return this.paquet.size();
	}
	
}
