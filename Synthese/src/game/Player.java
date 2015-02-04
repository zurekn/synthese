package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import data.Data;
import data.Stats;
import exception.IllegalMovementException;

public class Player {

	private int x = -1;
	private int y = -1;
	private Stats stats;

	public Player() {

	}

	public Player(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Player(int x, int y, Stats stats) {
		super();
		this.x = x;
		this.y = y;
		this.stats = stats;
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

	public void moveTo(String position) throws IllegalMovementException {
		String tokens[] = position.split(":");
		if (tokens.length != 2) {
			throw new IllegalMovementException("Invalid movement syntax");
		} else {

			int x = Integer.parseInt(tokens[0]);
			int y = Integer.parseInt(tokens[1]);

			if (x < 0 || x > Data.MAP_SIZE || y < 0 || y > Data.MAP_SIZE) {
				throw new IllegalMovementException("Movement is out of the map");
			} else {

				int xTmp = this.x, yTmp = this.y;
				int movePoints = this.stats.getMovementPoints();
				if (Math.sqrt(Math.pow(x - xTmp, 2) + Math.pow(y - yTmp, 2)) > movePoints) {
					throw new IllegalMovementException("Not enough movements points");
				} else {
					this.x = x;
					this.y = y;
				}
			}
		}
	}

	public void render(GameContainer container, Graphics g) {
		g.setColor(Color.blue);
		g.fillRect(getX() * Data.BLOCK_SIZE_X, getY() * Data.BLOCK_SIZE_Y,
				Data.BLOCK_SIZE_X, Data.BLOCK_SIZE_Y);
	}

	@Override
	public String toString() {
		return "Player [x=" + x + ", y=" + y + "]";
	}

}
