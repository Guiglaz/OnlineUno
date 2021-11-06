package executables;

import java.io.IOException;

import jeuReseau.Client;

public class ClientMain {
	public static void main(String[] args) throws IOException {
		
		
		Client clt1 = new Client("127.0.0.1");  // Initialisation d'un client pour l'adresse local, le port est rentre lors de l'execution avec 6789 par defaut
		
		
		clt1.jouer();   // realise l'action de jouer une partie
		
		clt1.close();   // ferme tous les objets a fermer une fois la partie terminee
		
	}
}
