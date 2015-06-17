package game;

import java.util.ArrayList;

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
				g.fillRect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X, Data.PLAYER_LIFE_RECT_Y_POS  +Data.MAP_Y + Data.MAP_HEIGHT , (s.getLifePercentage() * Data.PLAYER_LIFE_RECT_X_SIZE), Data.PLAYER_LIFE_RECT_Y_SIZE );
				//MANA BAR
				g.setColor(Color.blue);

				g.fillRect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT , ((float)s.getMana() / (float)s.getMaxMana()) * Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE );

				g.setColor(Color.black);
				//LIFE BAR
				g.drawRect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X , Data.PLAYER_LIFE_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_LIFE_RECT_X_SIZE, Data.PLAYER_LIFE_RECT_Y_SIZE);
				//MANA BAR
				g.drawRect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE);
				break;
			case 1: //left
				//LIFE BAR*
				g.rotate(Data.MAP_X + Data.MAP_WIDTH /2, Data.MAP_Y + Data.MAP_HEIGHT /2, 90);
				g.setColor(Color.green);
				g.fillRect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X, Data.PLAYER_LIFE_RECT_Y_POS  +Data.MAP_Y + Data.MAP_HEIGHT , (s.getLifePercentage() * Data.PLAYER_LIFE_RECT_X_SIZE), Data.PLAYER_LIFE_RECT_Y_SIZE );
				//MANA BAR
				g.setColor(Color.blue);
				g.fillRect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT , ((float)s.getMana() / (float)s.getMaxMana()) * Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE );

				g.setColor(Color.black);
				//LIFE BAR
				g.drawRect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X , Data.PLAYER_LIFE_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_LIFE_RECT_X_SIZE, Data.PLAYER_LIFE_RECT_Y_SIZE);
				//MANA BAR
				g.drawRect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE);
				g.rotate(Data.MAP_X + Data.MAP_WIDTH /2, Data.MAP_Y + Data.MAP_HEIGHT /2, -90);

				break;
			case 2: //TOP
				//LIFE BAR*
				g.rotate(Data.MAP_X + Data.MAP_WIDTH /2, Data.MAP_Y + Data.MAP_HEIGHT /2, 180);
				g.setColor(Color.green);
				g.fillRect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X, Data.PLAYER_LIFE_RECT_Y_POS  +Data.MAP_Y + Data.MAP_HEIGHT , (s.getLifePercentage() * Data.PLAYER_LIFE_RECT_X_SIZE), Data.PLAYER_LIFE_RECT_Y_SIZE );
				//MANA BAR
				g.setColor(Color.blue);
				g.fillRect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT , ((float)s.getMana() / (float)s.getMaxMana()) * Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE );

				g.setColor(Color.black);
				//LIFE BAR
				g.drawRect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X , Data.PLAYER_LIFE_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_LIFE_RECT_X_SIZE, Data.PLAYER_LIFE_RECT_Y_SIZE);
				//MANA BAR
				g.drawRect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE);
				g.rotate(Data.MAP_X + Data.MAP_WIDTH /2, Data.MAP_Y + Data.MAP_HEIGHT /2, -180);

				break;
			case 3: //left
				//LIFE BAR*
				g.rotate(Data.MAP_X + Data.MAP_WIDTH /2, Data.MAP_Y + Data.MAP_HEIGHT /2, -90);
				g.setColor(Color.green);
				g.fillRect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X, Data.PLAYER_LIFE_RECT_Y_POS  +Data.MAP_Y + Data.MAP_HEIGHT , (s.getLifePercentage() * Data.PLAYER_LIFE_RECT_X_SIZE), Data.PLAYER_LIFE_RECT_Y_SIZE );
				//MANA BAR
				g.setColor(Color.blue);
				g.fillRect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT , ((float)s.getMana() / (float)s.getMaxMana()) * Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE );

				g.setColor(Color.black);
				//LIFE BAR
				g.drawRect(Data.PLAYER_LIFE_RECT_X_POS + Data.MAP_X , Data.PLAYER_LIFE_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_LIFE_RECT_X_SIZE, Data.PLAYER_LIFE_RECT_Y_SIZE);
				//MANA BAR
				g.drawRect(Data.PLAYER_MANA_RECT_X_POS + Data.MAP_X, Data.PLAYER_MANA_RECT_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT, Data.PLAYER_MANA_RECT_X_SIZE, Data.PLAYER_MANA_RECT_Y_SIZE);
				g.rotate(Data.MAP_X + Data.MAP_WIDTH /2, Data.MAP_Y + Data.MAP_HEIGHT /2, 90);

				break;
			}
			
		}
	}

	public void render(GameContainer container, Graphics g) {
		for(int i = 0; i < player.size(); i++)
			player.get(i).render(container , g);		
	}
	
	
}
