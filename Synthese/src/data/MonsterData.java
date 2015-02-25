package data;

import game.Mob;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class MonsterData {
	
	public static ArrayList<Monster> monsters = new ArrayList<Monster>();
	
	public void addMonster(Monster m){
		monsters.add(m);
	}
	
	public static Animation[] getAnimationById(String id){
		for(int i = 0; i < monsters.size(); i++){
			if(monsters.get(i).getId().equals(id)){
				return monsters.get(i).getAnimation();
			}
		}
		return null;
	}
	
	public static Monster getMonsterById(String id){
		for(int i = 0; i < monsters.size(); i++){
			if(monsters.get(i).getId().equals(id)){
				return monsters.get(i);
			}
		}
		return null;
	}
	
	public static ArrayList<Mob> initMobs() {
		ArrayList<Mob> mobs = new ArrayList<Mob>();
//		mobs.add(new Mob(0, 0, "m1"));
//		mobs.add(new Mob(19, 10, "m2"));
		
		System.out.println("Initializating monsters from : "+Data.MAP_XML);
		
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(new File(Data.MAP_XML));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (doc.equals(null)) {
			System.out.println("Error : Can't load [" + Data.MAP_XML
					+ "]");
			System.exit(1);
		}
		
		Element root = doc.getRootElement();
		List monsters = root.getChildren("monster");

		Iterator i = monsters.iterator();
		int posX, posY;
		String id;
		List <Element> spells = new ArrayList<Element>();
		while (i.hasNext()) {

			Element el = (Element) i.next();
			id = el.getAttributeValue("id");
			posX = Integer.parseInt(el.getChildText("x"));
			posY = Integer.parseInt(el.getChildText("y"));
			Mob m = new Mob(posX, posY, id);
			System.out.println("Load : "+m.toString());
			mobs.add(m);
		}
		
		
		return mobs;
		
	}
	
	public static void loadMob() {
		MonsterData monsterData = new MonsterData();
		// Load the xml file
		System.out.println("Loading monsters, loading "+Data.MONSTER_DATA_XML);
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
		int life, armor, mana, strength, magicPower, luck, movementPoints, magicResist;
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
				magicResist = Integer.parseInt(el.getChildText("magicResist"));
				Iterator ii = spells.iterator();
				SpriteSheet ss = new SpriteSheet("" + el.getChildText("file"),
						Integer.parseInt(el.getChildText("celDimensionX")),
						Integer.parseInt(el.getChildText("celDimensionY")));
				Stats stats = new Stats(life, armor, mana, strength,
						magicPower, luck, movementPoints, magicResist);
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
			}catch (NumberFormatException e){
				e.printStackTrace();
			}
			System.out.println("Loading end : "+el.getChild("file").getText());
		}

	}
	
}
