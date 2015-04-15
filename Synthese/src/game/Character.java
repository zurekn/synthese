package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;

import ai.AStar;
import data.Data;
import data.SpellD;
import data.Stats;
import exception.IllegalActionException;
import exception.IllegalMovementException;

/**
 * Class representing a character which can be either a player or a monster.
 * 
 */
public abstract class Character {
	private int x;
	private int y;
	private String id;
	private Animation[] animation;
	private Stats stats;
	private boolean myTurn = false;
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	private String name;
	private String aiType;
	private Character focusedOn;

	public abstract void render(GameContainer container, Graphics g);

	public abstract void init();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public Animation[] getAnimation() {
		return animation;
	}

	public void setAnimation(Animation[] animation) {
		this.animation = animation;
	}

	public void setMyTurn(boolean b) {
		myTurn = b;
	}

	public boolean isMyTurn() {
		return myTurn;
	}

	public String getAiType() {
		return aiType;
	}

	public void setAiType(String aiType) {
		this.aiType = aiType;
	}

	public Character getFocusedOn() {
		return focusedOn;
	}

	public void setFocusedOn(Character focusedOn) {
		this.focusedOn = focusedOn;
	}

	/**
	 * Add the spell s to this character.
	 * 
	 * @param s
	 * @see Spell
	 */
	public void addSpell(SpellD s) {
		spells.add(new Spell(s.getId(), s.getName(), s.getDamage(),
				s.getHeal(), s.getMana(), s.getRange(), s.getEvent()));
	}

	public ArrayList<Spell> getSpells() {
		return spells;
	}

	public void setSpells(ArrayList<Spell> spells) {
		this.spells = spells;
	}

	/**
	 * Get a spell by its id
	 * 
	 * @param spellID
	 * @return the {@link Spell} or null if not found.
	 */
	public Spell getSpell(String spellID) {
		for (Iterator<Spell> it = spells.iterator(); it.hasNext();) {
			Spell s = it.next();
			if (s.getId().equals(spellID))
				return s;
		}
		return null;
	}

	/**
	 * 
	 * @param spellID
	 * @return true if this character know the spell, false otherwise.
	 */
	public boolean isSpellLearned(String spellID) {
		for (Iterator<Spell> it = spells.iterator(); it.hasNext();) {
			Spell s = it.next();
			if (s.getId().equals(spellID))
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Character [x=" + x + ", y=" + y + ", id=" + id + ", animation="
				+ Arrays.toString(animation) + ", stats=" + stats + ", myTurn="
				+ myTurn + ", spells=" + spells + ", name=" + name + "]";
	}

	/**
	 * Move the caracter to the position x:y if it's possible
	 * 
	 * @param position
	 * @throws IllegalMovementException
	 */

	public int moveTo(String position) throws IllegalMovementException {
		ArrayList<String> positions = WindowGame.windowGame.getAllPosition();
		if (positions.contains(position)) {
			throw new IllegalMovementException(
					"Caracter already at the position [" + position + "]");
		}

		if (Data.untraversableBlocks.containsKey(position)) {
			throw new IllegalMovementException("Untraversable block at ["
					+ position + "]");
		} else {
			String tokens[] = position.split(":");
			if (tokens.length != 2) {
				throw new IllegalMovementException("Invalid movement syntax ");
			} else {

				int x = Integer.parseInt(tokens[0]);
				int y = Integer.parseInt(tokens[1]);

				if (x < 0 || x > Data.BLOCK_NUMBER_X || y < 0
						|| y > Data.BLOCK_NUMBER_Y) {
					throw new IllegalMovementException(
							"Movement is out of the map");
				} else {
					int xTmp = this.x, yTmp = this.y;
					int movePoints = this.stats.getMovementPoints();
					int dist = (int) Math.sqrt(Math.pow(x - xTmp, 2)
							+ Math.pow(y - yTmp, 2));
					if (dist > movePoints) {
						throw new IllegalMovementException(
								"Not enough movements points");
					} else {
						AStar aStar = AStar.getInstance();
						this.x = x;
						this.y = y;
						this.stats.setMovementPoints(movePoints - dist);
						return 0;
					}
				}
			}
		}
	}

	/**
	 * Use a spell given by its id, throws an {@link IllegalActionException}
	 * otherwise.
	 * 
	 * @param spellID
	 * @param direction
	 * @throws IllegalActionException
	 */
	public void useSpell(String spellID, int direction)
			throws IllegalActionException {
		Spell spell = this.getSpell(spellID);
		if (spell == null)
			throw new IllegalActionException("Spell unkown");
	}

	/**
	 * 
	 * @param damage
	 *            the damage value
	 * @param type
	 *            type of damage (fire, ice, shock...)
	 */
	public void takeDamage(int damage, String type) {
		if (type.equals("magic")) {
			damage = damage - getStats().getMagicResist();
		} else if (type.equals("physic")) {
			damage = damage - getStats().getArmor();
		} else {
			System.out.println("Wrong damage type : " + type);
		}
		if (damage < 0)
			damage = 0;
		System.out.println(id + " take : [" + damage + "] damage");
	}

	public boolean checkDeath() {
		return stats.getLife() <= 0;
	}

}
