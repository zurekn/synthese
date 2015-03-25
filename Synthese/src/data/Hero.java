package data;

import game.Spell;

import java.util.ArrayList;

public class Hero {

	private String caracterClass;
	private Stats stat;
	private ArrayList<Spell> spells = new ArrayList<Spell>();

	public Hero(String caracterClass, Stats stat) {
		super();
		this.caracterClass = caracterClass;
		this.stat = stat;
	}

	public String getCaracterClass() {
		return caracterClass;
	}

	public void setCaracterClass(String caracterClass) {
		this.caracterClass = caracterClass;
	}

	public Stats getStat() {
		return stat;
	}

	public void setStat(Stats stat) {
		this.stat = stat;
	}

	public ArrayList<Spell> getSpells() {
		return spells;
	}

	public void setSpells(ArrayList<Spell> spells) {
		this.spells = spells;
	}

	public void addSpell(SpellD s) {
		// TODO
		spells.add(new Spell(s.getId(), s.getName(), s.getDamage(),
				s.getHeal(), s.getMana(), s.getRange(), s.getType(), s.getEvent()));
	}

}
