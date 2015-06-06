package ai;

import game.Character;
import game.Spell;

import java.util.ArrayList;
import java.util.Iterator;

import data.Stats;
import exception.IllegalActionException;
import exception.IllegalMovementException;

public class CharacterData {
	private String id;
	private int x;
	private int y;
	private int lastX;
	private int lastY;
	private Stats stats;
	private ArrayList<Spell> spells;
	private String aiType;
	private CharacterData focusedOn;
	private boolean npc;
	private boolean monster;

	public CharacterData(String id, int x, int y, int lastX, int lastY,
			Stats stats, ArrayList<Spell> spells, String aiType,
			CharacterData focusedOn, boolean npc, boolean monster) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.lastX = lastX;
		this.lastY = lastY;
		this.stats = stats;
		this.spells = spells;
		this.aiType = aiType;
		this.focusedOn = focusedOn;
		this.npc = npc;
		this.monster = monster;
	}

	public CharacterData(Character c) {
		this.id = c.getTrueID();
		this.x = c.getX();
		this.y = c.getY();
		this.lastX = c.getLastX();
		this.lastY = c.getLastY();
		this.stats = c.getStats().clone();
		this.spells = c.getSpells();
		this.aiType = c.getAiType();
		this.focusedOn = (c.getFocusedOn() != null) ? new CharacterData(c.getFocusedOn())
				: null;
		this.npc = c.isNpc();
		this.monster = c.isMonster();
	}
	
	public CharacterData(CharacterData c) {
		this.id = c.getId();
		this.x = c.getX();
		this.y = c.getY();
		this.lastX = c.getLastX();
		this.lastY = c.getLastY();
		this.stats = c.getStats().clone();
		this.spells = c.getSpells();
		this.aiType = c.getAiType();
		this.focusedOn = (c.getFocusedOn() != null) ? new CharacterData(c.getFocusedOn())
				: null;
		this.npc = c.isNpc();
		this.monster = c.isMonster();
	}

	/**
	 * Is intended to be used with alpha beta
	 * 
	 * @param position
	 * @throws IllegalMovementException
	 */
	public void moveAiTo(int x, int y) {
		lastX = this.x;
		lastY = this.y;
		this.x = x;
		this.y = y;

	}

	/**
	 * Use a spell given by its id, throws an {@link IllegalActionException}
	 * otherwise.
	 * 
	 * @param spellID
	 * @param direction
	 * @throws IllegalActionException
	 * @return damage
	 */
	public String useSpell(String spellID, int direction)
			throws IllegalActionException {
		Spell spell = this.getSpell(spellID);
		if (spell == null)
			throw new IllegalActionException("Spell unkown");
		// TODO handle the heal
		return spell.getDamage() + ":" + spell.getHeal();
	}

	public double distanceFrom(CharacterData c) {
		int a = x - c.x;
		int b = y - c.y;
		return Math.sqrt(a * a + b * b);
	}

	/**
	 * 
	 * @param damage
	 *            the damage value
	 * @param type
	 *            , type of damage (fire, ice, shock...)
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
		stats.setLife(stats.getLife() - damage);
		System.out.println(id + " take : [" + damage + "] damage");
	}

	public void heal(int heal) {
		stats.setLife(stats.getLife() + heal);
	}

	public boolean checkDeath() {
		return stats.getLife() <= 0;
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

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getLastX() {
		return lastX;
	}

	public void setLastX(int lastX) {
		this.lastX = lastX;
	}

	public int getLastY() {
		return lastY;
	}

	public void setLastY(int lastY) {
		this.lastY = lastY;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public ArrayList<Spell> getSpells() {
		return spells;
	}

	public void setSpells(ArrayList<Spell> spells) {
		this.spells = spells;
	}

	public String getAiType() {
		return aiType;
	}

	public void setAiType(String aiType) {
		this.aiType = aiType;
	}

	public CharacterData getFocusedOn() {
		return focusedOn;
	}

	public void setFocusedOn(CharacterData focusedOn) {
		this.focusedOn = focusedOn;
	}

	public boolean isNpc() {
		return npc;
	}

	public void setNpc(boolean npc) {
		this.npc = npc;
	}

	public boolean isMonster() {
		return monster;
	}

	public void setMonster(boolean monster) {
		this.monster = monster;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CharacterData other = (CharacterData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CharacterData [id=" + id + ", x=" + x + ", y=" + y
				+ ", aiType=" + aiType + ", npc=" + npc + ", monster="
				+ monster + "]";
	}
}
