package Scrabble_Java;

import java.util.ArrayList;
import java.util.Random;

public class Sac {
	

	private int [] numberOfLetters = {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1};
	private int [] letterValue = {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
	private char [] letters = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	private ArrayList<Case> objBag = new ArrayList<Case>();
	
	public Sac() {
		for(int j = 0; j < 26; j++)
		{
			for(int h = 0; h < numberOfLetters[j]; h++)
			{
				objBag.add(new Case(letters[j],letterValue[j]));
			}
		}	
	}

	public Case Draw()
	{
		Case sortie = objBag.get(0);
		objBag.remove(0);
		return sortie;
	}
	
	public void Shuffle()
	{
		
		Random rng = new Random();
	
		
		for(int i = 0; i < 500; i++)
		{
			int random = rng.nextInt(78);
			Case sortie = objBag.get(random);
			objBag.remove(random);
			objBag.add(sortie);
		}
	}
	public int Size(){
		return objBag.size();
	}

}
