import java.util.HashMap;
import java.util.Stack;
import java.util.PriorityQueue;
import java.util.Queue;
import java.math.BigInteger;

// GTNode as in General Tree Node (Each state of the puzzle is stored in an instant of a GTNode)
public class GTNode implements Comparable<GTNode> {

	// directions of a move of a state are stored to check for the feasibility of
	// the provided move
	String dir[] = { "up", "down", "right", "left" };
	public int dimension;
	// parent of the state is saved for tracking the solution when the goal-state is
	// found
	public GTNode parent;
	public int[][] matrix;
	public int level;
	// A* heuristic
	public int astar;

	// constructor for initial state of the puzzle where size is the dimensions of
	// the puzzle
	public GTNode(int size) {
		this.dimension = size;
		matrix = new int[size][size];
		parent = null;
		GTNode p = null;
		level = 0;
		astar = heuristic() + level;
	}

	// constructor for generated states from explore methods
	public GTNode(int[][] m, GTNode p) {
		matrix = m;
		parent = p;
		level = p.level + 1;
		astar = heuristic() + level;
	}

	// BFS exploration method to generate successors
	public void explore(HashMap<BigInteger, Integer> visited, Queue<GTNode> openList) {

		// loop to check the possible moves of the current state and then adding them to
		// the open list
		for (int i = 0; i < 4; i++) {
			int[][] temp = copy();
			// boolean flag to check if the generated possible state has already been
			// generated
			boolean already = false;
			if (move(temp, dir[i])) {
				if (visited.containsKey(hash(temp))) {
					already = true;
					continue;
				}
				if (!already) {
					openList.offer(new GTNode(temp, this));
					visited.put(hash(temp), 0);
				}
			}
		}
	}

	// DFS, DLS and ID exploration method to generate successors
	public void explore(HashMap<BigInteger, Integer> visited, Stack<GTNode> openList, int limit) {

		// if the given limit parameter is -1 then explore using DFS, if not then
		// explore using DLS or ID
		if (limit != -1)
			if (this.level >= limit)
				return;

		// loop to check the possible moves of the current state and then adding them to
		// the open list
		for (int i = 0; i < 4; i++) {
			int[][] temp = copy();
			// boolean flag to check if the generated possible state has already been
			// generated
			boolean already = false;
			if (move(temp, dir[i])) {
				if (visited.containsKey(hash(temp))) {
					already = true;
					continue;
				}
				if (!already) {
					openList.push(new GTNode(temp, this));
					visited.put(hash(temp), 0);
				}
			}
		}
	}

	// A* exploration method to generate successors
	public void exploreAstar(HashMap<BigInteger, Integer> visited, PriorityQueue<GTNode> openList) {
		for (int i = 0; i < 4; i++) {
			GTNode temp = new GTNode(this.copy(), this);
			boolean worse = false;
			if (move(temp.matrix, dir[i])) {
				if (visited.containsKey(hash(temp.matrix)))
					if (visited.get(hash(temp.matrix)) < temp.astar) {
						worse = true;
						continue;
					}
				if (!worse) {
					visited.put(hash(temp.matrix), temp.astar);
					openList.offer(temp);
				}
			}
		}
	}

	// calculates manhattan heuristic for given puzzle
	public int heuristic() {
		int c = 1;
		int h = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (c == matrix.length * matrix.length)
					continue;
				int x = find(c)[0];
				int y = find(c)[1];
				h += Math.abs(x - i) + Math.abs(y - j);
				c++;
			}
		}
		return h;
	}

	// prints the curent state of the puzzle
	public void print() {
		System.out.print("\n__________________________________");
		for (int i = 0; i < matrix.length; i++) {
			System.out.println("\n");
			for (int j = 0; j < matrix.length; j++) {
				System.out.print("|" + matrix[i][j] + "|" + "\t");
			}
		}
		System.out.println("\n__________________________________");
	}

	// fills the curent state of the puzzle (only used in the initial state)
	public int[][] fill() {
		int c = 1;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (i == dimension - 1 && j == dimension - 1)
					break;
				matrix[i][j] = c++;
			}
		}
		return matrix;
	}

	// shuffles the numbers in the curent state of the puzzle (only used in the
	// initial state)
	public void shuffle() {
		// centering the blank spot
		int limit = matrix.length - (matrix.length/2 + 1);
		for (int i = 0; i < limit; i++) {
			move(matrix, "up");
			move(matrix, "left");
		}
		// moving the numbers around
		for (int i = 0; i < 33 * dimension; i++) {
			move(matrix, dir[(int) (Math.random() * 4)]);
		}
	}

	// finds the coordinates of the given number
	public int[] find(int n) {
		int[] coord = new int[2];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j] == n) {
					coord[0] = i;
					coord[1] = j;
				}
			}
		}
		return coord;
	}

	// this method moves the empty slot in the direction specified
	public boolean move(int[][] a, String dir) {
		int zCoord[] = find(0);
		int x = zCoord[0];
		int y = zCoord[1];

		switch (dir) {

		case "up":
			if (x - 1 >= 0) {
				a[x][y] = a[x - 1][y];
				a[x - 1][y] = 0;
				return true;
			}
			break;
		case "down":
			if (x + 1 <= a.length - 1) {
				a[x][y] = a[x + 1][y];
				a[x + 1][y] = 0;
				return true;
			}
			break;
		case "right":
			if (y + 1 <= a.length - 1) {
				a[x][y] = a[x][y + 1];
				a[x][y + 1] = 0;
				return true;
			}
			break;
		case "left":
			if (y - 1 >= 0) {
				a[x][y] = a[x][y - 1];
				a[x][y - 1] = 0;
				return true;
			}
			break;
		default:
			return false;
		}
		return false;
	}

	// checks whether the puzzle is solved or not
	public boolean isComplete() {
		int c = 1;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (i == matrix.length - 1 && j == matrix.length - 1)
					break;
				if (matrix[i][j] != c)
					return false;
				c++;
			}
		}
		return true;
	}

	// returns a copy of the matrix of the given puzzle
	public int[][] copy() {
		int[][] copy = new int[matrix.length][matrix.length];
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix.length; j++) {
				copy[i][j] = matrix[i][j];
			}
		return copy;
	}

	// returns a completely unique hash function for the curent state of the puzzle
	public BigInteger hash(int[][] matrix) {
		String text = "";
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				text += Integer.toString(matrix[i][j]);
			}
		}
		BigInteger hash = new BigInteger(text);
		return hash;
	}

	// checks solvability of the puzzle
	public boolean isSolvable() {
		int linMatrix[] = new int[matrix.length * matrix.length];
		int count = 0;
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix.length; j++) {
				linMatrix[count] = matrix[i][j];
				count++;
			}
		int parity = 0;
		int gridWidth = (int) Math.sqrt(linMatrix.length);
		int row = 0;
		int blankRow = 0;

		for (int i = 0; i < linMatrix.length; i++) {
			if (i % gridWidth == 0) {
				row++;
			}
			if (linMatrix[i] == 0) {
				blankRow = row;
				continue;
			}
			for (int j = i + 1; j < linMatrix.length; j++) {
				if (linMatrix[i] > linMatrix[j] && linMatrix[j] != 0) {
					parity++;
				}
			}
		}

		if (gridWidth % 2 == 0) {
			if (blankRow % 2 == 0) {
				return parity % 2 == 0;
			} else {
				return parity % 2 != 0;
			}
		} else {
			return parity % 2 == 0;
		}
	}

	@Override
	public int compareTo(GTNode a) {
		if (this.astar > a.astar)
			return 1;
		else if (a.astar == this.astar)
			return 0;
		else
			return -1;
	}

}
