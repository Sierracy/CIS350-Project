package minesweeper;

import javax.swing.JFrame;

/*****************************************************************
 * 
 * @author Kate McGowan
 * 
 * @version February 2016
 *****************************************************************/

public class MineSweeper {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Mine Sweeper");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MineSweeperPanel panel = new MineSweeperPanel();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
