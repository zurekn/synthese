package game;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import data.*;
import exception.InvalidMovementException;

/**
 * Main class whith handle the game
 * Contain the init method for creating all game objects
 * @author bob
 *
 */
public class WindowGame extends BasicGame {
	
	 private GameContainer container;
	 private MobHandler mobHandler;
	 private ArrayList<Mob> mobs;
	 private playerHandler playerHandler;
	 private ArrayList<Player> player;
	 private MovementHandler movementHandler;
	 
	 private int playerNumber;
	 private int turn;
	 
	 private int turnTimer;
	 private long timeStamp = -1;
	 
	    public WindowGame() throws SlickException {
	        super(Data.NAME);
	    }

	    @Override
	    public void init(GameContainer container) throws SlickException {
	        this.container = container;
	        SpriteData.initMap();
	        SpriteData.initMob();
	        
	        movementHandler = new MovementHandler(this);
	        
	        player = new ArrayList<Player>();
	        player.add(new Player(5, 5, new Stats(100, 50)));
	        playerHandler = new playerHandler(player);
	        
	        mobs =  Data.initMobs();
	        mobHandler = new MobHandler(mobs);
	        
	        playerNumber = 1 + mobs.size();
	        
	        turnTimer = Data.TURN_MAX_TIME;
	        new Thread (movementHandler).start();
	        turn = 0;
	    }

	    public void render(GameContainer container, Graphics g) throws SlickException {
	    	SpriteData.map.render(0, 0);
	    	mobHandler.render(container, g);
	    	//TODO
	    	//Bug playerrender doesn't work
	    	playerHandler.render(container, g);
	    	
	    	g.setColor(Color.white);
	    	g.drawString("End of turn in : "+turnTimer, 10, 20);
	    }

	    @Override
	    public void update(GameContainer container, int delta) throws SlickException {
	    	if(System.currentTimeMillis() -  timeStamp > 1000){
	    		turnTimer --;
	    		timeStamp = System.currentTimeMillis();
	    	}
	    	
	    	if(turnTimer < 0){
	    		turnTimer = Data.TURN_MAX_TIME;
	    		turn = (turn + 1)%playerNumber;
	    		System.out.println("========================");
	    		System.out.println("Tour du joueur "+turn);
	    		System.out.println("========================");
	    	}
	    }

	    @Override
	    public void keyReleased(int key, char c) {
	    	System.out.println("WindowGame, keyReleased : "+key+", char : "+c);
	        if (Input.KEY_ESCAPE == key) {
	            container.exit();
	        }
	    }

		public void move(String str) {
			System.out.println("WindowGame get new movement : "+str);
			if(turn < player.size()){
				try {
					player.get(turn).moveTo(str);
				} catch (InvalidMovementException e) {

					e.printStackTrace();
				}
			}else{
				System.out.println("Mobs movement not set");
			}
			
		}
}
