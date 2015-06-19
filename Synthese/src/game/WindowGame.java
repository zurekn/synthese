package game;

import imageprocessing.APIX;
import imageprocessing.APIXAdapter;
import imageprocessing.MovementEvent;
import imageprocessing.QRCodeEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import ai.AStar;
import ai.CharacterData;
import ai.CommandHandler;
import ai.CommandListener;
import ai.ActionEvent;
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
	private Thread thread;
	private CommandHandler commands;
	private GameHandler handler;

	private GameContainer container;
	private MobHandler mobHandler;
	private ArrayList<Mob> mobs;
	private PlayerHandler playerHandler;
	private ArrayList<Player> players;
	private MovementHandler movementHandler;
	private ArrayList<Event> events = new ArrayList<Event>();
	private ArrayList<Trap> traps = new ArrayList<Trap>();
	private Character previousCharacter = null;
	private Character currentCharacter;
	private ArrayList<int[]> reachableBlock = new ArrayList<int[]>();

	private int playerNumber;
	private int turn;

	private boolean gameOn = false;
	private int timerInitPlayer;

	private int turnTimer;
	private long timeStamp = -1;
	private long eventTimer = -1;

	private static WindowGame windowGame = null;

	public static WindowGame getInstance() {
		if (windowGame == null)
			try {
				windowGame = new WindowGame();
			} catch (SlickException e) {
				e.printStackTrace();
			}
		return windowGame;
	}

	private WindowGame() throws SlickException {
		super(Data.NAME);
	}

	private WindowGame(String title, GameContainer container, MobHandler mobHandler, ArrayList<Mob> mobs, game.PlayerHandler playerHandler,
			ArrayList<Player> players, MovementHandler movementHandler, ArrayList<Event> events, Character currentCharacter, int playerNumber,
			int turn, int turnTimer, long timeStamp) {
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
		thread = Thread.currentThread();
		handler = new GameHandler(thread);

		Data.loadGame();
		Data.loadMap();
		SpellData.loadSpell();
		MonsterData.loadMonster();
		HeroData.loadHeros();
		// TrapData.loadTrap();
		Data.loadMap();

		initAPIX();
		initCommandHandler();

		// Create the monster list
		mobs = MonsterData.initMobs();
		mobHandler = new MobHandler(mobs);

		// Create the player list
		initPlayers();

		playerHandler = new PlayerHandler(players);

		new Thread(movementHandler).start();
		// Set the timer
		timerInitPlayer = Data.INIT_MAX_TIME;

		// start();
	}

	private void start() {
		playerNumber = players.size() + mobs.size();

		turnTimer = Data.TURN_MAX_TIME;
		turn = 0;
		players.get(turn).setMyTurn(true);
		currentCharacter = players.get(turn);

		reachableBlock = AStar.getInstance().getReachableNodes(new WindowGameData(players, mobs, turn), new CharacterData(currentCharacter));
		gameOn = true;
	}

	@SuppressWarnings("rawtypes")
	public void initPlayers() {

		players = new ArrayList<Player>();

		Collection<String> pos = Data.departureBlocks.keySet();
		Iterator it = pos.iterator();
		String var;
		String[] s;

		if (Data.debug) {
			int i = 0;
			/*
			 * while (it.hasNext() && i < Data.DEBUG_PLAYER) { var = (String)
			 * it.next(); addPlayer(var); i++; }
			 */
			// TODO test add chalenger
			try {
				if(Data.DEBUG_PLAYER > 0)
					addChalenger(10, 12);
				// players.add(new Player(10, 12, "P0", "mage"));
				if (Data.DEBUG_PLAYER > 1)
					addChalenger(15, 15);
				if (Data.DEBUG_PLAYER > 2)
					addChalenger(16, 15);
				if (Data.DEBUG_PLAYER > 3)
					addChalenger(7, 12);
			} catch (IllegalCaracterClassException e) {
				e.printStackTrace();
			} catch (IllegalMovementException e) {
				e.printStackTrace();
			} catch (IllegalActionException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Add a new player
	 * 
	 * @param x
	 * @param y
	 * @throws IllegalCaracterClassException
	 * @throws IllegalMovementException
	 * @throws IllegalActionException
	 */
	public void addChalenger(int x, int y) throws IllegalCaracterClassException, IllegalMovementException, IllegalActionException {
		if (gameOn)
			throw new IllegalActionException("Can not add player when game is on!");

		String position = x + ":" + y;

		if (WindowGame.getInstance().getAllPositions().contains(position)) {
			throw new IllegalMovementException("Caracter already at the position [" + position + "]");
		}

		if (Data.untraversableBlocks.containsKey(position))
			throw new IllegalMovementException("Untraversable block at [" + position + "]");
		//TODO ajout du message erreur dans renderText

		if (Data.MAX_PLAYER <= players.size())
			return;
		String id = "P" + players.size();
		String type = HeroData.getRandomHero();
		players.add(new Player(x, y, id, type));
		timerInitPlayer = Data.INIT_MAX_TIME;
		if (players.size() >= Data.MAX_PLAYER) {
			System.out.println(" ----Max player reached ----");
			start();
		}
	}

	public void addPlayer(String position) {
		if (!Data.departureBlocks.get(position)) {
			String[] s = position.split(":");
			try {
				players.add(new Player(Integer.parseInt(s[0]), Integer.parseInt(s[1]), "P" + players.size(), "mage"));
				Data.departureBlocks.remove(position);
				Data.departureBlocks.put(position, true);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalCaracterClassException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	public void initCommandHandler() {
		commands = CommandHandler.getInstance();
		commands.addCommandListener(new CommandListener() {

			public void newAction(ActionEvent e) {
				System.out.println("Nouvelle action recup de CommandHandler  : " + e.toString());
				try {
					decodeAction(e.getEvent());
				} catch (IllegalActionException e1) {
					System.err.println(e1.getLocalizedMessage());
				}
			}
		});
		commands.begin();
	}

	public void initAPIX() {
		apix = APIX.getInstance();
		if (!Data.RUN_APIX)
			return;

		movementHandler = new MovementHandler(this);

		apix.addAPIXListener(new APIXAdapter() {
			@Override
			public void newQRCode(QRCodeEvent e) {
				System.out.println("Un nouveau QRCode vien d'être recupèrer par WindowGame [" + e.toString() + "]");
				try {
					decodeAction(e.getId() + ":" + e.getDirection());
				} catch (IllegalActionException e1) {
					System.err.println(e1.getLocalizedMessage());
				}
			}

			public void newMouvement(MovementEvent e) {
				System.out.println("Une nouvelle position  vient d'être récupèrée par WindowGame [" + e.toString() + "], position sur le plateau ["
						+ e.getX() / apix.getBlockSizeX() + ":" + e.getY() / apix.getBlockSizeY() + "]");
				try {
					if (gameOn)
						decodeAction("m:" + (e.getX() / apix.getBlockSizeX()) + ":" + (e.getY() / apix.getBlockSizeY()));
					else
						addChalenger(e.getX() / apix.getBlockSizeX(), e.getY() / apix.getBlockSizeY());
				} catch (IllegalActionException e1) {
					System.err.println(e1.getLocalizedMessage());
				} catch (IllegalCaracterClassException e1) {
					System.err.println(e1.getLocalizedMessage());
				} catch (IllegalMovementException e1) {
					System.err.println(e1.getLocalizedMessage());
				}
			}
		});

		apix.begin();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

	}

	int i = 0;

	/**
	 * The render function Call all game's render
	 */
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.scale(Data.SCALE, Data.SCALE);
		if (!apix.isInit()) {
			g.setColor(Color.black);
			g.setBackground(Color.white);
			// TOP LEFT
			g.fillRect(Data.MAP_X - 20, Data.MAP_Y - 20, 40, 40);
			// TOP RIGHT
			g.fillRect(Data.MAP_X + Data.MAP_WIDTH - 20, Data.MAP_Y - 20, 40, 40);
			// //BOTTOM left
			g.fillRect(Data.MAP_X - 20, Data.MAP_Y + Data.MAP_HEIGHT - 20, 40, 40);
			i++;
			if (i > 60)
				apix.initTI();
			// Reset the timer unteal the paix init is over
			timerInitPlayer = Data.INIT_MAX_TIME;

		} else {
			Data.map.render(Data.MAP_X, Data.MAP_Y);

			if (gameOn) {
				mobHandler.render(container, g);
				renderDeckArea(container, g);
				playerHandler.render(container, g);
				renderReachableBlocks(container, g);
				renderEvents(container, g);
			} else {
				playerHandler.renderInitBlock(container, g);
			}
			playerHandler.renderPlayerStat(container, g);
			renderText(container, g);
		}
	}

	/**
	 * Display the reachables blocks of the current caracter
	 * 
	 * @param container
	 * @param g
	 */
	private void renderReachableBlocks(GameContainer container, Graphics g) {
		g.setColor(Data.BLOCK_REACHABLE_COLOR);
		for (int[] var : reachableBlock) {
			g.fillRect(Data.MAP_X + var[0] * Data.BLOCK_SIZE_X, Data.MAP_Y + var[1] * Data.BLOCK_SIZE_Y, Data.BLOCK_SIZE_X, Data.BLOCK_SIZE_Y);
		}
		g.setColor(Color.black);
	}

	/**
	 * Render the Deck Area
	 * 
	 * @param container
	 * @param g
	 */
	private void renderDeckArea(GameContainer container, Graphics g) {
		g.setColor(Color.magenta); // La coucleur a modifier
		// TOP
		g.fillRect(Data.MAP_X, Data.RELATIVE_Y_POS, Data.DECK_AREA_SIZE_X, Data.DECK_AREA_SIZE_Y);
		// BOTTOM
		g.fillRect(Data.MAP_X + Data.DECK_AREA_SIZE_X, Data.MAP_HEIGHT + Data.MAP_Y, Data.DECK_AREA_SIZE_X, Data.DECK_AREA_SIZE_Y);
		// LEFT
		g.fillRect(Data.MAP_X - Data.DECK_AREA_SIZE_Y, Data.MAP_Y + Data.MAP_WIDTH / 2, Data.DECK_AREA_SIZE_Y, Data.DECK_AREA_SIZE_X);
		// RIGHT
		g.fillRect(Data.MAP_X + Data.MAP_WIDTH, Data.DECK_AREA_SIZE_Y, Data.DECK_AREA_SIZE_Y, Data.DECK_AREA_SIZE_X);
		g.setColor(Color.white);
	}

	/**
	 * Render the Game Text
	 * 
	 * @param container
	 * @param g
	 */
	private void renderText(GameContainer container, Graphics g) {
		// render text
		g.setColor(Data.TEXT_COLOR);
		g.drawString(Data.MAIN_TEXT, 10, 20);
	}

	/**
	 * Render all events in the game
	 * 
	 * @param container
	 * @param g
	 */
	private void renderEvents(GameContainer container, Graphics g) {
		int x, y, xMin, yMin, xMax, yMax;
		xMin = Data.MAP_X;
		xMax = Data.MAP_X + Data.MAP_WIDTH;
		yMin = Data.MAP_Y;
		yMax = Data.MAP_Y + Data.BLOCK_NUMBER_Y * Data.BLOCK_SIZE_Y;
		for (int i = 0; i < events.size(); i++) {
			Event e = events.get(i);
			e.render(container, g);
			x = e.getX();
			y = e.getY();
			e.setRange(e.getRange() - 1);
			if (x < xMin || x > xMax || y < yMin || y > yMax || e.getRange() <= 1) {
				events.remove(i);
			}

		}
	}

	long eventTime = 0;

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		long time = System.currentTimeMillis();
		if (time - timeStamp > 1000) {
			if (gameOn) {
				turnTimer--;
				timeStamp = time;
				Data.MAIN_TEXT = Data.TURN_TEXT + turnTimer;
			} else {
				timerInitPlayer--;
				timeStamp = time;
				Data.MAIN_TEXT = Data.INIT_PLAYER_TEXT + timerInitPlayer;
			}
		}

		// Turn timer
		if (gameOn) {
			if (turnTimer < 0) {
				switchTurn();
			}
		} else {
			if (timerInitPlayer < 0)
				start();
		}

		// Update event
		/*
		 * if(time - eventTime > Data.REFRESH_TIME_EVENT){ int x, y, xMin, yMin,
		 * xMax, yMax; xMin = Data.MAP_X; xMax = Data.MAP_X + Data.MAP_WIDTH;
		 * yMin = Data.MAP_Y; yMax = Data.MAP_Y + Data.BLOCK_NUMBER_Y *
		 * Data.BLOCK_SIZE_Y; for (Event e : events) { x = e.getX(); y =
		 * e.getY(); e.setRange(e.getRange() - 1); if (x < xMin || x > xMax || y
		 * < yMin || y > yMax || e.getRange() < 1) { events.remove(i); } } }
		 */
	}

	/**
	 * Function call for switch the current character turn
	 */
	public void switchTurn() {
		// Reset the timer
		System.out.println("turn = " + turn + ", playerNumber = " + playerNumber + ", turnTimer = " + turnTimer);
		turnTimer = Data.TURN_MAX_TIME;
		turn = (turn + 1) % playerNumber;

		previousCharacter = currentCharacter;
		previousCharacter.regenMana();
		// Switch the turn
		// Set the new character turn
		if (turn < players.size()) {
			players.get(turn).setMyTurn(true);
			currentCharacter = players.get(turn);
		} else {
			mobs.get(turn - players.size()).setMyTurn(true);
			currentCharacter = mobs.get(turn - players.size());
		}

		// set to false the previous character turn
		previousCharacter.setMyTurn(false);
		if (currentCharacter.isMonster() && !Data.SHOW_MOB_REACHABLE_BLOCKS)
			reachableBlock = new ArrayList<int[]>();
		else
			reachableBlock = AStar.getInstance().getReachableNodes(new WindowGameData(players, mobs, turn), new CharacterData(currentCharacter));

		if (currentCharacter.isNpc() && !previousCharacter.isNpc())
			// launch the new action loader only if it's there's no npc before
			// it (reduce calculation in AIHandler)
			// TODO
			commands.startCommandsCalculation(currentCharacter, players, mobs, turn);

		// print the current turn in the console
		if (Data.debug) {
			System.out.println("========================");
			if (turn < players.size()) {
				System.out.println("Tour du Joueur " + turn);
				System.out.println("Player : " + players.get(turn).toString());
			} else {
				System.out.println("Tour du Monster" + (turn - players.size()));
				System.out.println("Monster " + mobs.get(turn - players.size()).toString());
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
				throw new IllegalActionException("Wrong number of arguments in action string");

			String spellID = tokens[0].split("\n")[0];
			int direction = Integer.parseInt(tokens[1]);

			if (currentCharacter.getSpell(spellID) == null)
				throw new IllegalActionException("Spell [" + spellID + "] not found");

			Event e = currentCharacter.getSpell(spellID).getEvent().getCopiedEvent();

			e.setDirection(direction);
			e.setX(Data.MAP_X + currentCharacter.getX() * Data.BLOCK_SIZE_X);
			e.setY(Data.MAP_Y + currentCharacter.getY() * Data.BLOCK_SIZE_Y);
			// Get the range to the next character to hit
			Focus focus = getFirstCharacterRange(getCharacterPositionOnLine(currentCharacter.getX(), currentCharacter.getY(), e.getDirection()), e);
			System.out.println("get focus : " + focus.toString());
			int r = focus.range > e.getRange() ? e.getRange() : focus.range;
			e.setRange(r);

			try {
				currentCharacter.useSpell(spellID, direction);
				if (focus.character != null) {
					if (currentCharacter.isMonster() == focus.character.isMonster())
						if (e.getHeal() > 0)
							focus.character.heal(e.getHeal());
						else
							focus.character.takeDamage(e.getDamage(), e.getType());
					else
						focus.character.takeDamage(e.getDamage(), e.getType());
					if (focus.character.checkDeath()) {
						// TODO ADD a textual event
						System.out.println("-----------------------------------------");
						System.out.println("DEATH FOR" + focus.character.toString());
						System.out.println("-----------------------------------------");
						players.remove(focus.character);
						;
						mobs.remove(focus.character);
						playerNumber--;
					}
				}
				events.add(e);
				System.out.println("Created " + e.toString());
			} catch (IllegalActionException iae) {
				iae.printStackTrace();
			}

		}

		else if (action.startsWith("t")) { // Trap action
			System.out.println("Find a trap action");
		}

		else if (action.startsWith("m")) {// Movement action
			try {
				String[] tokens = action.split(":");
				if (tokens.length != 3)
					throw new IllegalActionException("Wrong number of arguments in action string");

				String position = tokens[1] + ":" + tokens[2];
				// TODO call aStar and check if character don't fall into trap
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
	private Focus getFirstCharacterRange(ArrayList<Character> chars, Event e) {
		int range = Data.MAX_RANGE;
		System.out.println("Search the first character range : " + e.toString() + ", " + chars.toString());
		Character focus = null;
		for (Character c : chars) {
			if (e.getDirection() == Data.NORTH || e.getDirection() == Data.SOUTH) {
				int i = (Math.abs(c.getY() - (e.getYOnBoard() - 1))) + 1;
				System.out.println("c.getY() = [" + c.getY() + "], e.getXOnBoard = [" + (e.getYOnBoard() - 1) + "], i = [" + i + "]");

				if (i < range) {
					range = i;
					focus = c;
				}
			}
			if (e.getDirection() == Data.EAST || e.getDirection() == Data.WEST) {
				System.out.println("c.getX() = [" + c.getX() + "], e.getXOnBoard = [" + (e.getXOnBoard() - 1) + "], i = ["
						+ (c.getX() - e.getXOnBoard() - 1) + "]");
				int i = (Math.abs(c.getX() - (e.getXOnBoard() - 1))) + 1;

				if (i < range) {
					range = i;
					focus = c;
				}
			}
		}
		if (Data.debug)
			System.out.println("The Range is : " + range + ", focus is " + focus.toString());
		return new Focus(range, focus);
	}

	@Override
	public void keyReleased(int key, char c) {
		if (Data.debug) {
			if (gameOn)
				if (!currentCharacter.isNpc()) {
					System.out.println("WindowGame, keyReleased : " + key + ", char : " + c);
					try {
						if (Input.KEY_LEFT == key)
							decodeAction("m:" + (currentCharacter.getX() - 1) + ":" + currentCharacter.getY());
						if (Input.KEY_RIGHT == key)
							decodeAction("m:" + (currentCharacter.getX() + 1) + ":" + currentCharacter.getY());
						if (Input.KEY_UP == key)
							decodeAction("m:" + currentCharacter.getX() + ":" + (currentCharacter.getY() - 1));
						if (Input.KEY_DOWN == key)
							decodeAction("m:" + currentCharacter.getX() + ":" + (currentCharacter.getY() + 1));
						if (Input.KEY_NUMPAD8 == key)
							decodeAction("s8:" + Data.NORTH);
						if (Input.KEY_NUMPAD6 == key)
							decodeAction("s5:" + Data.EAST);
						if (Input.KEY_NUMPAD2 == key)
							decodeAction("s6:" + Data.SOUTH);
						if (Input.KEY_NUMPAD4 == key)
							decodeAction("s7:" + Data.WEST);
					} catch (IllegalActionException e) {
						// TODO Auto-generated catch block
						System.err.println("Yolo" +e.getMessage());
					}
				}
		}
		if (Input.KEY_SUBTRACT == key) {
			start();
		}
		if (Input.KEY_ADD == key) {
			try {
				Random rand = new Random();
				int x = rand.nextInt(Data.BLOCK_NUMBER_X - 0) + 0;
				int y = rand.nextInt(Data.BLOCK_NUMBER_Y - 0) + 0;
				addChalenger(x, y);
			} catch (IllegalCaracterClassException e) {
				e.printStackTrace();
			} catch (IllegalMovementException e) {
				e.printStackTrace();
			} catch (IllegalActionException e) {
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
	@Deprecated
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
	public ArrayList<String> getAllPositions() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < players.size(); i++)
			list.add(players.get(i).getX() + ":" + players.get(i).getY());
		for (int i = 0; i < mobs.size(); i++)
			list.add(mobs.get(i).getX() + ":" + mobs.get(i).getY());
		return list;
	}

	public ArrayList<String> getAllTraps() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < traps.size(); i++)
			list.add(traps.get(i).getX() + ":" + traps.get(i).getY());
		return list;
	}

	public ArrayList<Mob> getMobs() {
		return mobs;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	/**
	 * Get all character on a line line = Horizontal or Vertical
	 * 
	 * @param x
	 * @param y
	 * @param direction
	 * @return ArrayList<Character>
	 */
	private ArrayList<Character> getCharacterPositionOnLine(int x, int y, int direction) {

		ArrayList<Character> c = new ArrayList<Character>();

		for (int i = 0; i < players.size(); i++) {
			// above
			if (direction == Data.NORTH && players.get(i).getY() < y && players.get(i).getX() == x)
				c.add(mobs.get(i));
			// bottom
			if (direction == Data.SOUTH && players.get(i).getY() > y && players.get(i).getX() == x)
				c.add(mobs.get(i));
			// on left
			if (direction == Data.EAST && players.get(i).getY() == y && players.get(i).getX() > x)
				c.add(mobs.get(i));
			// on right
			if (direction == Data.WEST && players.get(i).getY() == y && players.get(i).getX() < x)
				c.add(mobs.get(i));
		}

		for (int i = 0; i < mobs.size(); i++) {
			// above
			if (direction == Data.NORTH && mobs.get(i).getY() < y && mobs.get(i).getX() == x)
				c.add(mobs.get(i));
			// bottom
			if (direction == Data.SOUTH && mobs.get(i).getY() > y && mobs.get(i).getX() == x)
				c.add(mobs.get(i));
			// on left
			if (direction == Data.EAST && mobs.get(i).getY() == y && mobs.get(i).getX() > x)
				c.add(mobs.get(i));
			// on right
			if (direction == Data.WEST && mobs.get(i).getY() == y && mobs.get(i).getX() < x)
				c.add(mobs.get(i));
		}
		System.out.println("getCharacterePositionOnLine From [" + x + ", " + y + "], Found" + c.toString());
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

	public GameHandler getHandler() {
		return handler;
	}

	private class Focus {
		protected int range;
		protected Character character;

		public Focus(int range, Character character) {
			this.range = range;
			this.character = character;
		}

		public String toString() {
			return "Focus [ range, " + range + ", " + character.toString() + "]";
		}
	}
}
