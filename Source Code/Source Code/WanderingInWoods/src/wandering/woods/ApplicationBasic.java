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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ApplicationBasic extends JFrame implements KeyListener {

	Icon player1Icon = new ImageIcon(
			new ImageIcon("player1.png").getImage().getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH));
	Icon player2Icon = new ImageIcon(
			new ImageIcon("player2.png").getImage().getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH));
	Icon empty = new ImageIcon("empty.png");
	Icon meetingIcon = new ImageIcon("meeting.gif");
	JButton buttons[][];
	String position[][];
	static int rows = 8;
	static int col = 8;
	String currentPlayer = "@";
	List<String> players = new ArrayList<String>(Arrays.asList("@", "#"));

	int playerOneCounts = 0;
	int playerTwoCounts = 0;

	public ApplicationBasic() {

		position = new String[rows][col];
		buttons = new JButton[rows][col];

		initializePosition();

		paintScreen();
		addKeyListener(this);
		setFocusable(true);
		setTitle("Wandering In Woods Game  -  Controls Up ↑, Down ↓, Right →, Left ←, Quit Q");
	}

	private void initializePosition() {
		for (String[] row : position)
			Arrays.fill(row, "*");
		position[0][0] = "@";
		position[rows - 1][col - 1] = "#";
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
			}
		}
	}

	public JButton getButton(String ch) {
		switch (ch) {
		case "@":
			return new JButton("", player1Icon);
		case "#":
			return new JButton("", player2Icon);

		default:
			return new JButton("", empty);
		}
	}

	public Icon getIcon(String ch) {
		switch (ch) {
		case "@":
			return player1Icon;
		case "#":
			return player2Icon;
		case "!":
			return meetingIcon;

		default:
			return empty;
		}
	}

	private boolean canMove(KeyEvent e) {
		// TODO Auto-generated method stub
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < col; j++) {
				if (position[i][j].equals(currentPlayer)) {
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
					} else if (e.getKeyChar() == 'Q' || e.getKeyChar() == 'q') {
						int isQuit = JOptionPane.showConfirmDialog(this, "Sure? You want to exit?",
								"Wandering In Woods", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (isQuit == JOptionPane.YES_OPTION) {
							System.exit(0);
						}
					}
					break;
				}
			}
		}
		return false;
	}

	private void movePosition(int i, int j, int newRow, int newCol) {

		position[i][j] = "*";
		if (players.contains(position[newRow][newCol])) {
			try {
				position[newRow][newCol] = "!";
				repaintScreen();
				File file = new File("game-over.wav");
				AudioInputStream sound = AudioSystem.getAudioInputStream(file);
				Clip clip = AudioSystem.getClip();
				clip.open(sound);
				clip.loop(Clip.LOOP_CONTINUOUSLY);

				int reset = JOptionPane.showConfirmDialog(null,
						"Game over! Player One moves: " + playerOneCounts + " Player two Moves: " + playerTwoCounts,
						"Wandering In Woods", JOptionPane.DEFAULT_OPTION);
				if (0 == reset) {
					clip.stop();
					initializePosition();
					repaintScreen();
				}
			} catch (Exception e) {
				System.out.println(e);
			}

		} else {

			position[newRow][newCol] = currentPlayer;
			playerOneCounts = currentPlayer.equals("@") ? playerOneCounts + 1 : playerOneCounts;
			playerTwoCounts = currentPlayer.equals("#") ? playerTwoCounts + 1 : playerTwoCounts;
			currentPlayer = currentPlayer.equals("@") ? "#" : "@";
			repaintScreen();
		}

	}

	static JTextField rowField;
	static JTextField colField;
	static JLabel errorMessage = new JLabel();

	private static boolean gameInputs() {

		int noOfPlayers = 2;

		rowField = new JTextField(2);
		colField = new JTextField(2);

		errorMessage.setForeground(Color.RED);
		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Game Size row:"));
		myPanel.add(rowField);
		// myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		myPanel.add(new JLabel(":Game Size Column"));
		myPanel.add(colField);

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

			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}

		} else {
			System.exit(0);
		}
		errorMessage.setText("");
		return true;
	}

	public static void run(String[] args) {
		while (!gameInputs()) {

		}
		new ApplicationBasic();
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
