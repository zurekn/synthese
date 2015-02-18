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
	private ArrayList<Player> player;
	private MovementHandler movementHandler;
	private ArrayList<Event> events = new ArrayList<Event>();

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
		Data.initMap();
		SpellData.initSpell();
		MonsterData.initMob();

		movementHandler = new MovementHandler(this);

		player = new ArrayList<Player>();
		player.add(new Player(16, 14, "P1", new Stats(100, 10, 50, 10, 10, 10,
				3, 10)));
		playerHandler = new playerHandler(player);

		mobs = Data.initMobs();
		mobHandler = new MobHandler(mobs);

		playerNumber = 1 + mobs.size();

		turnTimer = Data.TURN_MAX_TIME;
		new Thread(movementHandler).start();
		turn = 0;
		player.get(turn).setMyTurn(true);
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
		if (turn < player.size()) {
			player.get(turn).setMyTurn(true);
		} else {
			mobs.get(turn - player.size()).setMyTurn(true);
		}

		// set to false the previous character turn
		if (turn == 0) {
			mobs.get(mobs.size() - 1).setMyTurn(false);
		} else {
			if (turn <= player.size()) {
				player.get(turn - 1).setMyTurn(false);
			} else {
				mobs.get(turn - player.size() - 1).setMyTurn(false);
			}
		}

		// print the current turn in the console
		if (Data.debug) {
			System.out.println("========================");
			if (turn < player.size()) {
				System.out.println("Tour du Joueur " + turn);
				System.out.println("Player : " + player.get(turn).toString());
			} else {
				System.out.println("Tour du Monster" + (turn - player.size()));
				System.out.println("Monster "
						+ mobs.get(turn - player.size()).toString());
			}
			System.out.println("========================");
		}

	}

	/**
	 * decode a action and create associated event
	 * 
	 * @param s
	 *            , a string witch contains the action:x:y
	 * @throws IllegalMovementException
	 */
	public void pushAction(String s) throws IllegalMovementException {
		// decode the action
		String[] split = s.split(":");
		if (split.length != 3) {
			throw new IllegalMovementException("Invalid action synthaxe");
		}
		String action = split[0];
		int x = Integer.parseInt(split[1]);
		int y = Integer.parseInt(split[2]);

		decodeAction(action);
	}

	public void decodeAction(String action) {
		if (action.startsWith("s")) {
			System.out.println("Find a spell action");
		} else if (action.startsWith("t")) {
			System.out.println("Find a trap action");
		} else if (action.startsWith("a")) {
			System.out.println("Find a attack action");
		} else {
			System.err.println("Action not find : "+action);
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
				pushAction("s1:1:1");
			} catch (IllegalMovementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void move(String str) {
		System.out.println("WindowGame get new movement : " + str);
		if (turn < player.size()) {
			try {
				player.get(turn).moveTo(str);
			} catch (IllegalMovementException e) {
				e.printStackTrace();
			}
		} else {
			try {
				mobs.get(turn - player.size()).moveTo(str);
			} catch (IllegalMovementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
