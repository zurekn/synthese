package ai;

import java.util.LinkedList;

import game.Player;
import game.WindowGame;


public class AlphaBeta {
	private static AlphaBeta alphaBeta;
	
	private AlphaBeta(){
		
	}
	
	public static AlphaBeta getInstance(){
		if (alphaBeta == null) {
			alphaBeta = new AlphaBeta();
		}
		return alphaBeta;
	}
	
	
	private float h(){
		return 0;
	}
	
	private float minValue(float alpha, float beta, int depth){
		return 0 ;
	}
	
	private float maxValue(float alpha, float beta, int depth){
		return 0 ;
	}
	
	public LinkedList<String> getNpcCommands(Player p){
		LinkedList<String> commands = new LinkedList<String>();
		
		return commands;
	}
}