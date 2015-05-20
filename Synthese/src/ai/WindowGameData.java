package ai;

import game.Character;
import game.Mob;
import game.Player;
import game.Trap;

import java.util.ArrayList;
import java.util.Iterator;

public class WindowGameData {
	private ArrayList<Character> characters;
	private ArrayList<Trap> traps;
	private Character currentCharacter;
	private int turn;
	private Iterator<Mob> mobIterator;
	private Iterator<Player> playerIterator;

	public WindowGameData(ArrayList<Player> players, ArrayList<Mob> mobs,
			Character currentCharacter, int turn) {
		for (Player p : players)
			characters.add(p);
		for (Mob m : mobs)
			characters.add(m);

		this.currentCharacter = currentCharacter;
		this.turn = turn;
		mobIterator = mobs.iterator();
		playerIterator = players.iterator();

	}

	public ArrayList<Character> getCharacters() {
		return characters;
	}

	public void setCharacters(ArrayList<Character> characters) {
		this.characters = characters;
	}

	public Character getCurrentCharacter() {
		return currentCharacter;
	}

	public void setCurrentCharacter(Character currentCharacter) {
		this.currentCharacter = currentCharacter;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public Mob nextMob() {
		if (mobIterator.hasNext())
			return mobIterator.next();
		else
			return null;
	}

	public Player nextPlayer() {
		if (playerIterator.hasNext())
			return playerIterator.next();
		else
			return null;
	}

	public ArrayList<Player> getNearPlayers(Mob mob) {
		ArrayList<Player> players = new ArrayList<Player>();

		for (Iterator<Player> it = players.iterator(); it.hasNext();) {
			Player p = it.next();
			double distance = Math.sqrt(Math.pow(mob.getX() - p.getX(), 2)
					+ Math.pow(mob.getY() - p.getY(), 2));
			if (distance < mob.getStats().getEyeSight())
				players.add(p);
		}

		return players;
	}

	@Deprecated
	public Character nextCharacter() {
		// TODO add something similar to switch turn to get the next character
		return currentCharacter;
	}

}
