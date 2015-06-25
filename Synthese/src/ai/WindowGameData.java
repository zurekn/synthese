package ai;

import data.Data;
import data.Stats;
import game.Mob;
import game.Player;
import game.Spell;

import java.util.ArrayList;

public class WindowGameData {
	private ArrayList<CharacterData> characters = new ArrayList<CharacterData>();
	private int index = 0;

	public WindowGameData(ArrayList<Player> players, ArrayList<Mob> mobs,
			int turn) {
		int n = players.size() + mobs.size();
		index = (turn - players.size() + 1) % n;
		int initTurn = turn;

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

	public CharacterData getCharacter(CharacterData c) {
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
		if (index >= characters.size()) {
			index = 0;
			return null;
		}
		CharacterData c = characters.get(index);
		index++;
		return c;
		/*
		 * index++; if(index >= characters.size()-1){ index = -1; return null; }
		 * return characters.get(index);
		 */
		/*
		 * if (index < characters.size() - 1) { index++; return
		 * characters.get(index); } else if (index == characters.size() - 1) {
		 * index++; return characters.get(index - 1); } else { index = 0; return
		 * null; }
		 */
	}

	public void move(CharacterData character, int x, int y) {
		int i = characters.indexOf(character);
		characters.get(i).moveAiTo(x, y);
	}

	public void doCommand(String cmd) {
		// index value is on next character
		int i = (index - 1) % characters.size();
		CharacterData character = characters.get(i);

		if (cmd.startsWith("m")) {// Movement action
			String[] tokens = cmd.split(":");
			character.moveAiTo(Integer.parseInt(tokens[1]),
					Integer.parseInt(tokens[2]));
		} else if (cmd.startsWith("s")) {// spell
			String[] tokens = cmd.split(":");
			Spell spell = character.getSpell(tokens[0]);
			CharacterData target = null;
			float d = Float.MAX_VALUE;
			int direction = Integer.parseInt(tokens[1]);
			if (direction == Data.SELF) {
				useSpell(character, spell, target);
			} else {
				for (CharacterData c : characters) {
					if (c.getX() == character.getX()) {
						float diffY = c.getY() - character.getY();
						if (Math.abs(diffY) < d) {
							if (direction == Data.NORTH && diffY > 0) {
								target = c;
								d = Math.abs(diffY);
							}
							if (direction == Data.SOUTH && diffY < 0) {
								target = c;
								d = Math.abs(diffY);
							}
						}
					}

					if (c.getY() == character.getY()) {
						float diffX = c.getX() - character.getX();
						if (Math.abs(diffX) < d) {
							if (direction == Data.EAST && diffX < 0) {
								target = c;
								d = Math.abs(diffX);
							}
							if (direction == Data.WEST && diffX > 0) {
								target = c;
								d = Math.abs(diffX);
							}
						}
					}
				}
				if(target !=  null){
					useSpell(character, spell, target);
				}
			}
		}
	}

	public ArrayList<CharacterData> getTargetsInRange(CharacterData character,
			int range, boolean damageSpell) {
		ArrayList<CharacterData> targets = new ArrayList<CharacterData>();
		if (range == 0) {
			targets.add(character);
		} else {
			int max = 2 * range + 1;
			CharacterData[][] surroundings = new CharacterData[max][max];
			for (CharacterData c : characters)
				try {
					surroundings[c.getX() - character.getX() + range][c.getY()
							- character.getY() + range] = c;
				} catch (ArrayIndexOutOfBoundsException e) {
				}

			for (int x = range; x < max; x++) {
				if (isTarget(character, surroundings[x][range], damageSpell)) {
					targets.add(surroundings[x][range]);
					break;
				}
			}

			for (int x = range; x >= 0; x--) {
				if (isTarget(character, surroundings[x][range], damageSpell)) {
					targets.add(surroundings[x][range]);
					break;
				}
			}

			for (int y = range; y < max; y++) {
				if (isTarget(character, surroundings[range][y], damageSpell)) {
					targets.add(surroundings[range][y]);
					break;
				}
			}

			for (int y = range; y < max; y++) {
				if (isTarget(character, surroundings[range][y], damageSpell)) {
					targets.add(surroundings[range][y]);
					break;
				}
			}

		}
		return targets;
	}
	
	public int targetable(ArrayList<CharacterData> enemies, CharacterData character){
		int n=0;
		int eyesight = character.getStats().getEyeSight();
		int max = 2 * eyesight + 1;
		CharacterData[][] surroundings = new CharacterData[max][max];
		for (CharacterData e : enemies){
			try {
				surroundings[e.getX() - character.getX() + eyesight][e.getY()
						- character.getY() + eyesight] = e;
			} catch (ArrayIndexOutOfBoundsException aioobe) {
			}
			
			for (int x = eyesight; x < max; x++) {
				if (surroundings[x][eyesight]!=null) {
					n++;
					break;
				}
			}

			for (int x = eyesight; x >= 0; x--) {
				if (surroundings[x][eyesight]!=null) {
					n++;
					break;
				}
			}

			for (int y = eyesight; y < max; y++) {
				if (surroundings[eyesight][y]!=null) {
					n++;
					break;
				}
			}

			for (int y = eyesight; y < max; y++) {
				if(surroundings[eyesight][y]!=null) {
					n++;
					break;
				}
			}
		}
		return n;
	}

	public boolean isTarget(CharacterData current, CharacterData target,
			boolean damageSpell) {
		try {
			if (damageSpell && (current.isMonster() != target.isMonster()))
				return true;

			if (!damageSpell && (current.isMonster() == target.isMonster()))
				return true;
			return false;
		} catch (NullPointerException npe) {
			return false;
		}
	}

	public void useSpell(CharacterData character, Spell spell,
			CharacterData target) {
		Stats stats = character.getStats();
		if (spell.getMana() < stats.getMana()) {
			int newMana = stats.getMana() - spell.getMana();
			stats.setMana(newMana);
		}
		target.takeDamage(spell.getDamage(), spell.getType());
		target.heal(spell.getHeal());
		if(target.getStats().getLife()<0){
			if(characters.indexOf(target) <= characters.indexOf(character))
				index = (index-1) % characters.size();
				
			characters.remove(target);		
		}

	}
}
