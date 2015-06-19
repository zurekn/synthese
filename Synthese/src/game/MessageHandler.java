package game;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class MessageHandler {

	private int initialX = 10;
	private int inittialY = 50;
	public ArrayList<Message> messages = new ArrayList<Message>();
	
	public void render(GameContainer container, Graphics g){
		int i = 10;
		for(Message m : messages){
			
			m.render(container, g, initialX, inittialY + i);
			if(m.update())
				messages.remove(m);
			i += 10;
		}
	}

	public void update(){
//		for(Message m : messages){
//			if(m.update())
//				messages.remove(m);
//		}
	}
	
	public void addMessage(Message message) {
		messages.add(message);
	}
			
			
}
