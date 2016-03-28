package game;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import data.Data;
import data.Stats;

public class PlayerGeneticHandler {

	private ArrayList<PlayerGenetic> mobs;
	
	
	public PlayerGeneticHandler(ArrayList<PlayerGenetic> mobs){
		this.mobs = mobs;
	}
	
	public void render(GameContainer container, Graphics g) {
		for(int i = 0; i < mobs.size(); i++){
			mobs.get(i).render(container, g);
		}
	}

	public void renderMobStat(GameContainer container, Graphics g) {
		// TODO Auto-generated method stub
		if(!Data.MOB_LIFE_SHOW)
			return;
		float lifeRate = 0;
		for(int i = 0; i < mobs.size(); i++){
			Stats s = mobs.get(i).getStats();
				//LIFE BAR
				g.setColor(Color.red);
				lifeRate= ((float)s.getLife() / (float)s.getMaxLife());
				if(lifeRate < 0 )
					lifeRate = 0;
				g.fillRect(Data.MAP_X + mobs.get(i).getX() * Data.BLOCK_SIZE_X, Data.MAP_Y + mobs.get(i).getY()*Data.BLOCK_SIZE_Y -10, lifeRate  * Data.MOB_LIFE_RECT_X_SIZE, Data.MOB_LIFE_RECT_Y_SIZE );
				//LIFE BAR
				g.drawRect(Data.MAP_X + mobs.get(i).getX() * Data.BLOCK_SIZE_X, Data.MAP_Y + mobs.get(i).getY()*Data.BLOCK_SIZE_Y -10, Data.MOB_LIFE_RECT_X_SIZE, Data.MOB_LIFE_RECT_Y_SIZE);
		}
	}

}
