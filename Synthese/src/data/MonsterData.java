package data;

import java.util.ArrayList;

import org.newdawn.slick.Animation;

public class MonsterData {
	
	public static ArrayList<Monster> monsters = new ArrayList<Monster>();
	
	public void addMonster(Monster m){
		monsters.add(m);
	}
	
	public static Animation[] getAnimationById(String id){
		for(int i = 0; i < monsters.size(); i++){
			if(monsters.get(i).getId().equals(id)){
				return monsters.get(i).getAnimation();
			}
		}
		return null;
	}
	
	public static Monster getMonsterById(String id){
		for(int i = 0; i < monsters.size(); i++){
			if(monsters.get(i).getId().equals(id)){
				return monsters.get(i);
			}
		}
		return null;
	}
	
}
