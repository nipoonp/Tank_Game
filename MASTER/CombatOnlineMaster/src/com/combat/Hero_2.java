package com.combat;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

public class Hero_2 extends General implements Runnable {

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
	private int mapWidth = 950, mapHeight = 670;

	private int index = 0;

	String fromClient;
	Socket client;
	ServerSocket server;
	BufferedReader in;

	private boolean powerup = false;

	public boolean isPowerup() {
		return powerup;
	}

	public Hero_2(double x, double y, double angle, int modeSel) {

		try {
			server = new ServerSocket(10048);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.modeSel = modeSel;
		imageSet = new srcImage(modeSel);

		this.x = x;
		this.y = y;
		this.angle = angle;

		image = imageSet.getTank2Image();
		width = image.getWidth(null);
		height = image.getHeight(null);

		moveForward = moveBackward = false;

		loop = new Thread(this);
		loop.start();

	}

	public void moveForward() {
		prevX = x;
		prevY = y;
		x += speed * Math.cos(Math.toRadians(angle));
		y += speed * Math.sin(Math.toRadians(angle));
		if (x < 0 || x > (1024 - 48) || y < 0 || y > (768 - 48)) {
			x = prevX;
			y = prevY;
		}
	}
	
	public String generateObjectID() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.substring(0, 8);
		return uuid;

	}

	// backward
	public void moveBackword() {
		prevX = x;
		prevY = y;
		x -= speed * Math.cos(Math.toRadians(angle));
		y -= speed * Math.sin(Math.toRadians(angle));
		if (x < 0 || x > (1024 - 48) || y < 0 || y > (768 - 48)) {
			x = prevX;
			y = prevY;
		}
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public double getPrevX() {
		return prevX;
	}

	public double getPrevY() {
		return prevY;
	}

	public double getPrevAngle() {
		return prevAngle;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public ArrayList<String> getMissilesObjectID() {
		return missileObjectID;
	}

	// these are arraylist of missiles which stores the bullets which are shot
	public ArrayList<Missile> getMissiles() {
		return missiles;
	}
	
	public ArrayList<String> getMissilesAllObjectID() {
	return missileAllObjectID;
}
	
	public ArrayList<Missile> getMissilesAll() {
	return missilesAll;
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

		prevAngle = angle;

		// setting the hero angle
		setAngle(angle);

		// moving the hero
		if (moveForward) {
			moveForward();
		} else if (moveBackward) {
			moveBackword();
		}
	}

	// this plays the music
	public static void play(String filename) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(filename)));
			clip.start();
		} catch (Exception exc) {
			exc.printStackTrace(System.out);
		}
	}

	public void keyRemotePressed(int press) {
		if (press == 4) {
			fire();
		}
		if (press == 2) {
			moveForward = true;
		}
		if (press == 3) {
			moveBackward = true;
		}
		if (press == 0) {
			angle -= 22.5;
		}
		if (press == 1) {
			angle += 22.5;
		}
	}

	public void keyRemoteReleased(int press) {
		if (press == 2) {
			moveForward = false;
		}
		if (press == 3) {
			moveBackward = false;
		}
		if (press == 0) {
			angle -= 0;
		}
		if (press == 1) {
			angle += 0;
		}
	}

	// fire function called when a bullet is shot
	private void fire() {
		long timeDiff = System.currentTimeMillis() - lastShotTime;
		if (missiles.size() < 6 && timeDiff > fireRate) {
			missiles.add(new Missile(x + (width / 2), y + (height / 2), angle, modeSel));
			missilesAll.add(new Missile(x + (width / 2), y + (height / 2), angle, modeSel));
			String missID = generateObjectID();
			missileObjectID.add(missID);
			missileAllObjectID.add(missID);
			play("fire.wav");
			lastShotTime = System.currentTimeMillis();
		}
	}

	@Override
	public void run() {
		while (true) {
			for (int i = 0; i < missiles.size(); i++) {
				if (!(missiles.get(i)).isAlive()) {
					missiles.remove(i);
					missileObjectID.remove(i);
				}
			}

			long timeDiff = System.currentTimeMillis() - lastPowerupTime;
			if (timeDiff > 10000) {
				currentPowerup = 0;
				powerup = false;
				index = 0;
			}

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
				image = imageSet.getTank2Image();
			} else if (index == 1) {
				image = imageSet.getTank2ShieldImage();
			}

			try {
				client = server.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println("got connection on port 10048");

			try {
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				fromClient = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			 System.out.println("received: " + fromClient);

			String[] splitStr = fromClient.split("\\s+");

			if (Integer.parseInt(splitStr[3]) == 1) {
				keyRemotePressed(Integer.parseInt(splitStr[1]));
			} else if (Integer.parseInt(splitStr[3]) == 0) {
				keyRemoteReleased(Integer.parseInt(splitStr[1]));
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException ignore) {
			}
		}

	}

}
