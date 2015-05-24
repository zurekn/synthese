package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import game.Player;
import game.Character;
import game.Spell;
import game.WindowGame;

public class AlphaBeta {
	private static AlphaBeta alphaBeta;
	private TreeNode root;
	private int nodeCount = 0;
	private long startTime;

	// private static HashMap<String, LinkedList<int[]>>[] reachableBlocksMaps;

	private AlphaBeta() {
		root = new TreeNode(null, "root", 0.0f);
	}

	public static AlphaBeta getInstance() {
		if (alphaBeta == null) {
			alphaBeta = new AlphaBeta();
		}
		return alphaBeta;
	}

	private float h(WindowGameData gameData, Character c) {
		return 0;
	}

	private float minValue(WindowGameData gameData, int depth, int depthMax,
			TreeNode node, float alpha, float beta, Character character,
			Spell spell) {
		float value;
		// TODO use spell

		for (int[] position : AStar.getInstance().getReachableNodes(character)) {
			value = Float.MAX_VALUE;
			int x = position[0], y = position[1];
			WindowGameData data = gameData.clone();
			data.move(character, x, y);
			// Create node and add it to tree
			nodeCount++;
			TreeNode n = new TreeNode(node, "m:" + x + ":" + y);
			node.addSon(n);

			if (depth >= depthMax)
				value = h(data, character);
			else
				value = evalNextCharacter(gameData, depth, depthMax, n, alpha,
						beta, character, spell);

			n.setHeuristic(value);
			if (value < beta) {
				beta = value;

			}
			if (beta <= alpha) {
				node.cut(n);
				return alpha;
			}
		}
		return beta;
	}

	private float maxValue(WindowGameData gameData, int depth, int depthMax,
			TreeNode node, float alpha, float beta, Character character,
			Spell spell) {
		float value;
		// TODO use spell

		for (int[] position : AStar.getInstance().getReachableNodes(character)) {
			value = Float.MAX_VALUE;
			int x = position[0], y = position[1];
			WindowGameData data = gameData.clone();
			data.move(character, x, y);
			TreeNode n = new TreeNode(node, "m:" + x + ":" + y);
			node.addSon(n);
			nodeCount++;
			if (depth >= depthMax)
				value = h(data, character);
			else
				value = evalNextCharacter(gameData, depth, depthMax, n, alpha,
						beta, character, spell);

			n.setHeuristic(value);
			if (value > alpha) {
				alpha = value;
			}
			if (alpha >= beta) {
				node.cut(n);
				return beta;
			}
		}
		return alpha;
	}

	private float evalNextCharacter(WindowGameData gameData, int depth,
			int depthMax, TreeNode node, float alpha, float beta, Character character, Spell spell) {
		Character c = gameData.nextCharacter();
		try{
			System.out.println(character.getTrueID()+"\t"+character.isMonster());
			System.out.println(c.getTrueID()+"\t"+c.isMonster());
			if(character.isMonster()==c.isMonster()){
				System.out.println("Tutu");
				return maxValue(gameData, depth, depthMax, node, alpha, beta, c, spell);
			}else{
				System.out.println("MIIIIINNNNNNNE");
				return minValue(gameData, depth, depthMax, node, alpha, beta, c, spell);}
			
		}catch(NullPointerException npe){
			//Pos at original character (the choosen one)
			System.out.println("Nombre de noeuds : "+nodeCount);
			System.out.println("Time : "+(System.currentTimeMillis()-startTime));
			nodeCount=0;
			return maxValue(gameData, depth + 1, depthMax, node, alpha, beta, gameData.nextCharacter(), spell);
		}
	}

	public String getNpcCommand(WindowGameData gameData, Character character) {
		root = new TreeNode(null, "root", 0.0f);
		startTime = System.currentTimeMillis();
		maxValue(gameData, 0, 2, root, Float.MIN_VALUE, Float.MAX_VALUE, character, null);
		return "";
	}
}