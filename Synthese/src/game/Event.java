package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Music;

public class Event {
	
	private String id;
	private Animation[] animation;
	Music sound;
	
	
	public Event(String id, Animation[] animation, Music sound){
		this.id = id;
		this.animation = animation;
		this.sound = sound;
	}


	public String getId() {
		return id;
	}


	public Animation[] getAnimation() {
		return animation;
	}


	public Music getSound() {
		return sound;
	}
	

}
