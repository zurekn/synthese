package data;

import game.Spell;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

public class Monster {

	private String id;
	private String name;
	private SpriteSheet sprite;
	private Animation[] animation = new Animation[8];
	private Stats stats;
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	private String aiType;
	
	public Monster(String id, String aiType, String name, SpriteSheet sprite, Stats stats) {
		super();
		this.id = id;
		this.sprite = sprite;
		this.stats = stats;
		this.name = name;
		this.aiType = aiType;
		initAnimation();
	}
	
	public Monster(String id, String aiType, SpriteSheet sprite, Stats stats, ArrayList<Spell> spells) {
		super();
		this.id = id;
		this.sprite = sprite;
		this.stats = stats;
		this.spells = spells;
		this.aiType = aiType;
		initAnimation();
	}

	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SpriteSheet getSprite() {
		return sprite;
	}

	public void setSprite(SpriteSheet sprite) {
		this.sprite = sprite;
	}

	public Animation[] getAnimation() {
		return animation;
	}

	public void setAnimation(Animation[] animation) {
		this.animation = animation;
	}

	private void initAnimation(){
		this.animation[0] = loadAnimation(getSprite(), 0, 1, 0);
		this.animation[1] = loadAnimation(getSprite(), 0, 1, 1);
		this.animation[2] = loadAnimation(getSprite(), 0, 1, 2);
		this.animation[3] = loadAnimation(getSprite(), 0, 1, 3);
		this.animation[4] = loadAnimation(getSprite(), 1, 3, 0);
		this.animation[5] = loadAnimation(getSprite(), 1, 3, 1);
		this.animation[6] = loadAnimation(getSprite(), 1, 3, 2);
		this.animation[7] = loadAnimation(getSprite(), 1, 3, 3);
	}
	
	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
	    Animation animation = new Animation();
	    for (int x = startX; x < endX; x++) {
	        animation.addFrame(spriteSheet.getSprite(x, y), 100);
	    }
	    return animation;
	}
	
	@Override
	public String toString() {
		return "Monster [id=" + id + ", name=" + name + ", sprite=" + sprite + ", animation="
				+ animation + "]";
	}

	public void addSpell(SpellD s) {
		//TODO
		spells.add(new Spell(s.getId(), s.getName(), s.getDamage(), s.getHeal(), s.getMana(), s.getRange(), s.getType(), s.getEvent()));		
	}
	
	public Stats getStats(){
		return stats;
	}

	public ArrayList<Spell> getSpells() {
		return spells;
	}

	public String getAiType() {
		return this.aiType;
	}

}
