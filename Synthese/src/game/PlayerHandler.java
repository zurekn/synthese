package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import data.Data;
import data.Stats;

public class PlayerHandler {
	private ArrayList<Player> player ;
	
	public PlayerHandler(ArrayList<Player> player2){
		this.player = player2;
	}

public void renderPlayerStat(GameContainer container, Graphics g){

		
		for(int i = 0; i < player.size(); i++){
			Stats s = player.get(i).getStats();
			switch(i){
			case 0: //bottom
				//LIFE BAR*
				g.setColor(Color.green);
				g.fillRect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X, Data.PLAYER_LIFE_RECT_Y_POS  +Data.MAP_Y + Data.MAP_HEIGHT , ((float)s.getLife() / (float)s.getMaxLife())  * Data.PLAYER_LIFE_RECT_X_SIZE, Data.PLAYER_LIFE_RECT_Y_SIZE );
				//MANA BAR
				g.setColor(Color.blue);

				g.fillRect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT , ((float)s.getMana() / (float)s.getMaxMana()) * Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE );

				g.setColor(Color.black);
				//LIFE BAR
				g.drawRect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X , Data.PLAYER_LIFE_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_LIFE_RECT_X_SIZE, Data.PLAYER_LIFE_RECT_Y_SIZE);
				//MANA BAR
				g.drawRect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE);
				player.get(i).getIcon().draw(Data.MAP_X + Data.PLAYER_ICON_X_POS, Data.PLAYER_ICON_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT);
				break;
			case 1: //left
				//LIFE BAR*
				g.rotate(Data.MAP_X + Data.MAP_WIDTH /2, Data.MAP_Y + Data.MAP_HEIGHT /2, 90);
				g.setColor(Color.green);
				g.fillRect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X, Data.PLAYER_LIFE_RECT_Y_POS  +Data.MAP_Y + Data.MAP_HEIGHT , ((float)s.getLife() / (float)s.getMaxLife())  * Data.PLAYER_LIFE_RECT_X_SIZE, Data.PLAYER_LIFE_RECT_Y_SIZE );
				//MANA BAR
				g.setColor(Color.blue);
				g.fillRect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT , ((float)s.getMana() / (float)s.getMaxMana()) * Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE );

				g.setColor(Color.black);
				//LIFE BAR
				g.drawRect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X , Data.PLAYER_LIFE_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_LIFE_RECT_X_SIZE, Data.PLAYER_LIFE_RECT_Y_SIZE);
				//MANA BAR
				g.drawRect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE);
				player.get(i).getIcon().draw(Data.MAP_X + Data.PLAYER_ICON_X_POS, Data.PLAYER_ICON_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT);

				g.rotate(Data.MAP_X + Data.MAP_WIDTH /2, Data.MAP_Y + Data.MAP_HEIGHT /2, -90);

				break;
			case 2: //TOP
				//LIFE BAR*
				g.rotate(Data.MAP_X + Data.MAP_WIDTH /2, Data.MAP_Y + Data.MAP_HEIGHT /2, 180);
				g.setColor(Color.green);
				g.fillRect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X, Data.PLAYER_LIFE_RECT_Y_POS  +Data.MAP_Y + Data.MAP_HEIGHT , ((float)s.getLife() / (float)s.getMaxLife())  * Data.PLAYER_LIFE_RECT_X_SIZE, Data.PLAYER_LIFE_RECT_Y_SIZE );
				//MANA BAR
				g.setColor(Color.blue);
				g.fillRect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT , ((float)s.getMana() / (float)s.getMaxMana()) * Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE );

				g.setColor(Color.black);
				//LIFE BAR
				g.drawRect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X , Data.PLAYER_LIFE_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_LIFE_RECT_X_SIZE, Data.PLAYER_LIFE_RECT_Y_SIZE);
				//MANA BAR
				g.drawRect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE);
				player.get(i).getIcon().draw(Data.MAP_X + Data.PLAYER_ICON_X_POS, Data.PLAYER_ICON_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT);
				g.rotate(Data.MAP_X + Data.MAP_WIDTH /2, Data.MAP_Y + Data.MAP_HEIGHT /2, -180);

				break;
			case 3: //left
				//LIFE BAR*
				g.rotate(Data.MAP_X + Data.MAP_WIDTH /2, Data.MAP_Y + Data.MAP_HEIGHT /2, -90);
				g.setColor(Color.green);
				g.fillRect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X, Data.PLAYER_LIFE_RECT_Y_POS  +Data.MAP_Y + Data.MAP_HEIGHT , ((float)s.getLife() / (float)s.getMaxLife())  * Data.PLAYER_LIFE_RECT_X_SIZE, Data.PLAYER_LIFE_RECT_Y_SIZE );
				//MANA BAR
				g.setColor(Color.blue);
				g.fillRect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT , ((float)s.getMana() / (float)s.getMaxMana()) * Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE );

				g.setColor(Color.black);
				//LIFE BAR
				g.drawRect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X , Data.PLAYER_LIFE_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_LIFE_RECT_X_SIZE, Data.PLAYER_LIFE_RECT_Y_SIZE);
				//MANA BAR
				g.drawRect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE);
				player.get(i).getIcon().draw(Data.MAP_X + Data.PLAYER_ICON_X_POS, Data.PLAYER_ICON_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT);
				g.rotate(Data.MAP_X + Data.MAP_WIDTH /2, Data.MAP_Y + Data.MAP_HEIGHT /2, 90);

				break;
			}
			
		}
	}

	public void render(GameContainer container, Graphics g) {
		for(int i = 0; i < player.size(); i++)
			player.get(i).render(container , g);		
	}

	public void renderInitBlock(GameContainer container, Graphics g) {
		String []split;
		String var;
		boolean used;
		int x, y;
		Set<String> set = Data.departureBlocks.keySet();
		Iterator<String> it = set.iterator();
		
		g.setColor(Data.BLOCK_REACHABLE_COLOR);
		while(it.hasNext()){
			var = ((String) it.next());
			if(!Data.departureBlocks.get(var)){
				split = var.split(":");
				x = Integer.parseInt(split[0]);
				y = Integer.parseInt(split[1]);
				g.fillRect(Data.MAP_X + x * Data.BLOCK_SIZE_X, Data.MAP_Y + y * Data.BLOCK_SIZE_Y, Data.BLOCK_SIZE_X, Data.BLOCK_SIZE_Y);
			}
		}
		g.setColor(Data.DEFAULT_COLOR);
	}
	
	
}
