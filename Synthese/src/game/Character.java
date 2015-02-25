package game;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import data.Data;
import data.SpellD;
import data.Stats;
import exception.IllegalMovementException;

public abstract class Character {
	private int x;
	private int y;
	private String id;
	private Animation[] animation;
	private Stats stats;
	private boolean myTurn = false;
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	private String name;

	public abstract void render(GameContainer container, Graphics g);

	public abstract void init();


	/**
	 * 
	 * @param position
	 * @throws IllegalMovementException
	 */
public void moveTo(String position) throws IllegalMovementException {
		if (Data.untraversableBlocks.containsKey(position)) {
			throw new IllegalMovementException("Untraversable block at ["+position+"]");
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
					if (Math.sqrt(Math.pow(x - xTmp, 2) + Math.pow(y - yTmp, 2)) > movePoints) {
						throw new IllegalMovementException(
								"Not enough movements points");
					} else {
						this.x = x;
						this.y = y;
					}
				}
			}
		}
	}

	public void useSpell(Spell spell, int dir) {

	}

	public void useSpell(String spellID, int dir) {

	}

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

	public boolean checkDeath(){
		return stats.getLife() <= 0;
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

	public boolean getMyTurn() {
		return myTurn;
	}

	public void addSpell(SpellD s) {
		spells.add(new Spell(s.getId(), s.getName(), s.getDamage(),
				s.getHeal(), s.getMana(), s.getEvent()));
	}

	public ArrayList<Spell> getSpells(){
		return spells;
	}
	
	public void setSpells(ArrayList<Spell> spells){
		this.spells = spells;
	}
	
	public Spell getSpell(String spellID) {
		for (Iterator<Spell> it = spells.iterator(); it.hasNext();) {
			Spell s = it.next();
			if (s.getId().equals(spellID))
				return s;
		}
		return null;
	}

	
	public boolean isSpellLearned(String spellID) {
		for (Iterator<Spell> it = spells.iterator(); it.hasNext();) {
			Spell s = it.next();
			if (s.getId().equals(spellID))
				return true;
		}
		return false;
	}
}
