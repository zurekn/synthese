package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import data.Data;
import data.Monster;
import data.MonsterData;
import data.Stats;

public class Mob {

	private int x;
	private int y;
	private String id;
	private Animation[] animation;
	private Stats stats;

	
	public Mob(int x, int y, String id) {
		this.x = x;
		this.y = y;
		this.id = id;
		init();
		
		if(Data.debug){
			System.out.println("Debug : Mob created : "+toStringAll());
		}
	}
	
	public void init(){
		Monster m = MonsterData.getMonsterById(id);
		animation = m.getAnimation();
		this.stats = m.getStats();
		
	}

	public String getId(){
		return id;
	}
	
	public void setId(String id){
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

	
	public void render(GameContainer container, Graphics g) {
		//g.setColor(Color.red);
		//g.drawRect(getX() * Data.BLOCK_SIZE_X, getY() * Data.BLOCK_SIZE_Y, Data.BLOCK_SIZE_X, Data.BLOCK_SIZE_Y);
		g.drawAnimation(animation[6], x * Data.BLOCK_SIZE_X, y * Data.BLOCK_SIZE_Y);
	}

	@Override
	public String toString() {
		return "Mob [x=" + x + ", y=" + y + ", id=" + id + "]";
	}
	
	public String toStringAll(){
		return "Mob [x=" + x + ", y=" + y + ", id=" + id + ", "+stats.toString()+"]";
	}

	
}
