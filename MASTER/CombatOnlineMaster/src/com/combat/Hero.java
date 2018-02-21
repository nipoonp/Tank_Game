package com.combat;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.UUID;

public class Hero extends General implements Runnable {

	private Thread loop;
	private double speed = 3;
	private ArrayList<Missile> missiles = new ArrayList<Missile>();
	private ArrayList<Missile> missilesAll = new ArrayList<Missile>();
	private ArrayList<String> missileObjectID = new ArrayList<String>();
	private ArrayList<String> missileAllObjectID = new ArrayList<String>();
	long lastShotTime = System.currentTimeMillis();
	long lastPowerupTime = System.currentTimeMillis();
	private int currentPowerup = 0;
	private int fireRate = 500;
	private boolean moveForward, moveBackward;
	public boolean started = false;
	private double prevX = 0, prevY = 0, prevAngle = 0;
	private int index = 0;
	private int keyType = 0;
	private int value = 0;

	// this is the logger for the hero class, only this class is being logged
	private boolean powerup = false;
//	Logger logger = Logger.getLogger("MyLog");
	FileHandler fh;

	public boolean isPowerup() {
		return powerup;
	}

	// hero class is used in most of the boards
	public Hero(double x, double y, double angle, int modeSel) {

		this.modeSel = modeSel;
		imageSet = new srcImage(modeSel);

		this.x = x;
		this.y = y;
		this.angle = angle;

		// here we call imageSet and get the required tank image
		image = imageSet.getTank1Image();
		width = image.getWidth(null);
		height = image.getHeight(null);

		moveForward = moveBackward = false;
//		initLogger();
		loop = new Thread(this);
		loop.start();

	}
	
	public String generateObjectID() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.substring(0, 8);
		return uuid;

	}

//	private void initLogger() {
//		try {
//
//			// This block configure the logger with handler and formatter
//			fh = new FileHandler("MyLogFile.txt");
//			logger.addHandler(fh);
//			// logger.setLevel(Level.ALL);
//			SimpleFormatter formatter = new SimpleFormatter();
//			fh.setFormatter(formatter);
//
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}

	// move toward the angle
	// forward
	public void moveForward() {
		// Here we use the sin and cos fucntions to get the distance moved when
		// the tank is on an angle
		// The prevX and prevY is used to bring the tank back to the previous
		// coordinates if the tank tries to go out of the map
		prevX = x;
		prevY = y;
		x += speed * Math.cos(Math.toRadians(angle));
		y += speed * Math.sin(Math.toRadians(angle));
		if (x < 0 || x > (1024 - 48) || y < 0 || y > (768 - 48)) {
			x = prevX;
			y = prevY;
		}
	}

	// backward
	public void moveBackword() {
		// Here we use the sin and cos fucntions to get the distance moved when
		// the tank is on an angle
		// The prevX and prevY is used to bring the tank back to the previous
		// coordinates if the tank tries to go out of the map
		prevX = x;
		prevY = y;
		x -= speed * Math.cos(Math.toRadians(angle));
		y -= speed * Math.sin(Math.toRadians(angle));
		if (x < 0 || x > (1024 - 48) || y < 0 || y > (768 - 48)) {
			x = prevX;
			y = prevY;
		}
	}

	// Here set index is used for the srcImage class, where it gets the desired
	// images (images like tank image, bullet image, powerup image)
	public void setIndex(int index) {
		this.index = index;
	}

	public double getPrevX() {
		return prevX;
	}

	public double getPrevY() {
		return prevY;
	}

	public double getPrevAngle() { // do i even need this?
		return prevAngle;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	// these are arraylist of missiles which stores the bullets which are shot
	public ArrayList<Missile> getMissiles() {
		return missiles;
	}
		public ArrayList<Missile> getMissilesAll() {
		return missilesAll;
	}
	public ArrayList<String> getMissilesObjectID() {
		return missileObjectID;
	}
		public ArrayList<String> getMissilesAllObjectID() {
		return missileAllObjectID;
	}
		
		public void removeMissilesObjectID(int i) {
			missileObjectID.remove(i);
		}
	
	
	public void setCurrentPowerup(int currentPowerup) {
		this.currentPowerup = currentPowerup;
		lastPowerupTime = System.currentTimeMillis();
	}

	public int getCurrentPowerup() {
		return currentPowerup;
	}

	public void play() {

		// this is just to keep the angle between 0 and 360
		if (angle == 360){
			angle = 0;
		} else if (angle < 0){
			angle = 360 + angle;
		}

		prevAngle = angle;// do i need this?

		// setting the hero angle
		setAngle(angle);

		// moving the hero
		if (moveForward) {
			moveForward();
			// System.out.println("HI"); // check this?
		} else if (moveBackward) {
			moveBackword();
		}
	}

	// fire function called when a bullet is shot and this music functio is run
	public static void play(String filename) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(filename)));
			clip.start();
		} catch (Exception exc) {
			exc.printStackTrace(System.out);
		}
	}

	public void keyPressed(KeyEvent f) {
		value = 1;
		// System.out.println("KEY PRESSED");
		if (f.getKeyCode() == KeyEvent.VK_SPACE) {
			fire();
			// logger.info("Shoot");
			keyType = 4;
		}
		if (f.getKeyCode() == KeyEvent.VK_UP) {
			moveForward = true;
			// logger.info("Moving up");
			keyType = 2;
		}
		if (f.getKeyCode() == KeyEvent.VK_DOWN) {
			moveBackward = true;
			// logger.info("Moving down");
			keyType = 3;
		}
		if (f.getKeyCode() == KeyEvent.VK_LEFT) {
			angle -= 22.5;
			// logger.info("Moving left");
			keyType = 0;
		}
		if (f.getKeyCode() == KeyEvent.VK_RIGHT) {
			angle += 22.5;
			// logger.info("Moving right");
			keyType = 1;
		}

		// try {
		// Socket soc = new Socket("localhost", 10049);
		// PrintStream dout = new PrintStream(soc.getOutputStream());
		// dout.println("KEY PRESSED " + value);
		// dout.flush();
		// soc.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

	private void fire() {
		long timeDiff = System.currentTimeMillis() - lastShotTime;
		if (missiles.size() < 6 && timeDiff > fireRate) {
			missiles.add(new Missile(x + (width / 2), y + (height / 2), angle, modeSel));
			missilesAll.add(new Missile(x + (width / 2), y + (height / 2), angle, modeSel));
			String missID = generateObjectID();
			missileObjectID.add(missID);
			missileAllObjectID.add(missID);
			
//			if (missileObjectID != null && !missileObjectID.isEmpty()) {
//				System.out.println(missileObjectID.get(missileObjectID.size()-1));
//				}
//			if (missileAllObjectID != null && !missileAllObjectID.isEmpty()) {
//				System.out.println(missileAllObjectID.get(missileAllObjectID.size()-1));
//				}
			
			
			play("fire.wav");
			lastShotTime = System.currentTimeMillis();
		}
	}

	public void keyReleased(KeyEvent e) {
		value = 0;
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			moveForward = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			moveBackward = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			angle -= 0;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			angle += 0;
		}
	}

	@Override
	public void run() {
		// this run method keeps on running in the background, and removes any
		// bullets which are more than a certain time old. Hence shold dissapper
		while (true) {
			for (int i = 0; i < missiles.size(); i++) {
				if (!(missiles.get(i)).isAlive()) {
					missiles.remove(i);
					missileObjectID.remove(i);
				}
			}

			// this does a similiar thing but remoes the powerups as they are
			// more than 10 sec old
			long timeDiff = System.currentTimeMillis() - lastPowerupTime;
			if (timeDiff > 10000) {
				currentPowerup = 0;
				powerup = false;
				index = 0;

			}

			// over here poeper up 0 is normal, 1 is extra speed, 2 is less
			// speed, 3 is
			// high fire rate, 4 is lower fire rate, 5 is sheild
			switch (currentPowerup) {
			case 0:
				speed = 3;
				fireRate = 500;
				powerup = false;
				// life = 0;
				break;
			case 2:
				speed = 4.5;
				powerup = true;
				break;
			case 3:
				speed = 1.5;
				powerup = true;
				break;
			case 4:
				fireRate = 333;
				powerup = true;
				break;
			case 5:
				fireRate = 1000;
				powerup = true;
				break;
			case 1:
				// life++;
				powerup = true;
				break;
			}

			// this just changes the image of the tank as it gets a shield
			if (index == 0) {
				image = imageSet.getTank1Image();
			} else if (index == 1) {
				image = imageSet.getTank1ShieldImage();
			}

			try {
				Socket soc = new Socket("localhost", 10049);
				// System.out.println("keyType " + keyType + " value " + value);
				PrintStream dout = new PrintStream(soc.getOutputStream());
				dout.println("keyType " + keyType + " value " + value);
				dout.flush();
				soc.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException ignore) {
			}
		}

	}

}