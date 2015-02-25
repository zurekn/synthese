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
import org.newdawn.slick.Animation;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class SpellData {

	public static ArrayList<SpellD> spells = new ArrayList<SpellD>();
	
	public void addSpell(SpellD spell){
		spells.add(spell);
	}
	
	public static Animation[] getAnimationById(String id){
		for(int i = 0; i < spells.size(); i++){
			if(spells.get(i).getId().equals(id)){
				return spells.get(i).getEvent().getAnimation();
			}
		}
		return null;
	}
	
	/**
	 * Load all spell data from Data.SPELLS_DATA_XML
	 */
	public static void loadSpell(){

		Document doc = XMLReader.readXML(Data.SPELLS_DATA_XML);
		
		Element root = doc.getRootElement();

		List monsters = root.getChildren("spell");

		Iterator i = monsters.iterator();
		int damage, heal, mana, celNumber;
		String id, name, file;
		while (i.hasNext()) {
			try {
			Element el = (Element) i.next();
			damage = Integer.parseInt(el.getChildText("damage"));
			heal = Integer.parseInt(el.getChildText("heal"));
			mana = Integer.parseInt(el.getChildText("mana"));
			name = el.getChildText("name");
			id = el.getAttributeValue("id");
			file = el.getChildText("file");
			celNumber = Integer.parseInt(el.getChildText("celNumber"));
			Music sound = new Music(el.getChildText("sound"));
			SpriteSheet ss;
			
				ss = new SpriteSheet("" + el.getChildText("file"),
						Integer.parseInt(el.getChildText("celX")),
						Integer.parseInt(el.getChildText("celY")));
			spells.add(new SpellD(id, damage, heal, mana, name, celNumber, ss, sound));
			System.out.println("	Spell : ["+name+"] load end");
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static SpellD getSpellById(String text) {
		for(int i = 0; i < spells.size(); i++){
			if(spells.get(i).getId().equals(text))
				return spells.get(i);
		}
		return null;
	}
	
}
