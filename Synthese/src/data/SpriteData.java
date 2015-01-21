package data;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class SpriteData {
	
	public static TiledMap map;
	
	public static void initMap() throws SlickException{
		map = new TiledMap(Data.MAP_FILE);
	}
	
	/**
	 * Read the xml files and store data in a list
	 */
	public static void initMob(){
		
		
		
	}
}
