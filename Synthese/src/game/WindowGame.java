package game;

import imageprocessing.APIX;
import imageprocessing.APIXListener;
import imageprocessing.QRCodeAdapter;
import imageprocessing.QRCodeEvent;

import java.awt.List;
import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import ai.AIHandler;
import ai.WindowGameData;
import data.*;
import exception.IllegalActionException;
import exception.IllegalCaracterClassException;
import exception.IllegalMovementException;

/**
 * Main class which handle the game Contain the init method for creating all
 * game objects
 * 
 * @author bob
 *
 */
public class WindowGame extends BasicGame {

	private APIX apix;

	private GameContainer container;
	private MobHandler mobHandler;
	private ArrayList<Mob> mobs;
	private playerHandler playerHandler;
	private ArrayList<Player> players;
	private MovementHandler movementHandler;
	private ArrayList<Event> events = new ArrayList<Event>();
	// TODO add traps
	private Character currentCharacter;

	private int playerNumber;
	private int turn;

	private int turnTimer;
	private long timeStamp = -1;
	private long eventTimer = -1;

	public static WindowGame windowGame;

	public WindowGame() throws SlickException {
		super(Data.NAME);
		windowGame = this;
	}

	private WindowGame(String title, GameContainer container,
			MobHandler mobHandler, ArrayList<Mob> mobs,
			game.playerHandler playerHandler, ArrayList<Player> players,
			MovementHandler movementHandler, ArrayList<Event> events,
			Character currentCharacter, int playerNumber, int turn,
			int turnTimer, long timeStamp) {
		super(title);
		this.container = container;
		this.mobHandler = mobHandler;
		this.mobs = mobs;
		this.playerHandler = playerHandler;
		this.players = players;
		this.movementHandler = movementHandler;
		this.events = events;
		this.currentCharacter = currentCharacter;
		this.playerNumber = playerNumber;
		this.turn = turn;
		this.turnTimer = turnTimer;
		this.timeStamp = timeStamp;
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		this.container = container;

		Data.loadGame();
		SpellData.loadSpell();
		MonsterData.loadMonster();
		HeroData.loadHeros();
		TrapData.loadTrap();
		Data.loadMap();

		initAPIX();
		
		// Create the player list
		players = new ArrayList<Player>();
		try {
			players.add(new Player(16, 14, "P1", "mage"));
		} catch (IllegalCaracterClassException e) {
			e.printStackTrace();
			System.exit(1);
		}
		playerHandler = new playerHandler(players);

		// Create the monster list
		mobs = MonsterData.initMobs();
		mobHandler = new MobHandler(mobs);

		playerNumber = 1 + mobs.size();

		turnTimer = Data.TURN_MAX_TIME;
		new Thread(movementHandler).start();
		turn = 0;
		players.get(turn).setMyTurn(true);
		currentCharacter = players.get(turn);
	}

	public void initAPIX() {
		apix = new APIX();

		movementHandler = new MovementHandler(this);
		apix.addAPIXListener(new QRCodeAdapter() {
			@Override
			public void newQRCode(QRCodeEvent e) {
				System.out.println("Un nouveau QRCode vien d'�tre recup�rer par WindowGame ["+ e.getData() + "]");
			}
		});
	}

	/**
	 * The render function Call all game's render
	 */
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

	/**
	 * Render the Deck Area
	 * 
	 * @param container
	 * @param g
	 */
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

	/**
	 * Render the Game Text
	 * 
	 * @param container
	 * @param g
	 */
	private void renderText(GameContainer container, Graphics g) {
		// render text
		g.setColor(Color.white);
		g.drawString("End of turn in : " + turnTimer, 10, 20);
	}

	/**
	 * Render all events in the game
	 * 
	 * @param container
	 * @param g
	 */
	private void renderEvents(GameContainer container, Graphics g) {
		int x, y, xMin, yMin, xMax, yMax;
		xMin = Data.RELATIVE_X_POS;
		xMax = Data.RELATIVE_X_POS + Data.BLOCK_NUMBER_X * Data.BLOCK_SIZE_X;
		yMin = Data.RELATIVE_Y_POS;
		yMax = Data.RELATIVE_Y_POS + Data.BLOCK_NUMBER_Y * Data.BLOCK_SIZE_Y;
		for (int i = 0; i < events.size(); i++) {
			Event e = events.get(i);
			e.render(container, g);
			x = e.getX();
			y = e.getY();
			e.setRange(e.getRange() - 1);
			if (x < xMin || x > xMax || y < yMin || y > yMax
					|| e.getRange() <= 0) {
				events.remove(i);
			}

		}
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		long time = System.currentTimeMillis();
		if (time - timeStamp > 1000) {
			turnTimer--;
			timeStamp = time;
		}

		// Turn timer
		if (turnTimer < 0) {
			switchTurn();
		}
	}

	/**
	 * Function call for switch the current character turn
	 */
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
			currentCharacter = mobs.get(turn - players.size());
			String[] commands = AIHandler.getMobsMovements(new WindowGameData(
					mobs, players, currentCharacter, playerNumber, turn));

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

			if (currentCharacter.getSpell(spellID) == null)
				throw new IllegalActionException("Spell " + spellID
						+ " not found");

			Event e = currentCharacter.getSpell(spellID).getEvent()
					.getCopiedEvent();

			e.setDirection(direction);
			e.setX(Data.RELATIVE_X_POS + currentCharacter.getX()
					* Data.BLOCK_SIZE_X);
			e.setY(Data.RELATIVE_Y_POS + currentCharacter.getY()
					* Data.BLOCK_SIZE_Y);
			// Get the range to the next character to hit
			int r = getFirstCharacterRange(
					getCharacterePositionOnLine(currentCharacter.getX(),
							currentCharacter.getY(), e.getDirection()), e);
			r = r > e.getRange() ? e.getRange() : r;
			e.setRange(r);
			events.add(e);

			currentCharacter.useSpell(spellID, direction);
		}

		else if (action.startsWith("t")) { // Trap action
			System.out.println("Find a trap action");
		}

		else if (action.startsWith("m")) {// Monster movement action
			try {
				String[] tokens = action.split(":");
				if (tokens.length != 3)
					throw new IllegalActionException(
							"Wrong number of arguments in action string");
				String id = tokens[0];
				/*
				 * if (!currentCharacter.getId().equals(id)) throw new
				 * IllegalActionException( "Not your turn, try again later.");
				 */
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

	/**
	 * Return the distance between the currentCharacter and the closer mob
	 * 
	 * @param chars
	 * @param e
	 * @return
	 */
	private int getFirstCharacterRange(ArrayList<Character> chars, Event e) {
		int range = Data.MAX_RANGE;
		System.out
				.println("Search the first character range : " + e.toString());

		for (Character c : chars) {
			if (e.getDirection() == Data.NORTH
					|| e.getDirection() == Data.SOUTH) {
				int i = (Math.abs(c.getY() - e.getYOntBoard()));
				if (i < range)
					range = i;
			}
			if (e.getDirection() == Data.EAST || e.getDirection() == Data.WEST) {
				int i = (Math.abs(c.getX() - e.getXOnBoard()));
				if (i < range)
					range = i;
			}
		}
		if (Data.debug)
			System.out.println("The Range is : " + range);
		return range;
	}

	@Override
	public void keyReleased(int key, char c) {
		if (Data.debug) {
			System.out.println("WindowGame, keyReleased : " + key + ", char : "
					+ c);
			try {
				if (Input.KEY_LEFT == key)
					decodeAction("m:" + (currentCharacter.getX() - 1) + ":"
							+ currentCharacter.getY());
				if (Input.KEY_RIGHT == key)
					decodeAction("m:" + (currentCharacter.getX() + 1) + ":"
							+ currentCharacter.getY());
				if (Input.KEY_UP == key)
					decodeAction("m:" + currentCharacter.getX() + ":"
							+ (currentCharacter.getY() - 1));
				if (Input.KEY_DOWN == key)
					decodeAction("m:" + currentCharacter.getX() + ":"
							+ (currentCharacter.getY() + 1));
				if (Input.KEY_NUMPAD8 == key)
					decodeAction("s1:" + Data.NORTH);
				if (Input.KEY_NUMPAD6 == key)
					decodeAction("s1:" + Data.EAST);
				if (Input.KEY_NUMPAD2 == key)
					decodeAction("s1:" + Data.SOUTH);
				if (Input.KEY_NUMPAD4 == key)
					decodeAction("s1:" + Data.WEST);
			} catch (IllegalActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (Input.KEY_ESCAPE == key) {
			container.exit();
		}
	}

	/**
	 * Move the current player to the destination x:y if possible
	 * 
	 * @param str
	 *            , x:y
	 */
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

	/**
	 * Get all the Character positions
	 * 
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getAllPosition() {
		System.out.println("Toto");
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < players.size(); i++)
			list.add(players.get(i).getX() + ":" + players.get(i).getY());
		for (int i = 0; i < mobs.size(); i++)
			list.add(mobs.get(i).getX() + ":" + mobs.get(i).getY());
		return list;
	}

	/**
	 * Get all character on a line line = Horizontal or Vertical
	 * 
	 * @param x
	 * @param y
	 * @param direction
	 * @return ArrayList<Character>
	 */
	private ArrayList<Character> getCharacterePositionOnLine(int x, int y,
			int direction) {
		System.out.println("[" + x + "" + y + "] Direction = " + direction
				+ ", North = " + Data.NORTH + ", South = " + Data.SOUTH
				+ ", East = " + Data.EAST + ", West = " + Data.WEST);
		ArrayList<Character> c = new ArrayList<Character>();

		for (int i = 0; i < players.size(); i++) {
			// above
			if (direction == Data.NORTH && players.get(i).getY() < y
					&& players.get(i).getX() == x)
				c.add(mobs.get(i));
			// bottom
			if (direction == Data.SOUTH && players.get(i).getY() > y
					&& players.get(i).getX() == x)
				c.add(mobs.get(i));
			// on left
			if (direction == Data.EAST && players.get(i).getY() == y
					&& players.get(i).getX() > x)
				c.add(mobs.get(i));
			// on right
			if (direction == Data.WEST && players.get(i).getY() == y
					&& players.get(i).getX() < x)
				c.add(mobs.get(i));
		}

		for (int i = 0; i < mobs.size(); i++) {
			// above
			if (direction == Data.NORTH && mobs.get(i).getY() < y
					&& mobs.get(i).getX() == x)
				c.add(mobs.get(i));
			// bottom
			if (direction == Data.SOUTH && mobs.get(i).getY() > y
					&& mobs.get(i).getX() == x)
				c.add(mobs.get(i));
			// on left
			if (direction == Data.EAST && mobs.get(i).getY() == y
					&& mobs.get(i).getX() > x)
				c.add(mobs.get(i));
			// on right
			if (direction == Data.WEST && mobs.get(i).getY() == y
					&& mobs.get(i).getX() < x)
				c.add(mobs.get(i));
		}
		System.out.println(c.toString());
		return c;
	}

	/**
	 * Return the Character with have the x,y position
	 * 
	 * @param x
	 * @param y
	 * @return Character
	 */
	private Character getCharacterByPosition(int x, int y) {
		for (int i = 0; i < players.size(); i++)
			if (players.get(i).getX() == x && players.get(i).getY() == y)
				return players.get(i);

		for (int i = 0; i < mobs.size(); i++)
			if (mobs.get(i).getX() == x && mobs.get(i).getY() == y)
				return mobs.get(i);

		return null;
	}

}
