package com.combat;

import java.util.UUID;

import javax.swing.ImageIcon;

public class Missile extends General{

	public int speed = 1;
	long startTime = System.currentTimeMillis();
	private long estimatedTime = System.currentTimeMillis() - startTime;
	private boolean reflected = false; //notes if the bullet has hit a wall yet or not
	public String uuid;
	//this is the missile class used by all the tanks
	public Missile(double d, double e, double angle, int modeSel) {

		uuid = UUID.randomUUID().toString();
		uuid = uuid.substring(0, 8);
		
		
		this.modeSel = modeSel;
		imageSet = new srcImage(modeSel);
		
		image = imageSet.getBulletImage();
		width = image.getWidth(null);
		height = image.getHeight(null);
		this.angle = angle;
		x =  d;
		y =  e;
	}

	public void move() {
		// Here we use the sin and cos fucntions to get the distance moved when
		// the bullet is shot at an angle
		x += speed*Math.cos(Math.toRadians(angle));
		y += speed*Math.sin(Math.toRadians(angle));
	}
	
	public long getTimeAlive(){
		//the time since the object has been created
		return estimatedTime = System.currentTimeMillis() - startTime;
	}
	
	public boolean isAlive(){
		//if the time is less than 4500 the bullet is still alive
		estimatedTime = System.currentTimeMillis() - startTime;
		if (estimatedTime < 4500) {
			return true;
		}
		return false;
	}
	
	public String getUUID(){
		//the time since the object has been created
		return uuid;
	}

	public boolean isReflected() {
		return reflected;
	}

	public void setReflected(boolean reflected) {
		this.reflected = reflected;
	}
}
