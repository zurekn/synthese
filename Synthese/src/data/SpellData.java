package data;

import game.Spell;

import java.util.ArrayList;

import org.newdawn.slick.Animation;

public class SpellData {

	public static ArrayList<Spell> spells = new ArrayList<Spell>();
	
	public void addSpell(Spell spell){
		spells.add(spell);
	}
	
	public static Animation[] getAnimationById(String id){
		for(int i = 0; i < spells.size(); i++){
			if(spells.get(i).getId().equals(id)){
				return spells.get(i).getAnimation();
			}
		}
		return null;
	}
}
