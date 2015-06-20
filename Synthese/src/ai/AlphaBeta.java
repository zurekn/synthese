package ai;

//TODO handle allies in spell trajectory
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import data.HeroData;
import game.Character;
import game.Spell;

public class AlphaBeta {
	// TODO debug
	private static final int DEPTH_MAX = 2;
	private static final float[][] AI_FACTORS = {
			{ 2.f, 1.5f, 0.75f, 0.2f, 0.50f, 0.3f }, // coward
			{ 1.f, 0.5f, 0.40f, 0.3f, 0.70f, 0.9f }, // lonewolf
			{ 1.f, 1.0f, 0.50f, 0.5f, 0.80f, 0.6f }, // normal
			{ 1.f, 1.0f, 0.50f, 0.5f, 0.80f, 0.6f } // player
	};

	private static final int COWARD = 0;
	private static final int LONEWOLF = 1;
	private static final int NORMAL = 2;
	private static final int PLAYER = 3;

	private static final int ENEMIES_DETECTION = 0; 
	private static final int ALLIES_DETECTION = 1;
	private static final int MIN_LIFE = 2; //min percentage life before unfocus
	private static final int BACKUP_ALLIES = 3; //min percentage of allies' life to heal them
	private static final int RUNAWAY = 4; // min danger level to run away
	private static final int STICK_TO_ALLIES = 5; //min danger level to go near allies 

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
		String aiType = c.getAiType().split(":")[0];
		int type = 3;
		if(aiType.equals("coward"))
			type = 0;
		else if(aiType.equals("lonewolf"))
			type = 1;
		else if(aiType.equals("normal"))
			type = 2;
		else if(aiType.equals("player"))
			type = 3;

		if (c.getStats().getLifePercentage() < AI_FACTORS[type][MIN_LIFE]) {
			c.setFocusedOn(null);
		} else {
			ArrayList<CharacterData> enemies = gameData.getNearEnemies(c);
			ArrayList<CharacterData> allies = gameData.getNearAllies(c);
			CharacterData focus = null;

			if (c.getFocusedOn() == null) {
				// search to heal an ally
				for (CharacterData a : allies) {
					if (a.getStats().getLifePercentage() < AI_FACTORS[type][BACKUP_ALLIES]) {
						focus = a;
						break;
					}
				}

				if (focus == null) {
					float alliesFactor = allies.size()
							* AI_FACTORS[type][ALLIES_DETECTION];
					float enemiesFactor = enemies
							.size() * AI_FACTORS[type][ENEMIES_DETECTION];
					float sum = alliesFactor + enemiesFactor;
					float dangerFactor = (sum!=0) ? enemiesFactor/sum : 0;

					if (dangerFactor <= AI_FACTORS[type][RUNAWAY] && !enemies.isEmpty()) {
						int xMean = 0, yMean = 0;
						for (CharacterData e : enemies) {
							xMean += e.getX();
							yMean += e.getY();
						}
						xMean /= enemies.size();
						yMean /= enemies.size();
						focus = new CharacterData("point", -xMean, -yMean);
					} else if (dangerFactor > AI_FACTORS[type][RUNAWAY]
							&& dangerFactor <= AI_FACTORS[type][STICK_TO_ALLIES] && !allies.isEmpty()) {
						int xMean = 0, yMean = 0;
						for (CharacterData a : allies) {
							xMean += a.getX();
							yMean += a.getY();
						}
						xMean /= allies.size();
						yMean /= allies.size();
						focus = new CharacterData("point", xMean, yMean);
					} else {
						if (!enemies.isEmpty()) {
							CharacterData nearest = enemies.get(0);
							double d = c.distanceFrom(nearest);
							for (CharacterData e : enemies) {
								double dist = c.distanceFrom(e);
								if (e.isMonster() && d > dist) {
									d = dist;
									nearest = e;
								} else {// if it's a hero
									if (HeroData.CLASSES_VALUES.get(nearest
											.getStats().getCharacterClass()) < HeroData.CLASSES_VALUES
											.get(nearest.getStats()
													.getCharacterClass())
											&& dist < d) {
										d = dist;
										nearest = e;
									}
								}
							}
						}
					}
				}
				c.setFocusedOn(focus);
			} else { // if there's already a character focused
				// TODO handle unfocus cases
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

		if (!spellDone) {
			detectFocus(gameData, character);

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

				// If none character is focused, single movement
			} else {
				ArrayList<int[]> positions = AStar.getInstance()
						.getReachableNodes(gameData, character);
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

					value = h(data, character);
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
		}
	}

	private float maxValue(WindowGameData gameData, int depth, int depthMax,
			TreeNode node, float alpha, float beta,
			CharacterData characterData, boolean spellDone) {
		float value;
		// Ensure that the object is the one within the gameData
		CharacterData character = gameData.getCharacter(characterData);

		WindowGameData data = null;

		if (!spellDone) {
			detectFocus(gameData, character);

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
					value = Float.MIN_VALUE;
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

				// If none character is focused, single movement
			} else {
				ArrayList<int[]> positions = AStar.getInstance()
						.getReachableNodes(gameData, character);
				for (int[] position : positions) {
					
					value = Float.MIN_VALUE;
					int x = position[0], y = position[1];
					data = gameData.clone();
					data.move(character, x, y);

					// Create node and add it to tree
					nodeCount++;
					TreeNode n = new TreeNode(node, "m:" + x + ":" + y,
							depth + 1);
					node.addSon(n);

					value = h(data, character);
					n.setMaxDepthReached(true);
					
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
			CharacterData next = gameData.nextCharacter();
			next = gameData.nextCharacter();
			return maxValue(gameData, depth + 1, depthMax, node, alpha, beta,
					next, false);
		}
	}

	public void calculateNpcCommands(WindowGameData gameData,
			Character character) {
		startTime = System.currentTimeMillis();
		calculateNpcCommands(gameData, new CharacterData(character), false);

	}

	/**
	 * if spellDone is set to true, the algorithm will not search for a movement
	 * 
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
		if (cmd.startsWith("m")) { // If command is a movement go to next
									// character
			CharacterData c = gameData.nextCharacter();
			if (c.isNpc()) {// if next character is npc, continue
				nodeCount = 0;
				calculateNpcCommands(gameData, c, false);
			} else
				CommandHandler.getInstance().setCalculationDone(true);

		} else {// else search another command for the current character
			calculateNpcCommands(gameData, character, true);
		}
	}
}
