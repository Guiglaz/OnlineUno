package jeuReseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import elements_jeu_uno.Carte;
import elements_jeu_uno.Main;
import jeuUno.Joueur;
import jeuUno.MancheUno;

public class Serveur {



	private int nbJoueur;
	private int nbManche;
	
	private Joueur[] joueurList;
	private boolean aJoue;
	private int aPioche;

	
	private ServerSocket[] serveurSocket;
	private Socket[] socketList;
	
	private BufferedReader[] txtInPut;
	private PrintWriter[] txtOutPut;

	//Constructeur de serveur qui initialise toute la partie jusqu'a etre pret a jouer.
	public Serveur() throws IOException  {
		Scanner cons = new Scanner(System.in);
		String infoServIn = new String();
		int portDebut = 6789;
		System.out.println("Ports de connection ? x -> x+nbjoueur (defaut : x = 6789) ");
		infoServIn = cons.nextLine();
		
		if(!infoServIn.equals("")) {
			portDebut = Integer.valueOf(infoServIn);
		}

		System.out.println("Nombre de joueur ? (defaut : 2) ");
		infoServIn = cons.nextLine();

		if(infoServIn.equals("")) {
			this.nbJoueur = 2;
		}
		else if (Integer.valueOf(infoServIn) < 2) {
			this.nbJoueur = 2;
		}
		else {
			this.nbJoueur = Integer.valueOf(infoServIn);
		}
		int port_fin = portDebut+this.nbJoueur - 1;
		System.out.println("Server listening on port : " + portDebut + " -> " + port_fin );
		
		System.out.println("Nombre de manches ? (defaut : 1) ");
		infoServIn = cons.nextLine();
		if(infoServIn.equals("")) {
			this.nbManche = 1;
		}
		else {
			this.nbManche = Integer.valueOf(infoServIn);
		}
		
		this.joueurList = new Joueur[this.nbJoueur];
		this.aJoue = false;
		this.aPioche = 0;
		
		this.socketList = new Socket[this.nbJoueur];
		this.serveurSocket = new ServerSocket[this.nbJoueur];

		this.txtInPut = new BufferedReader[this.nbJoueur];
		this.txtOutPut = new PrintWriter[this.nbJoueur];
		
		System.out.println("Waiting for connections ");
		System.out.println();
		
		this.waitJoueurConnection(portDebut);
		
		this.sendMsgAll("debut-de-manche");
		cons.close();
	}

	// Fonction recevant les consignes du client numero "rang" durant la manche maManche.
	private void receiveMsg(int rang, MancheUno maManche) throws IOException {
		String txtIn = new String(this.txtInPut[rang].readLine());

		if(txtIn.equals("je-pioche")) {
			maManche.piocher(rang);
			this.sendMessage("prends " + maManche.getListJoueur()[rang].getMaMain().getPaquet().get(maManche.getListJoueur()[rang].getMaMain().size()-1), rang);

			this.sendMsgAllOther("joueur " + this.joueurList[rang] + " pioche 1", rang);
			this.aJoue = false;
			this.sendMessage("joue", rang);
			
			txtIn = new String(this.txtInPut[rang].readLine());

			while(!(txtIn.equals("je-passe") || txtIn.equals("je-pose " + maManche.getListJoueur()[rang].getMaMain().getPaquet().get(maManche.getListJoueur()[rang].getMaMain().size()-1)))) {
				
				this.sendMessage("Erreur : commande inconnue \nVous ne pouvez jouer que la carte piocher ou passer la main.", rang);
				this.sendMessage("joue", rang);
				txtIn = new String(this.txtInPut[rang].readLine());
			}


			if(txtIn.equals("je-passe")) {
				this.sendMsgAll("joueur " + this.joueurList[rang] + " passe");
				this.aJoue = true;
			}
			else if(maManche.getTalonManche().dessusTalon().carteSuperposable(Carte.string2Carte(txtIn.split(" ")[1])) && maManche.getListJoueur()[rang].getMaMain().removeCarte(Carte.string2Carte(txtIn.split(" ")[1]))) {   
				if(Carte.string2Carte(txtIn.split(" ")[1]).getEffet().equals("+")){
					for(int i =0;i<Carte.string2Carte(txtIn.split(" ")[1]).getNumber();i++) {
						System.out.println(this.joueurList[rang] + " fait piocher " + this.joueurList[(rang+1)%this.nbJoueur] + " " + Carte.string2Carte(txtIn.split(" ")[1]).getNumber());
						maManche.piocher((rang+1)%this.nbJoueur);
						this.sendMessage("prends " + maManche.getListJoueur()[(rang+1)%this.nbJoueur].getMaMain().getPaquet().get(maManche.getListJoueur()[(rang+1)%this.nbJoueur].getMaMain().size()-1), (rang+1)%this.nbJoueur);
						this.sendMsgAll(this.joueurList[rang] + " fait piocher " + this.joueurList[(rang+1)%this.nbJoueur] + " " + Carte.string2Carte(txtIn.split(" ")[1]).getNumber());
						this.aPioche = 1;
					}
				}
				
				maManche.getTalonManche().addCarte(Carte.string2Carte(txtIn.split(" ")[1]));
				this.sendMessage("OK", rang);
				this.sendMsgAll("joueur " + this.joueurList[rang] + " pose " + txtIn.split(" ")[1]);
				this.sendMsgAll("nouveau-talon " + maManche.getTalonManche().dessusTalon());
				this.aJoue = true;
			}
			else {
				this.sendMessage("prends" + txtIn.split(" ")[1], rang);
				maManche.piocher(rang);
				this.sendMessage("prends " + maManche.getListJoueur()[rang].getMaMain().getPaquet().get(maManche.getListJoueur()[rang].getMaMain().size()-1), rang);

				maManche.piocher(rang);
				this.sendMessage("prends " + maManche.getListJoueur()[rang].getMaMain().getPaquet().get(maManche.getListJoueur()[rang].getMaMain().size()-1), rang);

				this.sendMsgAllOther("joueur " + this.joueurList[rang] + " pioche 2", rang);
				this.aJoue = true;
			}

		}
		

		else if(txtIn.contains("je-pose") && txtIn.split(" ").length == 2) {
			
			if(maManche.getListJoueur()[rang].getMaMain().removeCarte(Carte.string2Carte(txtIn.split(" ")[1]))) {
				
				if(maManche.getTalonManche().dessusTalon().carteSuperposable(Carte.string2Carte(txtIn.split(" ")[1]))) {

					if(Carte.string2Carte(txtIn.split(" ")[1]).getEffet().equals("+")){
						for(int i =0;i<Carte.string2Carte(txtIn.split(" ")[1]).getNumber();i++) {
							System.out.println(this.joueurList[rang] + " fait piocher " + this.joueurList[(rang+1)%this.nbJoueur] + " " + Carte.string2Carte(txtIn.split(" ")[1]).getNumber());
							maManche.piocher((rang+1)%this.nbJoueur);
							this.sendMessage("prends " + maManche.getListJoueur()[(rang+1)%this.nbJoueur].getMaMain().getPaquet().get(maManche.getListJoueur()[(rang+1)%this.nbJoueur].getMaMain().size()-1), (rang+1)%this.nbJoueur);
							this.sendMsgAll(this.joueurList[rang] + " fait piocher " + this.joueurList[(rang+1)%this.nbJoueur] + " " + Carte.string2Carte(txtIn.split(" ")[1]).getNumber());
							this.aPioche = 1;
						}
					}

					maManche.getTalonManche().addCarte(Carte.string2Carte(txtIn.split(" ")[1]));
					this.sendMessage("OK", rang);
					this.sendMsgAll("joueur " + this.joueurList[rang] + " pose " + txtIn.split(" ")[1]);
					this.sendMsgAll("nouveau-talon " + maManche.getTalonManche().dessusTalon());
					this.aJoue = true;
				}

				else {

					maManche.getListJoueur()[rang].getMaMain().addCarte(Carte.string2Carte(txtIn.split(" ")[1]));
					this.sendMessage("prends " + txtIn.split(" ")[1], rang);

					maManche.piocher(rang);
					this.sendMessage("prends " + maManche.getListJoueur()[rang].getMaMain().getPaquet().get(maManche.getListJoueur()[rang].getMaMain().size()-1), rang);

					maManche.piocher(rang);
					this.sendMessage("prends " + maManche.getListJoueur()[rang].getMaMain().getPaquet().get(maManche.getListJoueur()[rang].getMaMain().size()-1), rang);

					this.sendMsgAllOther("joueur " + this.joueurList[rang] + " pioche 2", rang);
					this.aJoue = true;
				}

				
			}
			
			else {
				maManche.piocher(rang);
				this.sendMessage("prends " + maManche.getListJoueur()[rang].getMaMain().getPaquet().get(maManche.getListJoueur()[rang].getMaMain().size()-1), rang);

				maManche.piocher(rang);
				this.sendMessage("prends " + maManche.getListJoueur()[rang].getMaMain().getPaquet().get(maManche.getListJoueur()[rang].getMaMain().size()-1), rang);

				this.sendMsgAllOther("joueur " + this.joueurList[rang] + " pioche 2", rang);
				this.aJoue = true;
			}
		}


		else {
			this.sendMessage("Erreur : non valide", rang);
			this.aJoue = false;
		}
	}
	
	// Fonction envoyant "message" au client numero "rang" via son propre channel de communication.
	private void sendMessage(String message, int rang) throws IOException {
		this.txtOutPut[rang].println(message);

	}

	// Envoie "message" à tous les clients connectes.
	private void sendMsgAll(String message) throws IOException {
		for(int rang=0;rang<this.nbJoueur;rang++) {
			this.sendMessage(message, rang);
		}
	}

	// Envoie a tous les clients autre que le clients numero "rang" un même message (cette fonction est utilise pour suivre une particularite du protocole du serveur de test lors de l'action je-pioche)
	private void sendMsgAllOther(String message,int rang) throws IOException {
		for(int i=0; i<this.nbJoueur;i++) {
			if(i!=rang) {
				this.sendMessage(message, i);
			}
		}
	}

	// Configure les connexions de clients sur les différents ports joueurs.
	private void waitJoueurConnection(int portDebut) throws IOException {
		String txtIn = new String();
		int port = portDebut;
		for(int i=0; i<this.nbJoueur; i++) {
			
			System.out.println("Current connecting port " + port);
			
			this.serveurSocket[i] = new ServerSocket(port);
			this.socketList[i] = this.serveurSocket[i].accept();
			this.txtInPut[i] = new BufferedReader(new InputStreamReader(this.socketList[i].getInputStream()));
			this.txtOutPut[i] = new PrintWriter(this.socketList[i].getOutputStream(),true);

			txtIn = txtInPut[i].readLine();
			
			if(txtIn.contains("je-suis ")) {
				System.out.println(txtIn.split(" ")[1] + " connexion accepted");
				this.joueurList[i] = new Joueur(txtIn.split(" ")[1]);
			}
			else {

				this.joueurList[i] = new Joueur(txtIn);
			}
			this.sendMessage("bienvenue", i);	
			port += 1;
		}
		for (Joueur joueur : this.joueurList) {
			System.out.println(joueur);
		}
	}

	// Initialise les parametres de la manche pour pouvoir jouer (pioche, mains, talon) et en informe les clients.
	private void initialiserManche(MancheUno currentManche) throws IOException {
		for(int numCarte=0; numCarte<7; numCarte++) {
			for(int rang=0; rang<this.nbJoueur; rang++) {
				this.sendMessage("prends " + currentManche.getListJoueur()[rang].getMaMain().getPaquet().get(numCarte), rang);
				System.out.println("joueur " + currentManche.getListJoueur()[rang] +"prend carte : " + currentManche.getListJoueur()[rang].getMaMain().getPaquet().get(numCarte));
			}
		}
		this.sendMsgAll("nouveau-talon " + currentManche.getTalonManche().dessusTalon());
		
		if(currentManche.getTalonManche().dessusTalon().getEffet().equals("+")) {
			for(int a = 0; a<currentManche.getTalonManche().dessusTalon().getNumber(); a++) {
				currentManche.piocher(0);
				this.sendMessage("prends " + currentManche.getListJoueur()[0].getMaMain().getPaquet().get(currentManche.getListJoueur()[0].getMaMain().size()-1), 0);
			}
			this.aPioche = 1;
		}
	}

	// Modelise une partie a partir des parametres du serveur avec des boucles whiles pour recevoir les consignes clients.
	public void jouerPartie(int genPioche) throws IOException {
		int rang = 0;

		for(int i=0; i<this.nbManche; i++) {
			rang = 0;
			MancheUno currentManche = new MancheUno(this.joueurList, genPioche);
			this.initialiserManche(currentManche);
			System.out.println("manche initialisee\n");
			
			for (Joueur joueur : currentManche.getListJoueur()) {
				System.out.println(joueur);
				System.out.println(joueur.getMaMain());
				System.out.println();
			}

			
			
			while(!currentManche.isWin()) {
				
				rang = (rang+this.aPioche)%this.nbJoueur;
				this.sendMessage("joue", rang);
				System.out.println(this.joueurList[rang] + " joue");
				this.aJoue = false;
				this.aPioche = 0;
				
				this.receiveMsg(rang,currentManche);
				if(this.aJoue) {
					rang = (rang+1)%this.nbJoueur;
				}

			}



			this.joueurList = currentManche.getListJoueur();
			//reset des mains par sécurité
			for(Joueur joueur:this.joueurList) {
				joueur.setMaMain(new Main());
				joueur.setDessusTalon(new Carte());
			}

			
			
			
			
			String txtOut = new String("fin-de-manche ");
			for(Joueur joueur : this.joueurList) {
				txtOut += joueur.toString() + " score : ";
				txtOut += joueur.getScore();
				txtOut += "   ";
			}
			this.sendMsgAll(txtOut);

		}
		String txtOut = new String("fin-de-partie ");
		for(Joueur joueur : this.joueurList) {
			txtOut += joueur.toString() + " score : ";
			txtOut += joueur.getScore();
			txtOut += "   ";
		}
		this.sendMsgAll(txtOut);
	}

	// Ferme tous les objets devant etre ferme sur le serveur.
	public void close() throws IOException {
		for(int i = 0; i<this.nbJoueur; i++) {
			this.socketList[i].close();
			this.serveurSocket[i].close();
		}
	}

}
