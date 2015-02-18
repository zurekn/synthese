package game;

import data.TrapD;

public class Block {
	private int x;
	private int y;
	private boolean traversable;
	private TrapD trap;
	private int damage;
	private String damageType;
	
	public Block(int x, int y) {
		this.x=x;
		this.y=y;
		this.traversable=true;
		this.trap=null;
		this.damageType=null;
		this.damage=0;
		
	}

	public Block(int x, int y, String damageType, int damage) {
		this(x,y);
		this.damageType=damageType;
		this.damage=damage;
	}

	public Block(int x, int y, boolean traversable) {
		this(x,y);
		this.traversable=traversable;
	}

	public Block(int x, int y, TrapD trap) {
		this(x,y);
		this.trap=trap;
	}

	public Block(int x, int y, boolean traversable, TrapD trap, String damageType, int damage) {
		this(x, y, damageType, damage);
		this.traversable = traversable;
		this.trap=trap;
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

	public boolean isTraversable() {
		return traversable;
	}

	public void setTraversable(boolean traversable) {
		this.traversable = traversable;
	}

	public boolean isTrapped() {
		return trap!=null;
	}
	
	public TrapD getTrap(){
		return trap;
	}

	public void setTrap(TrapD trap) {
		this.trap = trap;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public String getDamageType() {
		return damageType;
	}

	public void setDamageType(String damageType) {
		this.damageType = damageType;
	}
}
