package com.adamsotheran.rain;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.adamsotheran.rain.graphics.Screen;
import com.adamsotheran.rain.graphics.Screen.SoundPlayer;
import com.adamsotheran.rain.input.Keyboard;
import com.adamsotheran.rain.invaderMissiles.InvaderMissiles;
import com.adamsotheran.rain.sprites.Sprites;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	public static int height = 300;
	public static int width = height * 16/9;
	public static int scale = 3;
	public static String title = "Rain";
	
	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	private Keyboard key;
	private Sprites[] invaders;
	private InvaderMissiles[] invaderMissiles;
	int maximumMissileCount = 20;
	public List<Integer> lowestInvaderInColumn = new ArrayList<>();
	
	
	
	int score = 0;
	int xFighterOffset = 200;
	int yFighterOffset = 280;
	int invadersPerRow = 10;
	int invaderRows = 5;
	int invaderCount = invadersPerRow * invaderRows; 
	int invaderXChange = -1;
	int invaderTick = 0;
	int invaderSpeed = 5;
	int fighterMissileStartY = yFighterOffset;
	int missileX;
	int missileY;
	boolean fighterMissileFired = false;
	boolean invaderMissilesFired = false;
	int invaderShootingTick = 0;
	
	private Screen screen;
	
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	private void SetupInvaders() {
		
		for (int r=0; r<invaderRows; r++) {
			for (int c=0; c<invadersPerRow; c++) {
				invaders[(r*invadersPerRow)+c] = new Sprites((r*invadersPerRow)+c, 50+(c*40), (35+(r*25)), r, true, invaderRows-r);
			}
		}
	}
	
	private void SetupInvaderMissiles() {
		for (int i=0; i<maximumMissileCount; i++) {
			invaderMissiles[i] = new InvaderMissiles(0,0,2,0,false,1);
		}
	}
	
	public Game() {
		Dimension size = new Dimension(width*scale, height*scale);
		setPreferredSize(size);
		screen = new Screen(width, height);
		frame = new JFrame();
		key = new Keyboard();
		invaders = new Sprites[invadersPerRow*invaderRows];
		SetupInvaders();
		invaderMissiles = new InvaderMissiles[20];  // Maybe replace this with a variable that increases the #missiles/level
		SetupInvaderMissiles();
		addKeyListener(key);
	}
	
	
	
	public synchronized void start() {
		running = true;
		thread = new Thread();
		thread = new Thread(this, "Display");
		thread.start();
	}
	
	public synchronized void stop()	{
		running = false;
		try {
			thread.join();
		}
		catch (InterruptedException e)		{
			e.printStackTrace();
		}
	}
	
	
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) /ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				updates++;
				delta --;
			}
			screen.clearScreen();
			render(xFighterOffset, yFighterOffset, invaders);
			
			frames ++;
			
			if (System.currentTimeMillis() - timer > 1000){
				timer+= 1000;
//				System.out.println(updates + " ups " + frames + " fps");
				frame.setTitle(title + " " + updates + " ups " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	
	private int moveFighterLeft(int x,int y) {
		if (x-5>10) {
			x-=2;
		}
		return x;
	}
	
	private int moveFighterRight(int x,int y) {
		if (x+5<500) {
			x+=2;
		}
		return x;
	}
	
	private void UpdateInvaderPositions(Sprites[] invaders) {
		for (int i=0; i<invaders.length; i++) {
			invaders[i].x += invaderXChange;
		}
	}
		
	private void CheckInvadersAtEdge(Sprites[] invaders) {
		boolean movedDown = false;
		for (int i=0; i<invaders.length; i++) {
			if (invaders[i].x-5 <10 && invaders[i].isVisible) {
				for (int c=0; c<invaders.length; c++) {
					movedDown = true;
					invaders[c].y+=3;
				}
				invaderXChange = 1;
			}
			if (invaders[i].x+5 >500 && invaders[i].isVisible) {
				for (int c=0; c<invaders.length; c++) {
					movedDown = true;
					invaders[c].y+=3;
				}
				invaderXChange = -1;
			}
		}
	}
	
	private void FireMissile() {
		fighterMissileFired = true;
		SoundPlayer.PlaySound("Y:\\Personal\\JavaCode\\rain\\bin\\sounds\\FighterPew1.wav");
		missileX = xFighterOffset+10;
		missileY = yFighterOffset;
	}
	
	private void CheckMissileBounds() {
		if (missileY-5<10) {
			fighterMissileFired = false;
			missileX = xFighterOffset + 10;
		}
	}
	
	private void UpdateMissilePosition() {
		missileY -=4;	
		CheckMissileBounds();
	}
	
	private int IdentifyHitInvader(int hitPoint) {
		int hitInvader = 999;
		fighterMissileFired = false;
		// Iterate through the list of invaders to find the one that was hit
		wholeLoop:
		for (int i=0; i<invaderCount; i++) {
			// Now iterate through rows/columns to identify if it has a pixel where the missile is
			for (int r=0; r<8; r++) {
				for (int c=0; c<8; c++) {
					int pixelIndex = ((invaders[i].y+r+1)*width)+invaders[i].x+c;
					//if (r == 7 || i==49) System.out.println("Index = " + pixelIndex + ".  hitPoint = " + hitPoint + " Invader y coordinate: " + invaders[i].y+8);
					if (pixelIndex - hitPoint >=-2 && pixelIndex - hitPoint <=2) {
						hitInvader = i;
						break wholeLoop;
					}
				}
			}
		}
		
		return hitInvader;
	}
	
	private void CheckFighterMissileCollision(int x, int y) {
		int hitPoint = (width*(y-1))+x+2;
		if (pixels[hitPoint] == 0xFF0000 || pixels[hitPoint] == 0x000001) {
			
			int hitInvader = IdentifyHitInvader(hitPoint);
			// System.out.println("Hit invader # " + hitInvader);
			if (hitInvader != 999) {
				SoundPlayer.PlaySound("Y:\\Personal\\JavaCode\\rain\\bin\\sounds\\Explosion.wav");
				score += 10;
				System.out.println("Score: " + score);
				invaders[hitInvader].isVisible = false;
				boolean remainingInvaders = false;
				for (int c = 0; c<invaderCount; c++) {
					if (invaders[c].isVisible) {
						remainingInvaders = true;
						//if (c<=40 && c>=30) invaderSpeed =2;
						//if (c<=30 && c>=20) invaderSpeed =3;
						//if (c<=20 && c>=10) invaderSpeed =4;
						
					
					}
				}
				if (!remainingInvaders){
					System.out.println("You win!");
					System.exit(1);
				}
					
			}
		}
	}
	
	private List<Integer> FindLowestInvaderInEachColumn(){
		List<Integer> tempList= new ArrayList<>();
		// iterate up from the bottom row of invaders to find the lowest in each column:
		// find which invaders in the bottom row are visible
		for (int i = invaderCount-1; i>invaderCount-invadersPerRow-1; i--) {
			for (int c=i; c>(i-((invaderRows-1)*invadersPerRow)-1); c-=invadersPerRow) {
				if (invaders[c].isVisible) {
					tempList.add(c);
					break;
				}
			}
			
		}
		//System.out.println(tempList);
		//System.exit(0);
		
		return tempList;
	}
	
	public void CheckForInvaderMissileCollision(int i) {
		
			int targetPx = ((invaderMissiles[i].y+4)*width)+invaderMissiles[i].x; 
			if (pixels[targetPx] == 0xFFFFFF) {
				System.out.println("You got hit");
				System.exit(1);
			}
	}
	
	public void CheckInvaderMissileEdgeOfScreen(int im) {
		if (invaderMissiles[im].y>285) {
				invaderMissiles[im].isVisible =false;
				invaderMissiles[im].x =0;
				invaderMissiles[im].y = 0;
			//	boolean someMissiles = false;
			//	for (int c=0; c<maximumMissileCount; c++) {
				//	if (invaderMissiles[i].isVisible) someMissiles = true;
			//	}
			//	if (!someMissiles) invaderMissilesFired = false;
		}
	} 
	
	
	public void UpdateInvaderMissilePositions() {
		for(int i=0; i<maximumMissileCount; i++) {
			if (invaderMissiles[i].isVisible){
				invaderMissiles[i].y += invaderMissiles[i].vel;
			}
		}
		
		for (int im=0; im<maximumMissileCount; im++) {
			if (invaderMissiles[im].isVisible) {
			CheckForInvaderMissileCollision(im);
			CheckInvaderMissileEdgeOfScreen(im);
			}
		}

	}
	
	public void InvaderMissileFiringProcess() {
		lowestInvaderInColumn.clear();
		lowestInvaderInColumn = FindLowestInvaderInEachColumn();
		Random random = new Random();
		int probabilityThreshold = random.nextInt(15-0)+0;
		for (int i =0; i<lowestInvaderInColumn.size()-1; i++) {
			int index = lowestInvaderInColumn.get(i);
			if (invaders[index].firingProbability>=probabilityThreshold) {
				// This means that this invader could fire...
				int secondProb = random.nextInt(4-0)+0;
				if (secondProb == 1) {
					System.out.println(index);
					// this means an invader has fired, it's time to see if there's capacity for a missile to fire, and if so, set up the missile
					for (int c=0; c<maximumMissileCount; c++) {
						if (invaderMissiles[c].isVisible == false) {
							// There is a space for this missile to fire, so let's set it up...
							invaderMissiles[c].id = index;
							invaderMissiles[c].x = invaders[index].x+4;		
							invaderMissiles[c].y = invaders[index].y+8; 	
							invaderMissiles[c].vel = random.nextInt(8-1)+1; // I like the idea of variable firing rate
							invaderMissiles[c].isVisible = true;
							invaderMissilesFired = true; // this means that when there's a check to see if missiles need to be drawn, they will be
							SoundPlayer.PlaySound("Y:\\Personal\\JavaCode\\rain\\bin\\sounds\\InvaderPew.wav");
							break;
						}
					}
					
				}
			}
		}
	}
	
	public void update() {
		requestFocus();
		key.update();
		if (key.left) xFighterOffset  = moveFighterLeft(xFighterOffset, yFighterOffset);
		if (key.right) xFighterOffset  = moveFighterRight(xFighterOffset, yFighterOffset);
		if ((key.fire) && !fighterMissileFired) FireMissile();
		if (fighterMissileFired) {
			UpdateMissilePosition();
			CheckFighterMissileCollision(missileX, missileY);		
		}
		// Next section is to figure out if any invaders are going to fire, and then fire missiles
		invaderShootingTick++;
		if (invaderShootingTick % 5 == 0) {
			InvaderMissileFiringProcess();
			invaderShootingTick =0;
		}
		
		
		// Next section is to move the invaders
		invaderTick+=1;
		if (invaderTick % 5 == 0) {
			UpdateInvaderPositions(invaders);
			CheckInvadersAtEdge(invaders);
			if (invaderMissilesFired) UpdateInvaderMissilePositions();
			invaderTick = 0;	
		}
		//System.out.println("Offset = " + xFighterOffset);		
	}
	
	public void render(int x, int y, Sprites[] invaders) {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.clearScreen();
		screen.render(xFighterOffset,yFighterOffset, invaders, fighterMissileFired, invaderMissiles, invaderMissilesFired, missileX, missileY);
		
		
		for (int i=0; i < pixels.length; i++ ) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(),  getHeight(), null);
		g.dispose();
		bs.show();
		
	}
	
	
	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle("Test Game - Rain");
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		game.requestFocus();
		game.start();
	}
	
}
