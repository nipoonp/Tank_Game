package com.combat;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.UUID;

public class Board_Online extends JPanel implements ActionListener {
	private Timer timer;
	private Timer timer2;
	public Hero hero;
	public Hero_2 hero_2;
	Image image;
	private int countdown = 3;
	private int p1score = 0;
	private String Player1 = "Nipoon";
	private String Player2 = "Suhan";
	private int p2score = 0;
	private int counttime = 120;
	private boolean pause = false;
	private boolean gameOver = false;
	private boolean start = false;
	private ArrayList<Powerup> powerupArray = new ArrayList<Powerup>();
	private ArrayList<Powerup> powerupArrayAll = new ArrayList<Powerup>();
	private ArrayList<String> powerupObjectIDArray = new ArrayList<String>();
	private ArrayList<String> powerupAllObjectIDArray = new ArrayList<String>();

	// private ArrayList<String> missileObjectIDArray = new ArrayList<String>();
	// private ArrayList<String> missileAllObjectIDArray = new
	// ArrayList<String>();
	// private ArrayList<Missile> missileArrayAll = new ArrayList<Missile>();
	//

	private ArrayList<Wall> wallArray = new ArrayList<Wall>();
	private boolean names_set = true;
	private boolean orange;
	private boolean red;
	public static int mapChoice = 0;
	public static int modeSelect = 0;
	private boolean dispTime = true;
	protected int explosiontime = 1;
	private srcImage imageSet;
	private String objectID;

	private String hero_objectID;
	private String hero2_objectID;
	private String powerupObjectID;

	public Board_Online() {
		init();
		setBackground(Color.darkGray);
		setDoubleBuffered(true);
		setFocusable(true);
	}

	private int changeAngleToSend(int angle) {
		int returnAngle = 0;
		switch (angle) {

		case 0:
			returnAngle = 4;
			break;
		case 22:
			returnAngle = 5;
			break;
		case 45:
			returnAngle = 6;
			break;
		case 67:
			returnAngle = 7;
			break;
		case 90:
			returnAngle = 8;
			break;
		case 112:
			returnAngle = 9;
			break;
		case 135:
			returnAngle = 10;
			break;
		case 157:
			returnAngle = 11;
			break;
		case 180:
			returnAngle = 12;
			break;
		case 202:
			returnAngle = 13;
			break;
		case 225:
			returnAngle = 14;
			break;
		case 247:
			returnAngle = 15;
			break;
		case 270:
			returnAngle = 0;
			break;
		case 292:
			returnAngle = 1;
			break;
		case 315:
			returnAngle = 2;
			break;
		case 337:
			returnAngle = 3;
			break;
		}
		
		return returnAngle;
	}

	private void init() {

		getWalls();

		imageSet = new srcImage(modeSelect);

		hero = new Hero(900, 350, 0, modeSelect);
		hero_objectID = generateObjectID();
		int returnedAngle = changeAngleToSend((int) hero.getAngle());
		sendInitObjectsToPython(hero_objectID, "0", Integer.toString((int) (hero.getX() + 24)),
				Integer.toString((int) (hero.getY() + 24)), Integer.toString(returnedAngle),
				Integer.toString((int) hero.getCurrentPowerup()), "1");

		hero_2 = new Hero_2(50, 350, 0, modeSelect);
		hero2_objectID = generateObjectID();
		returnedAngle = changeAngleToSend((int) hero_2.getAngle());
		sendInitObjectsToPython(hero2_objectID, "0", Integer.toString((int) (hero_2.getX() + 24)),
				Integer.toString((int) (hero_2.getY() + 24)), Integer.toString(returnedAngle),
				Integer.toString((int) hero_2.getCurrentPowerup()), "1");

		sendWallsToPython();

		gameStartThread();
		timer = new Timer(1000, this);
		timer.start();

	}

	public String generateObjectID() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.substring(0, 8);
		return uuid;

	}

	public void sendWallsToPython() {

		for (int i = 0; i < wallArray.size(); i++) {
			Wall xxx = wallArray.get(i);
			objectID = generateObjectID();
			sendInitObjectsToPython(objectID, "1", Integer.toString((int) (xxx.getX()+6)),
					Integer.toString((int) (xxx.getY())+6), "0", "0", "1");
		}
	}

	public void sendInitObjectsToPython(String objectID, String objType, String x, String y, String angle, String state,
			String alive) {

		String outputString = objectID + " " + objType + " " + x + " " + y + " " + angle + " " + state + " " + alive;

		try {
			Socket soc = new Socket("localhost", 10047);
			PrintStream dout = new PrintStream(soc.getOutputStream());
			dout.println(outputString);
			dout.flush();
			soc.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendObjectsToPython(String objectID, String objType, String x, String y, String angle, String state,
			String alive) {

		String outputString = objectID + " " + objType + " " + x + " " + y + " " + angle + " " + state + " " + alive;

		try {
			Socket soc = new Socket("localhost", 10046);
			PrintStream dout = new PrintStream(soc.getOutputStream());
			dout.println(outputString);
			dout.flush();
			soc.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isStart() {
		return start;
	}

	public void ResetTime() {
		counttime = 120;
	}

	public void setGameOver() {
		gameOver = true;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public void updateTimer() {
		if (pause) {
			this.timer.stop();
		} else {
			this.timer.start();
		}
	}

	public void StopTimer() {
		this.timer.stop();
	}

	public void StartTimer() {
		this.timer.start();
	}

	public void getWalls() {
		Scanner scanner = null;
		if (mapChoice == 0) {
			InputStream is = getClass().getResourceAsStream("Map1.txt");
			scanner = new Scanner(is);
		}
		while (scanner.hasNextInt()) {
			wallArray.add(new Wall(scanner.nextInt(), scanner.nextInt(), modeSelect));
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		checkCollision(g);
		if (gameOver) {
		} else {
			drawObjects(g);
			drawScores(g);
			if (dispTime) {
				drawCountdown(g);
			}

		}
		if (names_set) {
			drawStartCountdown(g);
			if (start) {
				names_set = false;
			}
		}
	}

	private void checkBulletWallCollision() {
		ArrayList<Missile> heroMissiles = hero.getMissiles();
		ArrayList<Missile> hero2Missile = hero_2.getMissiles();

		double x1Wall, y1Wall, x2Wall, y2Wall, x1Bullet, y1Bullet, x2Bullet, y2Bullet;
		boolean left, right, top, bottom;

		// tank 1 bullets and wall collision
		for (int i = 0; i < heroMissiles.size(); i++) {
			Missile m1 = heroMissiles.get(i);
			if (m1 == null) {
				break;
			}
			Rectangle r2b = m1.getBounds();
			for (int j = 0; j < wallArray.size(); j++) {
				Wall w1 = wallArray.get(j);
				Rectangle r2w = w1.getBounds();

				x1Wall = r2w.getMinX();
				y1Wall = r2w.getMinY();
				x2Wall = r2w.getMaxX();
				y2Wall = r2w.getMaxY();

				x1Bullet = r2b.getMinX();
				y1Bullet = r2b.getMinY();
				x2Bullet = r2b.getMaxX();
				y2Bullet = r2b.getMaxY();
				left = (x2Bullet < x1Wall);
				right = (x2Wall < x1Bullet);
				top = (y2Bullet < y1Wall);
				bottom = (y2Wall < y1Bullet);
				if ((left && !right && top && !bottom) || (!left && right && top && !bottom)
						|| (left && !right && !top && bottom) || (!left && right && !top && bottom)) {
				} else if (left && !right && !top && !bottom) {
					double leftDist = x1Wall - x2Bullet;
					if (leftDist < 2) {
						m1.setAngle(180 - (m1.getAngle()));
						m1.setReflected(true);
						break;
					}
				} else if (!left && right && !top && !bottom) {
					double rightDist = x1Bullet - x2Wall;
					if (rightDist < 2) {
						m1.setAngle(180 - (m1.getAngle()));
						m1.setReflected(true);
						break;
					}
				} else if (!left && !right && top && !bottom) {
					double topDist = y1Wall - y2Bullet;
					if (topDist < 2) {
						m1.setAngle(-(m1.getAngle()));
						m1.setReflected(true);
						break;
					}
				} else if (!left && !right && !top && bottom) {
					double bottomDist = y1Bullet - y2Wall;
					if (bottomDist < 2) {
						m1.setAngle(-(m1.getAngle()));
						m1.setReflected(true);
						break;
					}
				}
			}
		}
		// tank 2 bullets and wall collision
		for (int i = 0; i < hero2Missile.size(); i++) {
			Missile m1 = hero2Missile.get(i);
			if (m1 == null) {
				break;
			}
			Rectangle r2b = m1.getBounds();
			for (int j = 0; j < wallArray.size(); j++) {
				Wall w1 = wallArray.get(j);
				Rectangle r2w = w1.getBounds();

				x1Wall = r2w.getMinX();
				y1Wall = r2w.getMinY();
				x2Wall = r2w.getMaxX();
				y2Wall = r2w.getMaxY();
				x1Bullet = r2b.getMinX();
				y1Bullet = r2b.getMinY();
				x2Bullet = r2b.getMaxX();
				y2Bullet = r2b.getMaxY();
				left = (x2Bullet < x1Wall);
				right = (x2Wall < x1Bullet);
				top = (y2Bullet < y1Wall);
				bottom = (y2Wall < y1Bullet);
				if ((left && !right && top && !bottom) || (!left && right && top && !bottom)
						|| (left && !right && !top && bottom) || (!left && right && !top && bottom)) {
				} else if (left && !right && !top && !bottom) {
					double leftDist = x1Wall - x2Bullet;
					if (leftDist < 2) {
						m1.setAngle(180 - (m1.getAngle()));
						m1.setReflected(true);
						break;
					}
				} else if (!left && right && !top && !bottom) {
					double rightDist = x1Bullet - x2Wall;
					if (rightDist < 2) {
						m1.setAngle(180 - (m1.getAngle()));
						m1.setReflected(true);
						break;
					}
				} else if (!left && !right && top && !bottom) {
					double topDist = y1Wall - y2Bullet;
					if (topDist < 2) {
						m1.setAngle(-(m1.getAngle()));
						m1.setReflected(true);
						break;
					}
				} else if (!left && !right && !top && bottom) {
					double bottomDist = y1Bullet - y2Wall;
					if (bottomDist < 2) {
						m1.setAngle(-(m1.getAngle()));
						m1.setReflected(true);
						break;
					}
				}
			}
		}
	}

	private boolean getValidRespawnPosition(int x, int y, int imageW, int imageH) {
		Rectangle iRect = new Rectangle(x, y, imageW, imageH);
		for (Wall w : wallArray) {
			Rectangle wRect = w.getBounds();
			if (iRect.intersects(wRect)) {
				return true;
			}
		}
		for (Powerup p : powerupArray) {
			Rectangle pRect = p.getBounds();
			if (iRect.intersects(pRect)) {
				return true;
			}
		}
		return false;
	}

	private void checkCollision(Graphics g) {

		// collision detection for the tanks
		Graphics2D g2d = (Graphics2D) g.create();
		Rectangle heroRectangle = hero.getBounds();
		Rectangle hero2Rectangle = hero_2.getBounds();
		ArrayList<Missile> heroMissiles = hero.getMissiles();
		ArrayList<Missile> hero2Missile = hero_2.getMissiles();

		// hero1 --------------------
		AffineTransform at = g2d.getTransform();
		g2d.setTransform(at);
		at.rotate(Math.toRadians(hero.getAngle()), hero.getX() + hero.getImageWidth() / 2,
				hero.getY() + hero.getImageHeight() / 2);
		GeneralPath heroPath = new GeneralPath();
		heroPath.append(heroRectangle.getPathIterator(at), true);
		Area heroArea = new Area(heroPath);

		// hero2 ------------------------------
		at = new AffineTransform();
		g2d.setTransform(at);
		at.rotate(Math.toRadians(hero_2.getAngle()), hero_2.getX() + hero_2.getImageWidth() / 2,
				hero_2.getY() + hero_2.getImageHeight() / 2);
		GeneralPath hero2Path = new GeneralPath();
		hero2Path.append(hero2Rectangle.getPathIterator(at), true);
		Area hero2Area = new Area(hero2Path);

		// tank1 -- tank2 collision
		hero2Area.intersect(heroArea);
		if (!hero2Area.isEmpty()) {
			g2d.setColor(Color.RED);
			g2d.fill(heroArea);
			hero_2.spawnGenerator();
			while (getValidRespawnPosition((int) hero_2.getX(), (int) hero_2.getY(), hero_2.getImageWidth(),
					hero_2.getImageHeight())) {
				hero_2.spawnGenerator();
			}
			hero.spawnGenerator();
			while (getValidRespawnPosition((int) hero.getX(), (int) hero.getY(), hero.getImageWidth(),
					hero.getImageHeight())) {
				hero.spawnGenerator();
			}
		}
		// tank2 -- tank1 bullet collision
		at = new AffineTransform();
		g2d.setTransform(at);
		for (int i = 0; i < heroMissiles.size(); i++) {
			Missile m = heroMissiles.get(i);
			Rectangle tank1BulletRect = m.getBounds();
			GeneralPath tank1BulletPath = new GeneralPath();
			tank1BulletPath.append(tank1BulletRect.getPathIterator(at), true);
			heroArea = new Area(heroPath);
			hero2Area = new Area(hero2Path);
			Area tank1BulletArea = new Area(tank1BulletPath);
			hero2Area.intersect(tank1BulletArea);
			if (!hero2Area.isEmpty()) {
				g2d.setColor(Color.RED);
				g2d.fill(heroArea);
				if (hero_2.getCurrentPowerup() == 5) {
				} else {
					p1score++;
					hero_2.spawnGenerator();
					while (getValidRespawnPosition((int) hero_2.getX(), (int) hero_2.getY(), hero_2.getImageWidth(),
							hero_2.getImageHeight())) {
						hero_2.spawnGenerator();
					}
				}
				heroMissiles.remove(i);
				// missileObjectIDArray.remove(i);
				hero_2.setCurrentPowerup(0);
			}
		}
		// tank1 -- tank2 bullet collision
		at = new AffineTransform();
		g2d.setTransform(at);
		for (int i = 0; i < hero2Missile.size(); i++) {
			Missile m1 = hero2Missile.get(i);
			Rectangle tank2BulletRect = m1.getBounds();
			GeneralPath tank2BulletPath = new GeneralPath();
			tank2BulletPath.append(tank2BulletRect.getPathIterator(at), true);
			heroArea = new Area(heroPath);
			hero2Area = new Area(hero2Path);
			Area tank2BulletArea = new Area(tank2BulletPath);
			heroArea.intersect(tank2BulletArea);
			if (!heroArea.isEmpty()) {
				g2d.setColor(Color.RED);
				g2d.fill(heroArea);
				if (hero.getCurrentPowerup() == 5) {
				} else {
					p2score++;
					hero.spawnGenerator();
					while (getValidRespawnPosition((int) hero.getX(), (int) hero.getY(), hero.getImageWidth(),
							hero.getImageHeight())) {
						hero.spawnGenerator();
					}
				}
				hero2Missile.remove(i);
				hero.setCurrentPowerup(0);
			}
		}

		// tank1 bullet -- tank2 bullet collision
		at = new AffineTransform();
		g2d.setTransform(at);
		for (int i = 0; i < hero2Missile.size(); i++) {
			Missile m1 = hero2Missile.get(i);
			Rectangle tank2BulletRect = m1.getBounds();
			GeneralPath tank2BulletPath = new GeneralPath();
			tank2BulletPath.append(tank2BulletRect.getPathIterator(at), true);
			Area tank2BulletArea = new Area(tank2BulletPath);

			for (int j = 0; j < heroMissiles.size(); j++) {
				Missile m2 = heroMissiles.get(j);
				Rectangle tankBulletRect = m2.getBounds();
				GeneralPath tankBulletPath = new GeneralPath();
				tankBulletPath.append(tankBulletRect.getPathIterator(at), true);
				Area tankBulletArea = new Area(tankBulletPath);
				tank2BulletArea = new Area(tank2BulletPath);

				tankBulletArea.intersect(tank2BulletArea);
				if (!tankBulletArea.isEmpty()) {
					hero2Missile.remove(i);
					heroMissiles.remove(j);
				}
			}
		}

		// tank1 -- tank1 bullet collision
		at = new AffineTransform();
		g2d.setTransform(at);
		for (int i = 0; i < heroMissiles.size(); i++) {
			Missile m1 = heroMissiles.get(i);
			Rectangle tankBulletRect = m1.getBounds();
			GeneralPath tankBulletPath = new GeneralPath();
			tankBulletPath.append(tankBulletRect.getPathIterator(at), true);
			heroArea = new Area(heroPath);
			hero2Area = new Area(hero2Path);
			Area tankBulletArea = new Area(tankBulletPath);
			heroArea.intersect(tankBulletArea);

			if (!heroArea.isEmpty() && m1.isReflected()) {
				if (hero.getCurrentPowerup() == 5) {
				} else {
					p2score++;
					hero.spawnGenerator();
					while (getValidRespawnPosition((int) hero.getX(), (int) hero.getY(), hero.getImageWidth(),
							hero.getImageHeight())) {
						hero.spawnGenerator();
					}
				}
				heroMissiles.remove(i);
				hero.setCurrentPowerup(0);
			}
		}

		// tank2 -- tank2 bullet collision
		at = new AffineTransform();
		g2d.setTransform(at);
		for (int i = 0; i < hero2Missile.size(); i++) {
			Missile m1 = hero2Missile.get(i);
			Rectangle tank2BulletRect = m1.getBounds();
			GeneralPath tank2BulletPath = new GeneralPath();
			tank2BulletPath.append(tank2BulletRect.getPathIterator(at), true);
			heroArea = new Area(heroPath);
			hero2Area = new Area(hero2Path);
			Area tank2BulletArea = new Area(tank2BulletPath);
			hero2Area.intersect(tank2BulletArea);

			if (!hero2Area.isEmpty() && m1.isReflected()) {
				if (hero_2.getCurrentPowerup() == 5) {
				} else {
					p1score++;
					hero_2.spawnGenerator();
					while (getValidRespawnPosition((int) hero_2.getX(), (int) hero_2.getY(), hero_2.getImageWidth(),
							hero_2.getImageHeight())) {
						hero_2.spawnGenerator();
					}
				}
				hero2Missile.remove(i);
				hero_2.setCurrentPowerup(0);
			}
		}

		// tank1 -- powerup collision
		at = new AffineTransform();
		g2d.setTransform(at);
		for (int i = 0; i < powerupArray.size(); i++) {
			Rectangle powerupRectangle = (powerupArray.get(i)).getBounds();
			GeneralPath powerupPath = new GeneralPath();
			powerupPath.append(powerupRectangle.getPathIterator(at), true);
			heroArea = new Area(heroPath);
			hero2Area = new Area(hero2Path);
			Area powerupArea = new Area(powerupPath);
			heroArea.intersect(powerupArea);
			if (!heroArea.isEmpty()) {
				if ((powerupArray.get(i)).getPowerupType() == 5) {
					hero.setIndex(1);
				}
				g2d.setColor(Color.RED);
				g2d.fill(heroArea);
				int powerupType = (powerupArray.get(i)).getPowerupType();
				hero.setCurrentPowerup(powerupType);
				powerupArray.remove(i);
				powerupObjectIDArray.remove(i);
			}
		}
		// tank2 -- powerup collision
		at = new AffineTransform();
		g2d.setTransform(at);
		for (int i = 0; i < powerupArray.size(); i++) {
			Rectangle powerupRectangle = (powerupArray.get(i)).getBounds();
			GeneralPath powerupPath = new GeneralPath();
			powerupPath.append(powerupRectangle.getPathIterator(at), true);
			heroArea = new Area(heroPath);
			hero2Area = new Area(hero2Path);
			Area powerupArea = new Area(powerupPath);
			hero2Area.intersect(powerupArea);
			if (!hero2Area.isEmpty()) {
				if ((powerupArray.get(i)).getPowerupType() == 5) {
					hero_2.setIndex(1);
				}
				g2d.setColor(Color.RED);
				g2d.fill(heroArea);
				int powerupType = (powerupArray.get(i)).getPowerupType();
				hero_2.setCurrentPowerup(powerupType);
				powerupArray.remove(i);
				powerupObjectIDArray.remove(i);
			}
		}
		// tank 1 and wall collision
		at = new AffineTransform();
		g2d.setTransform(at);
		for (Wall w : wallArray) {
			Rectangle wallRect = w.getBounds();
			GeneralPath wallPath = new GeneralPath();
			wallPath.append(wallRect.getPathIterator(at), true);
			heroArea = new Area(heroPath);
			hero2Area = new Area(hero2Path);
			Area wallArea = new Area(wallPath);
			heroArea.intersect(wallArea);
			if (!heroArea.isEmpty()) {
				g2d.setColor(Color.RED);
				g2d.fill(heroArea);
				hero.setX(hero.getPrevX());
				hero.setY(hero.getPrevY());
			}
		}
		// tank 2 and wall collision
		at = new AffineTransform();
		g2d.setTransform(at);
		for (Wall w : wallArray) {
			Rectangle wallRect = w.getBounds();
			GeneralPath wallPath = new GeneralPath();
			wallPath.append(wallRect.getPathIterator(at), true);
			heroArea = new Area(heroPath);
			hero2Area = new Area(hero2Path);
			Area wallArea = new Area(wallPath);
			hero2Area.intersect(wallArea);
			if (!hero2Area.isEmpty()) {
				g2d.setColor(Color.RED);
				g2d.fill(hero2Area);
				hero_2.setX(hero_2.getPrevX());
				hero_2.setY(hero_2.getPrevY());
			}
		}
	}

	private void drawStartCountdown(Graphics g) {
		String count_down = "" + countdown;
		Font small = new Font("Helvetica", Font.BOLD, 600);
		FontMetrics fm = getFontMetrics(small);
		g.setColor(Color.yellow);
		g.setFont(small);
		g.drawString(count_down, 1024 / 2 - 200, 768 / 2 + 200);
	}

	private void drawCountdown(Graphics g) {
		String count_down = "Time Left: " + counttime + " seconds";
		Font small = new Font("Helvetica", Font.BOLD, 25);
		FontMetrics fm = getFontMetrics(small);
		g.setColor(Color.green);
		if (orange) {
			g.setColor(Color.orange);
		}
		if (red) {
			g.setColor(Color.red);
		}
		g.setFont(small);
		g.drawString(count_down, (1024 - fm.stringWidth(count_down)) / 2, 25);

	}

	private void drawScores(Graphics g) {
		g.setColor(new Color(0, 0, 0, 150));
		g.drawRect(0, 0, 1024, 38);
		g.fillRect(0, 0, 1024, 38);

		String count_down1 = Player1 + " : " + p1score;
		String count_down2 = Player2 + " : " + p2score;
		Font small = new Font("Helvetica", Font.BOLD, 30);
		FontMetrics fm1 = getFontMetrics(small);
		FontMetrics fm2 = getFontMetrics(small);
		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(count_down1, 75, 30);
		g.drawString(count_down2, 1024 - fm2.stringWidth(count_down2) - 75, 30);
	}

	private void drawObjects(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g.drawImage(imageSet.getBackgroundImage(), 0, 0, 1024, 768, this);

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		AffineTransform old = g2d.getTransform();
		// walls
		for (Wall wall : wallArray) {
			g2d.drawImage(wall.getImage(), (int) wall.getX(), (int) wall.getY(), this);
		}
		if (!pause) {
			// hero1
			g2d.rotate(Math.toRadians(hero.getAngle()), hero.getX() + hero.getImageWidth() / 2,
					hero.getY() + hero.getImageHeight() / 2);
			g2d.drawImage(hero.getImage(), (int) hero.getX(), (int) hero.getY(), hero.getImageWidth(),
					hero.getImageHeight(), this);
			g2d.setTransform(old);
			// hero2
			g2d.rotate(Math.toRadians(hero_2.getAngle()), hero_2.getX() + hero_2.getImageWidth() / 2,
					hero_2.getY() + hero_2.getImageHeight() / 2);
			g2d.drawImage(hero_2.getImage(), (int) hero_2.getX(), (int) hero_2.getY(), hero_2.getImageWidth(),
					hero_2.getImageHeight(), this);
			g2d.setTransform(old);
			// hero1 bullets
			ArrayList<Missile> ms = hero.getMissiles();
			for (Missile m : ms) {
				g2d.drawImage(m.getImage(), (int) m.getX(), (int) m.getY(), this);
			}
			// hero2 bullets
			ArrayList<Missile> ms2 = hero_2.getMissiles();
			for (Missile m2 : ms2) {
				g2d.drawImage(m2.getImage(), (int) m2.getX(), (int) m2.getY(), this);
			}
			// powerups
			for (Powerup m : powerupArray) {
				g2d.drawImage(m.getImage(), (int) m.getX(), (int) m.getY(), this);
			}
		}
	}

	private void updateMissiles() {
		ArrayList<Missile> ms = hero.getMissiles();
		for (int i = 0; i < ms.size(); i++) {
			Missile m = (Missile) ms.get(i);
			if (m == null) {
				break;
			}
			m.move();
		}
		ArrayList<Missile> ms2 = hero_2.getMissiles();
		for (int i = 0; i < ms2.size(); i++) {
			Missile m1 = (Missile) ms2.get(i);
			if (m1 == null) {
				break;
			}
			m1.move();
		}
	}

	public void update() {

		if (names_set) {
			timer2.start();
		}
		if (start) {
			timer2.stop();
			repaint();
			hero.play();
			hero_2.play();
			for (int i = 0; i < 9; i++) {
				updateMissiles();
				checkBulletWallCollision();
			}
		}
	}

	private void gameStartThread() {
		timer2 = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (names_set) {
					countdown--;
					if (countdown == 0) {
						start = true;
						initThread();
					}
				}
			}
		});

	}

	private void initThread() {
		Thread powerupRemoveThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					for (int i = 0; i < powerupArray.size(); i++) {
						if (!(powerupArray.get(i)).isAlive()) {
							powerupArray.remove(i);
							powerupObjectIDArray.remove(i);
						}
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException ignore) {
					}
				}
			}
		});
		Thread powerupAddThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					Random rand = new Random();
					int powerupTime1 = rand.nextInt((10000 - 0) + 1) + 0;
					Powerup newPowerup = new Powerup(modeSelect);
					newPowerup.spawnGenerator();
					while (getValidRespawnPosition((int) newPowerup.getX(), (int) newPowerup.getY(),
							newPowerup.getImageWidth(), newPowerup.getImageHeight())) {
						newPowerup.spawnGenerator();
					}
					powerupArray.add(newPowerup);
					powerupArrayAll.add(newPowerup);
					powerupObjectID = generateObjectID();
					powerupObjectIDArray.add(powerupObjectID);
					powerupAllObjectIDArray.add(powerupObjectID);
					try {
						Thread.sleep(powerupTime1);
					} catch (InterruptedException ignore) {
					}
				}
			}
		});

		Thread sendObjectsToPythonThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					int returnedAngle = 0;
					ArrayList<Missile> heroMissiles = hero.getMissiles();
					ArrayList<Missile> hero2Missile = hero_2.getMissiles();

					for (int ii = 0; ii < heroMissiles.size(); ii++) {
						System.out.println(Integer.toString((int) (heroMissiles.get(ii).getX())+4) + " " + Integer.toString((int) (heroMissiles.get(ii).getY())+4));
						returnedAngle = changeAngleToSend((int) heroMissiles.get(ii).getAngle());
						sendObjectsToPython((heroMissiles.get(ii)).getUUID(), "2",
								Integer.toString((int) (heroMissiles.get(ii).getX())+4),
								Integer.toString((int) (heroMissiles.get(ii).getY())+4),
								Integer.toString((int) returnedAngle), "0", "1");
					}
					
					for (int ii = 0; ii < hero2Missile.size(); ii++) {
						System.out.println(Integer.toString((int) (hero2Missile.get(ii).getX())+4) + " " + Integer.toString((int) (hero2Missile.get(ii).getY())+4));
						returnedAngle = changeAngleToSend((int) hero2Missile.get(ii).getAngle());
						sendObjectsToPython((hero2Missile.get(ii)).getUUID(), "2",
								Integer.toString((int) (hero2Missile.get(ii).getX())+4),
								Integer.toString((int) (hero2Missile.get(ii).getY())+4),
								Integer.toString((int) returnedAngle), "0", "1");
					}

					returnedAngle = changeAngleToSend((int) hero.getAngle());
					sendObjectsToPython(hero_objectID, "0", Integer.toString((int) (hero.getX() + 24)),
							Integer.toString((int) (hero.getY() + 24)), Integer.toString(returnedAngle),
							Integer.toString((int) hero.getCurrentPowerup()), "1");

					returnedAngle = changeAngleToSend((int) hero_2.getAngle());
					sendObjectsToPython(hero2_objectID, "0", Integer.toString((int) (hero_2.getX() + 24)),
							Integer.toString((int) (hero_2.getY() + 24)), Integer.toString(returnedAngle),
							Integer.toString((int) hero_2.getCurrentPowerup()), "1");

					for (int i = 0; i < powerupAllObjectIDArray.size(); i++) {
						String bigArray = powerupAllObjectIDArray.get(i);
						Powerup powerupxxx = powerupArrayAll.get(i);
						String same = "0";

						for (int j = 0; j < powerupObjectIDArray.size(); j++) {
							String smallArray = powerupObjectIDArray.get(j);

							if (bigArray == smallArray) {
								same = "1";
								break;
							} else {
								same = "0";
							}

						}
						sendObjectsToPython(powerupAllObjectIDArray.get(i), "3",
								Integer.toString((int) powerupxxx.getX()), Integer.toString((int) powerupxxx.getY()),
								"0",
								Integer.toString(powerupxxx.getPowerupType()), same);
					}

					try {
						Thread.sleep(33);
					} catch (InterruptedException ignore) {
					}
				}
			}
		});

		sendObjectsToPythonThread.start();
		powerupRemoveThread.start();
		powerupAddThread.start();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (start) {
			counttime--;
			if (counttime == 60) {
				orange = true;
			}
			if (counttime == 20) {
				red = true;
				orange = false;
			}
			if (counttime == 0) {
				gameOver = true;
				start = false;
			}
			if (red) {
				dispTime = !dispTime;
			}
		}
	}
}
