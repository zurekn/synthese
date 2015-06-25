package ai;

import java.util.ArrayList;
import java.util.Random;

import data.Data;
import data.HeroData;
import data.Stats;
import game.Character;
import game.Spell;

public class AlphaBeta {
	// TODO debug

	private static AlphaBeta alphaBeta;
	
	private TreeNode root;
	private int nodeCount = 0;
	private int max = 0;
	@SuppressWarnings("unused")
	private long startTime;
	private boolean halted = false;
	private float[][] coefMatrix;

	// private static HashMap<String, LinkedList<int[]>>[] reachableBlocksMaps;

	private AlphaBeta() {
		root = new TreeNode(null, "root", 0, 0.0f);
		coefMatrix = new float[Data.BLOCK_NUMBER_X][Data.BLOCK_NUMBER_Y];
		for (int i = 0; i < Data.BLOCK_NUMBER_X; i++) {
			for (int j = 0; j < Data.BLOCK_NUMBER_Y; j++) {
				try {
					coefMatrix[i][j] = Math.min(Data.AI_BORDERS[i],
							Data.AI_BORDERS[j]);
				} catch (ArrayIndexOutOfBoundsException e) {
					coefMatrix[i][j] = 0.f;
				}
			}
		}
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

		String aiType = c.getAiType().split(":")[0];
		int type = 3;
		if (aiType.equals("coward"))
			type = 0;
		else if (aiType.equals("lonewolf"))
			type = 1;
		else if (aiType.equals("normal"))
			type = 2;
		else if (aiType.equals("player"))
			type = 3;
		Stats characterStat = c.getStats();

		float heuristic = Data.AI_HEURISITCS[type][Data.LIFE]
				* ((float) characterStat.getLife() / (float) characterStat
						.getMaxLife())
				+ Data.AI_HEURISITCS[type][Data.MANA]
				* ((float) characterStat.getMana() / (float) characterStat
						.getMaxMana());

		for (CharacterData a : allies)
			heuristic += Data.AI_HEURISITCS[type][Data.ALLIES_LIFE]
					* ((float) a.getStats().getLife() / (float) a.getStats()
							.getMaxLife());

		for (CharacterData e : enemies)
			heuristic -= Data.AI_HEURISITCS[type][Data.ENEMIES_DETECTION]
					* ((float) e.getStats().getLife() / (float) e.getStats()
							.getMaxLife());

		heuristic -= coefMatrix[c.getX()][c.getY()];

		heuristic -= (float) Data.AI_HEURISITCS[type][Data.TARGET_DETECTION]
				* gameData.targetable(enemies, c);

		return heuristic;
	}

	private void detectFocus(WindowGameData gameData, CharacterData c) {
		String aiType = c.getAiType().split(":")[0];
		int type = 3;
		if (aiType.equals("coward"))
			type = 0;
		else if (aiType.equals("lonewolf"))
			type = 1;
		else if (aiType.equals("normal"))
			type = 2;
		else if (aiType.equals("player"))
			type = 3;

		if (((float) c.getStats().getLife() / (float) c.getStats().getMaxLife()) < Data.AI_FACTORS[type][Data.MIN_LIFE]) {
			c.setFocusedOn(null);
		} else {
			ArrayList<CharacterData> enemies = gameData.getNearEnemies(c);
			ArrayList<CharacterData> allies = gameData.getNearAllies(c);
			CharacterData focus = null;

			if (c.getFocusedOn() == null) {
				// search to heal an ally
				for (CharacterData a : allies) {
					if (((float) a.getStats().getLife() / (float) a.getStats()
							.getMaxLife()) < Data.AI_FACTORS[type][Data.BACKUP_ALLIES]) {
						focus = a;
						break;
					}
				}

				if (focus == null) {
					float alliesFactor = allies.size()
							* Data.AI_FACTORS[type][Data.ALLIES_DETECTION];
					float enemiesFactor = enemies.size()
							* Data.AI_FACTORS[type][Data.ENEMIES_DETECTION];
					float sum = alliesFactor + enemiesFactor;
					float dangerFactor = (sum != 0) ? enemiesFactor / sum : 0;

					if (dangerFactor > Data.AI_FACTORS[type][Data.RUNAWAY]
							&& !enemies.isEmpty()) { // if it's too dangerous
														// run
						int xMean = 0, yMean = 0;
						for (CharacterData e : enemies) {
							xMean += e.getX();
							yMean += e.getY();
						}
						xMean /= enemies.size();
						yMean /= enemies.size();

						int x = Math.abs(c.getX() - xMean);
						int y = Math.abs(c.getY() - yMean);
						focus = new CharacterData("point", c.getX() - x,
								c.getY() - y);
					} else if (dangerFactor <= Data.AI_FACTORS[type][Data.RUNAWAY] // if
																					// the
																					// situation
																					// is
																					// difficult
																					// stick
																					// to
																					// friends
							&& dangerFactor > Data.AI_FACTORS[type][Data.STICK_TO_ALLIES]
							&& !allies.isEmpty()) {
						int xMean = 0, yMean = 0;
						for (CharacterData a : allies) {
							xMean += a.getX();
							yMean += a.getY();
						}
						xMean /= allies.size();
						yMean /= allies.size();
						focus = new CharacterData("point", xMean, yMean);
					} else {
						if (!enemies.isEmpty()) {// try to focus an enemy
							CharacterData nearest = enemies.get(0);
							double d = c.distanceFrom(nearest);
							for (CharacterData e : enemies) {
								double dist = c.distanceFrom(e);
								if (e.isMonster()) {
									if (d > dist) {
										d = dist;
										nearest = e;
									}
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
							focus = nearest;
						} else if (!allies.isEmpty()) { // try to get the focus
														// of an ally

						}
					}
				}
				c.setFocusedOn(focus);
			} else { // if there's already a character focused
				// TODO handle unfocus cases
				focus = c.getFocusedOn();
				if (focus.getId().equals("point")
						|| focus.isMonster() == c.isMonster()) {// if previous
																// focus was a
																// point detect
																// another focus
					c.setFocusedOn(null);
					detectFocus(gameData, c);
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

		if (!spellDone) {
			detectFocus(gameData, character);

			// Spell Part
			ArrayList<Spell> spells = characterData.getSpells();

			CharacterData focus = character.getFocusedOn();
			for (Spell spell : spells) {
				if (spell.getMana() <= character.getStats().getMana()) {
					int range = spell.getRange();
					boolean damageSpell = spell.getDamage() > 0;
					ArrayList<CharacterData> targets = new ArrayList<CharacterData>();
					boolean focused = false;
					int direction = 0;
					try {
						direction = character.getSpellDirection(focus.getX(), focus.getY());
						if (focus.getId().equals("point") || direction == -1) {
							throw new NullPointerException();
						} else {
							targets.add(focus);
							focused = true;
						}
					} catch (NullPointerException npe) {
						targets = gameData.getTargetsInRange(character, range,
								damageSpell);
					}

					for (CharacterData target : targets) {
						boolean bool = false;
						if (focused) {
							if (gameData.isTarget(character, target,
									damageSpell))
								bool = true;
						} else {
							bool = true;
						}
						if (bool) {
							direction = character.getSpellDirection(
									target.getX(), target.getY());

							if (direction != -1) {
								data = gameData.clone();
								data.useSpell(character, spell, target);

								// Create node and add it to tree
								nodeCount++;
								TreeNode n = new TreeNode(node, spell.getId()
										+ ":" + direction, depth + 1);
								node.addSon(n);

								if (depth >= depthMax) {
									value = h(data, character);
									n.setMaxDepthReached(true);
								} else
									value = minValue(data, depth, depthMax, n,
											alpha, beta, character, true);

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

				try {
					if (focus.getStats().getLife() < 0) {
						character.setFocusedOn(null);
					}
				} catch (NullPointerException npe) {

				}

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

			CharacterData focus = character.getFocusedOn();
			for (Spell spell : spells) {
				if (spell.getMana() <= character.getStats().getMana()) {
					int range = spell.getRange();
					boolean damageSpell = spell.getDamage() > 0;
					ArrayList<CharacterData> targets = new ArrayList<CharacterData>();
					boolean focused = false;
					int direction = 0;
					try {
						direction = character.getSpellDirection(focus.getX(), focus.getY());
						if (focus.getId().equals("point") || direction == -1) {
							throw new NullPointerException();
						} else {
							targets.add(focus);
							focused = true;
						}
					} catch (NullPointerException npe) {
						targets = gameData.getTargetsInRange(character, range,
								damageSpell);
					}

					for (CharacterData target : targets) {
						boolean bool = false;
						if (focused) {
							if (gameData.isTarget(character, target,
									damageSpell))
								bool = true;
						} else {
							bool = true;
						}
						if (bool) {
							direction = character.getSpellDirection(
									target.getX(), target.getY());
							if (direction != -1) {
								data = gameData.clone();
								data.useSpell(character, spell, target);

								// Create node and add it to tree
								nodeCount++;
								TreeNode n = new TreeNode(node, spell.getId()
										+ ":" + direction, depth + 1);
								node.addSon(n);

								if (depth >= depthMax) {
									value = h(data, character);
									n.setMaxDepthReached(true);
								} else
									value = maxValue(data, depth, depthMax, n,
											alpha, beta, character, true);

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
				try {
					if (focus.getStats().getLife() < 0) {
						character.setFocusedOn(null);
					}
				} catch (NullPointerException npe) {

				}

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
		long time = System.currentTimeMillis() - startTime;
		if(time > Data.TIME_LIMIT){
			halted = true;
			return 0.f;
		}
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
		calculateNpcCommands(gameData, new CharacterData(character), false);

	}
	
	private void max(WindowGameData gameData, CharacterData characterData){
		System.err.println("Calculation halted for "+characterData.getId());
		CharacterData character = gameData.getCharacter(characterData);
		root = new TreeNode(null, "root", 0, 0.0f);
		detectFocus(gameData, character);
		WindowGameData data = null;
		// Spell Part
		ArrayList<Spell> spells = character.getSpells();

		CharacterData focus = character.getFocusedOn();
		for (Spell spell : spells) {
			if (spell.getMana() <= character.getStats().getMana()) {
				int range = spell.getRange();
				boolean damageSpell = spell.getDamage() > 0;
				ArrayList<CharacterData> targets = new ArrayList<CharacterData>();
				boolean focused = false;
				int direction = 0;
				try {
					direction = character.getSpellDirection(focus.getX(), focus.getY());
					if (focus.getId().equals("point") || direction == -1) {
						throw new NullPointerException();
					} else {
						targets.add(focus);
						focused = true;
					}
				} catch (NullPointerException npe) {
					targets = gameData.getTargetsInRange(character, range,
							damageSpell);
				}

				for (CharacterData target : targets) {
					boolean bool = false;
					if (focused) {
						if (gameData.isTarget(character, target,
								damageSpell))
							bool = true;
					} else {
						bool = true;
					}
					if (bool) {
						direction = character.getSpellDirection(
								target.getX(), target.getY());
						if (direction != -1) {
							data = gameData.clone();
							data.useSpell(character, spell, target);

							// Create node and add it to tree
							nodeCount++;
							TreeNode n = new TreeNode(root, spell.getId()
									+ ":" + direction, 1);
							root.addSon(n);

							n.setHeuristic(h(data, character));
							movements(n, data, character);
						}
					}
				}
			}
		}
		movements(root,gameData, character);
	}
	
	private void movements(TreeNode node, WindowGameData gameData, CharacterData character){
		ArrayList<int[]> positions = AStar.getInstance()
				.getReachableNodes(gameData, character);
		WindowGameData data = null;
		for (int[] position : positions) {
			int x = position[0], y = position[1];
			data = gameData.clone();
			data.move(character, x, y);

			// Create node and add it to tree
			nodeCount++;
			TreeNode n = new TreeNode(node, "m:" + x + ":" + y,node.getDepth()+1);
			node.addSon(n);

			n.setMaxDepthReached(true);

			n.setHeuristic(h(data, character));
			if(node.getHeuristic() < n.getHeuristic())
				node.setHeuristic(n.getHeuristic());
		}
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
		startTime = System.currentTimeMillis();
		root = new TreeNode(null, "root", 0, 0.0f);
		// int depthMax = Integer.parseInt(character.getAiType().split(":")[1]);
		int depthMax = 3;
		maxValue(gameData, 0, depthMax, root, Float.MIN_VALUE, Float.MAX_VALUE,
				character, spellDone);
		String cmd = "";
		
		if(halted)
			max(gameData, character);
			
		float value = Float.MIN_VALUE;
		ArrayList<String> commands = new ArrayList<String>();
		for (TreeNode n : root.getSons()) {
			if (n.getHeuristic() > value) {
				commands = new ArrayList<String>();
				value = n.getHeuristic();
			}
			if (n.getHeuristic() == value) {
				commands.add(n.getCommand());
			}
		}

		
		Random rand = new Random(System.nanoTime());
		cmd = commands.get(rand.nextInt(commands.size()));

		CommandHandler.getInstance().addCommand(cmd);
		/*
		 * if(Data.debug) System.out.println("Noeuds : " + nodeCount);
		 */
		max = Math.max(max, nodeCount);
		nodeCount = 0;

		gameData.doCommand(cmd);
		if (cmd.startsWith("m")) { // If command is a movement go to next
									// character
			CharacterData c = gameData.nextCharacter();
			if (c.isNpc()) {// if next character is npc, continue
				calculateNpcCommands(gameData, c, false);
			} else
				CommandHandler.getInstance().setCalculationDone(true);

		} else {// else search another command for the current character
			calculateNpcCommands(gameData, character, true);
		}
	}
}
