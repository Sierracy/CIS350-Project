package minesweeper;

import java.util.Random;

import javax.swing.JOptionPane;

/*****************************************************************
 * 
 * @author Kate McGowan
 * 
 * @version February 2016
 *****************************************************************/

public class MineSweeperGame {

	private int mineCount;
	private Cell[][] board;
	private int rows;
	private int cols;

	/*****************************************************************
	 * Constructor to initialize an object of type MineSweeperGame
	 *****************************************************************/
	public MineSweeperGame() {
		String row = JOptionPane.showInputDialog(null, "Enter the desired number of rows:");
		if (checkForNumbers(row) == false || row.isEmpty() || row == null || Integer.parseInt(row) < 3
				|| Integer.parseInt(row) > 30) {
			JOptionPane.showMessageDialog(null, "Invalid input. Rows set to default.");
			this.rows = 10;
		} else {
			this.rows = Integer.parseInt(row);
		}

		String col = JOptionPane.showInputDialog(null, "Enter the desired number of columns:");
		if (checkForNumbers(col) == false || col.isEmpty() || col == null || Integer.parseInt(col) < 3
				|| Integer.parseInt(col) > 30) {
			JOptionPane.showMessageDialog(null, "Invalid input. Columns set to default.");
			this.cols = 10;
		} else {
			this.cols = Integer.parseInt(col);
		}

		String mines = JOptionPane.showInputDialog(null, "Enter the desired mine count:");
		if (checkForNumbers(mines) == false || mines.isEmpty() || mines == null
				|| Integer.parseInt(mines) > (this.rows * this.cols)) {
			JOptionPane.showMessageDialog(null, "Invalid input. Mine count set to default.");
			this.mineCount = 10;
		} else {
			this.mineCount = Integer.parseInt(mines);
		}
		reset();
	}

	/*****************************************************************
	 * A method to check if a string is comprised of only numbers
	 * 
	 * @param input
	 *            the text to be checked
	 * @return true/false depending on whether the string contained only digits
	 *****************************************************************/
	private boolean checkForNumbers(String input) {
		char array[] = input.toCharArray();
		for (int i = 0; i < array.length; i++) {
			if (Character.isDigit(array[i]) == false) {
				return false;
			} else {
				return true;
			}
		}
		return true;
	}

	/*****************************************************************
	 * A method to add up the number of mines surrounding a cell
	 * 
	 * @param row
	 *            the row of the desired cell
	 * @param col
	 *            the column of the desired cell
	 * @return the final count of neighboring mines
	 *****************************************************************/
	public int getNeighborCount(int row, int col) {
		int neighborCount = 0;
		board[row][col].zeroNeighborCount();
		// upper left square
		if ((row - 1 < getRows() && row - 1 >= 0) && (col - 1 < getCols() && col - 1 >= 0)) {
			if (board[row - 1][col - 1].isMine())
				board[row][col].addNeighbors(1);
		}
		// upper middle square
		if ((row - 1 < getRows() && row - 1 >= 0) && (col < getCols() && col >= 0)) {
			if (board[row - 1][col].isMine())
				board[row][col].addNeighbors(1);
		}
		// upper right square
		if ((row - 1 < getRows() && row - 1 >= 0) && (col + 1 < getCols() && col + 1 >= 0)) {
			if (board[row - 1][col + 1].isMine())
				board[row][col].addNeighbors(1);
		}
		// middle left square
		if ((row < getRows() && row >= 0) && (col - 1 < getCols() && col - 1 >= 0)) {
			if (board[row][col - 1].isMine())
				board[row][col].addNeighbors(1);
		}
		// middle right square
		if ((row < getRows() && row >= 0) && (col + 1 < getCols() && col + 1 >= 0)) {
			if (board[row][col + 1].isMine())
				board[row][col].addNeighbors(1);
		}
		// lower left square
		if ((row + 1 < getRows() && row + 1 >= 0) && (col - 1 < getCols() && col - 1 >= 0)) {
			if (board[row + 1][col - 1].isMine())
				board[row][col].addNeighbors(1);
		}
		// lower middle square
		if ((row + 1 < getRows() && row + 1 >= 0) && (col < getCols() && col >= 0)) {
			if (board[row + 1][col].isMine())
				board[row][col].addNeighbors(1);
		}
		// lower right square
		if ((row + 1 < getRows() && row + 1 >= 0) && (col + 1 < getCols() && col + 1 >= 0)) {
			if (board[row + 1][col + 1].isMine())
				board[row][col].addNeighbors(1);
		}
		neighborCount = board[row][col].getNeighbors();

		return neighborCount;
	}

	/*****************************************************************
	 * A method to open up surrounding cells containing a zero neighbor count
	 * 
	 * @param row
	 *            the entered row coordinate
	 * @param col
	 *            the entered column coordinate
	 *****************************************************************/
	public void flood(int row, int col) {
		if (board[row][col].isFlagged()) {
			return;
		}
		if (!board[row][col].isExposed()) {
			board[row][col].setExposed(true);

			if (board[row][col].isMine()) {
				return;
			}
			if (getNeighborCount(row, col) == 0) {
				for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						if (row + i < getRows() && row + i >= 0 && col + j < getCols() && col + j >= 0) {
							flood(row + i, col + j);
						}
					}
				}
			}
		}
	}

	/*****************************************************************
	 * A method that returns the chosen cell
	 * 
	 * @param row
	 *            the entered row coordinate
	 * @param col
	 *            the entered column coordinate
	 *****************************************************************/
	public Cell getCell(int row, int col) {
		return board[row][col];
	}

	/*****************************************************************
	 * A method that returns the current game status
	 *****************************************************************/
	public GameStatus getGameStatus() {
		int count = 0;
		for (int row = 0; row < getRows(); row++) {
			for (int col = 0; col < getCols(); col++) {
				if (board[row][col].isMine() && board[row][col].isExposed()) {
					return GameStatus.Lost;
				} else if (board[row][col].isExposed()) {
					count++;
				}
			}

			int nonMines = (this.rows * this.cols) - getMineCount();
			if (count == nonMines) {
				return GameStatus.Won;
			}

		}
		return GameStatus.NotOverYet;
	}

	/*****************************************************************
	 * A method that returns the number of rows
	 *****************************************************************/
	public int getRows() {
		return this.rows;
	}

	/*****************************************************************
	 * A method that returns the number of columns
	 *****************************************************************/
	public int getCols() {
		return this.cols;
	}

	/*****************************************************************
	 * A method that returns the number of mines
	 *****************************************************************/
	public int getMineCount() {
		return this.mineCount;
	}

	/*****************************************************************
	 * A method that registers a selected cell
	 * 
	 * @param row
	 *            the entered row coordinate
	 * @param col
	 *            the entered column coordinate
	 *****************************************************************/
	public void select(int row, int col) {
		if (board[row][col].isFlagged() || board[row][col].isMine()) {
			return;
		} else {
			if (getNeighborCount(row, col) == 0) {
				flood(row, col);
			}
		}
	}

	/*****************************************************************
	 * A method that flags a cell
	 * 
	 * @param row
	 *            the entered row coordinate
	 * @param col
	 *            the entered column coordinate
	 *****************************************************************/
	public void flag(int row, int col) {
		board[row][col].setFlagged(true);
	}

	/*****************************************************************
	 * A method that unflags a cell
	 * 
	 * @param row
	 *            the entered row coordinate
	 * @param col
	 *            the entered column coordinate
	 *****************************************************************/
	public void unflag(int row, int col) {
		board[row][col].setFlagged(false);
	}

	/*****************************************************************
	 * A method that checks if a cell is flagged
	 * 
	 * @param row
	 *            the entered row coordinate
	 * @param col
	 *            the entered column coordinate
	 *****************************************************************/
	public boolean checkFlagged(int row, int col) {
		if (board[row][col].isFlagged() == true) {
			return true;
		} else {
			return false;
		}
	}

	/*****************************************************************
	 * A method that resets the board to a new game
	 *****************************************************************/
	public void reset() {

		board = new Cell[this.rows][this.cols];

		for (int row = 0; row < this.rows; row++) {
			for (int col = 0; col < this.cols; col++) {
				board[row][col] = new Cell(0, false, false, false);
			}
		}

		Random random = new Random();
		int count = 0;
		while (count < this.mineCount) {
			int col = random.nextInt(cols);
			int row = random.nextInt(rows);
			if (!board[row][col].isMine()) {
				board[row][col].setMine(true);
				count++;
			}

		}
	}
}