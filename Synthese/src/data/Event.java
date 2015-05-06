package data;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;

public class Event {
	private String id;
	private Animation[] animation;
	private Music sound;
	private int x;
	private int y;
	private int direction = Data.NORTH;
	private int duration = Data.INF;
	private int range;
	private int xRelative;
	private int yRelative;
	private boolean playSound = false;
	private int damage;
	private int heal;
	private String type;
	private int spriteDirection;

	public Event(String id) {
		this.id = id;
		String type = id.substring(0, 1);
		if (type.equalsIgnoreCase("S")) {// Spells
			this.animation = SpellData.getAnimationById(id);
		} else if (type.equalsIgnoreCase("T")) { // Traps
			this.animation = TrapData.getAnimationById(id);
		} else if (type.equalsIgnoreCase("D")) { // Deaths

		}
	}

	public Event(String id, Music sound) {
		this.id = id;
		this.sound = sound;
		if (this.sound != null)
			this.playSound = true;
	}

	public Event(String id, Animation[] animation, Music sound, int x, int y,
			int direction, int duration, int range, int xRelative,
			int yRelative, int spriteDirection) {
		super();
		this.id = id;
		this.animation = animation;
		this.sound = sound;
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.duration = duration;
		this.range = range;
		this.xRelative = xRelative;
		this.yRelative = yRelative;
		this.spriteDirection = spriteDirection;
		if (sound != null)
			playSound = true;
	}

	public int getSpriteDirection() {
		return spriteDirection;
	}

	public void setSpriteDirection(int spriteDirection) {
		this.spriteDirection = spriteDirection;
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public int getXOnBoard() {
		return (x - Data.RELATIVE_X_POS) / Data.BLOCK_NUMBER_X;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public int getYOnBoard() {
		return (y - Data.RELATIVE_X_POS) / Data.BLOCK_NUMBER_Y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Animation[] getAnimation() {
		return animation;
	}

	public void setAnimation(Animation[] animation) {
		this.animation = animation;
	}

	public Music getSound() {
		return sound;
	}

	public void setSound(Music sound) {
		this.sound = sound;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
		switch (direction) {
		case Data.SELF:
			this.xRelative = 0;
			this.yRelative = 0;
			break;
		case Data.NORTH:
			this.xRelative = 0;
			this.yRelative = -1 * Data.BLOCK_SIZE_Y;
			break;
		case Data.SOUTH:
			this.xRelative = 0;
			this.yRelative = 1 * Data.BLOCK_SIZE_Y;
			break;

		case Data.EAST:
			this.xRelative = 1 * Data.BLOCK_SIZE_X;
			this.yRelative = 0;
			break;
		case Data.WEST:
			this.xRelative = -1 * Data.BLOCK_SIZE_X;
			this.yRelative = 0;
			break;
		}
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;

	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public Event getCopiedEvent() {
		Event e = new Event(id, animation, sound, x, y, direction, duration,
				range, xRelative, yRelative, spriteDirection);
		e.setDamage(damage);
		e.setHeal(heal);
		e.setType(type);
		return e;
	}

	public void render(GameContainer container, Graphics g) {
		int dx = 0, dy = 0;
		int finalDirection = direction - spriteDirection;

		if (spriteDirection == 90) {
			if (finalDirection == Data.NORTH - spriteDirection) {
				dy = (Data.BLOCK_SIZE_X - animation[0].getHeight())/2;

			} else if (finalDirection == Data.SOUTH - spriteDirection) {
				dx = (4*Data.BLOCK_SIZE_Y - animation[0].getWidth())/2; 
				dy = (-Data.BLOCK_SIZE_X - animation[0].getHeight())/2;

			} else if (finalDirection == Data.EAST - spriteDirection) {
				dx = (4*Data.BLOCK_SIZE_Y - animation[0].getWidth())/2; 
				dy = (Data.BLOCK_SIZE_X - animation[0].getHeight())/2;

			} else if (finalDirection == Data.WEST - spriteDirection) {
				dx = (2*Data.BLOCK_SIZE_Y - animation[0].getWidth())/2; 
				dy = (-Data.BLOCK_SIZE_X - animation[0].getHeight())/2;
			}
		}

		g.rotate(x, y, direction - spriteDirection);

		g.drawAnimation(animation[0], x + dx, y + dy);

		g.rotate(x, y, -direction + spriteDirection);

		if (playSound) {
			playSound = false;
			sound.play();
		}
		this.x += this.xRelative;
		this.y += this.yRelative;

	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", x=" + x + ", y=" + y + ", direction="
				+ direction + ", duration=" + duration + ", range=" + range
				+ ", xRelative=" + xRelative + ", yRelative=" + yRelative
				+ ", damage=" + damage + ", heal=" + heal + ", type=" + type
				+ "]";
	}

}
