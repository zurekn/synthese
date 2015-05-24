package ai;

import exception.IllegalMovementException;
import game.Character;
import game.Mob;
import game.Player;
import game.Trap;

import java.util.ArrayList;
import java.util.Iterator;

public class WindowGameData implements Cloneable {
	private ArrayList<Character> characters= new ArrayList<Character>();
	private Iterator<Character> iterator;

	public WindowGameData(ArrayList<Player> players, ArrayList<Mob> mobs,
			int turn) {
		int initTurn = turn;
		int n =players.size() + mobs.size();
		do {
			if (turn < players.size()) {
				characters.add(players.get(turn));
			} else {
				characters.add(mobs.get(turn - players.size()));
			}
			turn = (turn+1)%n;
		} while (turn != initTurn);
		
		iterator = characters.iterator();
	}

	public WindowGameData(ArrayList<Character> characters,
			Iterator<Character> iterator) {
		super();
		this.characters = characters;
		this.iterator = iterator;
	}

	public ArrayList<Character> getCharacters() {
		return characters;
	}

	public void setCharacters(ArrayList<Character> characters) {
		this.characters = characters;
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

	public WindowGameData clone() {
		return new WindowGameData(characters, iterator);
	}

	public Character nextCharacter() {
		if (iterator.hasNext())
			return iterator.next();
		else {
			iterator = characters.iterator();
			return null;
		}
	}

	public void move(Character character, int x, int y) {
		int i = characters.indexOf(character);
		characters.get(i).moveAiTo(x, y);
	}

}
