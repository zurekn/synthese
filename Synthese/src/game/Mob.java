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
		g.drawAnimation(animation[6], x * Data.BLOCK_SIZE_X, y * Data.BLOCK_SIZE_Y);
	}

	@Override
	public String toString() {
		return "Mob [x=" + getX() + ", y=" + getY() + ", id=" + getId() + "]";
	}
	
	public String toStringAll(){
		return "Mob [x=" + getX() + ", y=" + getY() + ", id=" + getId() + ", " + getStats().toString()+"]";
	}

	
}
