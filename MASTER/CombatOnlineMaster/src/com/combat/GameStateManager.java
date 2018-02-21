package com.combat;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GameStateManager {

// This class does as per its name and manages the game states.
// It takes the current state and calls that states update, draw, key event
// functions, which are then transferred and called in the main game panel, where
// an instance of this GSM is created. 
	
	public static GameState currentGameState;
	private static int checkCnt = 1;

	

	public GameStateManager() {
		currentGameState = new State_Online(this);
		

	}

	 public static void setState(GameState state) {
		 checkCnt  = 0; // The point of this check count is so that we only update
		 currentGameState = state; // after we've set the state and initialised it. 
		 state.init(); //otherwise we were having null pointer issues due to update occuring before initialise.
		 checkCnt = 1;
	 }

	public void update() {
		
		if(checkCnt == 1){
		currentGameState.update();
		}
	}

	public void draw(Graphics g) {

	
		if(checkCnt == 1){
		currentGameState.draw(g);
		}
	}

	public void keyPressed(KeyEvent k) {

		
		currentGameState.keyPressed(k);
	}

	public void keyReleased(KeyEvent k) {

		
		currentGameState.keyReleased(k);
	}

}
