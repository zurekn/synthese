package game;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class MobHandler {

	private ArrayList<Mob> mobs;
	
	public MobHandler(ArrayList<Mob> mobs){
		this.mobs = mobs;
	}
	
	public void render(GameContainer container, Graphics g) {
		for(int i = 0; i < mobs.size(); i++){
			mobs.get(i).render(container, g);
		}
	}

}
