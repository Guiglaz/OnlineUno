
package executables;

import java.io.IOException;

import jeuReseau.Serveur;

public class ServeurMain {

	public static void main(String[] args) throws IOException {
		
		
		int genPioche = 2; // Parametre de choix de la pioche : 0 -> jeu aleatoire   1 -> seulement des cartes 2-rouge   2 -> pioche issue de "deck-numbers-plus2"
		
		
		Serveur monServeur = new Serveur(); // Initialise le serveur et attend la connexion des clients.
		
		monServeur.jouerPartie(genPioche);  // Joue une partie de UNO avec les clients durant une ou plusieurs manches.
		
		monServeur.close();                 // Ferme ce qui doit etre ferme.
		

	}

}
