package ai;

import java.util.ArrayList;
import java.util.Random;

import game.Character;
import game.Spell;

public class AlphaBeta {
	// TODO debug
	private static final int DEPTH_MAX = 2;
	private static AlphaBeta alphaBeta;
	private TreeNode root;
	private int nodeCount = 0;
	@SuppressWarnings("unused")
	private long startTime;

	// private static HashMap<String, LinkedList<int[]>>[] reachableBlocksMaps;

	private AlphaBeta() {
		root = new TreeNode(null, "root", 0, 0.0f);
	}

	public static AlphaBeta getInstance() {
		if (alphaBeta == null) {
			alphaBeta = new AlphaBeta();
		}
		return alphaBeta;
	}

	private float h(WindowGameData gameData, CharacterData c) {
		ArrayList<CharacterData> allies = gameData.getNearAllies(c);
		ArrayList<CharacterData> enemies = gameData.getNearEnemies(c);
		// TODO

		float heuristic = 10 * c.getStats().getLifePercentage();

		for (CharacterData a : allies)
			heuristic += a.getStats().getLifePercentage();

		for (CharacterData e : enemies)
			heuristic -= e.getStats().getLifePercentage();

		return heuristic;
	}

	private void detectFocus(WindowGameData gameData, CharacterData c) {
		ArrayList<CharacterData> enemies = gameData.getNearEnemies(c);
		ArrayList<CharacterData> allies = gameData.getNearAllies(c);

		if (c.getFocusedOn() == null) {
			if (!enemies.isEmpty()) {
				CharacterData nearest = enemies.get(0);
				double d = c.distanceFrom(nearest);
				for (CharacterData e : enemies) {
					if (d <= e.distanceFrom(c)) {
						d = e.distanceFrom(c);
						nearest = e;
					}
				}
				c.setFocusedOn(nearest);
			} else {
				if (!allies.isEmpty()) {
					CharacterData nearest = allies.get(0);
					double d = c.distanceFrom(nearest);
					for (CharacterData a : allies) {
						CharacterData focus = a.getFocusedOn();
						if (focus != null) {
							if (d <= focus.distanceFrom(c)) {
								d = focus.distanceFrom(c);
								nearest = focus;
							}
						}
					}
				}
			}
		}
	}

	private float minValue(WindowGameData gameData, int depth, int depthMax,
			TreeNode node, float alpha, float beta,
			CharacterData characterData, Spell spell) {
		float value;
		// Ensure that the object is the one within the gameData
		CharacterData character = gameData.getCharacter(characterData);

		WindowGameData data = null;

		detectFocus(gameData, character);
		// TODO use spell
		if (character.getFocusedOn() != null) {
			CharacterData focus = character.getFocusedOn();

			// Move
			ArrayList<int[]> positions = new ArrayList<int[]>();
			if (character.distanceFrom(focus) <= character.getStats()
					.getEyeSight()) {
				positions = AStar.getInstance().getReachableNodes(gameData,
						character, focus.getX(), focus.getY());
			} else {
				positions = AStar.getInstance().getReachableNodes(gameData,
						character, focus.getLastX(), focus.getLastY());
			}

			for (int[] position : positions) {
				value = Float.MAX_VALUE;
				int x = position[0], y = position[1];
				data = gameData.clone();
				data.move(character, x, y);

				// Create node and add it to tree
				nodeCount++;
				TreeNode n = new TreeNode(node, "m:" + x + ":" + y, depth + 1);
				node.addSon(n);

				if (depth >= depthMax) {
					value = h(data, character);
					n.setMaxDepthReached(true);
				} else
					value = evalNextCharacter(data, depth, depthMax, n, alpha,
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

			// If none character is focused, random movement
		} else {
			ArrayList<int[]> positions = AStar.getInstance().getReachableNodes(
					gameData, character);
			Random rand = new Random(System.nanoTime());
			int[] position = positions.get(rand.nextInt(positions.size()));
			int x = position[0], y = position[1];

			// Create node and add it to tree
			nodeCount++;
			TreeNode n = new TreeNode(node, "m:" + x + ":" + y, depth + 1);
			node.addSon(n);

			data = gameData.clone();
			data.move(character, x, y);

			value = h(data, character);
			n.setHeuristic(value);

			value = h(data, character);

			if (value < beta) {
				beta = value;

			}
			if (beta <= alpha) {
				node.cut(n);
				return alpha;
			}
			return beta;
		}
	}

	private float maxValue(WindowGameData gameData, int depth, int depthMax,
			TreeNode node, float alpha, float beta,
			CharacterData characterData, Spell spell) {
		float value;
		// Ensure that the object is the one within the gameData
		CharacterData character = gameData.getCharacter(characterData);

		WindowGameData data = null;

		detectFocus(gameData, character);
		// TODO use spell
		if (character.getFocusedOn() != null) {// if a a character is focused
			CharacterData focus = character.getFocusedOn();

			// Move
			ArrayList<int[]> positions = new ArrayList<int[]>();
			if (character.distanceFrom(focus) <= character.getStats()
					.getEyeSight()) {
				positions = AStar.getInstance().getReachableNodes(gameData,
						character, focus.getX(), focus.getY());
			} else {
				positions = AStar.getInstance().getReachableNodes(gameData,
						character, focus.getLastX(), focus.getLastY());
			}

			for (int[] position : positions) {
				value = Float.MAX_VALUE;
				int x = position[0], y = position[1];
				data = gameData.clone();
				data.move(character, x, y);

				// Create node and add it to tree
				nodeCount++;
				TreeNode n = new TreeNode(node, "m:" + x + ":" + y, depth + 1);
				node.addSon(n);

				if (depth >= depthMax) {
					value = h(data, character);
					n.setMaxDepthReached(true);
				} else
					value = evalNextCharacter(data, depth, depthMax, n, alpha,
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

			// If none character is focused, random movement
		} else {
			ArrayList<int[]> positions = AStar.getInstance().getReachableNodes(
					gameData, character);
			Random rand = new Random(System.nanoTime());
			int[] position = positions.get(rand.nextInt(positions.size()));
			int x = position[0], y = position[1];

			// Create node and add it to tree
			nodeCount++;
			TreeNode n = new TreeNode(node, "m:" + x + ":" + y, depth + 1);
			node.addSon(n);

			data = gameData.clone();
			data.move(character, x, y);

			value = h(data, character);
			n.setHeuristic(value);

			if (value > alpha) {
				alpha = value;
			}
			if (alpha >= beta) {
				node.cut(n);
				return beta;
			}
			return alpha;
		}
	}

	private float evalNextCharacter(WindowGameData gameData, int depth,
			int depthMax, TreeNode node, float alpha, float beta,
			CharacterData character, Spell spell) {
		CharacterData c = gameData.nextCharacter();
		if (nodeCount % 20000 == 0)
			System.out.println(nodeCount);
		try {
			/*
			 * System.out .println(character.getId() + "\t" +
			 * character.isMonster()); System.out.println(c.getId() + "\t" +
			 * c.isMonster());
			 */
			if (character.isMonster() == c.isMonster()) {
				// System.out.println("Tutu");
				return maxValue(gameData, depth, depthMax, node, alpha, beta,
						c, spell);
			} else {
				// System.out.println("MIIIIINNNNNNNE");
				return minValue(gameData, depth, depthMax, node, alpha, beta,
						c, spell);
			}

		} catch (NullPointerException npe) {
			// Pos at original character (the choosen one)
			/*
			 * System.out.println("Nombre de noeuds : " + nodeCount);
			 * System.out.println("Time : " + (System.currentTimeMillis() -
			 * startTime));
			 */
			// nodeCount = 0;
			return maxValue(gameData, depth + 1, depthMax, node, alpha, beta,
					gameData.nextCharacter(), spell);
		}
	}

	public void calculateNpcCommands(WindowGameData gameData,
			Character character) {
		startTime = System.currentTimeMillis();
		calculateNpcCommands(gameData, new CharacterData(character));

	}

	private void calculateNpcCommands(WindowGameData gameData,
			CharacterData character) {
		root = new TreeNode(null, "root", 0, 0.0f);
		maxValue(gameData, 0, DEPTH_MAX, root, Float.MIN_VALUE,
				Float.MAX_VALUE, character, null);
		String cmd = "";
		float value = Float.MIN_VALUE;
		for (TreeNode n : root.getSons()) {
			if (n.getHeuristic() > value) {
				value = n.getHeuristic();
				cmd = n.getCommand();
			}
		}
		CommandHandler.getInstance().addCommand(cmd);

		// if next character is npc, continue
		gameData.doCommand(cmd);
		CharacterData c = gameData.nextCharacter();
		if (c.isNpc()) {
			calculateNpcCommands(gameData, c);
		}else
			CommandHandler.getInstance().setCalculationDone(true);
	}
}