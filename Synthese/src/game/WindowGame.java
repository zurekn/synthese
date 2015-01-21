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
	 private TiledMap map;

	    public WindowGame() throws SlickException {
	        super("Jeu de plateau");
	    }

	    @Override
	    public void init(GameContainer container) throws SlickException {
	        this.container = container;
	        this.map = new TiledMap(Data.MAP_FILE);
	        
	    }

	    public void render(GameContainer container, Graphics g) throws SlickException {
	    	this.map.render(0, 0);
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
