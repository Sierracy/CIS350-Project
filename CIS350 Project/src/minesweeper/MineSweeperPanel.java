package minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*****************************************************************
 * 
 * @author Kate McGowan
 * 
 * @version February 2016
 *****************************************************************/

public class MineSweeperPanel extends JPanel {

	private JButton[][] board;
	private Cell iCell;
	private JButton quitButton;
	private JButton resetButton;
	private JButton minesButton;
	private JPanel buttonPanel;
	private JPanel gamePanel;
	private JLabel winLabel;
	private JLabel loseLabel;
	private ImageIcon smiley;
	private ImageIcon mine;
	private ImageIcon flag;
	private MineSweeperGame game;
	private int wins;
	private int losses;

	/*****************************************************************
	 * Constructor initializing game and GUI
	 *****************************************************************/
	public MineSweeperPanel() {
		game = new MineSweeperGame();

		winLabel = new JLabel("Wins: " + wins);
		loseLabel = new JLabel("Losses: " + losses);

		smiley = new ImageIcon("smiley.gif");
		mine = new ImageIcon("mine.jpg");
		flag = new ImageIcon("flag.png");

		quitButton = new JButton("Quit");
		quitButton.setFont(new Font("Arial", Font.PLAIN, 10));
		quitButton.addActionListener(new ButtonListener());

		resetButton = new JButton(smiley);
		resetButton.addActionListener(new ButtonListener());

		minesButton = new JButton("Mines");
		minesButton.addActionListener(new ButtonListener());
		minesButton.setFont(new Font("Arial", Font.PLAIN, 10));

		buttonPanel = new JPanel();
		buttonPanel.add(quitButton);
		buttonPanel.add(winLabel);
		buttonPanel.add(resetButton);
		buttonPanel.add(loseLabel);
		buttonPanel.add(minesButton);

		gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(game.getRows(), game.getCols()));
		gamePanel.setBackground(Color.gray);
		board = new JButton[game.getRows()][game.getCols()];
		createButtons();
		setLayout(new BorderLayout());
		add(buttonPanel, BorderLayout.NORTH);
		add(gamePanel, BorderLayout.CENTER);

	}

	/*****************************************************************
	 * A method to create the grid of buttons
	 *****************************************************************/
	private void createButtons() {
		for (int row = 0; row < game.getRows(); row++) {
			for (int col = 0; col < game.getCols(); col++) {
				// cannot be enabled with ImageIcon
				board[row][col] = new JButton();
				board[row][col].setPreferredSize(new Dimension(40, 40));
				board[row][col].addActionListener(new ButtonListener());
				board[row][col].addMouseListener(new ButtonListener());
				gamePanel.add(board[row][col]);

			}
		}
	}

	/*****************************************************************
	 * A method that updates the wins and losses counts
	 *****************************************************************/
	private void updateLabels() {
		winLabel.setText("Wins: " + wins);
		loseLabel.setText("Losses: " + losses);
	}

	/*****************************************************************
	 * A method that displays the game board
	 *****************************************************************/
	private void displayBoard() {
		for (int row = 0; row < game.getRows(); row++)

			for (int col = 0; col < game.getCols(); col++) {
				Cell iCell = new Cell();
				iCell = game.getCell(row, col);

				if (iCell.isExposed()) {
					board[row][col].setEnabled(false);

				} else

					board[row][col].setEnabled(true);

				if (iCell.isExposed()) {
					int nc = game.getNeighborCount(row, col);
					if (iCell.isMine()) {
						board[row][col].setIcon(mine);

					} else {
						board[row][col].setText("" + nc);
						board[row][col].setFont(new Font("Arial", Font.PLAIN, 11));
					}
				}
			}
	}

	/*****************************************************************
	 * A method that resets the button text
	 *****************************************************************/
	private void resetButtonText() {
		for (int row = 0; row < game.getRows(); row++) {
			for (int col = 0; col < game.getCols(); col++) {
				board[row][col].setText("");
				board[row][col].setIcon(null);
			}
		}
	}

	/*****************************************************************
	 * A method that is called when a button is clicked
	 *****************************************************************/
	private class ButtonListener implements ActionListener, MouseListener {
		public void actionPerformed(ActionEvent event) {

			JComponent buttonPressed = (JComponent) event.getSource();

			for (int row = 0; row < game.getRows(); row++) {
				for (int col = 0; col < game.getCols(); col++) {
					if (board[row][col] == event.getSource() && game.checkFlagged(row, col) == false) {
						game.select(row, col);
						game.flood(row, col);
						if (game.getGameStatus() == GameStatus.Lost) {
							JOptionPane.showMessageDialog(null, "You hit a mine. Game Over.");
							losses++;
						} else if (game.getGameStatus() == GameStatus.Won) {
							JOptionPane.showMessageDialog(null, "Congratulations! You won the game.");
							wins++;

						}

					}
				}

				if (buttonPressed == resetButton)

				{
					game.reset();
					resetButtonText();
				}

				if (buttonPressed == quitButton)

				{
					int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit the game?",
							"Quit", JOptionPane.YES_NO_OPTION);
					if (response == JOptionPane.YES_OPTION) {
						System.exit(1);
					} else {
						return;
					}

				}

				if (buttonPressed == minesButton) {
					for (int row2 = 0; row2 < game.getRows(); row2++) {
						for (int col2 = 0; col2 < game.getCols(); col2++) {
							iCell = game.getCell(row2, col2);
							if (iCell.isMine()) {
								board[row2][col2].setIcon(mine);
							}
						}
					}
				}
				updateLabels();
				displayBoard();

			}

		}

		public void mousePressed(MouseEvent e) {
			for (int row = 0; row < game.getRows(); row++) {
				for (int col = 0; col < game.getCols(); col++) {
					if (e.getButton() == MouseEvent.BUTTON3) {
						if (board[row][col] == e.getSource()) {
							if (game.checkFlagged(row, col) == false) {
								game.flag(row, col);
								board[row][col].setIcon(flag);
							} else {
								game.unflag(row, col);
								board[row][col].setIcon(null);
							}
						}
					}
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}
	}

}
