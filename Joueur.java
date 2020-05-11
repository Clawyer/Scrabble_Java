package Scrabble_Java;

import java.util.ArrayList;

public class Joueur {
	private int score;
	ArrayList<Case> cases_joueur = new ArrayList<Case>();
	
	public Joueur(){
		setScore(0);
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public void addTile(Case input ){
		cases_joueur.add(input);
		
	}
	
	public ArrayList<Case> getTiles(){
		return cases_joueur;
	}
	public void removeTile(int input) {
		cases_joueur.remove(input);
	}
}


