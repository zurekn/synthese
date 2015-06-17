package game;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import data.Data;
import data.Hero;
import data.HeroData;
import data.Monster;
import data.MonsterData;
import data.SpellData;
import data.Stats;
import exception.IllegalCaracterClassException;
import exception.IllegalMovementException;

public class Player extends Character {
	
	private Image icon;
	
	/**
	 * This constructor is not available
	 * @param x
	 * @param y
	 * @param id
	 * @param stats
	 * @deprecated
	 */
	public Player(int x, int y, String id, Stats stats) {
		this.setX(x);
		this.setY(y);
		this.setId(id);
		this.setStats(stats);
		this.setSpells(SpellData.getSpellForClass(this.getStats().getCharacterClass()));
		init();

		if (Data.debug) {
			System.out.println("Debug : Player " + getId() + " created");
		}
	}
	
	public Player(int x, int y, String id, String caracterClass) throws IllegalCaracterClassException{
		monster=false;
		this.setX(x);
		this.setY(y);
		this.setId(id);
		this.setAiType("hero");
		this.setTrueID(id);
		this.setNpc(false);
		Hero h = HeroData.getHeroByClass(caracterClass);
		icon = h.getIcon();
		if(h == null){
			throw (new IllegalCaracterClassException(caracterClass + "Doesn't exist in hero.xml"));
		}
			
		this.setStats(h.getStat());
		this.setSpells(h.getSpells());
		
		if (Data.debug) {
			System.out.println("Debug : Player " + this.toString() + " created");
		}
	}

	public void init() {

	}

	public Image getIcon(){
		return icon;
	}
	
	public void render(GameContainer container, Graphics g) {
		g.setColor(Color.black);
		if(Data.DISPLAY_PLAYER)
			g.fillRect(Data.MAP_X + getX() * Data.BLOCK_SIZE_X, Data.MAP_Y + getY() * Data.BLOCK_SIZE_Y,
				Data.BLOCK_SIZE_X, Data.BLOCK_SIZE_Y);
		if (isMyTurn()) {
			int posX = Data.MAP_X + getX() * Data.BLOCK_SIZE_X + Data.BLOCK_SIZE_X / 2 - getStats().getMovementPoints() * Data.BLOCK_SIZE_X - Data.BLOCK_SIZE_X / 2;
			int posY = Data.MAP_Y + getY() * Data.BLOCK_SIZE_Y + Data.BLOCK_SIZE_Y / 2 - getStats().getMovementPoints() * Data.BLOCK_SIZE_Y - Data.BLOCK_SIZE_Y / 2;
			int sizeX = 2 * getStats().getMovementPoints() * Data.BLOCK_SIZE_X + Data.BLOCK_SIZE_X ;
			int sizeY = 2 * getStats().getMovementPoints() * Data.BLOCK_SIZE_Y + Data.BLOCK_SIZE_Y ;
//			g.drawOval(posX, posY, sizeX, sizeY);
		}
	}
}
