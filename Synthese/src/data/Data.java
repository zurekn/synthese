package data;

import game.Mob;
import game.WindowGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;

/**
 * Class witch contains all static variables
 * 
 * @author bob
 *
 */
public class Data {

	public static final boolean tiDebug = true;
	public static final boolean debug = true;
	public static final boolean DISPLAY_PLAYER = true;
	public static final boolean runQRCam = false;
	public static final boolean RUN_APIX = false;
	public static boolean debugPicture = false; 
	public static final boolean inTest = true;
	public static final boolean debugQR = false;
	public static final int DEBUG_PLAYER = 0;
	public static String IMAGE_DIR ="C:/Users/boby/Google Drive/Master1/Synthèse/ImageDeTest/";
	//public static String IMAGE_DIR = "C:/Users/frédéric/Google Drive/Master Cergy/Projet_PlateauJeu/Synthèse/ImageDeTest/";
	
	public static String NAME = "Jeu de plateau";
	public static int MAP_WIDTH;
	public static int MAP_HEIGHT;
	public static int BLOCK_SIZE_X;
	public static int BLOCK_SIZE_Y;
	public static int MAP_X;
	public static int MAP_Y;
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

	public static int TURN_MAX_TIME = 50; // in sec

	//For the stat display
	public static int PLAYER_LIFE_RECT_X_POS = 10;
	public static int PLAYER_LIFE_RECT_Y_POS = 10;
	public static int PLAYER_LIFE_RECT_X_SIZE = 100;
	public static int PLAYER_LIFE_RECT_Y_SIZE = 10;
	public static int PLAYER_MANA_RECT_X_POS = 10;
	public static int PLAYER_MANA_RECT_Y_POS = 25;
	public static int PLAYER_MANA_RECT_X_SIZE = 50;
	public static int PLAYER_MANA_RECT_Y_SIZE = 10;
	public static int PLAYER_ICON_X_POS = 10;
	public static int PLAYER_ICON_Y_POS = 50;
	
	public static final int SELF = 360;
	public static final int NORTH = 0;
	public static final int NORTH_EAST = 45;
	public static final int EAST = 90;
	public static final int SOUTH_EAST = 135;
	public static final int SOUTH = 180;
	public static final int SOUTH_WEST = -135;
	public static final int WEST = -90;
	public static final int NORTH_WEST = -45;

	public static final int INF = 500;
	
	public static int SEUILINITTI = 100;
	public static int SEUILETI = 200;
	public static int MIN_SEUIL_FORM = 50;
	public static int MAX_SEUIL_FORM = 5000;

	public static final String MAP_FILE = "Synthese/res/images/map3.tmx";
	public static final String MONSTER_DATA_XML = "Synthese/res/xml/monstersData.xml";
	public static final String SPELLS_DATA_XML = "Synthese/res/xml/spells.xml";
	public static final String TRAPS_DATA_XML = "Synthese/res/xml/traps.xml";
	public static final String MAP_XML = "Synthese/res/xml/map.xml";
	public static final String HERO_XML = "Synthese/res/xml/hero.xml";

	public static final HashMap<String, Event> eventMap = new HashMap<String, Event>();
	public static final HashMap<String, Boolean> untraversableBlocks = new HashMap<String, Boolean>();
	public static final HashMap<String, Boolean> departureBlocks = new HashMap<String, Boolean>();
	public static final int MAX_RANGE = Integer.MAX_VALUE;
	public static final long WAINTING_TIME = 1000;
	public static  Color BLOCK_REACHABLE_COLOR = new Color(1f, 0f, 0f, .1f);
	public static final Color TEXT_COLOR = new Color(Color.black);
	public static  boolean SHOW_MOB_REACHABLE_BLOCKS = false;
	public static int MAX_PLAYER = 4;
	public static int INIT_MAX_TIME = 40;

	private static boolean initImageDir = false;

	public static TiledMap map;
	public static MonsterData monsterData;
	public static WindowGame game;
	public static long beginTime;
	
	public static final String INIT_PLAYER_TEXT = "Time until the game begin :";
	public static final String TURN_TEXT = "End of turn in : ";
	public static  String MAIN_TEXT = "";
	public static final long REFRESH_TIME_EVENT = 500;//in milli

	
	/**
	 * Load all game variables
	 * 
	 * @throws SlickException
	 */
	public static void loadGame() throws SlickException {

		System.out.println(" Begin data init ");
		beginTime = System.currentTimeMillis();

		map = new TiledMap(Data.MAP_FILE);

		Data.BLOCK_NUMBER_X = map.getHeight();
		Data.BLOCK_NUMBER_Y = map.getWidth();
		Data.BLOCK_SIZE_X = map.getTileHeight();
		Data.BLOCK_SIZE_Y = map.getTileWidth();
		Data.MAP_HEIGHT = Data.BLOCK_NUMBER_Y * Data.BLOCK_SIZE_Y;
		Data.MAP_WIDTH = Data.BLOCK_NUMBER_X * Data.BLOCK_SIZE_X;
		Data.DECK_AREA_SIZE_X = Data.MAP_WIDTH  / 2; // Voir pour la largeur de la surface des cartes
		Data.DECK_AREA_SIZE_Y = Data.BLOCK_SIZE_Y * 3;
		Data.RELATIVE_X_POS = 288;//Data.DECK_AREA_SIZE_Y * 3;
		Data.RELATIVE_Y_POS = 0;
		Data.MAP_X = Data.RELATIVE_X_POS + Data.DECK_AREA_SIZE_Y;
		Data.MAP_Y = Data.RELATIVE_Y_POS + Data.DECK_AREA_SIZE_Y;
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
				
				if(block.getChild("untraversable") == null)
					continue;
				
				x = block.getAttribute("x").getIntValue();
				y = block.getAttribute("y").getIntValue();
				untraversableBlocks.put(x + ":" + y, new Boolean(true));
				if(debug)
					System.out.println("New untravesable block at : ["+x+":"+y+"]");
			}
			
			for(Iterator<Element> it = root.getChild("departure").getChildren().iterator(); it.hasNext();){
				block = it.next();
				x = block.getAttribute("x").getIntValue();
				y = block.getAttribute("y").getIntValue();
				departureBlocks.put(x + ":" + y, false);
				if(debug)
					System.out.println("New departure blok at : ["+x+":"+y+"]");
			}
			
		} catch (DataConversionException e) {
			e.printStackTrace();
		}

	}
	
	
	public static String getImageDir(){
		if(!initImageDir){
			IMAGE_DIR += getDate()+"/"; 
			new File(IMAGE_DIR).mkdir(); 
			
			initImageDir = true;
		}
		return IMAGE_DIR;
	}
	
	
	public static String getDate(){
		Date date = new Date();
		DateFormat formater = new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
		return formater.format(date);
	}
	
	public static void checkValuesIni(String filePath)
	{
		Scanner scanner;
		try {
			File file = new File(filePath);
			if(file.exists())
			{
				scanner = new Scanner(file);	
				while (scanner.hasNextLine()) 
				{
				    String line = scanner.nextLine();
				    if(line.contains("="))
				    {
				    	line = line.replaceAll(" ", "");
				    	if(line.contains("seuilinit"))
				    		if(!line.substring(line.lastIndexOf("=")+1).equals(""))
				    			Data.SEUILINITTI = Integer.parseInt(line.substring(line.lastIndexOf("=")+1));
				    	if(line.contains("seuiletiquetage"))
				    		if(!line.substring(line.lastIndexOf("=")+1).equals(""))
				    			Data.SEUILETI = Integer.parseInt(line.substring(line.lastIndexOf("=")+1));
				    	if(line.contains("seuilmin"))
				    		if(!line.substring(line.lastIndexOf("=")+1).equals(""))
				    			Data.MIN_SEUIL_FORM = Integer.parseInt(line.substring(line.lastIndexOf("=")+1));
				    	if(line.contains("seuilmax"))
				    		if(!line.substring(line.lastIndexOf("=")+1).equals(""))
				    			Data.MAX_SEUIL_FORM = Integer.parseInt(line.substring(line.lastIndexOf("=")+1));
				    }
				}
				scanner.close();
			}
			else // file does not exist
			{
				try {
					file.createNewFile();
					FileWriter writer = new FileWriter(file, true);

					String texte = "seuilinit="+Data.SEUILINITTI+"\n";
					writer.write(texte,0,texte.length());
					writer.write("\r\n");
					
					texte = "seuiletiquetage="+Data.SEUILETI;
					writer.write(texte,0,texte.length());
					writer.write("\r\n");
					
					texte = "seuilmin="+Data.MIN_SEUIL_FORM;
					writer.write(texte,0,texte.length());
					writer.write("\r\n");
					
					texte = "seuilmax="+Data.MAX_SEUIL_FORM;
					writer.write(texte,0,texte.length());
					writer.write("\r\n");
					
					writer.close(); // fermer le fichier à la fin des traitements					
				} 
				catch (IOException e) 
				{e.printStackTrace();} 
			}
		} 
		catch (FileNotFoundException e) 
		{e.printStackTrace();}
	}
}
