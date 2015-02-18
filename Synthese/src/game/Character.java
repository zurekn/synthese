package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import data.Data;
import data.Stats;
import exception.IllegalMovementException;

public abstract class Character {
	private int x;
	private int y;
	private String id;
	private Animation[] animation;
	private Stats stats;

	public abstract void render(GameContainer container, Graphics g);

	public abstract void init();

	public void moveTo(String position) throws IllegalMovementException {
		if (Data.untraversableBlocks.containsKey(position)) {
			throw new IllegalMovementException("Untraversable block");
		} else {
			String tokens[] = position.split(":");
			if (tokens.length != 2) {
				throw new IllegalMovementException("Invalid movement syntax");
			} else {

				int x = Integer.parseInt(tokens[0]);
				int y = Integer.parseInt(tokens[1]);

				if (x < 0 || x > Data.BLOCK_NUMBER_X || y < 0
						|| y > Data.BLOCK_NUMBER_Y) {
					throw new IllegalMovementException(
							"Movement is out of the map");
				} else {

					int xTmp = this.x, yTmp = this.y;
					int movePoints = this.stats.getMovementPoints();
					if (Math.sqrt(Math.pow(x - xTmp, 2) + Math.pow(y - yTmp, 2)) > movePoints) {
						throw new IllegalMovementException(
								"Not enough movements points");
					} else {
						this.x = x;
						this.y = y;
					}
				}
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public Animation[] getAnimation() {
		return animation;
	}

	public void setAnimation(Animation[] animation) {
		this.animation = animation;
	}

}
