package ai;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;

import game.Character;
import game.Mob;
import game.Player;
import game.WindowGame;

/**
 * This class handle the ai for the game
 * 
 * @author bob
 *
 */
public class AIHandler implements Runnable {

	private final EventListenerList listeners = new EventListenerList();
	
	public void addAIListener(AIListener listener){
		listeners.add(AIListener.class, listener);
	}
	
	public void removeAIListener(AIListener listener){
		listeners.remove(AIListener.class, listener);
	}
	
	public AIListener[] getAIListener(){
		return listeners.getListeners(AIListener.class);
	}
	
	protected void newAction(String id, String data){
		ActionEvent event = null;
		for(AIListener listener : getAIListener()){
			if(event == null)
				event = new ActionEvent(id, data);
			listener.newAction(event);
		}
	}
	
	public AIHandler() {

	}

	public static String[] getMobsMovements(WindowGameData data) {
		Mob mob = data.nextMob();
		while (mob != null) {
			ArrayList<Player> players = data.getNearPlayers(mob);
			if (!players.isEmpty()) {

			}
			mob = data.nextMob();
		}

		return null;
	}

	public void loadAction(Character currentCharacter) {
		//TODO mettre en place le systeme de recolte d'action et les faire dans l'ordre
		
		//FOR DEBUG !!
		new ActionEvent(currentCharacter.getId(), "m:10:10");

	}

	public void run() {
		// TODO Auto-generated method stub

	}
}
