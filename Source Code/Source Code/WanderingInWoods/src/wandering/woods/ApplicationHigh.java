package wandering.woods;

/*
 * Author 1: Sai sathvik 
 * Email Id: saisathvikduduku@lewisu.edu
 * 
 * Author 2: Gopi
 * Email Id: gopisandeepyerra@lewisu.edu
 * 
 * Author 3: Sailusha 
 * Email Id: sailushaijjada@lewisu.edu
 * 
 * Author 4: Deepak uppu
 * Email Id: deepakuppu@lewisu.edu
 * 
 * Author 5: Vikas
 * Email Id: vikasmothe@lewisu.edu
 */


import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import wandering.woods.model.Player;

public class ApplicationHigh extends JFrame implements KeyListener {

	static ArrayList<Player> players = new ArrayList<Player>();
	Icon meeting = new ImageIcon("meeting.gif");
	Icon empty = new ImageIcon("empty.png");
	JButton buttons[][];
	static String position[][];
	static int rows = 0;
	static int col = 0;
	String currentPlayer = "1";

	static JTextField rowField ;
	static JTextField colField ;
	static JTextField[] playerRow;
	static JTextField[] playercol;
	static JTextField noOfPlayers;
	Clip clip;
	static JLabel errorMessage = new JLabel();
	LinkedList<Integer> playersMeet = new LinkedList<Integer>();

	public ApplicationHigh() {

		position = new String[rows][col];
		buttons = new JButton[rows][col];

		initializePosition();

		paintScreen();
		addKeyListener(this);
		setFocusable(true);
		setTitle("Wandering In Woods Game  -  Controls Up ↑, Down ↓, Right →, Left ←, Info I Quit Q Reset R");
	}

	private void initializePosition() {
		for (String[] row : position)
			Arrays.fill(row, "*");
		while (!playerPosition()) {

		}
	}

	public void paintScreen() {

		initialize();

		setLayout(new GridLayout(rows, col));
		setSize(600, 600);
		setVisible(true);
	}

	private void initialize() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < col; j++) {
				buttons[i][j] = getButton(position[i][j]);
				add(buttons[i][j]);
			}
		}
	}

	public void repaintScreen() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < col; j++) {
				buttons[i][j].setIcon(getIcon(position[i][j]));
				buttons[i][j].setText(getText(position[i][j]));
			}
		}
	}

	public JButton getButton(String ch) {
		switch (ch) {
		case "*":
			return new JButton("", empty);

		default:
			return new JButton("P: " + ch,
					players.stream().filter(p -> p.getSymbol().equals(ch)).findFirst().get().getAvator());

		}
	}

	public String getText(String ch) {
		switch (ch) {
		case "*":
			return "";

		default:
			return "P: " + ch;
		}
	}

	public Icon getIcon(String ch) {
		switch (ch) {
		case "*":
			return empty;

		default:
			if (players.stream().filter(p -> p.getSymbol().equals(ch)).findFirst().isPresent()) {
				return players.stream().filter(p -> p.getSymbol().equals(ch)).findFirst().get().getAvator();
			} else {
				return meeting;
			}

		}
	}

	private boolean canMove(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyChar() == 'Q' || e.getKeyChar() == 'q') {
			int isQuit = JOptionPane.showConfirmDialog(this, "Sure? You want to exit?", "Wandering In Woods",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (isQuit == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
			return true;
		} else if(e.getKeyChar() == 'R' || e.getKeyChar() == 'r') {
			 resetPosition();
			 return true;
		}
			else if (e.getKeyChar() == 'I' || e.getKeyChar() == 'i') {
		
			displayStatistics();
			return true;

		} else {
			
			if(players.size()==playersMeet.size()) {
				if (clip != null)
					clip.stop();
				return true;
			}
			
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < col; j++) {
					if (position[i][j].equals(currentPlayer) || position[i][j].contains(currentPlayer)) {
						if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
							if (col > j + 1) {
								movePosition(i, j, i, j + 1);
								return true;
							}
							return false;
						} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
							if (0 <= j - 1) {
								movePosition(i, j, i, j - 1);
								return true;
							}
							return false;
						} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
							if (rows > i + 1) {
								movePosition(i, j, i + 1, j);
								return true;
							}
							return false;
						} else if (e.getKeyCode() == KeyEvent.VK_UP) {
							if (0 <= i - 1) {
								movePosition(i, j, i - 1, j);
								return true;
							}
							return false;
						}

						break;
					}
				}
			}
		}

		return false;
	}

	private void displayStatistics() {
		
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		for (int i = 1; i <= players.size(); i++) {
			PathFinder p = new PathFinder();
			if (!String.valueOf(i).equals(currentPlayer)) {
				int distance = p.minDistance(position, currentPlayer, String.valueOf(i));
				myPanel.add(new Label(
						"Player " + currentPlayer + " to " + " Player " + i + " shortest distance is " + distance));
			}

		}

		int overallAvg = 0;
		for (Player filterPlayer : players) {
			String longestRun = filterPlayer.getLongestRun() == -1 ? "Nil" : "" + filterPlayer.getLongestRun();
			String shortestRun = filterPlayer.getShortestRun() == Integer.MAX_VALUE ? "Nil"
					: "" + filterPlayer.getShortestRun();
			int longest = longestRun.equals("Nil") ? 0 : Integer.parseInt(longestRun);
			int shortest = shortestRun.equals("Nil") ? 0 : Integer.parseInt(shortestRun);
			int averageRun = ((longest + shortest) / 2);
			overallAvg = overallAvg + averageRun;
			myPanel.add(new Label("Player " + filterPlayer.getSymbol() + " longest run is " + longestRun
					+ " and shortest run is " + shortestRun + " Average run is " + averageRun));
		}
		myPanel.add(new Label("Overall average run is " + (overallAvg / players.size())));
		
		JOptionPane.showConfirmDialog(null, myPanel, "Game Info Statistics", JOptionPane.OK_OPTION);
		if (clip != null)
			clip.stop();
	}

	private void resetPosition() {
		// TODO Auto-generated method stub
		for (String[] row : position)
			Arrays.fill(row, "*");
		for (int i = 1; i <= Integer.parseInt(noOfPlayers.getText()); i++) {
			position[Integer.parseInt(playerRow[i - 1].getText())][Integer
				                           							.parseInt(playercol[i - 1].getText())] = "" + i;
					
		}
		playersMeet = new LinkedList<Integer>();
		currentPlayer = "1";
		repaintScreen();
		
	}

	private void movePosition(int i, int j, int newRow, int newCol) {
		if (clip != null)
			clip.stop();
		if (position[i][j].contains("-")) {
			position[i][j] = position[i][j].replace(currentPlayer, "");
			position[i][j] = position[i][j].replace("-", "");
		} else {
			position[i][j] = "*";
		}

		if (players.stream().filter(p -> position[newRow][newCol].contains("-") || p.getSymbol().equals(position[newRow][newCol])).findAny().isPresent()) {
			try {
				File file = new File("game-over.wav");
				AudioInputStream sound = AudioSystem.getAudioInputStream(file);
				clip = AudioSystem.getClip();
				clip.open(sound);
				clip.loop(Clip.LOOP_CONTINUOUSLY);

				playersMeet.add(Integer.parseInt(currentPlayer));
				if (!position[newRow][newCol].contains("-"))
					playersMeet.add(Integer.parseInt(position[newRow][newCol]));
				

				players.stream().map(p -> {
					if (p.getSymbol().equals(currentPlayer)) {
						p.setMovements(p.getMovements() + 1);
						p.setLongestRun(p.getLongestRun() > p.getMovements() ? p.getLongestRun() : p.getMovements());
						p.setShortestRun(p.getShortestRun() < p.getMovements() ? p.getShortestRun() : p.getMovements());
						p.setMovements(0);
					}
					return p;
				}).collect(Collectors.toList());
				position[newRow][newCol] = currentPlayer + "-" + position[newRow][newCol];
			} catch (Exception e) {
				System.out.println(e);
			}
			

		} else {

			players.stream().map(p -> {
				if (p.getSymbol().equals(currentPlayer)) {
					p.setMovements(p.getMovements() + 1);
					p.setLongestRun(p.getLongestRun() > p.getMovements() ? p.getLongestRun() : p.getMovements());
				}
				return p;
			}).collect(Collectors.toList());
			position[newRow][newCol] = currentPlayer;

		}
		getPlayer();

		repaintScreen();
		if(playersMeet.size()==players.size()) {
			displayStatistics();
		}
	}

	private void getPlayer() {
		int tempPlayer = Integer.parseInt(currentPlayer);
		for (int i = 0; i < players.size(); i++) {
			tempPlayer = tempPlayer + 1 > players.size() ? 1:  (tempPlayer + 1);
			if (!playersMeet.contains(tempPlayer)) {
				currentPlayer = ""+tempPlayer;
				break;
			}
		}
		

	}

	public static void run(String[] args) {

		while (!gameInputs()) {

		}

		new ApplicationHigh();
	}

	private static boolean gameInputs() {
		
		noOfPlayers = new JTextField(1);

		rowField = new JTextField(2);
		colField = new JTextField(2);

		errorMessage.setForeground(Color.RED);
		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Game Size row:"));
		myPanel.add(rowField);
		//myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		myPanel.add(new JLabel(":Game Size Column"));
		myPanel.add(colField);
		myPanel.add(new JLabel("Number of Players"));
		myPanel.add(noOfPlayers);
		myPanel.add(errorMessage);

		int result = JOptionPane.showConfirmDialog(null, myPanel,
				"Please Enter Game size row & column value and number of players", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			try {
				rows = Integer.parseInt(rowField.getText());
				col = Integer.parseInt(colField.getText());
				if (rows < 4 || col < 4) {
					errorMessage.setText("Minimum game size must be of 4 * 4 ");
					return false;
				}
				if (Integer.parseInt(noOfPlayers.getText()) < 2 || Integer.parseInt(noOfPlayers.getText()) > 8) {
					errorMessage.setText("Number of players must be above 2 and less than 8");
					return false;
				}
				for (int i = 1; i <= Integer.parseInt(noOfPlayers.getText()); i++) {
					players.add(new Player("" + i));
				}
				playerRow = new JTextField[Integer.parseInt(noOfPlayers.getText())];
				playercol = new JTextField[Integer.parseInt(noOfPlayers.getText())];
			} catch (Exception e) {
				// TODO: handle exception
				errorMessage.setText("Invalid player position");
				return false;
			}

		} else {
			System.exit(0);
		}
		errorMessage.setText("");
		return true;
	}

	private static boolean playerPosition() {
		JPanel myPanel = new JPanel();

		for (int i = 0; i < players.size(); i++) {
			myPanel.add(new JLabel("Player :" + (i + 1) + " Position Row & Col"));
			playercol[i] = new JTextField(1);
			playerRow[i] = new JTextField(1);
			myPanel.add(playerRow[i]);
			myPanel.add(playercol[i]);
		}

		myPanel.add(errorMessage);

		int result = JOptionPane.showConfirmDialog(null, myPanel, "Please Enter the Initial position of the player ",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			try {

				for (int i = 1; i <= Integer.parseInt(noOfPlayers.getText()); i++) {
					if (Integer.parseInt(playerRow[i - 1].getText()) > position.length
							|| Integer.parseInt(playerRow[i - 1].getText()) < 0
							|| Integer.parseInt(playercol[i - 1].getText()) < 0
							|| Integer.parseInt(playercol[i - 1].getText()) > position[0].length) {
						errorMessage.setText("Invalid player position");
						return false;
					}
					if(!position[Integer.parseInt(playerRow[i - 1].getText())][Integer
					                              							.parseInt(playercol[i - 1].getText())].equals("*")) {
						errorMessage.setText("Invalid player position already occupied");
						for (String[] row : position)
							Arrays.fill(row, "*");
						
						return false;
					}
					position[Integer.parseInt(playerRow[i - 1].getText())][Integer
							.parseInt(playercol[i - 1].getText())] = "" + i;
				}

			} catch (Exception e) {
				// TODO: handle exception
				errorMessage.setText("Invalid player position");
				return false;
			}

		} else {
			System.exit(0);
		}

		return true;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (canMove(e)) {

		} else {
			JPanel jPanel = new JPanel();
			jPanel.add(new JLabel("Invalid Move player " + currentPlayer + " cannot move "
					+ getDirection(e.getKeyCode()) + ", Try Again!", getIcon(currentPlayer), SwingConstants.LEFT));
			JOptionPane.showMessageDialog(null, jPanel, "Alert", JOptionPane.ERROR_MESSAGE);
		}
	}

	public String getDirection(int ch) {
		switch (ch) {
		case KeyEvent.VK_LEFT:
			return "LEFT";
		case KeyEvent.VK_RIGHT:
			return "RIGHT";
		case KeyEvent.VK_UP:
			return "UP";
		case KeyEvent.VK_DOWN:
			return "DOWN";
		default:
			return "";

		}
		
	}

}

