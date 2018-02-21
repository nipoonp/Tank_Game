package com.combat;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public abstract class GameState {
	
	// This is the abstract GameState class that every game state must extend.
	// Each state will have all these methods which are run in the game panel class.

	protected static GameStateManager gsm;

	public abstract void init();

	public abstract void update();

	public abstract void draw(Graphics g);

	public abstract void keyPressed(KeyEvent k);

	public abstract void keyReleased(KeyEvent k);

}
