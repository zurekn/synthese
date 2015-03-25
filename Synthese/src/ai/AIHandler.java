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

	public AIHandler() {

	}

	/**
	 * 
	 * @return the action for the current player turn
	 */
	public String getAction() {

		return "";
	}

	public void run() {
		// TODO Auto-generated method stub

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
