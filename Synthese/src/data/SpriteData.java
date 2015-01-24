package data;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class SpriteData {
	
	public static TiledMap map;
	
	public static void initMap() throws SlickException{
		System.out.println(" Begin data init ");
		map = new TiledMap(Data.MAP_FILE);
		Data.MAP_SIZE = map.getHeight();
		Data.BLOCK_NUMBER = map.getLayerCount();
		Data.BLOCK_SIZE_X = map.getTileHeight();
		Data.BLOCK_SIZE_Y = map.getTileWidth();
		
		System.out.println("MAP_FILE = "+Data.MAP_FILE+", MAP_SIZE = "+Data.MAP_SIZE+", BLOCK_NUMBER = "+Data.BLOCK_NUMBER+", BLOCK_SIZE_X = "+Data.BLOCK_SIZE_X+", BLOCK_SIZE_Y = "+Data.BLOCK_SIZE_Y);
		
	}
	
	/**
	 * Read the xml files and store data in a list
	 */
	public static void initMob(){
		//TODO
		
		
	}
}
