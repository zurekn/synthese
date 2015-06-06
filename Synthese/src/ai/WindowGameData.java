package ai;

import game.Mob;
import game.Player;

import java.util.ArrayList;

public class WindowGameData implements Cloneable {
	private ArrayList<CharacterData> characters = new ArrayList<CharacterData>();
	private int index = 1;

	public WindowGameData(ArrayList<Player> players, ArrayList<Mob> mobs,
			int turn) {
		index = turn;
		int initTurn = turn;
		int n = players.size() + mobs.size();
		do {
			if (turn < players.size()) {
				characters.add(new CharacterData(players.get(turn)));
			} else {
				characters.add(new CharacterData(
						mobs.get(turn - players.size())));
			}
			turn = (turn + 1) % n;
		} while (turn != initTurn);
	}

	public WindowGameData(ArrayList<CharacterData> characters, int index) {
		this.characters = CharacterBuilder.build(characters);
		this.index = index;
	}

	public ArrayList<CharacterData> getCharacters() {
		return characters;
	}

	public void setCharacters(ArrayList<CharacterData> characters) {
		this.characters = characters;
	}

	public WindowGameData clone() {
		return new WindowGameData(characters, index);
	}

	public ArrayList<CharacterData> getNearAllies(CharacterData character) {
		ArrayList<CharacterData> allies = new ArrayList<CharacterData>();
		CharacterData current = characters.get(characters.indexOf(character));	
		for (CharacterData c : characters)
			if (!c.equals(current))
				if (c.isMonster() == current.isMonster())
					if (c.distanceFrom(current) <= current.getStats()
							.getEyeSight())
						allies.add(c);
		return allies;
	}

	public ArrayList<CharacterData> getNearEnemies(CharacterData character) {
		ArrayList<CharacterData> enemies = new ArrayList<CharacterData>();
		CharacterData current = characters.get(characters.indexOf(character));	
		for (CharacterData c : characters)
			if (!c.equals(current))
				if (c.isMonster() != current.isMonster())
					if (c.distanceFrom(current) <= current.getStats()
							.getEyeSight())
						enemies.add(c);
		return enemies;
	}
	
	public CharacterData getCharacter(CharacterData c){
		int i = characters.indexOf(c);
		return characters.get(i);
	}

	public ArrayList<String> getAllPositions() {
		ArrayList<String> positions = new ArrayList<String>();
		for (CharacterData c : characters)
			positions.add(c.getX() + ":" + c.getY());
		return positions;
	}

	public CharacterData nextCharacter() {
		if(index >= characters.size()){
			index = 0 ;
			return null;
		}
		CharacterData c = characters.get(index);
		index++;
		return c;
		/*index++;
		if(index >= characters.size()-1){
			index = -1;
			return null;
		}
		return characters.get(index);*/
		/*if (index < characters.size() - 1) {
			index++;
			return characters.get(index);
		} else if (index == characters.size() - 1) {
			index++;
			return characters.get(index - 1);
		} else {
			index = 0;
			return null;
		}*/
	}

	public void move(CharacterData character, int x, int y) {
		int i = characters.indexOf(character);
		characters.get(i).moveAiTo(x, y);
	}

}
