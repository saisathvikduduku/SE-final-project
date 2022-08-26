package wandering.woods.model;

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

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Player {

	private Icon avator;
	private int movements;
	private int shortestRun;
	private int longestRun;
	private String symbol;

	public Player(String symbol) {
		this.avator = new ImageIcon(new ImageIcon("player" + symbol + ".png").getImage().getScaledInstance(70, 70,
				java.awt.Image.SCALE_SMOOTH));
		;
		this.symbol = symbol;
		this.movements = 0;
		this.longestRun = -1;
		this.shortestRun = Integer.MAX_VALUE;
	}

	public int getMovements() {
		return movements;
	}

	public void setMovements(int movements) {
		this.movements = movements;
	}

	public int getShortestRun() {
		return shortestRun;
	}

	public void setShortestRun(int shortestRun) {
		this.shortestRun = shortestRun;
	}

	public int getLongestRun() {
		return longestRun;
	}

	public void setLongestRun(int longestRun) {
		this.longestRun = longestRun;
	}

	public Icon getAvator() {
		return avator;
	}

	public String getSymbol() {
		return symbol;
	}

}
