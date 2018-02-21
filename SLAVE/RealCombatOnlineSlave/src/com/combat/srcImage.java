package com.combat;

import java.awt.Image;

import javax.swing.ImageIcon;

public class srcImage {

	// these are all the different images
	Image speedUpImage;
	Image higherFireRateImage;
	Image shildImage;
	Image speedDownImage;
	Image lowerFireRateImage;
	Image backgroundImage;
	Image explosionAnimation;
	Image tank1Image;
	Image tank2Image;
	Image tank1ShieldImage;
	Image tank2ShieldImage;
	Image powerupImage;
	Image wallImage;
	Image bulletImage;

	// mode select defines which set of images we want to return, it is the
	// input to the class constuctor
	private int modeSelect = 0;

	public srcImage(int modeSelect) {
		this.modeSelect = modeSelect;
		initImages();

	}

	// here the images are loaded from the source folder
	public void initImages() {

		if (modeSelect == 0) {

			ImageIcon backgrnd = new ImageIcon(this.getClass().getResource("background1.png"));
			backgroundImage = backgrnd.getImage();

			ImageIcon t1 = new ImageIcon(this.getClass().getResource("tank11.png"));
			tank1Image = t1.getImage();

			ImageIcon t2 = new ImageIcon(this.getClass().getResource("tank12.png"));
			tank2Image = t2.getImage();

			ImageIcon t1p = new ImageIcon(this.getClass().getResource("tank11p.png"));
			tank1ShieldImage = t1p.getImage();

			ImageIcon t2p = new ImageIcon(this.getClass().getResource("tank12p.png"));
			tank2ShieldImage = t2p.getImage();

			ImageIcon powerup = new ImageIcon(this.getClass().getResource("powerup1.png"));
			powerupImage = powerup.getImage();

			ImageIcon wall = new ImageIcon(this.getClass().getResource("wall1.png"));
			wallImage = wall.getImage();

			ImageIcon bullet = new ImageIcon(this.getClass().getResource("bullet1.png"));
			bulletImage = bullet.getImage();

		} else if (modeSelect == 1) {

			ImageIcon backgrnd = new ImageIcon(this.getClass().getResource("background2.png"));
			backgroundImage = backgrnd.getImage();

			ImageIcon t1 = new ImageIcon(this.getClass().getResource("tank21.png"));
			tank1Image = t1.getImage();

			ImageIcon t2 = new ImageIcon(this.getClass().getResource("tank22.png"));
			tank2Image = t2.getImage();

			ImageIcon t1p = new ImageIcon(this.getClass().getResource("tank21p.png"));
			tank1ShieldImage = t1p.getImage();

			ImageIcon t2p = new ImageIcon(this.getClass().getResource("tank22p.png"));
			tank2ShieldImage = t2p.getImage();

			ImageIcon powerup = new ImageIcon(this.getClass().getResource("powerup2.png"));
			powerupImage = powerup.getImage();

			ImageIcon wall = new ImageIcon(this.getClass().getResource("wall2.png"));
			wallImage = wall.getImage();

			ImageIcon bullet = new ImageIcon(this.getClass().getResource("bullet2.png"));
			bulletImage = bullet.getImage();

		} else if (modeSelect == 2) {

			ImageIcon backgrnd = new ImageIcon(this.getClass().getResource("background3.png"));
			backgroundImage = backgrnd.getImage();

			ImageIcon t1 = new ImageIcon(this.getClass().getResource("tank31.png"));
			tank1Image = t1.getImage();

			ImageIcon t2 = new ImageIcon(this.getClass().getResource("tank32.png"));
			tank2Image = t2.getImage();

			ImageIcon t1p = new ImageIcon(this.getClass().getResource("tank31p.png"));
			tank1ShieldImage = t1p.getImage();

			ImageIcon t2p = new ImageIcon(this.getClass().getResource("tank13p.png"));
			tank2ShieldImage = t2p.getImage();

			ImageIcon powerup = new ImageIcon(this.getClass().getResource("powerup3.png"));
			powerupImage = powerup.getImage();

			ImageIcon wall = new ImageIcon(this.getClass().getResource("wall3.png"));
			wallImage = wall.getImage();

			ImageIcon bullet = new ImageIcon(this.getClass().getResource("bullet3.png"));
			bulletImage = bullet.getImage();

		} else if (modeSelect == 3) {

			ImageIcon backgrnd = new ImageIcon(this.getClass().getResource("background1.png"));
			backgroundImage = backgrnd.getImage();

			ImageIcon t1 = new ImageIcon(this.getClass().getResource("tank11.png"));
			tank1Image = t1.getImage();

			ImageIcon t2 = new ImageIcon(this.getClass().getResource("tank12.png"));
			tank2Image = t2.getImage();

			ImageIcon t1p = new ImageIcon(this.getClass().getResource("tank11p.png"));
			tank1ShieldImage = t1p.getImage();

			ImageIcon t2p = new ImageIcon(this.getClass().getResource("tank12p.png"));
			tank2ShieldImage = t2p.getImage();

			ImageIcon powerup = new ImageIcon(this.getClass().getResource("powerup1.png"));
			powerupImage = powerup.getImage();

			ImageIcon wall = new ImageIcon(this.getClass().getResource("wall1.png"));
			wallImage = wall.getImage();

			ImageIcon bullet = new ImageIcon(this.getClass().getResource("bullet1.png"));
			bulletImage = bullet.getImage();

		} else if (modeSelect == 4) {

			ImageIcon backgrnd = new ImageIcon(this.getClass().getResource("background4.png"));
			backgroundImage = backgrnd.getImage();

			ImageIcon t1 = new ImageIcon(this.getClass().getResource("tank11.png"));
			tank1Image = t1.getImage();

			ImageIcon t2 = new ImageIcon(this.getClass().getResource("tank12.png"));
			tank2Image = t2.getImage();

			ImageIcon t1p = new ImageIcon(this.getClass().getResource("tank11p.png"));
			tank1ShieldImage = t1p.getImage();

			ImageIcon t2p = new ImageIcon(this.getClass().getResource("tank12p.png"));
			tank2ShieldImage = t2p.getImage();

			ImageIcon powerup = new ImageIcon(this.getClass().getResource("powerup1.png"));
			powerupImage = powerup.getImage();

			ImageIcon wall = new ImageIcon(this.getClass().getResource("wall1.png"));
			wallImage = wall.getImage();

			ImageIcon bullet = new ImageIcon(this.getClass().getResource("bullet1.png"));
			bulletImage = bullet.getImage();

		}

		// these are the images which are alwyas loaded, as they are the
		// different powerups
		ImageIcon ii = new ImageIcon(this.getClass().getResource("speed.png"));
		speedUpImage = ii.getImage();
		ImageIcon v = new ImageIcon(this.getClass().getResource("1.png"));
		speedDownImage = v.getImage();
		ImageIcon iii = new ImageIcon(this.getClass().getResource("firerate.png"));
		higherFireRateImage = iii.getImage();
		ImageIcon vi = new ImageIcon(this.getClass().getResource("2.png"));
		lowerFireRateImage = vi.getImage();
		ImageIcon iv = new ImageIcon(this.getClass().getResource("shield.png"));
		shildImage = iv.getImage();
		ImageIcon vii = new ImageIcon(this.getClass().getResource("explosion.gif"));
		explosionAnimation = vii.getImage();

	}

	// the folloing are the get functinos for the individual images

	public Image getExplosionAnimation() {
		return explosionAnimation;
	}

	public Image getSpeedUpImage() {
		return speedUpImage;
	}

	public Image getHigherFireRateImage() {
		return higherFireRateImage;
	}

	public Image getShildImage() {
		return shildImage;
	}

	public Image getSpeedDownImage() {
		return speedDownImage;
	}

	public Image getLowerFireRateImage() {
		return lowerFireRateImage;
	}

	public Image getBackgroundImage() {
		return backgroundImage;
	}

	public Image getTank1Image() {
		return tank1Image;
	}

	public Image getTank2Image() {
		return tank2Image;
	}

	public Image getPowerupImage() {
		return powerupImage;
	}

	public Image getWallImage() {
		return wallImage;
	}

	public Image getBulletImage() {
		return bulletImage;
	}

	public Image getTank1ShieldImage() {
		return tank1ShieldImage;
	}

	public Image getTank2ShieldImage() {
		return tank2ShieldImage;
	}
}
