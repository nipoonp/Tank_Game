package com.combat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.UUID;

public class Slave extends JPanel implements KeyListener {
	private Timer timer;
	private Timer timer2;
	// public Hero hero;
	// public Hero_2 hero_2;
	Image image;
	private int countdown = 3;
	private int p1score = 0;
	private String Player1 = "Nipoon";
	private String Player2 = "Suhan";
	private int p2score = 0;
	private int counttime = 1200;
	private boolean gameOver = false;
	private boolean start = false;
	// private ArrayList<Powerup> powerupArray = new ArrayList<Powerup>();
	// private ArrayList<Powerup> powerupArrayAll = new ArrayList<Powerup>();
	// private ArrayList<String> powerupObjectIDArray = new ArrayList<String>();
	// private ArrayList<String> powerupAllObjectIDArray = new
	// ArrayList<String>();

	// private ArrayList<Wall> wallArray = new ArrayList<Wall>();
	private boolean names_set = true;
	private boolean orange;
	private boolean red;
	public static int mapChoice = 0;
	public static int modeSelect = 0;
	private boolean dispTime = true;
	protected int explosiontime = 1;
	private srcImage imageSet;
	private int i = 0;

	private String uniqueCode;
	private String objectID;
	private String x_t1 = "650";
	private String y_t1 = "300";
	private String x_t2 = "500";
	private String y_t2 = "600";
	private String x_bullets = "700";
	private String y_bullets = "359";
	private String x_powerups = "400";
	private String y_powerups = "200";
	private String x_wall = "300";
	private String y_wall = "250";
	private String angle_t1 = "275";
	private String angle_t2 = "135";
	private String[] IncomingData = new String[7];
	private String state;
	private String alive;
	private String tempUID = "";
	private String tempUID2 = "";
	private boolean gone_here = false;
	private int keyType = 0;
	private int value = 0;
	private boolean started = false;
	private int masterScore = 0;
	private int slaveScore = 0;

	ArrayList<String[]> wallsArrayList = new ArrayList<String[]>();
	ArrayList<String[]> tanksArrayList = new ArrayList<String[]>();
	ArrayList<String[]> bulletsArrayList = new ArrayList<String[]>();
	ArrayList<String[]> powerupsArrayList = new ArrayList<String[]>();

	String fromExistingClient;
	Socket existingClient;
	ServerSocket existingServer;
	BufferedReader existingIn;

	String fromInitClient;
	Socket initClient;
	ServerSocket initServer;
	BufferedReader initIn;

	public Slave() {
		init();
		setBackground(Color.darkGray);
		setDoubleBuffered(true);
		setFocusable(true);
		addKeyListener(this);
	}

	private void init() {

		imageSet = new srcImage(modeSelect);

		try {
			existingServer = new ServerSocket(10070);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			initServer = new ServerSocket(10071);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gameStartThread();
		getExistingObjectsFromPython();
		getInitObjectsFromPython();
		this.setPreferredSize(new Dimension(1024, 768));
		// timer = new Timer(1000, this);
		// timer.start();

	}

	public void sendInitObjectsToPython(String objectID, String objType, String x, String y, String angle, String state,
			String alive) {

		String outputString = objectID + " " + objType + " " + x + " " + y + " " + angle + " " + state + " " + alive;

		// try {
		// Socket soc = new Socket("localhost", 10047);
		// PrintStream dout = new PrintStream(soc.getOutputStream());
		// dout.println(outputString);
		// dout.flush();
		// soc.close();
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	public void sendObjectsToPython(String objectID, String objType, String x, String y, String angle, String state,
			String alive) {

		String outputString = objectID + " " + objType + " " + x + " " + y + " " + angle + " " + state + " " + alive;

		// try {
		// Socket soc = new Socket("localhost", 10046);
		// PrintStream dout = new PrintStream(soc.getOutputStream());
		// dout.println(outputString);
		// dout.flush();
		// soc.close();
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	// public void getWalls() {
	// Scanner scanner = null;
	// if (mapChoice == 0) {
	// InputStream is = getClass().getResourceAsStream("Map1.txt");
	// scanner = new Scanner(is);
	// }
	// while (scanner.hasNextInt()) {
	// wallArray.add(new Wall(scanner.nextInt(), scanner.nextInt(),
	// modeSelect));
	// }
	// }

	public void paint(Graphics g) {
		super.paint(g);
		// checkCollision(g);
		// if (gameOver) {
		// } else {
		drawObjects(g);
		// drawScores(g);
		// if (dispTime) {
		// //drawCountdown(g);
		// }
		//
		// }
		// if (names_set) {
		// //drawStartCountdown(g);
		// if (start) {
		// names_set = false;
		// }
		// }
	}

	// private void drawStartCountdown(Graphics g) {
	// String count_down = "" + countdown;
	// Font small = new Font("Helvetica", Font.BOLD, 600);
	// FontMetrics fm = getFontMetrics(small);
	// g.setColor(Color.yellow);
	// g.setFont(small);
	// g.drawString(count_down, 1024 / 2 - 200, 768 / 2 + 200);
	// }

	// private void drawCountdown(Graphics g) {
	// String count_down = "Time Left: " + counttime + " seconds";
	// Font small = new Font("Helvetica", Font.BOLD, 25);
	// FontMetrics fm = getFontMetrics(small);
	// g.setColor(Color.green);
	// if (orange) {
	// g.setColor(Color.orange);
	// }
	// if (red) {
	// g.setColor(Color.red);
	// }
	// g.setFont(small);
	// g.drawString(count_down, (1024 - fm.stringWidth(count_down)) / 2, 25);
	//
	// }

	// private void drawScores(Graphics g) {
	// g.setColor(new Color(0, 0, 0, 150));
	// g.drawRect(0, 0, 1024, 38);
	// g.fillRect(0, 0, 1024, 38);
	//
	// String count_down1 = Player1 + " : " + p1score;
	// String count_down2 = Player2 + " : " + p2score;
	// Font small = new Font("Helvetica", Font.BOLD, 30);
	// FontMetrics fm1 = getFontMetrics(small);
	// FontMetrics fm2 = getFontMetrics(small);
	// g.setColor(Color.white);
	// g.setFont(small);
	// g.drawString(count_down1, 75, 30);
	// g.drawString(count_down2, 1024 - fm2.stringWidth(count_down2) - 75, 30);
	// }

	private void drawObjects(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g.drawImage(imageSet.getBackgroundImage(), 0, 0, 1024, 768, this);

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		AffineTransform old = g2d.getTransform();
		// walls
		// for (Wall wall : wallArray) {
		// g2d.drawImage(wall.getImage(), (int) wall.getX(), (int) wall.getY(),
		// this);
		// }

		for (int i = 0; i < wallsArrayList.size(); i++) {

			String[] myString = new String[7];
			myString = wallsArrayList.get(i);
			for (int j = 0; j < myString.length; j++) {
				if (Integer.parseInt(myString[1]) == 1) {
					g2d.drawImage(imageSet.getWallImage(), Integer.parseInt(myString[2]), Integer.parseInt(myString[3]),
							this);

				}

			}
		}

		// System.out.println(bulletsArrayList.size());

		for (int i = 0; i < bulletsArrayList.size(); i++) {
			String[] myString = new String[7];
			myString = bulletsArrayList.get(i);
			for (int j = 0; j < myString.length; j++) {
				g2d.drawImage(imageSet.getBulletImage(), Integer.parseInt(myString[2]), Integer.parseInt(myString[3]),
						this);

			}
		}

		for (int i = 0; i < powerupsArrayList.size(); i++) {
			String[] myString = new String[7];
			myString = powerupsArrayList.get(i);
			for (int j = 0; j < myString.length; j++) {
				g2d.drawImage(imageSet.getPowerupImage(), Integer.parseInt(myString[2]), Integer.parseInt(myString[3]),
						this);

			}
		}

		for (i = 0; i < tanksArrayList.size(); i++) {
			String[] myString = new String[7];
			myString = tanksArrayList.get(i);

			for (int j = 0; j < myString.length; j++) {
				if (myString[0].equals(tempUID)) {
					g2d.rotate(Math.toRadians((22.5 * Double.parseDouble(myString[4])) - 90),
							Double.parseDouble(myString[2]) + 24, Double.parseDouble(myString[3]) + 24);
					g2d.drawImage(imageSet.getTank1Image(), Integer.parseInt(myString[2]),
							Integer.parseInt(myString[3]), 48, 48, this);
					g2d.setTransform(old);
				}
				if (myString[0].equals(tempUID2)) {
					g2d.rotate(Math.toRadians((22.5 * Double.parseDouble(myString[4])) - 90),
							Double.parseDouble(myString[2]) + 24, Double.parseDouble(myString[3]) + 24);
					g2d.drawImage(imageSet.getTank2Image(), Integer.parseInt(myString[2]),
							Integer.parseInt(myString[3]), 48, 48, this);
					g2d.setTransform(old);
				}
			}

		}

		// hero1
		// g2d.rotate(Math.toRadians(Double.parseDouble(angle_t1)),
		// Integer.parseInt(x_t1) + 48 / 2,
		// Integer.parseInt(y_t1) + 48 / 2);
		//// g2d.drawImage(imageSet.getTank1Image(), Integer.parseInt(x_t1),
		// Integer.parseInt(y_t1), 48,
		// 48, this);
		// g2d.setTransform(old);
		// // hero2
		// g2d.rotate(Math.toRadians(Double.parseDouble(angle_t2)),
		// Integer.parseInt(x_t2) + 48 / 2,
		// Integer.parseInt(y_t2) + 48 / 2);
		// g2d.drawImage(imageSet.getTank2Image(), Integer.parseInt(x_t2),
		// Integer.parseInt(y_t2), 48,
		// 48, this);
		// g2d.setTransform(old);
		// // hero1 bullets
		//// ArrayList<Missile> ms = hero.getMissiles();
		//// for (Missile m : ms) {
		// g2d.drawImage(imageSet.getBulletImage(), Integer.parseInt(x_bullets),
		// Integer.parseInt(y_bullets), this);
		// }
		// hero2 bullets
		// ArrayList<Missile> ms2 = hero_2.getMissiles();
		// for (Missile m2 : ms2) {
		// g2d.drawImage(imageSet.getBulletImage(), Integer.parseInt(x),
		// Integer.parseInt(y), this);
		// }
		// powerups
		// for (Powerup m : powerupArray) {
		// g2d.drawImage(imageSet.getPowerupImage(),
		// Integer.parseInt(x_powerups), Integer.parseInt(y_powerups), this);
		// }

	}

	// private void updateMissiles() {
	// ArrayList<Missile> ms = hero.getMissiles();
	// for (int i = 0; i < ms.size(); i++) {
	// Missile m = (Missile) ms.get(i);
	// if (m == null) {
	// break;
	// }
	// m.move();
	// }
	// ArrayList<Missile> ms2 = hero_2.getMissiles();
	// for (int i = 0; i < ms2.size(); i++) {
	// Missile m1 = (Missile) ms2.get(i);
	// if (m1 == null) {
	// break;
	// }
	// m1.move();
	// }
	// }

	public void update() {

		// if (names_set) {
		// timer2.start();
		// }
		// if (start) {
		// timer2.stop();
		repaint();
		// hero.play();
		// hero_2.play();
		// for (int i = 0; i < 9; i++) {
		// //updateMissiles();
		// //checkBulletWallCollision();
		// }
		// }
	}

	private void getExistingObjectsFromPython() {
		Thread getExistingObjectsFromPython = new Thread(new Runnable() {
			public void run() {
				while (true) {

					try {
						existingClient = existingServer.accept();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// System.out.println("got connection on port 10048");
					try {
						existingIn = new BufferedReader(new InputStreamReader(existingClient.getInputStream()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						fromExistingClient = existingIn.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(fromExistingClient);

					String[] splitStr = fromExistingClient.split("\\s+");
					if (splitStr.length == 2) {
							masterScore = Integer.parseInt(splitStr[0]);
							slaveScore = Integer.parseInt(splitStr[1]);
							System.out.println(masterScore);
							System.out.println(slaveScore);
							
					} else {
						if (Integer.parseInt(splitStr[1]) == 0) {

							if (splitStr[0].equals(tempUID)) {
								tanksArrayList.set(0, splitStr);

							} else if (splitStr[0].equals(tempUID2)) {
								tanksArrayList.set(1, splitStr);
							}

						} else if (Integer.parseInt(splitStr[1]) == 2) {

							if (bulletsArrayList.size() == 0) {
								bulletsArrayList.add(splitStr);
							}

							for (int ii = 0; ii < bulletsArrayList.size(); ii++) {
								if (((bulletsArrayList.get(ii))[0]).equals(splitStr[0])) {
									bulletsArrayList.set(ii, splitStr);
									break;
								} else if (ii == (bulletsArrayList.size() - 1)) {
									bulletsArrayList.add(splitStr);
								}
							}

							// boolean same = false;
							// int index = 0;
							// if(bulletsArrayList.size() == 0){
							// bulletsArrayList.add(splitStr);
							// }
							//
							// for(int ii = 0; ii < bulletsArrayList.size();
							// ii++){
							// if
							// (((bulletsArrayList.get(ii))[0]).equals(splitStr[0])){
							// index = ii;
							// same = true;
							// break;
							// }
							// }
							// if (!same){
							// bulletsArrayList.add(splitStr);
							// } else if (same){
							// bulletsArrayList.set(index, splitStr);
							// }
							// System.out.println("SAME IS : " + same);
							//

						} else if (Integer.parseInt(splitStr[1]) == 3) {
							powerupsArrayList.add(splitStr);
						}
					}

				}
			}
		});

		getExistingObjectsFromPython.start();
	}

	private void getInitObjectsFromPython() {
		Thread getInitObjectsFromPython = new Thread(new Runnable() {
			public void run() {
				while (true) {

					try {
						initClient = initServer.accept();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// System.out.println("got connection on port 10048");
					try {
						initIn = new BufferedReader(new InputStreamReader(initClient.getInputStream()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						fromInitClient = initIn.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String[] splitStr = fromInitClient.split("\\s+");
					wallsArrayList.add(splitStr);

					if (Integer.parseInt(splitStr[1]) == 0 && !gone_here) {
						tempUID = splitStr[0];
						gone_here = true;
						tanksArrayList.add(splitStr);
					} else if (Integer.parseInt(splitStr[1]) == 0 && gone_here) {
						tempUID2 = splitStr[0];
						tanksArrayList.add(splitStr);
					}
				}
			}
		});

		getInitObjectsFromPython.start();
	}

	private void gameStartThread() {
		// timer2 = new Timer(1000, new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// if (names_set) {
		// countdown--;
		// if (countdown == 0) {
		// start = true;
		// //initThread();
		// }
		// }
		// }
		// });

		Thread updateGame = new Thread(new Runnable() {
			public void run() {
				while (true) {
					update();
					try {
						Thread.sleep(33);
					} catch (InterruptedException ignore) {
					}
				}
			}
		});

		updateGame.start();

	}

	private void startKeys() {
		System.out.println("Thread ON");
		Thread sendKeys = new Thread(new Runnable() {
			public void run() {
				while (true) {

					try {
						Socket soc = new Socket("localhost", 10049);
						System.out.println("keyType " + keyType + " value " + value);
						PrintStream dout = new PrintStream(soc.getOutputStream());
						dout.println("keyType " + keyType + " value " + value);
						dout.flush();
						soc.close();

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		});

		sendKeys.start();

	}

	@Override
	public void keyPressed(KeyEvent e) {
		value = 1;
		if (started == false) {
			startKeys();
			started = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			keyType = 4;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			keyType = 2;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			keyType = 3;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			keyType = 0;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			keyType = 1;

		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		value = 0;

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			keyType = 4;

		}

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			keyType = 2;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			keyType = 3;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			keyType = 0;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			keyType = 1;
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	// private void initThread() {
	//
	// Thread sendObjectsToPythonThread = new Thread(new Runnable() {
	// public void run() {
	// while (true) {
	//
	// for (int i = 0; i < getImageWidthpowerupAllObjectIDArray.size(); i++) {
	// String bigArray = powerupAllObjectIDArray.get(i);
	// Powerup powerupxxx = powerupArrayAll.get(i);
	// String same = "0";
	//
	// for (int j = 0; j < powerupObjectIDArray.size(); j++) {
	// String smallArray = powerupObjectIDArray.get(j);
	//
	// if (bigArray == smallArray) {
	// same = "1";
	// } else {
	// same = "0";
	// }
	//
	// }
	//
	// sendObjectsToPython(powerupAllObjectIDArray.get(i), "3",
	// Integer.toString((int) powerupxxx.getX()), Integer.toString((int)
	// powerupxxx.getY()),
	// Integer.toString((int) powerupxxx.getAngle()),
	// Integer.toString(powerupxxx.getPowerupType()), same);
	// }
	//
	// try {
	// Thread.sleep(33);
	// } catch (InterruptedException ignore) {
	// }
	// }
	// }
	// });
	//
	// sendObjectsToPythonThread.start();
	//
	// }

	// @Override
	// public void actionPerformed(ActionEvent arg0) {
	// //if (start) {
	// counttime--;
	// if (counttime == 60) {
	// orange = true;
	// }
	// if (counttime == 20) {
	// red = true;
	// orange = false;
	// }
	// if (counttime == 0) {
	// gameOver = true;
	// start = false;
	// }
	// if (red) {
	// dispTime = !dispTime;
	// }
	// }
	// //}

}
