package Scrabble_Java;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;

public class Dico {

	private Set<String> wordsSet = new HashSet<>();
	private ArrayList<String> list;

	
	
	
	public Dico() throws IOException
    {
        
        File path1 = new File("src\\Scrabble\\Dico\\dico_a-g.txt");
        File path2 = new File("src\\Scrabble\\Dico\\dico_h-z.txt");
        
        try (Scanner s = new Scanner(new FileReader(path1))) {
            while (s.hasNext()) {
                wordsSet.add(s.nextLine());
            }
        }
        try (Scanner s = new Scanner(new FileReader(path2))) {
            while (s.hasNext()) {
                wordsSet.add(s.nextLine());
             
            }
        }

       
       list = new ArrayList<String>(wordsSet);
       Collections.sort(list); 
   
       
    }
	
	

    public boolean contient(String word)
    {
    	
        return list.contains(word.toLowerCase());
    }
    

    List<String> findValidWords(String word){
    	char[] letters = new char[word.length()];
    	
    	 for (int i = 0; i <word.length(); i++) { 
	            letters[i] = word.charAt(i); 
	        }
        int []dispo = new int[26];
        for(char c : letters){
            int index = c - 'a';
            dispo[index]++;
        }
        List<String> result = new ArrayList();
        for(String w: list){
            int []count = new int[26];
            boolean ok = true;
            for(char c : w.toCharArray()){
                int index = c - 'a';
                count[index]++;
                if(count[index] > dispo[index]){
                    ok = false;
                    break;
                }
            }
            if(ok){
                result.add(w);
            }
        }
    
        List<String> Bestresult = new ArrayList();
        for(String w: result) {
        	if (w.length() > 3 ) {
        		if (!Bestresult.contains(w)) {
        			Bestresult.add(w);
        		} else {
        		      		
        	}
        	}
        }
        if(Bestresult.size() == 0) {
        	JOptionPane.showMessageDialog(null, "Pas de suggestion!");
        }
       
        return Bestresult;
        
    }
 


}


