package game;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import data.*;

public class WindowGame extends BasicGame {
	
	 private GameContainer container;
	 private MobHandler mobHandler;
	 private ArrayList<Mob> mobs;
	 private playerHandler playerHandler;
	 private Player player;
	 
	 private int playerNumber;
	 private int turn;
	 
	    public WindowGame() throws SlickException {
	        super(Data.NAME);
	    }

	    @Override
	    public void init(GameContainer container) throws SlickException {
	        this.container = container;
	        SpriteData.initMap();
	        SpriteData.initMob();
	        
	        player = new Player(5, 5);
	        playerHandler = new playerHandler(player);
	        
	        mobs =  Data.initMobs();
	        mobHandler = new MobHandler(mobs);
	    }

	    public void render(GameContainer container, Graphics g) throws SlickException {
	    	SpriteData.map.render(0, 0);
	    	mobHandler.render(container, g);
	    	//TODO
	    	//Bug playerrender doesn't work
	    	playerHandler.render(container, g);
	    }

	    @Override
	    public void update(GameContainer container, int delta) throws SlickException {
	    }

	    @Override
	    public void keyReleased(int key, char c) {
	    	System.out.println("WindowGame, keyReleased : "+key+", char : "+c);
	        if (Input.KEY_ESCAPE == key) {
	            container.exit();
	        }
	    }
}
