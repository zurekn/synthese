package game;

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

	    public WindowGame() throws SlickException {
	        super(Data.NAME);
	        mobHandler = new MobHandler();
	    }

	    @Override
	    public void init(GameContainer container) throws SlickException {
	        this.container = container;
	        SpriteData.initMap();
	        
	    }

	    public void render(GameContainer container, Graphics g) throws SlickException {
	    	SpriteData.map.render(0, 0);
	    	mobHandler.render(container, g);
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
