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
			int direction, int duration, int range, int xRelative, int yRelative) {
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
		if (sound != null)
			playSound = true;
	}

	public String getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
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
				range, xRelative, yRelative);
		return e;
	}

	public void render(GameContainer container, Graphics g) {
		int dx = 0, dy = 0;
		switch (direction) {
		case Data.NORTH:
			break;

		case Data.SOUTH:
			dx = -Data.BLOCK_SIZE_X;
			dy = -Data.BLOCK_SIZE_Y;
			break;

		case Data.EAST:
			dx = 0;
			dy = -Data.BLOCK_SIZE_Y;
			break;

		case Data.WEST:
			dx = -Data.BLOCK_SIZE_X;
			dy = 0;
			break;
		}

		g.rotate(x, y, direction);

		g.drawAnimation(animation[0], x + dx, y + dy);

		g.rotate(x, y, -direction);

		if (playSound) {
			playSound = false;
			sound.play();
		}
		this.x += this.xRelative;
		this.y += this.yRelative;

	}

}
