package com.adamsotheran.rain.graphics;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.*;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.adamsotheran.rain.invaderMissiles.InvaderMissiles;
import com.adamsotheran.rain.sprites.Sprites;

public class Screen {
	
	
	
	public int[] cboard  =  { 0,1,0,1,0,1,0,1,
							  1,0,1,0,1,0,1,0, 
							  0,1,0,1,0,1,0,1,
							  1,0,1,0,1,0,1,0,
							  0,1,0,1,0,1,0,1,
							  1,0,1,0,1,0,1,0, 
							  0,1,0,1,0,1,0,1,
							  1,0,1,0,1,0,1,0};
	
	public int[] squareInvader  =  {1,1,1,1,1,1,1,1,
			  						1,0,0,0,0,0,0,1,
			  						1,0,0,0,0,0,0,1,
			  						1,0,0,0,0,0,0,1,
			  						1,0,0,0,0,0,0,1,
			  						1,0,0,0,0,0,0,1,
			  						1,0,0,0,0,0,0,1,
			  						1,1,1,1,1,1,1,1};
	
	public int[] rowsInvader = {1,1,1,1,1,1,1,1,
								0,1,1,1,1,1,1,0,
								0,0,1,1,1,1,0,0,
								0,0,0,1,1,0,0,0,
								0,0,0,1,1,0,0,0,
								0,0,1,1,1,1,0,0,
								0,1,1,1,1,1,1,0,
								1,1,1,1,1,1,1,1};
	
	public int[] hInvader = {1,1,0,0,0,0,1,1,
							 1,1,0,0,0,0,1,1,
							 1,1,0,0,0,0,1,1,
							 1,1,1,1,1,1,1,1,
							 1,1,1,1,1,1,1,1,
							 1,1,0,0,0,0,1,1,
							 1,1,0,0,0,0,1,1,
							 1,1,0,0,0,0,1,1};
	
	public int[] crossInvader = {1,1,1,1,1,1,1,1,
								 1,0,0,1,1,0,0,1,
								 1,0,0,1,1,0,0,1,
								 1,1,1,1,1,1,1,1,
								 1,1,1,1,1,1,1,1,
								 1,1,0,0,0,0,1,1,
								 1,0,1,1,1,1,0,1,
								 1,1,1,1,1,1,1,1};
	
	public int[] fighterMissile = {0,1,1,0,
								   0,1,1,0,
								   0,1,1,0,
								   0,1,1,0};
	
	
							 
	
	
	
	public int[] fighter     = {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,
								0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,
								0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,
								0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,	0,	0,	0,	0,	0,	0,	0,  0,	0,
								0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,
								0,	0,	1,	0,	0,	0,	0,	0,	0,	1,	1,	0,	0,	0,	0,	0,	0,	1,	0,	0,
								0,	0,	1,	0,	0,	0,	0,	0,	1,	1,	1,	1,	0,	0,	0,	0,	0,	1,	0,	0,
								0,	0,	1,	1,	0,	0,	0,	0,	1,	0,	0,	1,	0,	0,	0,	0,	1,	1,	0,	0,
								0,	0,	1,	1,	1,	0,	0,	1,	1,	0,	0,	1,	1,	0,	0,	1,	1,	1,	0,	0,
								0,	0,	1,	1,	1,	0,	1,	1,	1,	0,	0,	1,	1,	1,	0,	1,	1,	1,	0,	0,
								0,	0,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	0,	0,
								0,	0,	1,	1,	1,	1,	1,	1,	1,	0,	0,	1,	1,	1,	1,	1,	1,	1,	0,	0,
								0,	0,	1,	1,	1,	1,	1,	1,	0,	0,	0,	0,	1,	1,	1,	1,	1,	1,	0,	0,
								0,	0,	1,	1,	1,	1,	1,	0,	1,	1,	1,	1,	0,	1,	1,	1,	1,	1,	0,	0,
								0,	0,	1,	1,	0,	0,	1,	1,	1,	1,	1,	1,	1,	1,	0,	0,	1,	1,	0,	0,
								0,	0,	1,	1,	0,	0,	1,	1,	1,	1,	1,	1,	1,	1,	0,	0,	1,	1,	0,	0,
								0,	0,	1,	0,	0,	0,	0,	1,	0,	0,	0,	0,	1,	0,	0,	0,	0,	1,	0,	0,
								0,	0,	1,	0,	0,	0,	0,	1,	1,	0,	0,	1,	1,	0,	0,	0,	0,	1,	0,	0,
								0,	0,	1,	0,	0,	0,	0,	1,	1,	0,	0,	1,	1,	0,	0,	0,	0,	1,	0,	0,
								0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0};
	
	int[][] invaderDefinitions = new int[][] {cboard, squareInvader, rowsInvader, hInvader, crossInvader};
	
	
	
	private int width;
	private int height;
	public int[] pixels;
	public int[] whiteStars;
	public int[] yellowStars;
	
	int time=0;
	int counter = 0;
	int scroll = 0;

	public Screen(int width, int height) {
		this.width=width;
		this.height=height;
		pixels = new int[width * height];
		whiteStars = new int[75];
		yellowStars = new int[75];
		Random random = new Random();
		for (int i=0; i<whiteStars.length; i++) {
			int index = random.nextInt(pixels.length-0)+0;
			whiteStars[i] = index;
		}
		for (int i=0; i<whiteStars.length; i++) {
			int index = random.nextInt(pixels.length-0)+0;
			yellowStars[i] = index;
		}
		
	}

	public void clearScreen() {
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				pixels[x + (y*width)] = 0x000000;
			}
		}
	}

	
	private void drawFighter(int StartX, int StartY, int length) {
		for (int r=0; r<length; r++) {
			for (int c=0; c<length; c++) {
				int offset = (r*length)+ c;
				if (fighter[offset] == 1) {
					int t=((offset)/length);
					pixels[(width * (StartY + t)) + StartX + offset - (length*r)] = 0xFFFFFF;
				}
			}
		}
	}
	
	
			
	
	
	private void drawObject (int invaders, int XPos, int YPos, int length) {
		// get starting pixel index from XPos, YPos
		int pixelIndex = XPos + (width * YPos);
		// at some point, we need to find out if the sprite fits inside the screen.  We'll assume it does for now.
		// Draw the first line
		for (int r=0; r<length; r++) // this is working through each row of the graphic
		for (int c = 0; c< length; c++) { // this is working through the columns of each row
			if (invaderDefinitions[invaders][(r*length)+c] == 1) {
				int pxIndex = pixelIndex + (r*width) + c;
				pixels[pxIndex] = 0xFF0000;
			}
			if (invaderDefinitions[invaders][(r*length)+c] == 0) {
				int pxIndex = pixelIndex + (r*width) + c;
				pixels[pxIndex] = 0x000001;
			}
		}
	}
		
	
	
	private void DrawMissile(int x, int y, int length) {
		
	
		for (int r=0; r<length; r++) {
			for (int c=0; c<length; c++) {
				int offset = (r*length)+ c;
				if (fighterMissile[offset] == 1) {
					int t=((offset)/length);
					pixels[(width * (y + t)) + x + offset - (length*r)] = 0x55FF55;
				}
			}
		}
	}
	
	private void DrawStarfield(int[] positions, int colorIndex ) {
		for (int i=0; i<positions.length; i++) {
			if (colorIndex == 0) pixels[positions[i]] = 0xFFFFFE;
			if (colorIndex == 1) pixels[positions[i]] = 0xFFFF00;
		}
	}
	
	private void DrawInvaderMissiles(InvaderMissiles[] invaderMissiles, int length) {
		for (int imIndex=0; imIndex<20; imIndex++) {
			for (int r=0; r<length; r++) {
				for (int c=0; c<length; c++) {
					int offset = (r*length)+ c;
					if (fighterMissile[offset] == 1) {
						int t=((offset)/length);
						pixels[(width * (invaderMissiles[imIndex].y + t)) + invaderMissiles[imIndex].x + offset - (length*r)] = 0x9999FF;
					}
				}
			}

		}
		
	}
	
	public void render(int xOffset, int yOffset, Sprites[] badguys,boolean fighterMissileFired, InvaderMissiles[] invaderMissiles, boolean invaderMissilesFired, int missileX, int missileY) {
			DrawStarfield(whiteStars,0);
			DrawStarfield(yellowStars,1);
			for (int i=0; i<badguys.length; i++) {
				if (badguys[i].isVisible) drawObject(badguys[i].graphicsIndex, badguys[i].x, badguys[i].y, 8);
			}
		drawFighter(xOffset,yOffset,20);
		if (fighterMissileFired) DrawMissile(missileX, missileY, 4);
		if (invaderMissilesFired) DrawInvaderMissiles(invaderMissiles, 4);
	}

	public class SoundPlayer {
	    public static void PlaySound(String fileName) {
	        try {
	            File soundFile = new File(fileName);
	            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
	            Clip clip = AudioSystem.getClip();
	            clip.open(audioStream);
	            clip.start();
	        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
	            e.printStackTrace();
	        }
	    }

	    public static void main(String[] args) {
	        PlaySound("FighterPew1.wav");
	    }

	}

}
