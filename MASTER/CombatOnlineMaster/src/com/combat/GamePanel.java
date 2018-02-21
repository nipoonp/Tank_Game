package com.combat;

import java.awt.*;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener {

	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;

	// game thread
	private Thread thread;
	private boolean running;
	private int FPS = 30;
	private long targetTime = 1000 / FPS;

	// image
	private BufferedImage image;
	private Graphics g;

	// gamestatemanager
	private GameStateManager gsm; // = new GameStateManager();

	public GamePanel() {
		super();

		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		setFocusable(true);
		requestFocus();	//sets the JPanels dimensions to the the required dimensions
						// and focus is set to allow key events. 
	}

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			addKeyListener(this); //Starts the main game loop thread.
			thread.start();
		}
	}

	@Override
	public void keyPressed(KeyEvent key) {
		gsm.keyPressed(key); 	// checks for any key presses from the set state.

	}

	@Override
	public void keyReleased(KeyEvent key) {
		gsm.keyReleased(key);	// checks for any key presses from the set state.

	}

	@Override
	public void keyTyped(KeyEvent key) {
		// TODO Auto-generated method stub

	}
	// Below is the main game loop. where the game objects get updated based on the 
	// "wait" variable. this is dependent on the FPS variable to refresh the game
	// in the most accurate way based on the fps.
	@Override
	public void run() {
		init();

		long start;
		long elapsed;
		long wait;

		while (running) {

			start = System.currentTimeMillis();

			update();
			draw();

			drawToScreen();

			elapsed = System.currentTimeMillis() - start;

			wait = targetTime - elapsed / 1000000;

			try {
				Thread.sleep(wait);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();	// draws "everything" to the screen.

	}

	private void draw() {
		gsm.draw(g); 	// draws the set state.

	}

	private void update() {
		gsm.update(); //updates the set state. 

	}

	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics) image.createGraphics();
		running = true;

		gsm = new GameStateManager(); //creates the game state manager to manage all states.

	}

}
