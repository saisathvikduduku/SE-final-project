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

import java.util.LinkedList;
import java.util.Queue;

class GameGrid {
	int row;
	int col;
	int dist;

	public GameGrid(int row, int col, int dist) {
		this.row = row;
		this.col = col;
		this.dist = dist;
	}
}

public class PathFinder {
	int minDistance(String[][] grid, String src, String dest) {
		GameGrid source = new GameGrid(0, 0, 0);

		firstLoop: for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {

				if (grid[i][j].equals(src)) {
					source.row = i;
					source.col = j;
					break firstLoop;
				}
			}
		}

		Queue<GameGrid> queue = new LinkedList<>();
		queue.add(new GameGrid(source.row, source.col, 0));

		boolean[][] visited = new boolean[grid.length][grid[0].length];
		visited[source.row][source.col] = true;

		while (queue.isEmpty() == false) {
			GameGrid p = queue.remove();

			if (grid[p.row][p.col].equals(dest))
				return p.dist;

			if (isValid(p.row - 1, p.col, grid, visited)) {
				queue.add(new GameGrid(p.row - 1, p.col, p.dist + 1));
				visited[p.row - 1][p.col] = true;
			}

			if (isValid(p.row + 1, p.col, grid, visited)) {
				queue.add(new GameGrid(p.row + 1, p.col, p.dist + 1));
				visited[p.row + 1][p.col] = true;
			}

			if (isValid(p.row, p.col - 1, grid, visited)) {
				queue.add(new GameGrid(p.row, p.col - 1, p.dist + 1));
				visited[p.row][p.col - 1] = true;
			}

			if (isValid(p.row, p.col + 1, grid, visited)) {
				queue.add(new GameGrid(p.row, p.col + 1, p.dist + 1));
				visited[p.row][p.col + 1] = true;
			}
		}
		return -1;
	}

	private static boolean isValid(int x, int y, String[][] grid, boolean[][] visited) {
		if (x >= 0 && y >= 0 && x < grid.length && y < grid[0].length && grid[x][y] != "0" && visited[x][y] == false) {
			return true;
		}
		return false;
	}

}