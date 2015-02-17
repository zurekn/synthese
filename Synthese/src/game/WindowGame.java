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
 * Main class whith handle the game
 * Contain the init method for creating all game objects
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
	 private ArrayList<Spell> spells = new ArrayList<Spell>();
	 
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
	        SpriteData.initMap();
	        SpellData.initSpell();
	        MonsterData.initMob();
	        
	        movementHandler = new MovementHandler(this);
	        
	        player = new ArrayList<Player>();
	        player.add(new Player(5, 5, "P1" ,new Stats(100, 50)));
	        playerHandler = new playerHandler(player);
	        
	        mobs =  Data.initMobs();
	        mobHandler = new MobHandler(mobs);
	        
	        playerNumber = 1 + mobs.size();
	        
	        turnTimer = Data.TURN_MAX_TIME;
	        new Thread (movementHandler).start();
	        turn = 0;
	    }

	    public void render(GameContainer container, Graphics g) throws SlickException {
	    	SpriteData.map.render(Data.DECK_AREA_SIZE_Y, Data.DECK_AREA_SIZE_Y);
	    	mobHandler.render(container, g);
	    	//TODO
	    	//Bug playerrender doesn't work every time
	    	playerHandler.render(container, g);
	    	renderSpell(container, g);
	    	renderDeckArea(container, g);
	    	renderText(container, g);
	    }
	    
	    private void renderDeckArea(GameContainer container, Graphics g){
	    	g.setColor(Color.white);
	    	//TOP
	    	g.drawRect(Data.DECK_AREA_SIZE_Y, 0, Data.DECK_AREA_SIZE_X, Data.DECK_AREA_SIZE_Y);
	    	//BOTTOM
	    	g.drawRect(Data.DECK_AREA_SIZE_Y,  Data.DECK_AREA_SIZE_Y + Data.DECK_AREA_SIZE_X, Data.DECK_AREA_SIZE_X, Data.DECK_AREA_SIZE_Y);
	    	//LEFT
	    	g.drawRect(0, Data.DECK_AREA_SIZE_Y, Data.DECK_AREA_SIZE_Y, Data.DECK_AREA_SIZE_X);
	    	//RIGHT
	    	g.drawRect(Data.DECK_AREA_SIZE_X + Data.DECK_AREA_SIZE_Y, Data.DECK_AREA_SIZE_Y, Data.DECK_AREA_SIZE_Y, Data.DECK_AREA_SIZE_X);
	    }
	    
	    private void renderText(GameContainer container, Graphics g){
	    	//render text
	    	g.setColor(Color.white);
	    	g.drawString("End of turn in : "+turnTimer, 10, 20);
	    }
	    
	    private void renderSpell(GameContainer container, Graphics g){
	    	for(int i = 0; i < spells.size(); i++){
	    		spells.get(i).render(container, g);
	    	}
	    }
	    
	    @Override
	    public void update(GameContainer container, int delta) throws SlickException {
	    	if(System.currentTimeMillis() -  timeStamp > 1000){
	    		turnTimer --;
	    		timeStamp = System.currentTimeMillis();
	    	}
	    	
	    	//Turn timer 
	    	if(turnTimer < 0){
	    		turnTimer = Data.TURN_MAX_TIME;
	    		turn = (turn + 1)%playerNumber;
	    		System.out.println("========================");
	    		System.out.println("Tour du joueur "+turn);
	    		System.out.println("========================");
	    	}
	    }

	    public void pushAction(String s){
	    	
	    }
	    
	    @Override
	    public void keyReleased(int key, char c) {
	    	System.out.println("WindowGame, keyReleased : "+key+", char : "+c);
	        if (Input.KEY_ESCAPE == key) {
	            container.exit();
	        }
	        
	        if(Input.KEY_P == key){
	        	pushAction("spell:1:1");
	        }
	    }

		public void move(String str) {
			System.out.println("WindowGame get new movement : "+str);
			if(turn < player.size()){
				try {
					player.get(turn).moveTo(str);
				} catch (IllegalMovementException e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("Mobs movement not set");
			}
			
		}
}
