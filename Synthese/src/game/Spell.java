package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import data.Data;

public class Spell {

	private String id;
	private String name;
	private int damage;
	private int heal;
	private int mana;
	private Animation[] animation = new Animation[1];
	private int directionX = 1;
	private int directionY = 1;
	private int x = 20;
	private int y = 20;

	public Spell(String id, String name, int damage, int heal, int mana,
			Animation[] animation) {
		super();
		this.id = id;
		this.name = name;
		this.damage = damage;
		this.heal = heal;
		this.mana = mana;
		this.animation = animation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Animation[] getAnimation() {
		return animation;
	}

	public void setAnimation(Animation[] animation) {
		this.animation = animation;
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

	@Override
	public String toString() {
		return "Spell [id=" + id + ", name=" + name + ", damage=" + damage
				+ ", heal=" + heal + ", mana=" + mana + "]";
	}

	public void render(GameContainer container, Graphics g) {
		g.drawAnimation(animation[0], x, y);
	}


}
