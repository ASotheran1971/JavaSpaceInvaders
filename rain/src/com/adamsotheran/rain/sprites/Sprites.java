package com.adamsotheran.rain.sprites;



public class Sprites {
	
	public int id;
	public int x;
	public int y;
	public int graphicsIndex;
	public boolean isVisible;
	public int firingProbability;
	
	public Sprites(int id, int x, int y, int graphicsIndex, boolean isVisible, int firingProbability) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.graphicsIndex = graphicsIndex;
		this.isVisible = true;
		this.firingProbability = firingProbability;
	}

}
