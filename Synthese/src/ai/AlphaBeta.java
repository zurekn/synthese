package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import game.Player;
import game.Character;
import game.WindowGame;


public class AlphaBeta {
	private static AlphaBeta alphaBeta;
	//private static HashMap<String, LinkedList<int[]>>[] reachableBlocksMaps;
	
	private AlphaBeta(){}
	
	public static AlphaBeta getInstance(){
		if (alphaBeta == null) {
			alphaBeta = new AlphaBeta();
		}
		return alphaBeta;
	}
	
	
	private float h(Character c){
		return 0;
	}
	
	private float minValue(WindowGameData gameData, float alpha, float beta, int depth){
		Character currentCharacter = gameData.getCurrentCharacter();
		ArrayList<Character> characters = gameData.getCharacters();
		for(Character c : characters)
			System.out.print("");
		
		return 0 ;
	}
	
	private float maxValue(WindowGameData gameData, float alpha, float beta, int depth){
		return 0 ;
	}
	
	public LinkedList<String> getNpcCommands(ArrayList<Character> allies, ArrayList<Character> opponents){
		LinkedList<String> commands = new LinkedList<String>();
		AStar aStar = AStar.getInstance();
		
		
		return commands;
	}
}