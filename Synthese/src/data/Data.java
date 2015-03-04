package data;

import game.Mob;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;
/**
 * Class witch contains all static variables
 * @author bob
 *
 */
public class Data {

	public static final boolean debug = true;
	public static final boolean DISPLAY_PLAYER = true;

	public static String NAME = "Jeu de plateau";
	public static int MAP_WIDTH;
	public static int MAP_HEIGHT;
	public static int BLOCK_SIZE_X;
	public static int BLOCK_SIZE_Y;
	public static int BLOCK_NUMBER_X;
	public static int BLOCK_NUMBER_Y;
	public static int DECK_AREA_SIZE_X;
	public static int DECK_AREA_SIZE_Y;
	public static int RELATIVE_X_POS;
	public static int RELATIVE_Y_POS;
	public static float SCALE;
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static int TOTAL_WIDTH;
	public static int TOTAL_HEIGHT;

	public static int TURN_MAX_TIME = 20;

	public static int SELF = 0;
	public static int NORTH = 1;
	public static int EAST = 2;
	public static int SOUTH = 3;
	public static int WEST = 4;

	public static final String MAP_FILE = "Synthese/res/images/map2.tmx";
	public static final String MONSTER_DATA_XML = "Synthese/res/xml/monstersData.xml";
	public static final String SPELLS_DATA_XML = "Synthese/res/xml/spells.xml";
	public static final String TRAPS_DATA_XML = "Synthese/res/xml/traps.xml";
	public static final String MAP_XML = "Synthese/res/xml/map.xml";
	public static final String HERO_XML = "Synthese/res/xml/hero.xml";

	public static final HashMap<String, Event> eventMap = new HashMap<String, Event>();
	public static final HashMap<String, Boolean> untraversableBlocks = new HashMap<String, Boolean>();

	public static TiledMap map;
	public static MonsterData monsterData;

	/**
	 * Load all game variables
	 * @throws SlickException
	 */
	public static void loadGame() throws SlickException {
		
		System.out.println(" Begin data init ");
		
		map = new TiledMap(Data.MAP_FILE);
		
		Data.BLOCK_NUMBER_X = map.getHeight();
		Data.BLOCK_NUMBER_Y = map.getWidth();
		Data.BLOCK_SIZE_X = map.getTileHeight();
		Data.BLOCK_SIZE_Y = map.getTileWidth();
		Data.MAP_HEIGHT = Data.BLOCK_NUMBER_Y * Data.BLOCK_SIZE_Y;
		Data.MAP_WIDTH = Data.BLOCK_NUMBER_X * Data.BLOCK_SIZE_X;
		Data.DECK_AREA_SIZE_X = Data.BLOCK_SIZE_X * Data.BLOCK_NUMBER_X;
		Data.DECK_AREA_SIZE_Y = Data.BLOCK_SIZE_Y * 3;
		Data.RELATIVE_X_POS = Data.RELATIVE_Y_POS = Data.DECK_AREA_SIZE_Y;
		Data.TOTAL_HEIGHT = Data.MAP_HEIGHT + 2 * Data.DECK_AREA_SIZE_Y;
		Data.TOTAL_WIDTH = Data.MAP_WIDTH + 2 * Data.DECK_AREA_SIZE_Y;
		Data.SCALE = (float) Data.SCREEN_HEIGHT / Data.TOTAL_HEIGHT;

		System.out.println("MAP_FILE = " + Data.MAP_FILE + ", MAP_WIDTH = "
				+ Data.MAP_WIDTH + ", MAP_HEIGHT = " + Data.MAP_HEIGHT
				+ ", BLOCK_NUMBER = " + Data.BLOCK_NUMBER_X
				+ ", BLOCK_SIZE_X = " + Data.BLOCK_SIZE_X + ", BLOCK_SIZE_Y = "
				+ Data.BLOCK_SIZE_Y + ", SCALE = " + Data.SCALE);

	}

	public static void initSpell() {

	}


	public static void loadMap() {
		Document doc = XMLReader.readXML(Data.MAP_XML);

		try {
			Element root = doc.getRootElement();
			List<Element> blocks = root.getChildren("block");
			int x, y;
			Element block;
			for (Iterator<Element> it = blocks.iterator(); it.hasNext();) {
				block = it.next();
				x = block.getAttribute("x").getIntValue();
				y = block.getAttribute("y").getIntValue();
				untraversableBlocks.put(x + ":" + y, new Boolean(true));
			}
		} catch (DataConversionException e) {
			e.printStackTrace();
		}

	}
}
