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

		/*
		float enemiesThreat, careForAllies, backUpAllies;
		
		String type = c.getAiType().split(":")[0];
		switch(type){
		case "coward":
			enemiesThreat =2.f;
			careForAllies = 2.f;
			backUpAllies = 1.f;
			break;
		case "normal":
			enemiesThreat =1.f;
			careForAllies = 1.f;
			backUpAllies = 1.f;
			break;
		case "lonewolf":
			enemiesThreat =1.f;
			careForAllies = 0.2f;
			backUpAllies = 0.35f;
			break;
		default:
			enemiesThreat =1.f;
			careForAllies = 1.f;
			backUpAllies = 1.f;
		}
		
		*/
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
			CharacterData characterData, boolean spellDone) {
		float value;
		// Ensure that the object is the one within the gameData
		CharacterData character = gameData.getCharacter(characterData);

		WindowGameData data = null;

		detectFocus(gameData, character);

		if (!spellDone) {
			// Spell Part
			ArrayList<Spell> spells = characterData.getSpells();

			for (Spell spell : spells) {
				if (spell.getMana() <= character.getStats().getMana()) {
					int range = spell.getRange();
					boolean damageSpell = spell.getDamage() > 0;
					ArrayList<CharacterData> targets = gameData
							.getTargetsInRange(character, range, damageSpell);
					for (CharacterData target : targets) {
						data = gameData.clone();
						data.useSpell(character, spell, target);

						// Create node and add it to tree
						nodeCount++;
						int direction = character.getSpellDirection(
								target.getX(), target.getY());
						TreeNode n = new TreeNode(node, spell.getId() + ":"
								+ direction, depth + 1);
						node.addSon(n);

						if (depth >= depthMax) {
							value = h(data, character);
							n.setMaxDepthReached(true);
						} else
							value = minValue(data, depth, depthMax, n, alpha,
									beta, character, true);

						n.setHeuristic(value);
						if (value < beta) {
							beta = value;

						}
						if (beta <= alpha) {
							node.cut(n);
							return alpha;
						}
					}
				}
			}
			// try with only movement
			value = minValue(gameData, depth, depthMax, node, alpha, beta,
					character, true);
			if (value < beta) {
				beta = value;

			}
			if (beta <= alpha) {
				return alpha;
			}

			return beta;
		} else {
			// Movement part

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
					TreeNode n = new TreeNode(node, "m:" + x + ":" + y,
							depth + 1);
					node.addSon(n);

					if (depth >= depthMax) {
						value = h(data, character);
						n.setMaxDepthReached(true);
					} else
						value = evalNextCharacter(data, depth, depthMax, n,
								alpha, beta, character, spellDone);

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
				ArrayList<int[]> positions = AStar.getInstance()
						.getReachableNodes(gameData, character);
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
	}

	private float maxValue(WindowGameData gameData, int depth, int depthMax,
			TreeNode node, float alpha, float beta,
			CharacterData characterData, boolean spellDone) {
		float value;
		// Ensure that the object is the one within the gameData
		CharacterData character = gameData.getCharacter(characterData);

		WindowGameData data = null;

		detectFocus(gameData, character);

		if (!spellDone) {
			// Spell Part
			ArrayList<Spell> spells = characterData.getSpells();

			for (Spell spell : spells) {
				if (spell.getMana() <= character.getStats().getMana()) {
					int range = spell.getRange();
					boolean damageSpell = spell.getDamage() > 0;
					ArrayList<CharacterData> targets = gameData
							.getTargetsInRange(character, range, damageSpell);
					for (CharacterData target : targets) {
						data = gameData.clone();
						data.useSpell(character, spell, target);

						// Create node and add it to tree
						nodeCount++;
						int direction = character.getSpellDirection(
								target.getX(), target.getY());
						TreeNode n = new TreeNode(node, spell.getId() + ":"
								+ direction, depth + 1);
						node.addSon(n);

						if (depth >= depthMax) {
							value = h(data, character);
							n.setMaxDepthReached(true);
						} else
							value = maxValue(data, depth, depthMax, n, alpha,
									beta, character, true);

						n.setHeuristic(value);
						if (value > alpha) {
							alpha = value;
						}
						if (alpha >= beta) {
							node.cut(n);
							return beta;
						}
					}
				}
			}

			// try with only movement
			value = maxValue(gameData, depth, depthMax, node, alpha, beta,
					character, true);
			if (value > alpha) {
				alpha = value;
			}
			if (alpha >= beta) {
				return beta;
			}
			return alpha;
		} else {
			// Movement part
			if (character.getFocusedOn() != null) {// if a a character is
													// focused
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
					TreeNode n = new TreeNode(node, "m:" + x + ":" + y,
							depth + 1);
					node.addSon(n);

					if (depth >= depthMax) {
						value = h(data, character);
						n.setMaxDepthReached(true);
					} else
						value = evalNextCharacter(data, depth, depthMax, n,
								alpha, beta, character, spellDone);

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
				ArrayList<int[]> positions = AStar.getInstance()
						.getReachableNodes(gameData, character);
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
	}

	private float evalNextCharacter(WindowGameData gameData, int depth,
			int depthMax, TreeNode node, float alpha, float beta,
			CharacterData character, boolean spellDone) {
		CharacterData c = gameData.nextCharacter();
		try {
			/*
			 * System.out .println(character.getId() + "\t" +
			 * character.isMonster()); System.out.println(c.getId() + "\t" +
			 * c.isMonster());
			 */
			if (character.isMonster() == c.isMonster()) {
				// System.out.println("Tutu");
				return maxValue(gameData, depth, depthMax, node, alpha, beta,
						c, false);
			} else {
				// System.out.println("MIIIIINNNNNNNE");
				return minValue(gameData, depth, depthMax, node, alpha, beta,
						c, false);
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
					gameData.nextCharacter(), spellDone);
		}
	}

	public void calculateNpcCommands(WindowGameData gameData,
			Character character) {
		startTime = System.currentTimeMillis();
		calculateNpcCommands(gameData, new CharacterData(character),false);

	}

	/**
	 * if spellDone is set to true, the algorithm will not search for a movement
	 * @param gameData
	 * @param character
	 * @param spellDone
	 */
	private void calculateNpcCommands(WindowGameData gameData,
			CharacterData character, boolean spellDone) {
		root = new TreeNode(null, "root", 0, 0.0f);
		int depthMax = Integer.parseInt(character.getAiType().split(":")[1]);
		maxValue(gameData, 0, depthMax, root, Float.MIN_VALUE, Float.MAX_VALUE,
				character, spellDone);
		String cmd = "";
		float value = Float.MIN_VALUE;
		for (TreeNode n : root.getSons()) {
			if (n.getHeuristic() > value) {
				value = n.getHeuristic();
				cmd = n.getCommand();
			}
		}
		CommandHandler.getInstance().addCommand(cmd);
		System.out.println("Noeuds : " + nodeCount);

		
		gameData.doCommand(cmd);
		if (cmd.startsWith("m")) { //If command is a movement go to next character
			CharacterData c = gameData.nextCharacter();
			if (c.isNpc()) {// if next character is npc, continue
				nodeCount = 0;
				calculateNpcCommands(gameData, c, false);
			} else
				CommandHandler.getInstance().setCalculationDone(true);
		} else {//else search another command for the current character
			calculateNpcCommands(gameData, character, true);
		}
	}
}