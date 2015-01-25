package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class playerHandler {

	private Player player ;
	
	public playerHandler(Player player){
		this.player = player;
	}

	
	public void render(GameContainer container, Graphics g) {
		player.render(container , g);		
	}
	
	
}
