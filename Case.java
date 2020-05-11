package Scrabble_Java;

public class Case {

	private char letter;
	private int value;
	private BonusCase bonus;
	
	
	
	
	public Case(char letter, int value) {
		this.letter = letter;
		this.value = value;
	}

	public char getLetter() {
		return letter;
	}
	
	public void setLetter(char letter) {
		this.letter = letter;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}

	public String toString() {
		return "[" + letter + "-" + value + "]";
	}
	
	public enum BonusCase {
		
		LD,
		LT,
		MD,
		MT,
		Vide;
	}
	
	
}
