package data;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Music;
import org.newdawn.slick.SpriteSheet;

public class TrapD {
	private String id;
	private String name;
	private String damageType;
	private int celNumber;
	private int damage;
	private Event event;
	
	

	public TrapD(String id, int damage, String damageType, String name, int celNumber,
			SpriteSheet ss, Music sound) {
		this.id = id;
		this.damage = damage;
		this.name = name;
		this.celNumber = celNumber;
		this.damageType = damageType;
		this.event = new Event(id, sound);
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

	public String getDamageType() {
		return damageType;
	}

	public void setDamageType(String damageType) {
		this.damageType = damageType;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

}
