package ai;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;

import data.Handler;
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
public class AIHandler extends Handler {
	private static AIHandler aiHandler;
	private boolean start = false;
	private WindowGameData gameData = null;
	private Character character = null;

	private AIHandler() {
		super();
	}

	public static AIHandler getInstance() {
		if (aiHandler == null) {
			aiHandler = new AIHandler();
		}
		return aiHandler;
	}

	@Override
	public void begin() {
		System.out.println("Launch the AI Handler Thread");
		getThread().start();
	}

	public void run() {
		System.out.println("AIHandler : DANS LE RUN");
		CommandHandler commandHandler = CommandHandler.getInstance();
		while(true){
			commandHandler.waitLock();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if(start){
				AlphaBeta.getInstance().calculateNpcCommands(gameData, character);
				start=false;
			}
		}
	}
	
	public void startCommandsCalculation(WindowGameData gameData, Character c){
		this.gameData = gameData;
		this.character = c;
		start = true;		
	}

	/*public static String[] getMobsMovements(WindowGameData data) {
		Mob mob = data.nextMob();
		while (mob != null) {
			ArrayList<Player> players = data.getNearPlayers(mob);
			if (!players.isEmpty()) {

			}
			mob = data.nextMob();
		}

		return null;
	}*/
}
