package Scrabble_Java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import java.awt.Toolkit;

public class GUI extends JFrame {

	private boolean boolCanExchange, motexistant;
	private Container contentPane;
	public Plateau scrabbleBoard;
	public Joueur[] playerList;
	public int intCurrentPlayer;
	private JPanel eastPanel, westPanel, centerPanel, southPanel;
	private JRadioButton rdbtnExchange;
	private JRadioButton rdbtnPlace;
	private JButton btnExchange, nextTurnBtn, txtField, valider, suggestion;
	private int score;
	public ArrayList<JButton> listOfInput;
	private String Nbjoueur;

	public GUI() {
		super("Scrabble");

//+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----++-----+-----+-----+-----+-----+-----+

//Fenêtre

		Toolkit tk = Toolkit.getDefaultToolkit();
		int xSize = ((int) tk.getScreenSize().getWidth() - 300);
		int ySize = ((int) tk.getScreenSize().getHeight());

		scrabbleBoard = new Plateau();
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0, 0));

//+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----++-----+-----+-----+-----+-----+-----+

//Panel		
		// Titre
		Font h = new Font("Helvetica", Font.PLAIN, 25);
		JLabel Accueil = new JLabel("SCRABBLE GAME", SwingConstants.CENTER);
		Accueil.setFont(h);
		Accueil.setForeground(Color.CYAN);
		contentPane.add(Accueil, BorderLayout.NORTH);

		// Grille
		centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(15, 15, 5, 5));
		boardUpdate(); // Appel Grille
		contentPane.add(centerPanel, BorderLayout.CENTER);

		// Chevalet
		southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(1, 0, 0, 0));
		contentPane.add(southPanel, BorderLayout.SOUTH);

		// Panel droit
		eastPanel = new JPanel();
		contentPane.add(eastPanel, BorderLayout.EAST);
		eastPanel.setLayout(new GridLayout(0, 1, 0, 0));
		eastPanel.setPreferredSize(new Dimension(100, 100));

		// Panel gauche
		westPanel = new JPanel();
		contentPane.add(westPanel, BorderLayout.WEST);
		westPanel.setLayout(new GridLayout(0, 1, 0, 0));
		westPanel.setPreferredSize(new Dimension(100, 100));

//+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----++-----+-----+-----+-----+-----+-----+

//Content setup	
//		
		rdbtnPlace = new JRadioButton("Place");
		rdbtnPlace.setSelected(true);
		rdbtnPlace.addActionListener(new radioClick());
		eastPanel.add(rdbtnPlace);

		rdbtnExchange = new JRadioButton("Changer de lettre(s)");
		rdbtnExchange.addActionListener(new radioClick());
		eastPanel.add(rdbtnExchange);

		btnExchange = new JButton("Changer");
		btnExchange.setEnabled(false);
		eastPanel.add(btnExchange);
		btnExchange.addActionListener(new exchangeExecute());

		valider = new JButton("Valider");
		valider.setEnabled(true);
		eastPanel.add(valider);
		valider.addActionListener(new validermot());

		suggestion = new JButton("Aide ?");
		suggestion.setEnabled(true);
		eastPanel.add(suggestion);
		suggestion.addActionListener(new suggestionmot());

		setBounds(0, 0, xSize, (ySize - 50));
		setVisible(true);
		repaint();
	}

//+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----++-----+-----+-----+-----+-----+-----+

//Listeners	

	private class tilePress implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ea) {

			ArrayList<String> lettres = new ArrayList<>();
			score = 0;

			JButton input = (JButton) ea.getSource();
			listOfInput = new ArrayList<>();
			if (rdbtnPlace.isSelected() && input.getBackground() == Color.WHITE
					|| input.getBackground() == Color.CYAN) {
				for (int i = 0; i < southPanel.getComponentCount(); i++) {
					if (southPanel.getComponent(i).getBackground() == Color.green) {

						ArrayList<Case> cases_joueur = playerList[intCurrentPlayer].getTiles();
						listOfInput.add(input);
						input.setText(cases_joueur.get(i).getLetter() + "");
						input.setBackground(Color.yellow);

						score = playerList[intCurrentPlayer].getScore() + cases_joueur.get(i).getValue();
						playerList[intCurrentPlayer].setScore(score);
						playerList[intCurrentPlayer].removeTile(i);

						// Permet de pouvoir quand même jouer si un mot n'est pas bon à partir du second
						// tour
						for (int j = 0; j < centerPanel.getComponentCount(); j++) {
							JButton test = (JButton) centerPanel.getComponent(j);
							if (input.equals(test)) {
								scrabbleBoard.setSpace(j, input.getText().charAt(0));
							}
						}

						lettres.add(input.getText());
						southPanel.remove(input);
						rackUpdate();
						boolCanExchange = false;
						break;
					}
				}
			}

		}
	}

// Ajouter les lettres sur la grid
	private class rackPress implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ea) {
			if (rdbtnPlace.isSelected()) {
				for (int i = 0; i < southPanel.getComponentCount(); i++) {
					southPanel.getComponent(i).setBackground(Color.orange);
				}
			}
			switchColor((JButton) ea.getSource());
		}
	}

// Changer les lettres selectionnées -> Changement joueur
	private class exchangeExecute implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ea) {
			if (rdbtnExchange.isSelected()) {
				for (int i = 0; i < playerList[intCurrentPlayer].getTiles().size();) {
					if (southPanel.getComponent(i).getBackground() == Color.GREEN) {
						playerList[intCurrentPlayer].removeTile(i);
					} else
						i++;
				}
				scrabbleBoard.getTiles(playerList[intCurrentPlayer]);
				nextTurnBtn.doClick();
				rdbtnPlace.doClick();
			}

		}
	}

// Valider mot en fonction du dico
	private class validermot implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ea) {

			char[][] tempBoard = scrabbleBoard.getObjBoard();
			String mot;
			ArrayList<String> list = new ArrayList<String>();

			JButton temp = new JButton();

			for (int i = 0; i < tempBoard.length; i++) {
				for (int j = 0; j < tempBoard[i].length; j++) {

					if (tempBoard[i][j] != 0) {
						temp.setText(tempBoard[i][j] + "");
						list.add(temp.getText());

					}

				}

			}

			StringBuilder sb = new StringBuilder();

			for (String ch : list) {
				sb.append(ch);
			}

			mot = sb.toString();

			Dico dico;
			try {
				dico = new Dico();

				if (dico.contient(mot)) {
					motexistant = true;
					nextTurnBtn.doClick();

				} else {

					motexistant = false;
					JOptionPane.showMessageDialog(null, "Mot non existant");

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
	}

	private class suggestionmot implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ea) {
			JTextArea txtArea = new JTextArea(0, 5);

			txtArea.setEditable(false);

			ArrayList<Case> currentTiles = playerList[intCurrentPlayer].getTiles();
			ArrayList<Character> lettres = new ArrayList<Character>();
			char[][] tempBoard = scrabbleBoard.getObjBoard();
			String motTab;
			ArrayList<String> motGrid = new ArrayList<String>();

			JButton temp = new JButton();

			for (int i = 0; i < tempBoard.length; i++) {
				for (int j = 0; j < tempBoard[i].length; j++) {

					if (tempBoard[i][j] != 0) {
						temp.setText(tempBoard[i][j] + "");
						motGrid.add(temp.getText());

					}

				}

			}

			StringBuilder sb = new StringBuilder();

			for (String ch : motGrid) {
				sb.append(ch);
			}

			motTab = sb.toString().toLowerCase();

			for (int i = 0; i < currentTiles.size(); i++) {
				lettres.add((currentTiles.get(i).getLetter()));
			}
			for (Character ch : lettres) {
				sb.append(ch);
			}
			String word = sb.toString().toLowerCase() + motTab;
			String word2 = word.replace("*", "");

			Dico dico;

			try {

				dico = new Dico();

				String message = "\n Suggestions \n ";

				for (String mot : dico.findValidWords(word2)) {
					message += "\n" + mot;
					txtArea.setText(message.toUpperCase());
				}

				JScrollPane scrollPane = new JScrollPane(txtArea);
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				JOptionPane.showMessageDialog(null, scrollPane);

			} catch (IOException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private class radioClick implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ea) {
			if (((JRadioButton) ea.getSource()).equals(rdbtnPlace)) {
				rdbtnExchange.setSelected(false);
			} else if (((JRadioButton) ea.getSource()).equals(rdbtnExchange) && boolCanExchange) {
				rdbtnPlace.setSelected(false);
			} else {
				rdbtnExchange.setSelected(false);

				JOptionPane.showMessageDialog(null, "Vous ne pouvez pas changer de lettres après en avoir déjà placé");
				;
			}
		}
	}

// Nombre de joueurs
	private class txtFieldButton implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ea) {

			Nbjoueur = JOptionPane.showInputDialog("Nombre de joueurs: ", JOptionPane.INFORMATION_MESSAGE);

			try {

				int temp = Integer.parseInt(Nbjoueur);

				playerList = new Joueur[temp];

				if (temp < 1) {
					System.err.println("Entrez un chiffre");
				}

				else

				{
					for (int i = 0; i < temp; i++) {
						playerList[i] = new Joueur();
						scrabbleBoard.getTiles(playerList[i]);
					}
					intCurrentPlayer = 0;
					btnExchange.setEnabled(true);
					westPanelRedraw();
					rackUpdate();
				}
			} catch (NumberFormatException ex) {
				System.err.println("Entrez un chiffre");
			}

		}
	}

	private class changePlayers implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ea) {
			scrabbleBoard.getTiles(playerList[intCurrentPlayer]);
			if (intCurrentPlayer == playerList.length - 1) {
				intCurrentPlayer = 0;
			} else {
				intCurrentPlayer++;
			}
			boolCanExchange = true;
			westPanelRedraw();
			boardUpdate();
			rackUpdate();

		}
	}
	// +-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----++-----+-----+-----+-----+-----+-----+

//Functions

	public void switchColor(JButton input) {
		if (input.getBackground() == Color.orange) {
			input.setBackground(Color.green);
		} else if (input.getBackground() == Color.green) {
			input.setBackground(Color.orange);
		} else if (input.getBackground() == Color.white) {
			input.setBackground(Color.yellow);
		} else if (input.getBackground() == Color.yellow) {
			input.setBackground(Color.white);
		}
	}

	public void boardUpdate() {
		centerPanel.removeAll();
		char[][] tempBoard = scrabbleBoard.getObjBoard();
		for (int i = 0; i < tempBoard.length; i++) {
			for (int j = 0; j < tempBoard[i].length; j++) {

				JButton temp = new JButton();
				if (tempBoard[i][j] != 0) {
					temp.setText(tempBoard[i][j] + "");
					temp.setBackground(Color.CYAN);
				} else {
					temp.setBackground(Color.white);
				}
				temp.addActionListener(new tilePress());
				centerPanel.add(temp);
			}
		}

		centerPanel.revalidate();
		centerPanel.repaint();
		repaint();
	}

	public void startUp() {

		txtField = new JButton("Entrez un nombre de joueur  ");

		southPanel.add(txtField);
		txtField.addActionListener(new txtFieldButton());
		boolCanExchange = true;
	}

	public void westPanelRedraw() {
		westPanel.removeAll();

		JLabel displayPlayer = new JLabel("Tour du joueur : " + (intCurrentPlayer + 1));
		westPanel.add(displayPlayer);

		// Affichage score

		for (int i = 0; i < playerList.length; i++) {
			JLabel displayScores = new JLabel();
			displayScores.setText("Joueur " + (i + 1) + ": " + playerList[i].getScore());
			westPanel.add(displayScores);
		}

		nextTurnBtn = new JButton("Joueur suivant");
		westPanel.add(nextTurnBtn);
		nextTurnBtn.addActionListener(new changePlayers());
		westPanel.revalidate();
		westPanel.repaint();

	}

	public void rackUpdate() {
		southPanel.removeAll();
		for (Case tile : playerList[intCurrentPlayer].getTiles()) {
			JButton temp = new JButton();
			temp.setText(tile.getLetter() + "");
			temp.setBackground(Color.orange);
			temp.addActionListener(new rackPress());
			southPanel.add(temp);
		}
		southPanel.revalidate();
		southPanel.repaint();
	}

}
