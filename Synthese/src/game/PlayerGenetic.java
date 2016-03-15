package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import game.Character;
import game.WindowGame;
import data.Data;
import data.Hero;
import data.HeroData;
import exception.IllegalActionException;
import exception.IllegalCaracterClassException;

public class PlayerGenetic extends Character {
	
	private Image icon;
	private int number;
	WindowGame windowgame = WindowGame.getInstance();
	
	public PlayerGenetic(){
		
	}
	
	public void run() throws IllegalActionException
	{
		
	}
	
	@SuppressWarnings("unused")
	public void initPlayerGenetic(int x, int y, String id, String caracterClass) throws IllegalCaracterClassException{
		monster=false;
		this.setX(x);
		this.setY(y);
		this.setId(id);
		this.setAiType("player");
		this.setTrueID(id);
		this.setName(id);
		this.setNpc(false);
		Hero h = HeroData.getHeroByClass(caracterClass);
		icon = h.getIcon();
		if(h == null){
			throw (new IllegalCaracterClassException(caracterClass + "Doesn't exist in hero.xml"));
		}
			
		this.setStats(h.getStat().clone());
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
		//if (isMyTurn()) 
			//Data.IMAGE_HALO.draw(getX() * Data.BLOCK_SIZE_X + Data.MAP_X - 10, getY() * Data.BLOCK_SIZE_Y + Data.MAP_Y - 10, Data.BLOCK_SIZE_X + 20 , Data.BLOCK_SIZE_Y + 20);
		if(Data.DISPLAY_PLAYER)
			g.fillRect(Data.MAP_X + getX() * Data.BLOCK_SIZE_X, Data.MAP_Y + getY() * Data.BLOCK_SIZE_Y,
				Data.BLOCK_SIZE_X, Data.BLOCK_SIZE_Y);

			//int posX = Data.MAP_X + getX() * Data.BLOCK_SIZE_X + Data.BLOCK_SIZE_X / 2 - getStats().getMovementPoints() * Data.BLOCK_SIZE_X - Data.BLOCK_SIZE_X / 2;
			//int posY = Data.MAP_Y + getY() * Data.BLOCK_SIZE_Y + Data.BLOCK_SIZE_Y / 2 - getStats().getMovementPoints() * Data.BLOCK_SIZE_Y - Data.BLOCK_SIZE_Y / 2;
			//int sizeX = 2 * getStats().getMovementPoints() * Data.BLOCK_SIZE_X + Data.BLOCK_SIZE_X ;
			//int sizeY = 2 * getStats().getMovementPoints() * Data.BLOCK_SIZE_Y + Data.BLOCK_SIZE_Y ;
			//g.drawOval(posX, posY, sizeX, sizeY);
			g.fillRect(	Data.MAP_X + getX() * Data.BLOCK_SIZE_X,Data.MAP_Y + getY() * Data.BLOCK_SIZE_Y,
						Data.BLOCK_SIZE_X, Data.BLOCK_SIZE_Y);
	}

	public int getNumber(){
		return number;
	}
	
	public void setNumber(int n) {
		this.number = n;
	}
}
