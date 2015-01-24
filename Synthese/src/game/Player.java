package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import data.Data;

public class Player {

	private int x = -1;
	private int y = -1;
	
	
	public Player() {
		
	}

	public Player(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}



	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void render(GameContainer container, Graphics g) {
		System.out.println(this.toString());
		g.setColor(Color.blue);
		g.drawRect(getX() * Data.BLOCK_SIZE_X, getY() * Data.BLOCK_SIZE_Y, Data.BLOCK_SIZE_X, Data.BLOCK_SIZE_Y);
	}

	@Override
	public String toString() {
		return "Player [x=" + x + ", y=" + y + "]";
	}
	
	

}
