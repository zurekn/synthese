package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import data.Data;
import data.Monster;
import data.MonsterData;
import data.Stats;

public class Mob extends Character{

	public Mob(int x, int y, String id) {
		this.setX(x);
		this.setY(y);
		this.setId(id);
		init();
		
		if(Data.debug){
			System.out.println("Debug : Mob created : "+toStringAll());
		}
	}
	
	public void init(){
		Monster m = MonsterData.getMonsterById(this.getId());
		this.setAnimation(m.getAnimation());
		this.setStats(m.getStats());
		
	}
	
	public void render(GameContainer container, Graphics g) {
		Animation[] animation = this.getAnimation();
		int x = this.getX();
		int y = this.getY();
		//g.setColor(Color.red);
		//g.drawRect(getX() * Data.BLOCK_SIZE_X, getY() * Data.BLOCK_SIZE_Y, Data.BLOCK_SIZE_X, Data.BLOCK_SIZE_Y);

		
		animation[6].draw(Data.DECK_AREA_SIZE_Y + x * Data.BLOCK_SIZE_X, Data.DECK_AREA_SIZE_Y +  y * Data.BLOCK_SIZE_Y, Data.BLOCK_SIZE_X, Data.BLOCK_SIZE_Y);
		if (getMyTurn()) {
			int posX = Data.DECK_AREA_SIZE_Y +  getX() * Data.BLOCK_SIZE_X + Data.BLOCK_SIZE_X / 2 - getStats().getMovementPoints() * Data.BLOCK_SIZE_X - Data.BLOCK_SIZE_X / 2;
			int posY = Data.DECK_AREA_SIZE_Y + getY() * Data.BLOCK_SIZE_Y + Data.BLOCK_SIZE_Y / 2 - getStats().getMovementPoints() * Data.BLOCK_SIZE_Y - Data.BLOCK_SIZE_Y / 2;
			int sizeX = 2 * getStats().getMovementPoints() * Data.BLOCK_SIZE_X + Data.BLOCK_SIZE_X ;
			int sizeY = 2 * getStats().getMovementPoints() * Data.BLOCK_SIZE_Y + Data.BLOCK_SIZE_Y ;
			g.drawOval(posX, posY, sizeX, sizeY);
		}
		
	}

	@Override
	public String toString() {
		return "Mob [x=" + getX() + ", y=" + getY() + ", id=" + getId() + "]";
	}
	
	public String toStringAll(){
		return "Mob [x=" + getX() + ", y=" + getY() + ", id=" + getId() + ", " + getStats().toString()+"]";
	}

	
}
