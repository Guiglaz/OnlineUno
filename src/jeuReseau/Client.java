package jeuReseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import elements_jeu_uno.Carte;
import elements_jeu_uno.Main;
import jeuUno.Joueur;

public class Client {

	private Socket socket;
	private Boolean enJeu;
	private Joueur joueur;
	private String txtIn;
	private String txtOut;
	private Scanner consReader;
	private BufferedReader txtInPut;
	private PrintWriter txtOutPut;

	//Constructeur nécéssitant l'ip et le port de connexion en parametre
	public Client(String host, int port) throws IOException {
		this.socket = new Socket(host, port);
		this.enJeu = false;
		this.joueur = new Joueur();
		this.txtIn = new String();
		this.txtOut = new String();
		this.consReader = new Scanner(System.in);
		this.txtInPut = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.txtOutPut = new PrintWriter(this.socket.getOutputStream(),true);


	}

	//Constructeur ne nécéssitant que l'ip en paramètre, le port est a entrer lors de l'execution.
	public Client(String host) throws IOException {
		
		this.enJeu = false;
		this.joueur = new Joueur();
		this.txtIn = new String();
		this.txtOut = new String();
		this.consReader = new Scanner(System.in);
		
		String port = new String();
		System.out.println("Port de connection ? (defaut 6789)");
		port = this.instrConsole();
		
		if(port.contentEquals("")){
			port = "6789";
		}
		
		this.socket = new Socket(host, Integer.valueOf(port));
		
		this.txtInPut = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.txtOutPut = new PrintWriter(this.socket.getOutputStream(),true);
		

	}

	// Ferme tous les objets à fermer dans la class.
	public void close() throws IOException {
		this.socket.close();
		this.consReader.close();
	}

	// Envoie le paramere message au serveur connecté.
	private void sendMessage(String message) throws IOException {
		this.txtOutPut.println(message);
	}

	// Recupere les informations entree dans la console par l'uitlisateur.
	private String instrConsole() {
		String consJoueur = new String();
		consJoueur = this.consReader.nextLine();
		return consJoueur;    	
	}

	// Fonction demarant la communication avec le serveur connecte.
	private void donnerNom() throws IOException {
		String instruction = new String("je-suis ");
		System.out.println(instruction);
		this.joueur.setNom(instrConsole());
		instruction += this.joueur.getNom();
		this.sendMessage(instruction);
	}

	// Fonction ajoutant la carte, reçue sous forme de String depuis le serveur, dans la main joueur.
	private void ajouterCarte(String txtIn) throws IOException {
		String[] txtCarte = txtIn.split(" ");
		this.joueur.getMaMain().addCarte(Carte.string2Carte(txtCarte[1]));
	}

	// Fonction réalisant une boucle tant que la partie n'est pas terminee pour realiser les actions de jeu.
	public void jouer() throws IOException {
		this.donnerNom();
		this.receiveMessage();
		while(this.enJeu) {
			this.receiveMessage();
		}
	}

	// Fonction reçevant et traitant les messages envoyes par le serveur.
	private void receiveMessage() throws IOException {
		this.txtIn = (new String(this.txtInPut.readLine()));

		System.out.println(this.txtIn);


		if(this.txtIn.contains("nouveau-talon")) {
			String[] txtSplit = this.txtIn.split(" ");
			this.joueur.setDessusTalon(Carte.string2Carte(txtSplit[1]));
		}

		if(this.txtIn.equals("bienvenue")) {

			this.enJeu = true;
			this.joueur.setScore(0);
			this.joueur.setMaMain(new Main());
			this.joueur.setDessusTalon(new Carte());
			System.out.println("Vous êtes entré dans la partie");

		}

		if(this.txtIn.equals("Erreur : plus de place")) {
			this.enJeu = false;
			System.out.println("Serveur plein");
		}

		if(this.txtIn.equals("debut-de-manche")) {
			this.enJeu = true;
			this.joueur.setMaMain(new Main());
			this.joueur.setDessusTalon(new Carte());
		}

		if(this.txtIn.contains("prends")) {
			this.ajouterCarte(this.txtIn);
			System.out.println("Taille Main actuelle : " + this.joueur.getMaMain().size() +"\n");
		}

		if(this.txtIn.equals("joue")) {
			this.jouerCoup();
		}

		if(this.txtIn.equals("OK")) {
		}

		if(this.txtIn.contains("fin-de-manche")) {
			this.joueur.setScore(this.joueur.getScore() + this.joueur.getMaMain().score()) ;
			this.joueur.setMaMain(new Main());
			this.joueur.setDessusTalon(new Carte());
		}
		
		if(this.txtIn.contains("fin-de-partie")) {
			this.enJeu = false;
		}
		
		if(this.txtIn.contains("Erreur") && !this.txtIn.equals("Erreur : plus de place")) {

		}


	}

	// Modelise l'action de jouer un tour pour le client en vérifiant les consignes donnees.
	private void jouerCoup() throws IOException {
		System.out.println("\nMain : " + this.joueur.getMaMain().getPaquet());
		System.out.println("Talon : " + this.joueur.getDessusTalon() + "\nFaite une action : je-pose n-couleur || je-pioche \n");

		this.txtOut = this.instrConsole();

		while(!(this.txtOut.equals("je-pioche") || (this.txtOut.contains("je-pose") && this.txtOut.split(" ").length == 2))) {
			System.out.println("Action invalide, veuillez choisir entre : je-pose n-couleur || je-pioche");
			this.txtOut = this.instrConsole();
		}

		if(this.txtOut.equals("je-pioche")) {
			this.sendMessage(this.txtOut);
			this.receiveMessage();

			while(!this.txtIn.equals("joue")) {
				this.txtIn = (new String(txtInPut.readLine()));
			}

			System.out.println("Faite une action : je-pose n-couleur || je-passe");
			this.txtOut = this.instrConsole();

			while(!(this.txtOut.equals("je-passe") || (this.txtOut.equals("je-pose" + this.joueur.getMaMain().getPaquet().get(this.joueur.getMaMain().size()-1)) ))) {
				System.out.println("Action invalide, veuillez choisir entre : je-passe || je-joue n-couleur ");
				this.txtOut = this.instrConsole();
			}

			if(this.txtOut.contains("je-pose")) {
				this.poserCarte();
			}
			else {
				this.sendMessage(this.txtOut);
			}
		}

		else {
			this.poserCarte();
		}
	}

	// Modelise et realise l'action de poser une carte pour un joueur en verifiant les conditions pour poser la carte.
	public void poserCarte() throws IOException {
		String[] txtCarte = this.txtOut.split(" ");

		while(!(this.joueur.getMaMain().removeCarte(Carte.string2Carte(txtCarte[1])) || txtCarte[1].equals("je-pioche") || txtCarte[1].equals("je-passe"))) {
			System.out.println("Vous n'avez pas cette carte, choisissez une autre carte!");
			txtCarte[1] = this.instrConsole();
			if(txtCarte[1].contains("je-pose")) {
				txtCarte[1] = txtCarte[1].split(" ")[1];
			}
		}

		if(txtCarte[1].equals("je-pioche") || txtCarte[1].equals("je-passe")) {
			this.sendMessage(txtCarte[1]);
		}
		else {
			this.txtOut = "je-pose " + txtCarte[1];


			if(!this.joueur.getDessusTalon().carteSuperposable(Carte.string2Carte(txtCarte[1]))) {
				System.out.println("Cette carte n'est pas posable, voulez vous quand même la poser? oui || non ");
				String rep = this.instrConsole();
				while(!(rep.equals("oui") || rep.equals("non"))) {
					System.out.println("Veuillez répondre oui || non ");
					rep = this.instrConsole();
				}

				if(rep.equals("oui")) {
					this.sendMessage(this.txtOut);
				}
				else {

					System.out.println("Quelle carte voulez-vous poser? ");
					txtCarte[1] = this.instrConsole();
					while(!(this.joueur.getMaMain().removeCarte(Carte.string2Carte(txtCarte[1])) || txtCarte[1].equals("je-pioche") || txtCarte[1].equals("je-passe"))) {
						System.out.println("Vous n'avez pas cette carte, choisissez une autre carte!");
						txtCarte[1] = this.instrConsole();
						if(txtCarte[1].contains("je-pose")) {
							txtCarte[1] = txtCarte[1].split(" ")[1];
						}
					}
					if(txtCarte[1].equals("je-pioche") || txtCarte[1].equals("je-passe")) {
						this.sendMessage(txtCarte[1]);
					}
					else {
						this.txtOut = "je-pose" + txtCarte[1];
						this.sendMessage(this.txtOut);
					}
				}
			}
			else {
				this.sendMessage(this.txtOut);
			}
		}	
	}
		
}

