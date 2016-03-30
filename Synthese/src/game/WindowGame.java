package game;

import imageprocessing.APIX;
import imageprocessing.APIXAdapter;
import imageprocessing.MovementEvent;
import imageprocessing.QRCodeEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import main.Main;

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
	private PlayerGeneticHandler genHandler;
	private ArrayList<Mob> mobs;
	private ArrayList<Mob> originMobs;
	private PlayerHandler playerHandler;
	private ArrayList<Player> players;
	private ArrayList<PlayerGenetic> genPlayers;
	private ArrayList<PlayerGenetic> originGenPlayers;
	private MovementHandler movementHandler;
	private MessageHandler messageHandler;
	private ArrayList<Event> events = new ArrayList<Event>();
	private ArrayList<Trap> traps = new ArrayList<Trap>();
	private Character previousCharacter = null;
	private Character currentCharacter;
	private ArrayList<int[]> reachableBlock = new ArrayList<int[]>();

	private int playerNumber;
	private int turn;
	private int global_turn;
	private int actionLeft = Data.ACTION_PER_TURN;

	public boolean gameOn = false;
	private boolean gameEnded = false;
	private boolean gameWin = false;
	private boolean gameLose = false;
	private int timerInitPlayer;

	private int turnTimer;
	private long timeStamp = -1;
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

	public WindowGame() throws SlickException {
		super(Data.NAME);
		windowGame = this;
	}

	private WindowGame(String title, GameContainer container,PlayerGeneticHandler genHandler,ArrayList<PlayerGenetic> genPlayers,MobHandler mobHandler, ArrayList<Mob> mobs, game.PlayerHandler playerHandler,
			ArrayList<Player> players, MovementHandler movementHandler, ArrayList<Event> events, Character currentCharacter, int playerNumber,
			int turn, int turnTimer, long timeStamp) {
		super(title);
		this.container = container;
		this.genHandler = genHandler;
		this.genPlayers = genPlayers;
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
		gameOn = false;
		this.container = container;
		thread = Thread.currentThread();
		handler = new GameHandler(thread);

		container.setMouseGrabbed(true);;
		Data.loadMap();
		Data.loadGame();
		SpellData.loadSpell();
		MonsterData.loadMonster();
		HeroData.loadHeros();
		// TrapData.loadTrap();
		initAPIX();
		initCommandHandler();
		// Create the monster list
		mobs = MonsterData.initMobs();
		mobHandler = new MobHandler(mobs);
		// créer une liste statique des mobs
		originMobs = new ArrayList<Mob>();
		for(Mob mo : mobs)
		{
			originMobs.add(mo);
		}
		
		messageHandler = new MessageHandler();

		// Create the player list
		players = new ArrayList<Player>();
		genPlayers = new ArrayList<PlayerGenetic>();
		originGenPlayers = new ArrayList<PlayerGenetic>();
		if(Data.autoIA)
		{
			initGeneticPlayers();
		}
		else
		{
			initPlayers();
			playerHandler = new PlayerHandler(players);
		}
		//new Thread(movementHandler).start();
		// Set the timer
		timerInitPlayer = Data.INIT_MAX_TIME;

		// start();
	}
	

	private void start() {
		genHandler = new PlayerGeneticHandler(genPlayers);
		if(Data.autoIA){
			for(PlayerGenetic pg : genPlayers)
			{
				originGenPlayers.add(pg);
			}
		}
		turnTimer = Data.TURN_MAX_TIME;
		global_turn = 1;
		turn = 0;
		if(!Data.autoIA)
		{
			players.get(turn).setMyTurn(true);
			playerNumber = players.size() + mobs.size();
			currentCharacter = players.get(turn);
			reachableBlock = AStar.getInstance().getReachableNodes(new WindowGameData(players, mobs, turn), new CharacterData(currentCharacter));
			
		}
		else
		{
			genPlayers.get(turn).setMyTurn(true);
			playerNumber = genPlayers.size() + mobs.size();
			currentCharacter = genPlayers.get(turn);
		}
		gameOn = true;
		//currentCharacter.findScriptAction(0);//Pour lancer l'action du premier joueur
	}
	
	public void initGeneticPlayers(){
		
		Collection<String> pos = Data.departureBlocks.keySet();
		pos.iterator();
		try {
			if (Data.DEBUG_PLAYER > 0)
			{
				addGeneticPlayer(10, 8, -1,"m5");
			}
			// players.add(new Player(10, 12, "P0", "mage"));
			if (Data.DEBUG_PLAYER > 1)
			{
				addGeneticPlayer(15, 15, -1, "m8");
			//	CompileString.compile("p1");
			}
			if (Data.DEBUG_PLAYER > 2)
			{
				addGeneticPlayer(19, 15, -1, "m7");
			//	CompileString.compile("p2");
			}
			if (Data.DEBUG_PLAYER > 3)
			{
				addGeneticPlayer(7, 12, -1, "m9");
			//	CompileString.compile("p3");
			}
			
		} catch (IllegalCaracterClassException e) {
			e.printStackTrace();
		} catch (IllegalMovementException e) {
			e.printStackTrace();
		} catch (IllegalActionException e) {
			e.printStackTrace();
		}
		
		
	}

	public void initPlayers() {

		

		Collection<String> pos = Data.departureBlocks.keySet();
		pos.iterator();
		if (Data.debug) {
			try {
				if (Data.DEBUG_PLAYER > 0)
				{
					addChalenger(10, 8, -1,Color.magenta);
				}
				// players.add(new Player(10, 12, "P0", "mage"));
				if (Data.DEBUG_PLAYER > 1)
				{
					addChalenger(15, 15, -1, Color.orange);
				//	CompileString.compile("p1");
				}
				if (Data.DEBUG_PLAYER > 2)
				{
					addChalenger(19, 15, -1, Color.blue);
				//	CompileString.compile("p2");
				}
				if (Data.DEBUG_PLAYER > 3)
				{
					addChalenger(7, 12, -1, Color.red);
				//	CompileString.compile("p3");
				}
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
	@SuppressWarnings("unused")
	public void addChalenger(int x, int y, int size) throws IllegalCaracterClassException, IllegalMovementException, IllegalActionException {
		if (gameOn)
			throw new IllegalActionException("Can not add player when game is on!");

		String position = x + ":" + y;

		if (WindowGame.getInstance().getAllPositions().contains(position)) {
			// messageHandler.addMessage(new
			// Message("Position ["+position+"] non disponible", 1));

			throw new IllegalMovementException("Caracter already at the position [" + position + "]");
		}

		if (Data.untraversableBlocks.containsKey(position)) {
			messageHandler.addGlobalMessage(new Message("Position [" + position + "] non disponible", 1));
			throw new IllegalMovementException("Untraversable block at [" + position + "]");
		}

		if (Data.departureBlocks.containsKey(position) || (Data.DEBUG_DEPARTURE && Data.debug)) {
			Data.departureBlocks.put(position, true);
		} else {
			messageHandler.addGlobalMessage(new Message(Data.DEPARTURE_BLOCK_ERROR, Data.MESSAGE_TYPE_ERROR));
			throw new IllegalMovementException("Caracter must be at a departure position");
		}

		if (Data.MAX_PLAYER <= players.size())
			return;
		String id = "P" + players.size();
		String type = HeroData.getRandomHero();

		Player p = new Player(x, y, id, type);
		/*		
		//---------------------	Ajout d'un IA génétique	--------------------------
		CompileString.compile(id);
		CompilerHandler ch = CompileString.CompileAndInstanciateClass(id);
		CompileString.InvokeInitPlayer(ch, x, y, id, type);
		
		CompileString.InvokeSetNumber(ch, players.size());
		CompileString.InvokeSetSizeCharacter(ch, size);
		*/
		p.setNumber(players.size());
		p.setSizeCharacter(size);
		players.add(p);

		timerInitPlayer = Data.INIT_MAX_TIME;
		if (players.size() >= Data.MAX_PLAYER) {
			System.out.println(" ----Max player reached ----");
			start();
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
	@SuppressWarnings("unused")
	public void addGeneticPlayer(int x, int y, int size, String id) throws IllegalCaracterClassException, IllegalMovementException, IllegalActionException {
		if (gameOn)
			throw new IllegalActionException("Can not add player genetic when game is on!");

		String position = x + ":" + y;

		if (WindowGame.getInstance().getAllPositions().contains(position)) {
			// messageHandler.addMessage(new
			// Message("Position ["+position+"] non disponible", 1));

			throw new IllegalMovementException("Caracter already at the position [" + position + "]");
		}

		if (Data.untraversableBlocks.containsKey(position)) {
			messageHandler.addGlobalMessage(new Message("Position [" + position + "] non disponible", 1));
			throw new IllegalMovementException("Untraversable block at [" + position + "]");
		}

		if (Data.departureBlocks.containsKey(position) || (Data.DEBUG_DEPARTURE && Data.debug)) {
			Data.departureBlocks.put(position, true);
		} else {
			messageHandler.addGlobalMessage(new Message(Data.DEPARTURE_BLOCK_ERROR, Data.MESSAGE_TYPE_ERROR));
			throw new IllegalMovementException("Caracter must be at a departure position");
		}

		if (Data.MAX_PLAYER <= genPlayers.size())
			return;
		//String id = "P" + players.size();
		PlayerGenetic p = new PlayerGenetic(x, y, id, "g"+genPlayers.size());
		//p.setNumber(players.size());
		//p.setSizeCharacter(size);
		genPlayers.add(p);

		timerInitPlayer = Data.INIT_MAX_TIME;
		if (genPlayers.size() >= Data.MAX_PLAYER) {
			System.out.println(" ----Max genetic player reached ----");
			start();
		}
	}

	

	/**
	 * Add a new player with color
	 * 
	 * @param x
	 * @param y
	 * @param color
	 * @throws IllegalCaracterClassException
	 * @throws IllegalMovementException
	 * @throws IllegalActionException
	 */
	@SuppressWarnings("unused")
	public void addChalenger(int x, int y, int size, Color pColor) throws IllegalCaracterClassException, IllegalMovementException, IllegalActionException {
		if (gameOn)
			throw new IllegalActionException("Can not add player when game is on!");

		String position = x + ":" + y;

		if (WindowGame.getInstance().getAllPositions().contains(position)) {
			// messageHandler.addMessage(new
			// Message("Position ["+position+"] non disponible", 1));
			throw new IllegalMovementException("Caracter already at the position [" + position + "]");
		}

		if (Data.untraversableBlocks.containsKey(position)) {
			messageHandler.addGlobalMessage(new Message("Position [" + position + "] non disponible", 1));
			throw new IllegalMovementException("Untraversable block at [" + position + "]");
		}

		if (Data.departureBlocks.containsKey(position) || (Data.DEBUG_DEPARTURE && Data.debug)) {
			Data.departureBlocks.put(position, true);
		} else {
			messageHandler.addGlobalMessage(new Message(Data.DEPARTURE_BLOCK_ERROR, Data.MESSAGE_TYPE_ERROR));
			throw new IllegalMovementException("Caracter must be at a departure position");
		}

		if (Data.MAX_PLAYER <= players.size())
			return;
		String id = "P" + players.size();
		String type = HeroData.getRandomHero();

		Player p = new Player(x, y, id, type);

		p.setNumber(players.size());
		p.setSizeCharacter(size);
		p.setPlayerColor(pColor);
		players.add(p);
		
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
				if(currentCharacter.isMonster()){
					messageHandler.addGlobalMessage(new Message("QR Code laissé durant tour IA !", Data.MESSAGE_TYPE_ERROR));
					return;
				}
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
						if (!currentCharacter.isMonster())
							decodeAction("m:" + (int)(e.getX() / apix.getBlockSizeX()) + ":" + (int)(e.getY() / apix.getBlockSizeY()));
						else
							System.err.println("Récupération d'une valeur de l'apix durant le tour de l'ia");
					else
						addChalenger((int)(e.getX() / apix.getBlockSizeX()), (int)(e.getY() / apix.getBlockSizeY()), e.getSize());
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
	int renderj = 0;
	/**
	 * The render function Call all game's render
	 */
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.scale(Data.SCALE, Data.SCALE);
		
		if(gameEnded){
			renderj++;
			Data.map.render(Data.MAP_X,  Data.MAP_Y);
			mobHandler.render(container, g);
			//renderDeckArea(container, g);
			if(Data.autoIA)
				genHandler.render(container, g);
			else
				playerHandler.render(container, g);
			if(gameWin){
				Data.WIN_IMAGE.draw(Data.ENDING_ANIMATION_X, Data.ENDING_ANIMATION_Y, (float) Data.WIN_IMAGE.getWidth() * Data.ENDING_ANIMATION_SCALE, (float) Data.WIN_IMAGE.getHeight() * Data.ENDING_ANIMATION_SCALE);
			}
			if(gameLose){
				Data.LOSE_IMAGE.draw(Data.ENDING_ANIMATION_X, Data.ENDING_ANIMATION_Y, (float) Data.LOSE_IMAGE.getWidth() * Data.ENDING_ANIMATION_SCALE, (float) Data.LOSE_IMAGE.getHeight() * Data.ENDING_ANIMATION_SCALE);
			}
			
			if(Data.ENDING_ANIMATION_Y < (Data.MAP_HEIGHT - Data.LOSE_IMAGE.getHeight() * Data.ENDING_ANIMATION_SCALE) / 2)
				Data.ENDING_ANIMATION_Y ++;
			//this.container.sleep(1000);
			if(renderj==500)
			{
				renderj = 0;
				gameOn = false;
				gameEnded = false;
				gameWin = false;
				gameLose = false;
				originMobs.get(0).getFitness().renameScoreFile();
				//Main.reloadGame();
			}
			return;
		}
		if (!apix.isInit()) {
			g.setColor(Color.black);
			g.setBackground(Color.white);
			//Data.map.render(Data.MAP_X, Data.MAP_Y);
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
			if (!Data.BACKGROUND_MUSIC.playing())
				Data.BACKGROUND_MUSIC.loop(Data.MUSIC_PITCH, Data.MUSIC_VOLUM);
			
			Data.map.render(Data.MAP_X, Data.MAP_Y);

			if (gameOn) {
				mobHandler.render(container, g);
				if(Data.autoIA)genHandler.render(container, g);
				renderDeckArea(container, g);
				if(!Data.autoIA)playerHandler.render(container, g);
				renderReachableBlocks(container, g);
				renderEvents(container, g);
			} else {
				if(!Data.autoIA)playerHandler.renderInitBlock(container, g);
			}
			if(!Data.autoIA)playerHandler.renderPlayerStat(container, g);
			mobHandler.renderMobStat(container, g);
			if(Data.autoIA)genHandler.renderMobStat(container, g);
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
		messageHandler.render(container, g);

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
			if(!e.isFinalFrame()){
				e.render(container, g);
				x = e.getX();
				y = e.getY();
				
				if(e.isMobile())
					e.move();
				
				if( e.getRange() <= 1){
				//	System.out.println("range <= 1");
					e.setMobile(false);
				}
			
				if (x < xMin || x > xMax || y < yMin || y > yMax)			
					e.setFinalFrame(true);
			}else{
				e.renderPostRemove(container, g);
				if(e.isNeeDelete())
					events.remove(i);
			}
		}
		long eventTime = 0;
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		if(gameEnded)
			return;
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

		messageHandler.update();
	}

	/**
	 * Function call for switch the current character turn
	 */
	public void switchTurn() {
		// Reset the timer
		System.out.println("turn = " + turn + ", playerNumber = " + playerNumber + ", turnTimer = " + turnTimer);
		messageHandler.addGlobalMessage(new Message("Next turn"));
		turnTimer = Data.TURN_MAX_TIME;
		
		
		turn = (turn + 1) % playerNumber;
		if(turn == 0)
		{
			currentCharacter.getFitness().debugFile("\n\t\t\t\t=== TOUR "+global_turn+" ===", true);
			checkEndGame();
			
			global_turn++;
			messageHandler.addPlayerMessage(new Message("Tour de jeu numéro  "+global_turn, Data.MESSAGE_TYPE_INFO), turn);	
			for(Mob mo:mobs){
				mo.getFitness().addTurn();
			}
			if(Data.autoIA){
				for(PlayerGenetic po:genPlayers){
					po.getFitness().addTurn();
				}
			}
		}
		previousCharacter = currentCharacter;
		previousCharacter.regenMana();
		// Switch the turn
		// Set the new character turn
		if (!Data.autoIA) {
			if (turn < players.size()){
			players.get(turn).setMyTurn(true);
			currentCharacter = players.get(turn);
			}
		}else if(turn < genPlayers.size()&& Data.autoIA){
			genPlayers.get(turn).setMyTurn(true);
			currentCharacter = genPlayers.get(turn);
		} 
		else if(!Data.autoIA){
			mobs.get(turn - players.size()).setMyTurn(true);
			currentCharacter = mobs.get(turn - players.size());
		}else{
			mobs.get(turn - genPlayers.size()).setMyTurn(true);
			currentCharacter = mobs.get(turn - genPlayers.size());
		}

		// set to false the previous character turn
		previousCharacter.setMyTurn(false);
		if (currentCharacter.isMonster() && !Data.SHOW_MOB_REACHABLE_BLOCKS)
			reachableBlock = new ArrayList<int[]>();
		else if(!Data.autoIA)
			reachableBlock = AStar.getInstance().getReachableNodes(new WindowGameData(players, mobs, turn), new CharacterData(currentCharacter));
		
			
		messageHandler.addGlobalMessage(new Message("Turn of " + currentCharacter.getName()));
		actionLeft = Data.ACTION_PER_TURN;

		if (currentCharacter.isNpc())// && !previousCharacter.isNpc())// mettre le run du bot IAGénétique
		{	
			currentCharacter.findScriptAction(0); //commands.startCommandsCalculation(currentCharacter, players, mobs, turn);
		}

		if(!currentCharacter.isNpc())
		{
			//currentCharacter.
			//currentCharacter.findScriptAction(0);
		}

		// print the current turn in the console
		if (Data.debug && !gameEnded) {
			System.out.println("========================");
			if (turn < players.size() && !Data.autoIA) {
				System.out.println("Tour du Joueur " + turn);
				System.out.println("Player : " + players.get(turn).toString());
			} else if (turn < genPlayers.size() && Data.autoIA) {
				System.out.println("Tour du Joueur Genetic " + turn);
				System.out.println("Player : " + genPlayers.get(turn).toString());
			}
			else if(!Data.autoIA){
				System.out.println("Tour du Monster" + (turn - players.size()));
				System.out.println("Monster " + mobs.get(turn - players.size()).toString());
			}else{
				System.out.println("Tour du Monster" + (turn - genPlayers.size()));
				System.out.println("Monster " + mobs.get(turn - genPlayers.size()).toString());
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
		if(gameEnded || !gameOn) // si jeu fini ou non lancé
			return;
		System.out.println("### decodeAction debug : action="+action);
		
		if (action.startsWith("s")) // Spell action 
		{ 
			if(actionLeft <= 0)
			{
				if(!Data.debug){
					messageHandler.addPlayerMessage(new Message(Data.ERROR_TOO_MUCH_ACTION, 1), turn);
					return;
				}else{
					messageHandler.addPlayerMessage(new Message("Action interdite, mais on est en mode debug... ", 1), turn);
				}
			}
			String[] tokens = action.split(":");
			if (tokens.length != 2)
				throw new IllegalActionException("Wrong number of arguments in action string");

			String spellID = tokens[0].split("\n")[0];
			int direction = Integer.parseInt(tokens[1]);
			
			if (currentCharacter.getSpell(spellID) == null){ //si peut pas lancer spell
				messageHandler.addPlayerMessage(new Message("Vous n'avez pas le sort : "+SpellData.getSpellById(spellID).getName(), Data.MESSAGE_TYPE_ERROR), turn);
				throw new IllegalActionException("Spell [" + spellID + "] not found");
			}
			float speed = currentCharacter.getSpell(spellID).getSpeed();//vitesse déplacement d'un spell
			Event e = currentCharacter.getSpell(spellID).getEvent().getCopiedEvent(); //event d'un spell

			e.setDirection(direction, speed);
			e.setX(Data.MAP_X + currentCharacter.getX() * Data.BLOCK_SIZE_X);
			e.setY(Data.MAP_Y + currentCharacter.getY() * Data.BLOCK_SIZE_Y);
			// Get the range to the next character to hit
			Focus focus = getFirstCharacterRange(getCharacterPositionOnLine(currentCharacter.getX(), currentCharacter.getY(), e.getDirection()), e);
			//System.out.println("get focus : " + focus.toString());
			if (focus.range > e.getRange()) {
				focus.range = e.getRange();
				focus.character = null;
			}
			e.setRange(focus.range);

			try {//lancement du spell
				String res = currentCharacter.useSpell(spellID, direction);
				String []split = res.split(":");
				int damage = Integer.parseInt(split[0]);
				int heal = Integer.parseInt(split[1]);
				int state = Integer.parseInt(split[2]);
				
				if(state == -1){// echec critique
					
					messageHandler.addPlayerMessage(new Message("Echec critique du sort "+SpellData.getSpellById(spellID).getName(), Data.MESSAGE_TYPE_ERROR), turn);
					if(heal > 0){
						currentCharacter.heal(heal);
						if(focus.character != null)
						{	
							currentCharacter.getFitness().scoreHeal(focus.character, currentCharacter); // scoring
						}
						else
						{
							currentCharacter.getFitness().scoreUnlessSpell();
							currentCharacter.getFitness().debugFile((currentCharacter.isMonster()?"mob ":"genPlayer ")
							+currentCharacter.getName()+" "+currentCharacter.getTrueID()+" a soigné personne ."+currentCharacter.getFitness().toStringFitness(),true);
						}
						messageHandler.addPlayerMessage(new Message("Heal critic "+heal+" to the "+focus.character.getName()+"", Data.MESSAGE_TYPE_ERROR), turn);

					}else{
						currentCharacter.takeDamage(damage, e.getType());
						if(focus.character != null)
						{	
							currentCharacter.getFitness().scoreSpell(focus.character, currentCharacter); // scoring
						}
						else
						{
							currentCharacter.getFitness().scoreUnlessSpell();
							currentCharacter.getFitness().debugFile((currentCharacter.isMonster()?"mob ":"genPlayer ")
									+currentCharacter.getName()+" "+currentCharacter.getTrueID()+"a attaqué personne (echec crit) ."+currentCharacter.getFitness().toStringFitness(),true);
						}
						messageHandler.addPlayerMessage(new Message("Use "+SpellData.getSpellById(spellID).getName()+" on "+currentCharacter.getName()+" and deal critic "+damage, Data.MESSAGE_TYPE_ERROR), turn);	
					}
					if (currentCharacter.checkDeath()) {
						// TODO ADD a textual event
						System.out.println("-----------------------------------------");
						System.out.println("DEATH FOR" + currentCharacter.toString());
						System.out.println("-----------------------------------------");
						//System.out.println(currentCharacter.getFitness().toStringFitness());
						messageHandler.addPlayerMessage(new Message(currentCharacter.getName()+"Died "), turn);	
						if(!Data.autoIA)
							players.remove(currentCharacter);
						else
							genPlayers.remove(currentCharacter);
						currentCharacter.getFitness().debugFile("*** "+(currentCharacter.isMonster()?"mob ":"genPlayer ")
								+currentCharacter.getName()+" "+currentCharacter.getTrueID()+" est mort ."+currentCharacter.getFitness().toStringFitness(),true);
					
						mobs.remove(currentCharacter);
						playerNumber--;
						checkEndGame();
						switchTurn();
					}
					
				}else{//si pas echec critique 
					if (focus.character != null) {// si spell a une cible
						if (currentCharacter.isMonster() == focus.character.isMonster())
							if (e.getHeal() > 0){ // Si cible est dans la même équipe que toi
								focus.character.heal(heal);
								if(state > 0 )
									messageHandler.addPlayerMessage(new Message("Heal critic "+heal+" to the "+focus.character.getName()+"", Data.MESSAGE_TYPE_ERROR), turn);
								else
									messageHandler.addPlayerMessage(new Message("Heal "+heal+" to the "+focus.character.getName()+""), turn);
								currentCharacter.getFitness().scoreHeal(focus.character, currentCharacter); // scoring
							}else{
								damage = focus.character.takeDamage(damage, e.getType());

								messageHandler.addPlayerMessage(new Message("Use "+SpellData.getSpellById(spellID).getName()+" on "+focus.character.getName()+" and deal "+damage), turn);	
								currentCharacter.getFitness().scoreSpell(focus.character, currentCharacter); // scoring
							}
						else{// si ennemi
							damage = focus.character.takeDamage(damage, e.getType());
							if(state > 0)
								messageHandler.addPlayerMessage(new Message("Use "+SpellData.getSpellById(spellID).getName()+" on "+focus.character.getName()+" and deal critic "+damage, Data.MESSAGE_TYPE_ERROR), turn);	
							else
								messageHandler.addPlayerMessage(new Message("Use "+SpellData.getSpellById(spellID).getName()+" on "+focus.character.getName()+" and deal "+damage), turn);	
							if(focus.character != null)
								currentCharacter.getFitness().scoreSpell(focus.character, currentCharacter); // scoring

						}
						if (focus.character.checkDeath()) {// si mort
							// TODO ADD a textual event
							System.out.println("-----------------------------------------");
							System.out.println("DEATH FOR" + focus.character.toString());
							System.out.println("-----------------------------------------");
							//System.out.println(focus.character.getFitness().toStringFitness());
							messageHandler.addPlayerMessage(new Message(focus.character.getName()+"Died "), turn);	
							if(Data.autoIA)
								genPlayers.remove(focus.character);
							else
								players.remove(focus.character);
							currentCharacter.getFitness().debugFile("*** "+(focus.character.isMonster()?"mob ":"genPlayer ")+
									focus.character.getName()+" "+focus.character.getTrueID()+" a été tué par "+(currentCharacter.isMonster()?"mob ":"genPlayer ")+currentCharacter.getName()+" "+currentCharacter.getTrueID()+".",true);
							mobs.remove(focus.character);
							playerNumber--;
							checkEndGame();
						}
					}else{
						messageHandler.addPlayerMessage(new Message("Vous avez lancé "+SpellData.getSpellById(spellID).getName()+" mais personne n'a été touché"), turn);
						currentCharacter.getFitness().scoreUnlessSpell();
						currentCharacter.getFitness().debugFile((currentCharacter.isMonster()?"mob ":"genPlayer ")
								+currentCharacter.getName()+" "+currentCharacter.getTrueID()+" a lancé un sort sur personne ."+currentCharacter.getFitness().toStringFitness(),true);
					}
				}
				events.add(e);
				System.out.println("Created " + e.toString());
				actionLeft --;
			} catch (IllegalActionException iae) {
				//iae.printStackTrace();
				System.out.println(iae.getLocalizedMessage() +"----------------------------"+iae.getMessage());
				messageHandler.addPlayerMessage(new Message(iae.getLocalizedMessage(),Data.MESSAGE_TYPE_ERROR), turn);
			}

		}

		else if (action.startsWith("t")) { // Trap action
			System.out.println("Find a trap action");
		}
		else if (action.startsWith("p")) { // Pass turn
			currentCharacter.getFitness().scorePassTurn();
			currentCharacter.getFitness().debugFile((currentCharacter.isMonster()?"mob ":"genPlayer ")+
					currentCharacter.getName()+" "+currentCharacter.getTrueID()+" PASSE son tour ."+currentCharacter.getFitness().toStringFitness(),true);
		
			switchTurn();
		}
		else if (action.startsWith("m")) {// Movement action
			try {
				String[] tokens = action.split(":");
				if (tokens.length != 3)
					throw new IllegalActionException("Wrong number of arguments in action string");
				
				String position = tokens[1] + ":" + tokens[2];
				// TODO call aStar and check if character don't fall into trap
				currentCharacter.moveTo(position);
				currentCharacter.getFitness().scoreMove();
				currentCharacter.getFitness().debugFile((currentCharacter.isMonster()?"mob ":"genPlayer ")
						+currentCharacter.getName()+" "+currentCharacter.getTrueID()+" BOUGE en "+position+" ."+currentCharacter.getFitness().toStringFitness(),true);
				switchTurn();

			}catch (IllegalMovementException ime) {
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
		float range = Data.MAX_RANGE;
		System.out.println("Search the first character range : " + e.toString() + ", " + chars.toString());
		Character focus = null;
		for (Character c : chars) {
			if (e.getDirection() == Data.NORTH || e.getDirection() == Data.SOUTH) {
				int i = (Math.abs(c.getY() - (e.getYOnBoard())));
				System.out.println("c.getY() = [" + c.getY() + "], e.getYOnBoard = [" + (e.getYOnBoard()) + "], i = [" + i + "]");

				if (i < range) {
					range = i;
					focus = c;
				}
			}
			if (e.getDirection() == Data.EAST || e.getDirection() == Data.WEST) {
				System.out.println("c.getX() = [" + c.getX() + "], e.getXOnBoard = [" + (e.getXOnBoard()) + "], i = [" + (c.getX() - e.getXOnBoard())
						+ "]");
				int i = (Math.abs(c.getX() - (e.getXOnBoard())));

				if (i < range) {
					range = i;
					focus = c;
				}
			}
		}
		
		if(e.getDirection() == Data.SELF)
		{
			return new Focus(0, currentCharacter);
		}
		
		if (Data.debug && focus != null)
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
							decodeAction("s3:" + Data.NORTH);
						if (Input.KEY_NUMPAD6 == key)
							decodeAction("s9:" + Data.EAST);
						if (Input.KEY_NUMPAD2 == key)
							decodeAction("s10:" + Data.SOUTH);
						if (Input.KEY_NUMPAD4 == key)
							decodeAction("s4:" + Data.WEST);
						if (Input.KEY_NUMPAD5 == key)
							decodeAction("s1:" + Data.SELF);
					} catch (IllegalActionException e) {
						// TODO Auto-generated catch block
						System.err.println(e.getMessage());
					}
				}
			
			if (Input.KEY_DIVIDE == key) {
				currentCharacter.takeDamage(20, "magic");
			}
			if (Input.KEY_SUBTRACT == key) {
				if(gameEnded)
					start();
			}
			if (Input.KEY_ADD == key) {
				try {
					Random rand = new Random();
					int x = rand.nextInt(Data.BLOCK_NUMBER_X - 0) + 0;
					int y = rand.nextInt(Data.BLOCK_NUMBER_Y - 0) + 0;
					addChalenger(x, y, -1);
				} catch (IllegalCaracterClassException e) {
					e.printStackTrace();
				} catch (IllegalMovementException e) {
					e.printStackTrace();
				} catch (IllegalActionException e) {
					e.printStackTrace();
				}
			}
			
			if(Input.KEY_L == key){
				gameEnded = true;
				gameLose = true;
				stopAllThread();
			}
			
			if(Input.KEY_W == key){
				gameEnded = true;
				gameWin = true;
				stopAllThread();
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
		if(!Data.autoIA)
		{
		for (int i = 0; i < players.size(); i++)
			list.add(players.get(i).getX() + ":" + players.get(i).getY());
		}else{
			for (int i = 0; i < genPlayers.size(); i++)
				list.add(genPlayers.get(i).getX() + ":" + genPlayers.get(i).getY());
		}
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

	public GameHandler getHandler() {
		return handler;
	}
	
	public Character getCurrentPlayer() {
		return currentCharacter;
	}
	
	public void checkEndGame(){
		
			if(Data.debug && !Data.RUN_APIX)
			{	
				if( mobs.size() <= 0 ){
					//GAME WIN
					gameEnded = true;
					gameWin = true;
				}else if( (players.size() <= 0 && !Data.autoIA) || (genPlayers.size() <= 0 && Data.autoIA) || global_turn == Data.maxTurn)
				{
					//GAME LOSE
					gameEnded = true;
					gameLose = true;
				}
			}
		if(gameEnded)
		{
			
			System.out.println("-- FIN DE JEU-- ");
			originMobs.get(0).getFitness().debugFile("-- FIN DE JEU --", true);
			for(Mob mo : originMobs){
				System.out.println("Mob id="+mo.getId()+" name="+mo.getName()+" "+mo.getFitness().toStringFitness());
				mo.getFitness().debugFile("Mob id="+mo.getTrueID()+" name="+mo.getName()+" "+mo.getFitness().toStringFitness(), true);
			}	
			if(Data.autoIA)
			{
				for(PlayerGenetic po : originGenPlayers){
					po.getFitness().debugFile("Player id="+po.getTrueID()+" name="+po.getName()+" "+po.getFitness().toStringFitness(), true);
				}	
			}
			originMobs.get(0).getFitness().renameScoreFile();
			stopAllThread();
		}
	}
	
	public void stopAllThread(){
		//apix.stop();
		//commands.getInstance().getThread().stop();
		commands.getInstance().getThread().interrupt();
		turnTimer = Integer.MAX_VALUE;
	}
	
	/**
	 * Get all character on a line line = Horizontal or Vertical
	 * 
	 * @param x
	 * @param y
	 * @param direction
	 * @return ArrayList<Character>
	 */
	public ArrayList<Character> getCharacterPositionOnLine(int x, int y, int direction) {

		ArrayList<Character> c = new ArrayList<Character>();
		
		if(direction == Data.SELF)
		{
			c.add(currentCharacter);
			return c;
		}
		if(!Data.autoIA)
		{
			for (int i = 0; i < players.size(); i++) {
				// above
				if (direction == Data.NORTH && players.get(i).getY() < y && players.get(i).getX() == x)
					c.add(players.get(i));
				// bottom
				if (direction == Data.SOUTH && players.get(i).getY() > y && players.get(i).getX() == x)
					c.add(players.get(i));
				// on left
				if (direction == Data.EAST && players.get(i).getY() == y && players.get(i).getX() > x)
					c.add(players.get(i));
				// on right
				if (direction == Data.WEST && players.get(i).getY() == y && players.get(i).getX() < x)
					c.add(players.get(i));
			}
		}else{
			for (int i = 0; i < genPlayers.size(); i++) {
				// above
				if (direction == Data.NORTH && genPlayers.get(i).getY() < y && genPlayers.get(i).getX() == x)
					c.add(genPlayers.get(i));
				// bottom
				if (direction == Data.SOUTH && genPlayers.get(i).getY() > y && genPlayers.get(i).getX() == x)
					c.add(genPlayers.get(i));
				// on left
				if (direction == Data.EAST && genPlayers.get(i).getY() == y && genPlayers.get(i).getX() > x)
					c.add(genPlayers.get(i));
				// on right
				if (direction == Data.WEST && genPlayers.get(i).getY() == y && genPlayers.get(i).getX() < x)
					c.add(genPlayers.get(i));
			}
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

	private class Focus {

		protected float range;
		protected Character character;

		public Focus(float range, Character character) {
			this.range = range;
			this.character = character;
		}

		public String toString() {
			return "Focus [ range, " + range + ", " + character.toString() + "]";
		}
	}


}
