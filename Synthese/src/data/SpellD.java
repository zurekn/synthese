package data;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Music;
import org.newdawn.slick.SpriteSheet;

public class SpellD {

	private String id;
	private String name;
	private int damage;
	private int heal;
	private int mana;
	private Event event;

	public SpellD(String id, int damage, int heal, int mana, String name,
			int celNumber, SpriteSheet ss, Music sound) {
		this.id = id;
		this.damage = damage;
		this.heal = heal;
		this.mana = mana;
		this.name = name;
		this.event = new Event(id,sound);
		initEventAnimation(celNumber, ss);
	}

	private void initEventAnimation(int n, SpriteSheet ss) {
		Animation[] a = new Animation[1];
		a[0]= loadAnimation(ss, 0, n, 0);
		this.event.setAnimation(a);
	}

	private Animation loadAnimation(SpriteSheet spriteSheet, int startX,
			int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX; x < endX; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 100);
		}
		return animation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

}
