package data;

import game.Mob;

import java.util.ArrayList;

public class Data {
	
	public static final boolean debug = true;
	
	public static String NAME = "Jeu de plateau";
	public static int MAP_SIZE;
	public static int BLOCK_SIZE_X;
	public static int BLOCK_SIZE_Y;
	public static int BLOCK_NUMBER_X;
	public static int BLOCK_NUMBER_Y;
	public static int DECK_AREA_SIZE_X;
	public static int DECK_AREA_SIZE_Y;
	public static int RELATIVE_X_POS;
	public static int RELATIVE_Y_POS;
	
	public static int TURN_MAX_TIME = 10;
	
	public static final String MAP_FILE = "Synthese/res/images/map2.tmx";
	public static final String MONSTER_DATA_XML = "Synthese/res/xml/monstersData.xml";
	public static final String SPELLS_DATA_XML = "Synthese/res/xml/spells.xml";

	public static ArrayList<Mob> initMobs() {
		ArrayList<Mob> mobs = new ArrayList<Mob>();
		mobs.add(new Mob(10, 10, "m1"));
		mobs.add(new Mob(20, 20, "m2"));
		return mobs;
	}
}
