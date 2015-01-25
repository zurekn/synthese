package data;

import game.Mob;

import java.util.ArrayList;

public class Data {
	
	public static final boolean debug = true;
	
	public static String NAME = "Jeu de plateau";
	public static int MAP_SIZE;
	public static int BLOCK_SIZE_X;
	public static int BLOCK_SIZE_Y;
	public static int BLOCK_NUMBER;
	
	public static final String MAP_FILE = "Synthese/res/images/map2.tmx";
	public static final String MONSTER_XML = "Synthese/res/xml/monsters.xml";

	public static ArrayList<Mob> initMobs() {
		ArrayList<Mob> mobs = new ArrayList<Mob>();
		mobs.add(new Mob(10, 10, "m1"));
		
		return mobs;
	}
}
