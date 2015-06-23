package data;

import game.Mob;
import game.WindowGame;

import java.awt.GraphicsConfigTemplate;
import java.awt.GraphicsConfiguration;
import java.awt.font.GraphicAttribute;
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

import javax.imageio.ImageIO;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
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
	
	public static final boolean FULLSCREEN = false;
	public static final boolean DEBUG_DEPARTURE = true;
	public static final boolean tiDebug = true;
	public static final boolean debug = true;
	public static final boolean DISPLAY_PLAYER = true;
	public static final boolean runQRCam = false;
	public static final boolean RUN_APIX = false;
	public static boolean debugPicture = false; 
	public static final boolean inTest = true;
	public static final boolean debugQR = false;
	public static final int DEBUG_PLAYER = 4;
	//public static String IMAGE_DIR ="C:/Users/boby/Google Drive/Master1/Synthèse/ImageDeTest/";
	public static String IMAGE_DIR = "C:/Users/frédéric/Google Drive/Master Cergy/Projet_PlateauJeu/Synthèse/ImageDeTest/";
	
	// Const TI Part
	public static final long WAIT_TI = 5000;
	public static int SEUILINITTI = 100;
	public static int SEUILETI = 200;
	public static int MIN_SEUIL_FORM = 50;
	public static int MAX_SEUIL_FORM = 5000;
	public static final int QRCamSeuil = 60;//Data.SEUILETI;
	
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

	public static int TURN_MAX_TIME = 5000; // in sec

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
	public static int PLAYER_MESSAGE_X_POS = 120;
	public static int PLAYER_MESSAGE_Y_POS = 10;
	
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

	public static String MAP_FILE = "Synthese/res/images/map3.tmx";
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
	
	public static final long REFRESH_TIME_EVENT = 500;//in milli
	public static final long MESSAGE_DURATION = 2000;
	
	public static Music BACKGROUND_MUSIC; 
	
	//AI
	public static final int AI_DEPTH_MAX = 1;
	public static final float[][] AI_FACTORS = {
			{ 2.f, 1.5f, 0.75f, 0.2f, 0.50f, 0.3f }, // coward
			{ 1.f, 0.5f, 0.40f, 0.3f, 0.70f, 0.9f }, // lonewolf
			{ 1.f, 1.0f, 0.50f, 0.5f, 0.80f, 0.6f }, // normal
			{ 1.f, 1.0f, 0.50f, 0.5f, 0.80f, 0.6f } // player
	};
	
	public static final float[][] AI_HEURISITCS = {
		{2.25f, 8.f, 2.0f, 2.0f},	//coward
		{1.00f, 7.f, 0.5f, 2.0f},	//lonewolf
		{1.00f, 7.f, 1.0f, 2.0f},	//normal
		{1.00f, 7.f, 1.0f, 2.0f}	//player
	};

	public static final int COWARD = 0;
	public static final int LONEWOLF = 1;
	public static final int NORMAL = 2;
	public static final int PLAYER = 3;
	
	public static final int LIFE = 1 ;
	public static final int ALLIES_LIFE = 2;
	public static final int MANA = 3;

	public static final int ENEMIES_DETECTION = 0; 
	public static final int ALLIES_DETECTION = 1;
	public static final int MIN_LIFE = 2; //min percentage life before unfocus
	public static final int BACKUP_ALLIES = 3; //min percentage of allies' life to heal them
	public static final int RUNAWAY = 4; // min danger level to run away
	public static final int STICK_TO_ALLIES = 5; //min danger level to go near allies 
	
	//MESSAGES PARAM
	public static final Color MESSAGE_COLOR_TYPE_1 = new Color(Color.red);
	public static final Color MESSAGE_COLOR_TYPE_0 = new Color(Color.white);
	private static final Color MESSAGE_COLOR_TYPE_2 = new Color(Color.green);
	public static final int MESSAGE_TYPE_INFO = 0;
	public static final int MESSAGE_TYPE_ERROR = 1;
	private static final int MESSAGE_TYPE_SUCCES = 2;
	public static final Color DEFAULT_COLOR = Color.black;
	public static final int ACTION_PER_TURN = 1;
	
	//ERROR MESSAGES
	public static final String ERROR_TOO_MUCH_ACTION = "Une seul action par tour !";
	
	//MESSAGES
	public static final String INIT_PLAYER_TEXT = "Time until the game begin :";
	public static final String TURN_TEXT = "End of turn in : ";
	public static final String DEPARTURE_BLOCK_ERROR = "Le pion doit être sur une case de départ !";
	public static final int FONT_SIZE = 10;
	public static Image IMAGE_HALO = null;
	public static float MUSIC_VOLUM = .1f;
	public static float MUSIC_PITCH = 1;
	public static String MAIN_TEXT = "";
	public static int MESSAGE_MAX_LENGTH;

	
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
		Data.MESSAGE_MAX_LENGTH = (Data.MAP_WIDTH - Data.DECK_AREA_SIZE_X - Data.PLAYER_LIFE_RECT_X_SIZE) / Data.FONT_SIZE;

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
			
			BACKGROUND_MUSIC = new Music(root.getChildText("music"));
			IMAGE_HALO = new Image(root.getChildText("halo_image"));
		} catch (DataConversionException e) {
			e.printStackTrace();
		} catch (SlickException e) {
			e.printStackTrace();
		}

		
	}
	
	public static void playBackgroundMusic(){
		BACKGROUND_MUSIC.loop(MUSIC_PITCH, MUSIC_VOLUM);
	}
	
	public static void musicVolumeUp(int volume){
		MUSIC_VOLUM = volume;
		BACKGROUND_MUSIC.setVolume(MUSIC_VOLUM);
		
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

	public static Color getColorMessage(int type) {
		Color color;
		switch(type){
		case Data.MESSAGE_TYPE_INFO:
			color = MESSAGE_COLOR_TYPE_0;
			break;
		case Data.MESSAGE_TYPE_ERROR:
			color = MESSAGE_COLOR_TYPE_1;
			break;
		case Data.MESSAGE_TYPE_SUCCES:
			color = MESSAGE_COLOR_TYPE_2;
			break;
		default:
			color = MESSAGE_COLOR_TYPE_0;
			break;
		}
		return color;
	}

	public static long getDurationMessage(int type) {
		switch (type){
		case 0:
			return MESSAGE_DURATION;
		case 1:
			return MESSAGE_DURATION * 2;
		default:
			return MESSAGE_DURATION;
		}
	}
}
