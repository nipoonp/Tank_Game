package com.combat;

import javax.swing.ImageIcon;

public class Wall extends General{

	//the wall class is used in every game mode
	public Wall(int x, int y, int modeSel) {

		this.modeSel = modeSel;
		imageSet = new srcImage(modeSel);
		
		this.x = x;
		this.y = y;
		
		//we get the image from the secImage class
		image = imageSet.getWallImage();
		width = image.getWidth(null);
		height = image.getHeight(null);
	}

	public void respawn() {
		//we do not really use this for walls
		x = Math.random() * 924;
		y = Math.random() * 668;
	}
}
