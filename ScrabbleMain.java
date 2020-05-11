package Scrabble_Java;

import java.io.IOException;

import javax.swing.JFrame;

public class ScrabbleMain { // Lancement jeu
	
	public static void main(String[] args) throws IOException {
		GUI Window = new GUI();
		Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Window.startUp();
	}

}
