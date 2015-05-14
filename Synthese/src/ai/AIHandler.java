package ai;

import java.util.ArrayList;

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

	private Thread thread ;
	private static AIHandler aiHandler;
	
	private AIHandler(){
		thread = new Thread(this);
	}

	/**
	 * 
	 * @return the action for the current player turn
	 */
	public String getAction() {

		return "";
	}

	public void run() {
		System.out.println("AIHandler : DANS LE RUN");
		PositionHandler.getInstance().begin();
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while(true){
			
		}

	}
	
	public Thread getThread(){
		return thread;
	}
	
	public static AIHandler getInstance(){
		if(aiHandler == null){
			aiHandler = new AIHandler();
		}
		return aiHandler;
	}
	
	public void begin() {
		System.out.println("Launch the AI Handler Thread");
		thread.start();
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
}
