package com.combat;

import javax.swing.JFrame;

public class Game extends JFrame {



	public static void main(String[] args) {
		JFrame window = new JFrame("Combat 2.0");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		
	}

}
