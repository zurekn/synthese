package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import data.Data;
import data.Monster;
import data.MonsterData;
import data.Stats;
import exception.IllegalMovementException;

public class Player extends Character {

	public Player(int x, int y, String id, Stats stats) {
		this.setX(x);
		this.setY(y);
		this.setId(id);
		this.setStats(stats);
		init();

		if (Data.debug) {
			System.out.println("Debug : Player " + getId() + " created");
		}
	}

	public void init() {
		/*
		 * Monster m = MonsterData.getMonsterById(this.getId());
		 * this.setAnimation(m.getAnimation());
		 */

	}

	public void render(GameContainer container, Graphics g) {
		g.setColor(Color.blue);
		g.fillRect(getX() * Data.BLOCK_SIZE_X, getY() * Data.BLOCK_SIZE_Y,
				Data.BLOCK_SIZE_X, Data.BLOCK_SIZE_Y);
		if (getMyTurn()) {
			g.drawOval(getX() * Data.BLOCK_SIZE_X + Data.BLOCK_SIZE_X / 2,
					getY() * Data.BLOCK_SIZE_Y + Data.BLOCK_SIZE_Y / 2,
					2 * getStats().getMovementPoints() * Data.BLOCK_SIZE_X,
					2 * getStats().getMovementPoints() * Data.BLOCK_SIZE_Y);
		}
	}

	@Override
	public String toString() {
		return "Player [id=" + getId() + ", x=" + getX() + ", y=" + getY()
				+ "]";
	}

}
