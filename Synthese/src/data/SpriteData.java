package data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;

public class SpriteData {

	public static TiledMap map;
	public static MonsterData monsterData;

	public static void initMap() throws SlickException {
		System.out.println(" Begin data init ");
		map = new TiledMap(Data.MAP_FILE);
		Data.MAP_SIZE = map.getHeight();
		Data.BLOCK_NUMBER_X = map.getHeight();
		Data.BLOCK_NUMBER_Y = map.getWidth();
		Data.BLOCK_SIZE_X = map.getTileHeight();
		Data.BLOCK_SIZE_Y = map.getTileWidth();
		Data.DECK_AREA_SIZE_X = Data.BLOCK_SIZE_X * Data.MAP_SIZE;
		Data.DECK_AREA_SIZE_Y = Data.BLOCK_SIZE_Y * 3;
		Data.RELATIVE_X_POS = Data.RELATIVE_Y_POS = Data.DECK_AREA_SIZE_Y;
		System.out.println("MAP_FILE = " + Data.MAP_FILE + ", MAP_SIZE = "
				+ Data.MAP_SIZE + ", BLOCK_NUMBER = " + Data.BLOCK_NUMBER_X
				+ ", BLOCK_SIZE_X = " + Data.BLOCK_SIZE_X + ", BLOCK_SIZE_Y = "
				+ Data.BLOCK_SIZE_Y);

	}

	public static void initSpell() {

	}

	/**
	 * Read the xml files and store data in a list
	 */
	public static void initMob() {
		MonsterData monsterData = new MonsterData();
		// Load the xml file
		System.out.println("Initializing monsters, loading "+Data.MONSTER_DATA_XML);
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(new File(Data.MONSTER_DATA_XML));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (doc.equals(null)) {
			System.out.println("Error : Can't load [" + Data.MONSTER_DATA_XML
					+ "]");
			System.exit(1);
		}
		Element root = doc.getRootElement();

		List monsters = root.getChildren("monster");

		Iterator i = monsters.iterator();
		int life, armor, mana, strength, magicPower, luck, movementPoints;
		List <Element> spells = new ArrayList<Element>();
		while (i.hasNext()) {

			Element el = (Element) i.next();
			try {
				life = Integer.parseInt(el.getChildText("life"));
				armor = Integer.parseInt(el.getChildText("armor"));
				mana = Integer.parseInt(el.getChildText("mana"));
				strength = Integer.parseInt(el.getChildText("strength"));
				magicPower= Integer.parseInt(el.getChildText("magicPower"));
				luck = Integer.parseInt(el.getChildText("luck"));
				movementPoints = Integer.parseInt(el.getChildText("movementPoints"));
				String id = el.getAttributeValue("id");
				spells = el.getChild("spells").getChildren("spell");
				Iterator ii = spells.iterator();
				SpriteSheet ss = new SpriteSheet("" + el.getChildText("file"),
						Integer.parseInt(el.getChildText("celDimensionX")),
						Integer.parseInt(el.getChildText("celDimensionY")));
				Stats stats = new Stats(life, armor, mana, strength,
						magicPower, luck, movementPoints);
				Monster m = new Monster(id, ss, stats);
				while(ii.hasNext()){
					Element e = (Element) ii.next();
					SpellD s = SpellData.getSpellById(e.getText());
					if(s != null)
						m.addSpell(s);
						
				}

				monsterData.addMonster(m);
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Loading end : "+el.getChild("file").getText());
		}

	}
}
