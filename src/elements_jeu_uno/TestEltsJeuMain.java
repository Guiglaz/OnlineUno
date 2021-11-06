// Class de test des differentes fonctions du programme.
// L'affichage de true dans la console implique que l'action visee a bien ete realisee.


package elements_jeu_uno;

import jeuUno.Joueur;

public class TestEltsJeuMain {

	public static void main(String[] args) {
		
		Pioche maPioche = new Pioche(0);
		for(Carte carte : maPioche.paquet) {
			System.out.println(carte);
			
		}
		
		System.out.println(maPioche.paquet.size());
		System.out.println();
		Main maMain = new Main();

//		maMain.creerMain(maPioche);
		System.out.println("Ma main est\n" + maMain);

		System.out.println("carte superposable " + new Carte("+",2,"rouge").carteSuperposable(new Carte(4, "rouge")));
		System.out.println("carte superposable " + !( new Carte("+",2,"vert").carteSuperposable(new Carte(4, "rouge"))));
		System.out.println("carte superposable " + !( new Carte("+",2,"vert").carteSuperposable(new Carte(2, "rouge"))));
		System.out.println("carte superposable " + new Carte(2,"rouge").carteSuperposable(new Carte(2, "vert")));
		System.out.println("carte superposable " + !( new Carte(2,"vert").carteSuperposable(new Carte(4, "rouge"))));
		System.out.println("carte superposable " + ( new Carte("+",2,"vert").carteSuperposable(new Carte("+",2, "vert"))));

		System.out.println();
		
		System.out.println(maMain);
		
		System.out.println("String to carte " + Carte.string2Carte(new Carte(3,"rouge").toString()));

		System.out.println("Equals " + maMain.getPaquet().get(0).equals(Carte.string2Carte(maMain.getPaquet().get(0).toString())));
		System.out.println();
		
		Joueur j1 = new Joueur("guiglaz",maMain);
		System.out.println(j1);
		j1.ajoutScore();
		System.out.println("j1 score " + j1.getScore());
		
		System.out.println("Retirer une carte :" + j1.getMaMain().removeCarte(j1.getMaMain().getPaquet().get(0)));
		System.out.println(j1);
		System.out.println("Retirer une carte inexistante:" +  ! j1.getMaMain().removeCarte(new Carte(10,"blanc")));
		System.out.println(j1);
		
		System.out.println("String to carte " + Carte.string2Carte("+2-jaune").equals(new Carte("+",2,"jaune")));
		System.out.println("String to carte " + Carte.string2Carte("+2-jaune").getEffet().equals("+"));


	

	}
}
