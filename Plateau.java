package Scrabble;

public class Plateau {
	
	Sac objBag = new Sac();
	
	 
	public Plateau(){
		objBag.Shuffle();
		
	}
	
	private char[][] objBoard = new char[15][15];
	
	
	public char[][] getObjBoard() {
		
		
		return objBoard;
	}

	public boolean setSpace(int space,char input){
		int x = space/15;
		int y = space%15;
		if (objBoard[x][y]<9){
			objBoard[x][y]=input;
			return true;
		}
		return false;
	}
	
	public void getTiles(Joueur chevalet_joueur){
		int nblettre = chevalet_joueur.getTiles().size();
		for (int i = 0;i< 7-nblettre;i++){
			if (objBag.Size()>0){
				chevalet_joueur.addTile(objBag.Draw());
			}
		}
	}
	
	public void returnTiles(Joueur chevalet_joueur){
		int nblettre = chevalet_joueur.getTiles().size();
		for (int i = 0;i< 7-nblettre;i++){
			if (objBag.Size()>0){
				chevalet_joueur.addTile(objBag.Draw());
			}
		}
	}

}
