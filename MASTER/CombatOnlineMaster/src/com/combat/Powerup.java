package com.combat;

import java.util.Random;
import javax.swing.ImageIcon;

public class Powerup extends General {
	public int powerupType;
	long startTime = System.currentTimeMillis();

	public Powerup(int modeSel) {

		//here we are getting the image of the bullet from the srcImage class
		this.modeSel = modeSel;
		imageSet = new srcImage(modeSel);
		
		image = imageSet.getPowerupImage();
		width = image.getWidth(null);
		height = image.getHeight(null);
		powerupGenerator();
	}

	public int getPowerupType() {
		return powerupType;
	}

	public void powerupGenerator() {
		//random generator for the type of powerup being spawned.
		Random rand = new Random();
		powerupType = rand.nextInt((5 - 1) + 1) + 1;
	}

	public boolean isAlive() {
		//it is alive if the object was created less than 10 seconds ago
		long estimatedTime = System.currentTimeMillis() - startTime;
		if (estimatedTime < 10000) {
			return true;
		}
		return false;
	}
}
