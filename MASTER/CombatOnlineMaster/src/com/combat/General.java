package com.combat;

//This is a class which is inherited by other classes such as hero, and the AI class

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class General {

	//These are variables common to all the classes that inherit it
	
	protected Image image;
	public int width;
	public int height;
	public double x = 0;
	public double y = 0;
	public double angle = 0;
	protected srcImage imageSet;
	protected int modeSel;

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, width, height);
	}

	public Image getImage() {
		return image;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void setAngle(double angle){
//		System.out.println(angle/22.5);
		this.angle = angle;
	}
	
	public int getImageWidth() {
		return width;
	}

	public int getImageHeight() {
		return height;
	}

	public void spawnGenerator() {
		//This spawnGenerator is called to respawn the tank, everytime it is shot
		x = Math.random() * 924;
		y = Math.random() * 668;
		//angle = Math.random() * 360;
	}

}
