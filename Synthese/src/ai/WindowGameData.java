package ai;

import game.Character;
import game.Mob;
import game.Player;

import java.util.ArrayList;
import java.util.Iterator;

public class WindowGameData {
	private ArrayList<Mob> mobs;
	private ArrayList<Player> players;
	private Character currentCharacter;
	private int playerNumber;
	private int turn;
	private Iterator<Mob> mobIterator;
	private Iterator<Player> playerIterator;
	
	public WindowGameData(ArrayList<Mob> mobs, ArrayList<Player> players,
			Character currentCharacter, int playerNumber, int turn) {
		this.mobs = mobs;
		this.players = players;
		this.currentCharacter = currentCharacter;
		this.playerNumber = playerNumber;
		this.turn = turn;
		mobIterator = mobs.iterator();
		playerIterator = players.iterator();
				
	}

	public ArrayList<Mob> getMobs() {
		return mobs;
	}

	public void setMobs(ArrayList<Mob> mobs) {
		this.mobs = mobs;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public Character getCurrentCharacter() {
		return currentCharacter;
	}

	public void setCurrentCharacter(Character currentCharacter) {
		this.currentCharacter = currentCharacter;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}
	
	public Mob nextMob(){
		if(mobIterator.hasNext())
			return mobIterator.next();
		else
			return null;
	}
	
	public Player nextPlayer(){
		if(playerIterator.hasNext())
			return playerIterator.next();
		else
			return null;
	}
	
	public void resetMobIterator(){
		mobIterator = mobs.iterator();
	}
	
	public void resetPlayerIterator(){
		playerIterator = players.iterator();
	}

	public ArrayList<Player> getNearPlayers(Mob mob) {
		ArrayList<Player> players = new ArrayList<Player>();
		
		for(Iterator<Player> it = players.iterator() ; it.hasNext() ; ){
			Player p = it.next();
			double distance = Math.sqrt(Math.pow(mob.getX()-p.getX(), 2)+Math.pow(mob.getY()-p.getY(), 2));
			if(distance < mob.getStats().getEyeSight())
				players.add(p);
		}
		
		return players;
	}

}
