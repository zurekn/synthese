package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import data.Data;

public class Message {

	private int x;
	private int y;
	private String message;
	private int type;
	private Color color;
	private long startTime;

	public Message(int x, int y, String message) {
		super();
		this.x = x;
		this.y = y;
		this.message = message;
		type = 0;
		color = Data.getColorMessage(type);
		startTime = System.currentTimeMillis();
	}

	public Message(String string) {
		message = string;
		type = 0;
		color = Data.getColorMessage(type);
		startTime = System.currentTimeMillis();
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void render(GameContainer container, Graphics g, int x, int y) {
		g.setColor(color);
		g.drawString(message, x, y);
	}

	public boolean update() {
		return System.currentTimeMillis() - startTime > Data.MESSAGE_DURATION;
	}

}
