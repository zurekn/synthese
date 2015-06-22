package ai;

//TODO handle allies in spell trajectory
import java.util.ArrayList;
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
		Stats characterStat = c.getStats();

		float heuristic = Data.AI_HEURISITCS[type][Data.LIFE] * characterStat.getLifePercentage() + Data.AI_HEURISITCS[type][Data.LIFE] * characterStat.getManaPercentage();

		for (CharacterData a : allies)
			heuristic += Data.AI_HEURISITCS[type][Data.ALLIES_LIFE]* a.getStats().getLifePercentage();

		for (CharacterData e : enemies)
			heuristic -= Data.AI_HEURISITCS[type][Data.ENEMIES_DETECTION]*e.getStats().getLifePercentage();

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

		if (c.getStats().getLifePercentage() < Data.AI_FACTORS[type][Data.MIN_LIFE]) {
			c.setFocusedOn(null);
		} else {
			ArrayList<CharacterData> enemies = gameData.getNearEnemies(c);
			ArrayList<CharacterData> allies = gameData.getNearAllies(c);
			CharacterData focus = null;

			if (c.getFocusedOn() == null) {
				// search to heal an ally
				for (CharacterData a : allies) {
					if (a.getStats().getLifePercentage() < Data.AI_FACTORS[type][Data.BACKUP_ALLIES]) {
						focus = a;
						break;
					}
				}

				if (focus == null) {
					float alliesFactor = allies.size()
							* Data.AI_FACTORS[type][Data.ALLIES_DETECTION];
					float enemiesFactor = enemies
							.size() * Data.AI_FACTORS[type][Data.ENEMIES_DETECTION];
					float sum = alliesFactor + enemiesFactor;
					float dangerFactor = (sum!=0) ? enemiesFactor/sum : 0;

					if (dangerFactor <= Data.AI_FACTORS[type][Data.RUNAWAY] && !enemies.isEmpty()) {
						int xMean = 0, yMean = 0;
						for (CharacterData e : enemies) {
							xMean += e.getX();
							yMean += e.getY();
						}
						xMean /= enemies.size();
						yMean /= enemies.size();
						focus = new CharacterData("point", -xMean, -yMean);
					} else if (dangerFactor > Data.AI_FACTORS[type][Data.RUNAWAY]
							&& dangerFactor <= Data.AI_FACTORS[type][Data.STICK_TO_ALLIES] && !allies.isEmpty()) {
						int xMean = 0, yMean = 0;
						for (CharacterData a : allies) {
							xMean += a.getX();
							yMean += a.getY();
						}
						xMean /= allies.size();
						yMean /= allies.size();
						focus = new CharacterData("point", xMean, yMean);
					} else {
						if (!enemies.isEmpty()) {//try to focus an enemy
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
							focus = nearest;
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
		if(Data.debug)
		System.out.println("Noeuds : " + nodeCount);
		nodeCount=0;
		

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
	
	@Deprecated
	private float min(WindowGameData gameData, int depth, int depthMax,
			TreeNode node, float alpha, float beta,
			CharacterData characterData, boolean spellDone){
		float value = 0.f;
		
		CharacterData character = gameData.getCharacter(characterData);

		WindowGameData data = null;

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
							value = min(data, depth, depthMax, n, alpha,
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
			value = min(gameData, depth, depthMax, node, alpha, beta,
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
				ArrayList<int[]> positions = AStar.getInstance().getReachableNodes(gameData, character);

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
						value = next(data, depth, depthMax, n,
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
		}
	}
	
	@Deprecated
	private float max(WindowGameData gameData, int depth, int depthMax,
			TreeNode node, float alpha, float beta,
			CharacterData characterData, boolean spellDone){
		float value = 0.f;
		CharacterData character = gameData.getCharacter(characterData);

		WindowGameData data = null;

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
							value = max(data, depth, depthMax, n, alpha,
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
			value = max(gameData, depth, depthMax, node, alpha, beta,
					character, true);
			if (value > alpha) {
				alpha = value;
			}
			if (alpha >= beta) {
				return beta;
			}
			return alpha;
		} else {
			ArrayList<int[]> positions = AStar.getInstance().getReachableNodes(gameData, character);
	
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
						value = next(data, depth, depthMax, n,
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

			
		}
	}
	
	@Deprecated
	private float next(WindowGameData gameData, int depth,
			int depthMax, TreeNode node, float alpha, float beta,
			CharacterData character, boolean spellDone){

		if(nodeCount > 100000)
			System.out.println("fuck");
		CharacterData c = gameData.nextCharacter();
		try {
			if (character.isMonster() == c.isMonster()) {
				return max(gameData, depth, depthMax, node, alpha, beta,
						c, false);
			} else {
				return min(gameData, depth, depthMax, node, alpha, beta,
						c, false);
			}

		} catch (NullPointerException npe) {
			CharacterData next = gameData.nextCharacter();
			next = gameData.nextCharacter();
			return max(gameData, depth + 1, depthMax, node, alpha, beta,
					next, false);
		}
	}
}
