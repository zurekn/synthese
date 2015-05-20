package game;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class PlayerHandler {

	private ArrayList<Player> player ;
	
	public PlayerHandler(ArrayList<Player> player2){
		this.player = player2;
	}

	
	public void render(GameContainer container, Graphics g) {
		for(int i = 0; i < player.size(); i++)
			player.get(i).render(container , g);		
	}
	
	
}
