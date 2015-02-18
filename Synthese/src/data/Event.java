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
	private int xRelative;
	private int yRelative;

	public Event(String id) {
		this.id = id;
		String type = id.substring(0, 1);
		if(type.equals("S")){//Spells
			this.animation = SpellData.getAnimationById(id);
		}else if(type.equals("T")){ //Traps
			this.animation = TrapData.getAnimationById(id);
		}else if(type.equals("D")){ //Deaths
			
		}
	}
	
	public Event(String id, Music sound){
		this.id = id;
		this.sound = sound;
	}

	public String getId() {
		return id;
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

	public void render(GameContainer container, Graphics g) {
		g.drawAnimation(animation[0], xRelative, yRelative);
		
	}
	
	
}
