package com.combat;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class State_Online extends GameState {

	public Board_Online board_Human_VS_Human = new Board_Online();
;
	private boolean pause = false;

	public State_Online(GameStateManager gsm) {
		this.gsm = gsm;
		
	}

	@Override
	public void init() {

		board_Human_VS_Human = new Board_Online();
	}

	@Override
	public void update() {
		if (!pause) {
			board_Human_VS_Human.update();
		} //only play (or update) the game when the pause is off
		board_Human_VS_Human.updateTimer();
		// keep updating the timer no matter what to check for pauses.
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, 1024, 768);

		board_Human_VS_Human.paint(g);

		String Pause = "Game Paused";
		Font small = new Font("Helvetica", Font.BOLD, 75);
		g.setColor(Color.BLACK);
		g.setFont(small);	//draws the pause screen and updates the draw method in board

		if (pause) {
			g.drawString(Pause, 1024 / 2 - 250, 768 / 2);
		}

	}

	@Override
	public void keyPressed(KeyEvent k) {

		if (k.getKeyCode() == KeyEvent.VK_P) {
			pause = !pause; //toggles pause
			board_Human_VS_Human.setPause(pause);
		}
		if(k.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(0);
//			GameStateManager.setState(new State_Menu(gsm));
			//sets the state back to the main menu after escape is pushed.
		}

		if (k.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
			board_Human_VS_Human.setGameOver();
		}

//		if (k.getKeyCode() == KeyEvent.VK_ENTER) {
//			if (board_Human_VS_Human.isGameOver()) {
//				GameStateManager.currentGameState = new State_Menu(gsm);
//				
//			} //when the game is over and enter is pressed, you're taken back to the main
//		}		// menu
		
		if (!pause && (board_Human_VS_Human.isStart() == true)) {
			board_Human_VS_Human.hero.keyPressed(k); //only allow tanks to move after the 
//			board_Human_VS_Human.hero_2.keyPressed(k);// game has started
		}
		
	}

	@Override
	public void keyReleased(KeyEvent k) {
		// if (board != null) {
		if (!pause && (board_Human_VS_Human.isStart() == true)) {
			board_Human_VS_Human.hero.keyReleased(k);
//			board_Human_VS_Human.hero_2.keyReleased(k);
		}
		// }
	}

}
