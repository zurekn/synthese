package game;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import data.*;
import exception.IllegalActionException;
import exception.IllegalMovementException;

/**
 * Main class whith handle the game Contain the init method for creating all
 * game objects
 * 
 * @author bob
 *
 */
public class WindowGame extends BasicGame {

	private GameContainer container;
	private MobHandler mobHandler;
	private ArrayList<Mob> mobs;
	private playerHandler playerHandler;
	private ArrayList<Player> players;
	private MovementHandler movementHandler;
	private ArrayList<Event> events = new ArrayList<Event>();
	//TODO add traps
	private Character currentCharacter;

	private int playerNumber;
	private int turn;

	private int turnTimer;
	private long timeStamp = -1;

	public WindowGame() throws SlickException {
		super(Data.NAME);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		this.container = container;
		Data.loadGame();
		SpellData.loadSpell();
		MonsterData.loadMob();
		// Data.initMapXML();

		movementHandler = new MovementHandler(this);

		players = new ArrayList<Player>();
		players.add(new Player(16, 14, "P1", new Stats(100, 10, 50, 10, 10, 10,
				3, 10)));
		playerHandler = new playerHandler(players);

		mobs = MonsterData.initMobs();
		mobHandler = new MobHandler(mobs);

		playerNumber = 1 + mobs.size();

		turnTimer = Data.TURN_MAX_TIME;
		new Thread(movementHandler).start();
		turn = 0;
		players.get(turn).setMyTurn(true);
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		g.scale(Data.SCALE, Data.SCALE);
		Data.map.render(Data.DECK_AREA_SIZE_Y, Data.DECK_AREA_SIZE_Y);
		mobHandler.render(container, g);
		// TODO
		// Bug playerrender doesn't work every time
		playerHandler.render(container, g);
		renderEvents(container, g);
		renderDeckArea(container, g);
		renderText(container, g);
	}

	private void renderDeckArea(GameContainer container, Graphics g) {
		g.setColor(Color.white);
		// TOP
		g.drawRect(Data.DECK_AREA_SIZE_Y, 0, Data.DECK_AREA_SIZE_X,
				Data.DECK_AREA_SIZE_Y);
		// BOTTOM
		g.drawRect(Data.DECK_AREA_SIZE_Y, Data.DECK_AREA_SIZE_Y
				+ Data.DECK_AREA_SIZE_X, Data.DECK_AREA_SIZE_X,
				Data.DECK_AREA_SIZE_Y);
		// LEFT
		g.drawRect(0, Data.DECK_AREA_SIZE_Y, Data.DECK_AREA_SIZE_Y,
				Data.DECK_AREA_SIZE_X);
		// RIGHT
		g.drawRect(Data.DECK_AREA_SIZE_X + Data.DECK_AREA_SIZE_Y,
				Data.DECK_AREA_SIZE_Y, Data.DECK_AREA_SIZE_Y,
				Data.DECK_AREA_SIZE_X);
	}

	private void renderText(GameContainer container, Graphics g) {
		// render text
		g.setColor(Color.white);
		g.drawString("End of turn in : " + turnTimer, 10, 20);
	}

	private void renderEvents(GameContainer container, Graphics g) {
		for (int i = 0; i < events.size(); i++) {
			events.get(i).render(container, g);
		}
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		if (System.currentTimeMillis() - timeStamp > 1000) {
			turnTimer--;
			timeStamp = System.currentTimeMillis();
		}

		// Turn timer
		if (turnTimer < 0) {
			switchTurn();

		}
	}

	public void switchTurn() {
		// Reset the timer
		turnTimer = Data.TURN_MAX_TIME;
		turn = (turn + 1) % playerNumber;

		// Switch the turn
		// Set the new character turn
		if (turn < players.size()) {
			players.get(turn).setMyTurn(true);
			currentCharacter = players.get(turn);
		} else {
			mobs.get(turn - players.size()).setMyTurn(true);
			currentCharacter = mobs.get(turn);
		}

		// set to false the previous character turn
		if (turn == 0) {
			mobs.get(mobs.size() - 1).setMyTurn(false);
		} else {
			if (turn <= players.size()) {
				players.get(turn - 1).setMyTurn(false);
			} else {
				mobs.get(turn - players.size() - 1).setMyTurn(false);
			}
		}

		// print the current turn in the console
		if (Data.debug) {
			System.out.println("========================");
			if (turn < players.size()) {
				System.out.println("Tour du Joueur " + turn);
				System.out.println("Player : " + players.get(turn).toString());
			} else {
				System.out.println("Tour du Monster" + (turn - players.size()));
				System.out.println("Monster "
						+ mobs.get(turn - players.size()).toString());
			}
			System.out.println("========================");
		}

	}


	/**
	 * decode a action and create associated event
	 * 
	 * @param action
	 *            , a string which defines the action
	 * @throws IllegalMovementException
	 */
	public void decodeAction(String action) throws IllegalActionException {
		if (action.startsWith("s")) { // Spell action
			String[] tokens = action.split(":");
			if (tokens.length != 2)
				throw new IllegalActionException(
						"Wrong number of arguments in action string");

			String spellID = tokens[0];
			int direction = Integer.parseInt(tokens[1]);
			currentCharacter.useSpell(spellID, direction);
			events.add(currentCharacter.getSpell(spellID).getEvent());
		}

		else if (action.startsWith("t")) { // Trap action
			System.out.println("Find a trap action");
		} 
		
		else if (action.startsWith("m")) {
			try {
				String[] tokens = action.split(":");
				if (tokens.length != 3)
					throw new IllegalActionException(
							"Wrong number of arguments in action string");
				String id = tokens[0];
				if(!currentCharacter.getId().equals(id))
					throw new IllegalActionException("Not your turn, try again later.");
					
				String position = tokens[1] + ":" + tokens[2];
				currentCharacter.moveTo(position);
				switchTurn();
			} catch (IllegalMovementException ime) {
				throw new IllegalActionException("Mob can't reach this block");
			}
		} else {
			throw new IllegalActionException("Action not found : " + action);
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		System.out
				.println("WindowGame, keyReleased : " + key + ", char : " + c);
		if (Input.KEY_ESCAPE == key) {
			container.exit();
		}

		if (Input.KEY_P == key) {
			try {
				decodeAction("s1:1:1");
			} catch (IllegalActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void move(String str) {
		System.out.println("WindowGame get new movement : " + str);
		if (turn < players.size()) {
			try {
				players.get(turn).moveTo(str);
			} catch (IllegalMovementException e) {
				e.printStackTrace();
			}
		} else {
			try {
				mobs.get(turn - players.size()).moveTo(str);
			} catch (IllegalMovementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public Mob getMobById(String id) {
		for (int i = 0; i < mobs.size(); i++) {
			if (mobs.get(i).getId().equals(id)) {
				return mobs.get(i);
			}
		}
		return null;
	}
}
