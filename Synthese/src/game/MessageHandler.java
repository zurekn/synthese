package game;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import data.Data;

public class MessageHandler {

	private int initialX = 10;
	private int inittialY = 50;
	public ArrayList<Message> globalMessages = new ArrayList<Message>();//Global Message
	public ArrayList<ArrayList<Message>> playerMessages = new ArrayList<ArrayList<Message>>();
	
	private ArrayList<Message> deletedMessage = new ArrayList<Message>();
	private ArrayList<ArrayList<Message>> deletedPlayerMessage = new ArrayList<ArrayList<Message>>()
			;
	public void render(GameContainer container, Graphics g){
		int i = 10;
		for(Message m : globalMessages){
			
			m.render(container, g, initialX, inittialY + i);
			if(m.update())
				deletedMessage.add(m);
			i += 10;
		}
		
		int n = 0;
		i = 0;
		for(ArrayList<Message> list : playerMessages){
			g.rotate(Data.MAP_X + Data.MAP_WIDTH /2, Data.MAP_Y + Data.MAP_HEIGHT /2, n * 90);
			for(Message m : list){
				m.render(container, g, Data.PLAYER_MESSAGE_X_POS + Data.MAP_X, Data.PLAYER_MESSAGE_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT + i);
				if(m.update())
					deletedPlayerMessage .get(n).add(m);
				i += 10;
			}
			g.rotate(Data.MAP_X + Data.MAP_WIDTH /2, Data.MAP_Y + Data.MAP_HEIGHT /2, n * 90);
			i = 0;
			n++;
		}
		
		//For the delete
		for(Message m : deletedMessage){
			globalMessages.remove(m);
		}
		
		n = 0;
		for(ArrayList<Message> list : deletedPlayerMessage){
			for(Message m : list){
				playerMessages.get(n).remove(m);
			}
			list.clear();
			n++;
		}
				
		
		deletedMessage.clear();
		
	}

	public void update(){
//		for(Message m : messages){
//			if(m.update())
//				messages.remove(m);
//		}
	}
	
	public void addGlobalMessage(Message message) {
		globalMessages.add(message);
	}
	
	public void addPlayerMessage(Message message, int player){
		//first call we create the playerList
		for(int i = 0; i < WindowGame.getInstance().getPlayers().size(); i++){
			playerMessages.add(new ArrayList<Message>());
			deletedPlayerMessage.add(new ArrayList<Message>());
		}
		
		if(player > WindowGame.getInstance().getPlayers().size())
			addGlobalMessage(message);
		else{
			switch (player) {
			case 0:
				message.setRotation(0);
				break;
			case 1:
				message.setRotation(90);
				break;
			case 2:
				message.setRotation(180);
				break;
			case 3:
				message.setRotation(-90);
				break;

			default:
				break;
			}
			playerMessages.get(player).add(message);
			
		}
		}
			
			
}
