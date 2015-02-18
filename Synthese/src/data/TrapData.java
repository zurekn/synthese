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

public class TrapData {
	public static ArrayList<TrapD> traps = new ArrayList<TrapD>();

	public void addTrap(TrapD t) {
		traps.add(t);
	}

	public static TrapD getTrapById(String id) {
		for (int i = 0; i < traps.size(); i++) {
			if (traps.get(i).getId().equals(id)) {
				return traps.get(i);
			}
		}
		return null;
	}
	
	public static Animation[] getAnimationById(String id){
		for(int i = 0; i < traps.size(); i++){
			if(traps.get(i).getId().equals(id)){
				return traps.get(i).getEvent().getAnimation();
			}
		}
		return null;
	}
	
	public static void initTrap(){

		// Load the xml file
		System.out.println("Initializing traps, loading "+Data.TRAPS_DATA_XML);
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(new File(Data.TRAPS_DATA_XML));
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (doc.equals(null)) {
			System.out.println("Error : Can't load [" + Data.TRAPS_DATA_XML
					+ "]");
			System.exit(1);
		}
		Element root = doc.getRootElement();

		List trapsList = root.getChildren("trap");

		Iterator i = trapsList.iterator();
		int damage, celNumber;
		String id, name, damageType, file, sound;
		while (i.hasNext()) {
			try {
			Element el = (Element) i.next();
			damage = Integer.parseInt(el.getChildText("damage"));
			name = el.getChildText("name");
			id = el.getAttributeValue("id");
			file = el.getChildText("file");
			sound = el.getChildText("sound");
			celNumber = Integer.parseInt(el.getChildText("celNumber"));
			damageType = el.getChildText("damageType");
			SpriteSheet ss;
			
				ss = new SpriteSheet("" + el.getChildText("file"),
						Integer.parseInt(el.getChildText("celX")),
						Integer.parseInt(el.getChildText("celY")));
			traps.add(new TrapD(id, damage, damageType, name, celNumber, ss, new Music(sound)));
			System.out.println("   Spell : ["+name+"] load end");
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
