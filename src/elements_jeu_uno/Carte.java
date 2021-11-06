//Class modelisant une carte de uno.

package elements_jeu_uno;

public class Carte {
	
	private String effet;			//+ si c'est une carte +2 , rien sinon
	private int number;
	private String color;

	
	public Carte(int number, String color) {
		this.number = number;
		this.color = color;
		this.effet = "";
	}
	
	
	public Carte(String color) {
		this.color = color;
	}

	public Carte() {
	}
	
	public Carte(String effet, int number, String color) {
		this.number = number;
		this.color = color;
		this.effet = effet;
	}


	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getEffet() {
		return effet;
	}
	public void setEffet(String effet) {
		this.effet = effet;
	}

	
	// Vérifie qu'une carte est jouable et renvoie un booleen.
	public boolean carteSuperposable(Carte talon) {
		if(this.getColor().equals("joker") || talon.getColor().equals(this.getColor())){
			return true;
		}
		else if(talon.getEffet().equals(this.getEffet()) && talon.getNumber() == this.getNumber()) {
			return true;
		}
		return false;
	}
	
	// Compare l'égalité de contenu de 2 cartes et renvoie true si les 2 cartes sont identiques en contenu. 
	public boolean equals(Carte carteA) {
		if(carteA.color.equals(this.getColor()) && carteA.effet.equals(this.getEffet()) && carteA.number == this.getNumber()) {
			return true;
		}
		return false;
	}
	
	public String toString() {
		return effet + number + '-' + color;
	}
	
	// Transforme une chaine de charactere representant une carte en l'objet carte directement.
	public static Carte string2Carte(String txt) {
		
		Carte maCarte = new Carte();
		//Permet d'eviter une erreur en creant une fausse carte non possedable.
		if(txt.equals("je-pioche") || txt.equals("je-passe")) {
			maCarte.setEffet("");
			maCarte.setNumber(5);
			maCarte.setColor("blanc");
			return maCarte;
		}
		
		else if(txt.contains("+")) {
			maCarte.setEffet("+");
			maCarte.setNumber(Integer.valueOf(String.valueOf(txt.toCharArray()[1])));
			
			if(String.valueOf(txt.toCharArray()[3]).equals("r")){
				maCarte.setColor("rouge");
			}
			else if(String.valueOf(txt.toCharArray()[3]).equals("v")) {
				maCarte.setColor("vert");
			}
			else if(String.valueOf(txt.toCharArray()[3]).equals("j") && String.valueOf(txt.toCharArray()[4]).equals("a")) {
				maCarte.setColor("jaune");
			}
			else if(String.valueOf(txt.toCharArray()[3]).equals("b")) {
				maCarte.setColor("bleu");
			}
			else if(String.valueOf(txt.toCharArray()[3]).equals("j") && String.valueOf(txt.toCharArray()[4]).equals("o")){
				maCarte.setColor("joker");
			}
		}
		
		else {
			maCarte.setEffet("");
			maCarte.setNumber(Integer.valueOf(String.valueOf(txt.toCharArray()[0])));
			if(String.valueOf(txt.toCharArray()[2]).equals("r")) {
				maCarte.setColor("rouge");
			}
			else if(String.valueOf(txt.toCharArray()[2]).equals("v")) {
				maCarte.setColor("vert");
			}
			else if(String.valueOf(txt.toCharArray()[2]).equals("b")) {
				maCarte.setColor("bleu");
			}
			else if(String.valueOf(txt.toCharArray()[2]).equals("j") && String.valueOf(txt.toCharArray()[3]).equals("a")){
				maCarte.setColor("jaune");
			}
			else if(String.valueOf(txt.toCharArray()[2]).equals("j") && String.valueOf(txt.toCharArray()[3]).equals("o")){
				maCarte.setColor("joker");
			}
			
		}
		return maCarte;
	}
	
	
	
}
