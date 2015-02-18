package data;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Music;

public class Event {
	String id;
	Animation[] animation;
	Music sound;

	public Event(String id) {
		this.id = id;
		String type = id.substring(0, 1);
		if(type.equalsIgnoreCase("S")){//Spells
			this.animation = SpellData.getAnimationById(id);
		}else if(type.equalsIgnoreCase("T")){ //Traps
			this.animation = TrapData.getAnimationById(id);
		}else if(type.equalsIgnoreCase("D")){ //Deaths
			
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
	
	
}
