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
		this.lock();
		PositionHandler.getInstance().begin();
		for (int j = 0; j < 1; j++) {
			try {
				Thread.sleep(400);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			unlockTemporay(1);

			for (int i = 0; i < 10; i++) {
				System.out.println("AI :" + i);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * 
	 * @return the action for the current player turn
	 */
	public String getAction() {

		return "";
	}

	private final EventListenerList listeners = new EventListenerList();

	public void addAIListener(AIListener listener) {
		listeners.add(AIListener.class, listener);
	}

	public void removeAIListener(AIListener listener) {
		listeners.remove(AIListener.class, listener);
	}

	public AIListener[] getAIListener() {
		return listeners.getListeners(AIListener.class);
	}

	protected void newAction(String id, String data) {
		ActionEvent event = null;
		for (AIListener listener : getAIListener()) {
			if (event == null)
				event = new ActionEvent(id, data);
			listener.newAction(event);
		}
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

	public void loadAction(Character currentCharacter, ArrayList<Player> players, ArrayList<Mob> mobs, int turn) {
		// TODO mettre en place le systeme de recolte d'action et les faire dans
		// l'ordre
		Character c = WindowGame.getInstance().getMobById("m1");
		AlphaBeta.getInstance().getNpcCommand(new WindowGameData(players, mobs, turn), c);

		// FOR DEBUG !!
		newAction(currentCharacter.getId(), "m:10:10");

	}
}
