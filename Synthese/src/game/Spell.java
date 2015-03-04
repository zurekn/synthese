package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import data.Event;

public class Spell {
//TODO add range in constructors
	
	private String id;
	private String name;
	private int damage;
	private int heal;
	private int mana;
	private int range ;
	private Event event;
	private int directionX = 1;
	private int directionY = 1;
	private int x = 20;
	private int y = 20;

	public Spell(String id, String name, int damage, int heal, int mana,
			Event event) {
		super();
		this.id = id;
		this.name = name;
		this.damage = damage;
		this.heal = heal;
		this.mana = mana;
		this.event = event;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getHeal() {
		return heal;
	}

	public void setHeal(int heal) {
		this.heal = heal;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	@Override
	public String toString() {
		return "Spell [id=" + id + ", name=" + name + ", damage=" + damage
				+ ", heal=" + heal + ", mana=" + mana + "]";
	}

	public void render(GameContainer container, Graphics g) {
		g.drawAnimation(event.getAnimation()[0], x, y);
	}


}
