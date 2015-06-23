package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.jdom2.Document;
import org.jdom2.Element;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class HeroData {

	public static ArrayList<Hero> heros = new ArrayList<Hero>();
	public static ArrayList<String> caraClass = new ArrayList<String>();
	public static final HashMap<String, Integer> CLASSES_VALUES = new HashMap<String, Integer>(); 
	
	public void addHero(Hero h) {
		heros.add(h);
	}
	
	public static Hero getHeroByClass(String caracterClass){
		for(int i = 0; i < heros.size(); i++){
			if(heros.get(i).getStat().getCharacterClass().equals(caracterClass))
				return heros.get(i);
		}
		return null;
	}
	
	public static String getRandomHero(){
		if(Data.debug)
			return "mage";
		String result = "";
		Random rand = new Random();
		int n = rand.nextInt((HeroData.getHeroNumber() - 0)) + 0;
		
		return heros.get(n).getCaracterClass();
	}
	
	public static int getHeroNumber(){
		return heros.size();
	}
	
	public static void loadHeros(){
		HeroData heroData = new HeroData();
		Document doc = XMLReader.readXML(Data.HERO_XML);
		
		Element root = doc.getRootElement();

		List heros = root.getChildren("hero");

		Iterator i = heros.iterator();
		String id;
		int life, armor, mana, strength, magicPower, luck, movementPoints, magicResist, eyeSight;
		List <Element> spells = new ArrayList<Element>();
		
		while(i.hasNext()){
			Element el = (Element) i.next();
			
			id = el.getAttributeValue("id");
			life = Integer.parseInt(el.getChildText("life"));
			armor = Integer.parseInt(el.getChildText("armor"));
			mana = Integer.parseInt(el.getChildText("mana"));
			strength = Integer.parseInt(el.getChildText("strength"));
			magicPower = Integer.parseInt(el.getChildText("magicPower"));
			luck = Integer.parseInt(el.getChildText("luck"));
			movementPoints = Integer.parseInt(el.getChildText("movementPoints"));
			magicResist = Integer.parseInt(el.getChildText("magicResist"));
			eyeSight = Integer.parseInt(el.getChildText("eyeSight"));
			Image icon = null;
			try {
				icon = new Image(el.getChildText("icon"));
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Hero h = new Hero(id, icon, new Stats(life, armor, mana, strength, magicPower, luck, movementPoints, magicResist, eyeSight ,id));
			CLASSES_VALUES.put(id, Integer.parseInt(el.getChildText("value")));
			Iterator<Element> ii =  el.getChild("spells").getChildren("spell").iterator();
			
			while(ii.hasNext()){
				Element e = (Element) ii.next();
				SpellD s = SpellData.getSpellById(e.getAttributeValue("id"));
				if(s != null)
					h.addSpell(s);
			}
			heroData.addHero(h);
			caraClass.add(h.getCaracterClass());
			System.out.println("	Hero : "+h.getCaracterClass());
		}
		//TODO 
//		CLASSES_VALUES.put("mage", 0);
//		CLASSES_VALUES.put("barbarian", 10);
//		CLASSES_VALUES.put("rogue", 5);
//		CLASSES_VALUES.put("cleric", 0);
	}

}
