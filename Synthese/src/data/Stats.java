package data;

public class Stats {
	private int life = 1;
	private int armor = 1;
	private int mana = 1;
	private int strength = 1;
	private int magicPower = 1;
	private int luck = 1;
	private int movementPoints = 1;
	private int magicResist = 1;
	private String characterClass = "";

	public Stats(int life, int mana) {
		super();
		this.life = life;
		this.mana = mana;
	}

	public Stats(int life, int armor, int mana, int strength, int magicPower,
			int luck, int movementPoints, int magicResist) {
		super();
		this.life = life;
		this.armor = armor;
		this.mana = mana;
		this.strength = strength;
		this.magicPower = magicPower;
		this.luck = luck;
		this.movementPoints = movementPoints;
		this.magicResist = magicResist;
	}

	public Stats(int life, int armor, int mana, int strength, int magicPower,
			int luck, int movementPoints, int magicResist, String caracterClass) {
		super();
		this.life = life;
		this.armor = armor;
		this.mana = mana;
		this.strength = strength;
		this.magicPower = magicPower;
		this.luck = luck;
		this.movementPoints = movementPoints;
		this.magicResist = magicResist;
		this.characterClass = caracterClass;
	}

	public String getCharacterClass() {
		return characterClass;
	}

	public void setCharacterClass(String characterClass) {
		this.characterClass = characterClass;
	}

	public int getMagicResist() {
		return magicResist;
	}

	public void setMagicResist(int magicResist) {
		this.magicResist = magicResist;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getArmor() {
		return armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getMagicPower() {
		return magicPower;
	}

	public void setMagicPower(int magicPower) {
		this.magicPower = magicPower;
	}

	public int getLuck() {
		return luck;
	}

	public void setLuck(int luck) {
		this.luck = luck;
	}

	public int getMovementPoints() {
		return movementPoints;
	}

	public void setMovementPoints(int movementPoints) {
		this.movementPoints = movementPoints;
	}

	@Override
	public String toString() {
		return "Stats [life=" + life + ", armor=" + armor + ", mana=" + mana
				+ ", strength=" + strength + ", magicPower=" + magicPower
				+ ", luck=" + luck + ", movementPoints=" + movementPoints
				+ ", magicResist=" + magicResist + ", characterClass="
				+ characterClass + "]";
	}



}
