package data;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.Sound;

public class Event {
	private String id;
	private Animation[] animation;
	private Sound sound;
	private int x;
	private int y;
	private int direction = Data.NORTH;
	private int duration = Data.INF;
	private float range;
	private float xRelative;
	private float yRelative;
	private boolean playSound = false;
	private boolean showFirstFrame = false;
	private boolean finalFrame = false;
	private boolean mobile = false;
	private boolean neeDelete = false;
	private int countShowFirst = 0;
	private int countShowFinal = 0;
	private int damage;
	private int heal;
	private String type;
	private int spriteDirection;
	private float speed;

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

	public Event(String id, Sound sound) {
		this.id = id;
		this.sound = sound;
		if (this.sound != null)
			this.playSound = true;
		showFirstFrame = false;
		finalFrame = false;
		mobile=true;
		countShowFirst = 0;
		countShowFinal = 0;
	}

	

	public Event(String id, Animation[] animation, Sound sound, int x, int y, int direction, int duration, float range, float xRelative, float yRelative,
			int spriteDirection, float speed) {
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
		this.speed = speed;
		if (sound != null)
			playSound = true;
		showFirstFrame = true;
		finalFrame = false;
		mobile = true;
		countShowFirst = 0;
		countShowFinal = 0;
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
		return (x - Data.MAP_X) / Data.BLOCK_SIZE_X;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public int getYOnBoard() {
		return (y - Data.MAP_Y) / Data.BLOCK_SIZE_Y;
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

	public Sound getSound() {
		return sound;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

	public boolean isNeeDelete() {
		return neeDelete;
	}

	public void setNeeDelete(boolean neeDelete) {
		this.neeDelete = neeDelete;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction, float speed) {
		this.direction = direction;
		this.speed = speed;
		switch (direction) {
		case Data.SELF:
			this.xRelative = 0;
			this.yRelative = 0;
			break;
		case Data.NORTH:
			this.xRelative = 0;
			this.yRelative = (0-this.speed) * Data.BLOCK_SIZE_Y;
			break;
		case Data.SOUTH:
			this.xRelative = 0;
			this.yRelative = this.speed * Data.BLOCK_SIZE_Y;
			break;

		case Data.EAST:
			this.xRelative = this.speed * Data.BLOCK_SIZE_X;
			this.yRelative = 0;
			break;
		case Data.WEST:
			this.xRelative = (0-this.speed) * Data.BLOCK_SIZE_X;
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

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}
	
	public boolean isFinalFrame() {
		return finalFrame;
	}

	public void setFinalFrame(boolean isFinalFrame) {
		this.finalFrame = isFinalFrame;
	}
	
	
	public boolean isMobile() {
		return mobile;
	}

	public void setMobile(boolean canMove) {
		this.mobile = canMove;
	}

	public Event getCopiedEvent() {
		Event e = new Event(id, animation, sound, x, y, direction, duration, range, xRelative, yRelative, spriteDirection, speed);
		e.setDamage(damage);
		e.setHeal(heal);
		e.setType(type);
		return e;
	}

	public void render(GameContainer container, Graphics g) {
		int dx = 0, dy = 0;
		int finalDirection = direction - spriteDirection;
		int finalFrame = (animation[0].getFrameCount() - 1);
		
		if (spriteDirection == 90) {
			if (finalDirection == Data.NORTH - spriteDirection) {
				dx = (2 * Data.BLOCK_SIZE_Y - animation[0].getWidth()) / 2;
				dy = (Data.BLOCK_SIZE_X - animation[0].getHeight()) / 2;

			} else if (finalDirection == Data.SOUTH - spriteDirection) {
				dx = (4 * Data.BLOCK_SIZE_Y - animation[0].getWidth()) / 2;
				dy = (-Data.BLOCK_SIZE_X - animation[0].getHeight()) / 2;

			} else if (finalDirection == Data.EAST - spriteDirection) {
				dx = (4 * Data.BLOCK_SIZE_Y - animation[0].getWidth()) / 2;
				dy = (Data.BLOCK_SIZE_X - animation[0].getHeight()) / 2;

			} else if (finalDirection == Data.WEST - spriteDirection) {
				dx = (2 * Data.BLOCK_SIZE_Y - animation[0].getWidth()) / 2;
				dy = (-Data.BLOCK_SIZE_X - animation[0].getHeight()) / 2;
			}
		}
		g.rotate(x, y, direction - spriteDirection);
		
		if(!mobile && animation[0].getFrame()<finalFrame-1){
			animation[0].setCurrentFrame(animation[0].getFrame()+1);
		}
		if(showFirstFrame){ // On n'affiche la première frame qu'une fois
			animation[0].restart();
			g.drawAnimation(animation[0], x + dx, y + dy);
			showFirstFrame = false;
		}else if(countShowFirst<4)	{
			g.drawAnimation(animation[0], x + dx, y + dy);
				countShowFirst += 1;
				if(countShowFirst == 4)
					animation[0].setCurrentFrame(1);
		}else if(animation[0].getFrame()== finalFrame-1){ // On n'affiche pas la dernière frame
			
			animation[0].stop();
			g.drawAnimation(animation[0], x + dx, y + dy);
			animation[0].setCurrentFrame(1);
			if(!mobile)
				this.finalFrame = true;
			
		}else if(animation[0].getFrame()!= 0 ){ // On affiche chaque frame intermédiaire
			g.drawAnimation(animation[0], x + dx, y + dy);
		}else{ 									// On réaffiche pas la première frame
			animation[0].setCurrentFrame(1);
			g.drawAnimation(animation[0], x + dx, y + dy);
		}
		
		g.rotate(x, y, -direction + spriteDirection);

		if (playSound) {
			playSound = false;
			sound.play();
		}
	}
	
	public void renderPostRemove(GameContainer container, Graphics g) {
		int dx = 0, dy = 0;
		int finalDirection = direction - spriteDirection;
		int finalFrame = (animation[0].getFrameCount() - 1);

		if (spriteDirection == 90) {
			if (finalDirection == Data.NORTH - spriteDirection) {
				dx = (2 * Data.BLOCK_SIZE_Y - animation[0].getWidth()) / 2;
				dy = (Data.BLOCK_SIZE_X - animation[0].getHeight()) / 2;

			} else if (finalDirection == Data.SOUTH - spriteDirection) {
				dx = (4 * Data.BLOCK_SIZE_Y - animation[0].getWidth()) / 2;
				dy = (-Data.BLOCK_SIZE_X - animation[0].getHeight()) / 2;

			} else if (finalDirection == Data.EAST - spriteDirection) {
				dx = (4 * Data.BLOCK_SIZE_Y - animation[0].getWidth()) / 2;
				dy = (Data.BLOCK_SIZE_X - animation[0].getHeight()) / 2;

			} else if (finalDirection == Data.WEST - spriteDirection) {
				dx = (2 * Data.BLOCK_SIZE_Y - animation[0].getWidth()) / 2;
				dy = (-Data.BLOCK_SIZE_X - animation[0].getHeight()) / 2;
			}
		}
		g.rotate(x, y, direction - spriteDirection);
		
		animation[0].setCurrentFrame(finalFrame);
		animation[0].stop();
		if(countShowFinal<3){
			g.drawAnimation(animation[0], x + dx, y + dy);
				countShowFinal += 1;
				if(countShowFinal == 3)
					neeDelete = true;
		}
		
		animation[0].restart();
		g.rotate(x, y, -direction + spriteDirection);
	}
	
	public void move() {
		this.x += this.xRelative;
		this.y += this.yRelative;
		float test = range;
		switch (direction) {
		case Data.NORTH:
			range += yRelative / Data.BLOCK_SIZE_Y;
			break;
		case Data.SOUTH:
			range -= yRelative / Data.BLOCK_SIZE_Y;
			break;
		case Data.EAST:
			range -= xRelative / Data.BLOCK_SIZE_X;
			break;
		case Data.WEST:
			range += xRelative / Data.BLOCK_SIZE_X;
			break;
		case Data.SELF:
			range -= 1;
		default:
			range -= 1;
			break;
		}
		/*
		if(test == range)
			System.out.println(" === WHAAT === ...  "+" xRelative = "+ xRelative+" yRelative = "+yRelative+
					" Block X = "+ Data.BLOCK_SIZE_X+" Block Y = "+ Data.BLOCK_SIZE_Y );
					*/
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", x=" + x + ", y=" + y + ", direction=" + direction + ", duration=" + duration + ", range=" + range
				+ ", xRelative=" + xRelative + ", yRelative=" + yRelative + ", damage=" + damage + ", heal=" + heal + ", type=" + type + "]";
	}

}
