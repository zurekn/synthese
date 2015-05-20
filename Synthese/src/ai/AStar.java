//TODO optimize a star
package ai;

import game.WindowGame;
import game.Character;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import data.Data;

public class AStar {
	private Node initMap[][];
	private Node map[][];
	private ArrayList<String> positions = new ArrayList<String>();
	private Node goal;
	private LinkedList<Node> openList = new LinkedList<Node>();
	private LinkedList<Node> closedList = new LinkedList<Node>();
	private static final int WEIGHT = 1000;

	private static AStar aStar;

	private AStar(Set<String> obstacles, int mapX, int mapY) {
		this.map = new Node[mapX][mapY];
		this.initMap = new Node[mapX][mapY];

		for (int i = 0; i < mapX; i++) {
			for (int j = 0; j < mapY; j++) {
				map[i][j] = new Node(i, j);
				initMap[i][j] = new Node(i, j);
			}
		}

		for (String s : obstacles) {
			String[] tokens = s.split(":");
			int x = Integer.parseInt(tokens[0]);
			int y = Integer.parseInt(tokens[1]);
			initMap[x][y] = null;
			map[x][y] = null;
		}
	}

	public static AStar getInstance() {
		if (aStar == null) {
			aStar = new AStar(Data.untraversableBlocks.keySet(),
					Data.BLOCK_NUMBER_X, Data.BLOCK_NUMBER_Y);
		}
		return aStar;
	}

	public String[] pathfinder(Character c, int goalX, int goalY) {
		positions = WindowGame.getInstance().getAllPositions();
		goal = new Node(goalX, goalY);
		int gMax = c.getStats().getMovementPoints()*WEIGHT;
		String[] path = null;

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map[i][j] = initMap[i][j];
				if (initMap[i][j] != null)
					//Making new references to nodes
					initMap[i][j] = new Node(i, j);
			}
		}
		for (String s : positions) {
			String[] tokens = s.split(":");
			map[Integer.parseInt(tokens[0])][Integer.parseInt(tokens[1])] = null;
		}
		map[c.getX()][c.getY()] = new Node(c.getX(),c.getY());
		openList.add(map[c.getX()][c.getY()]);

		boolean done = false;

		while (!done && !openList.isEmpty()) {
			Node currentNode = getLowestFInOpen();			
			currentNode.setG(g(currentNode));
			closedList.add(currentNode);
			openList.remove(currentNode);
			if (currentNode.equals(goal)) {
				goal = currentNode;
				done = true;
			} else {
				// Different objects than in the Node matrices
				ArrayList<Node> adjacentNodes = getAdjacentNodes(currentNode);
				for (Node e : adjacentNodes) {
					int ind = openList.indexOf(e);
					int g = g(e);
					if (g <= gMax) {
						if (ind != -1) {//if it's in openList
							Node tmp = openList.get(ind);
							if (g < tmp.getG()) {
								tmp.setParent(e.getParent());
								tmp.setG(g);
							}
						} else {
							int x = e.getX(), y = e.getY();
							map[x][y].setG(g);
							map[x][y].setParent(e.getParent());
							openList.add(map[x][y]);
						}
					}
				}

			}
		}
		
		if (done) {
			LinkedList<Node> nodePath = new LinkedList<Node>();
			while (goal != null) {
				nodePath.add(goal);
				goal = goal.getParent();
			}
			path = new String[nodePath.size()];
			for (int i = 0; i < path.length; i++) {
				Node e = nodePath.pollLast();
				path[i] = e.getX() + ":" + e.getY();
			}
		}

		return path;
	}

	public LinkedList<int[]> getReachableNodes(Character c){
		LinkedList<int[]> list = new LinkedList<int[]>();
		positions = WindowGame.getInstance().getAllPositions();
		int gMax = c.getStats().getMovementPoints()*WEIGHT;

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map[i][j] = initMap[i][j];
				if (initMap[i][j] != null)
					initMap[i][j] = new Node(i, j);
			}
		}
		for (String s : positions) {
			String[] tokens = s.split(":");
			map[Integer.parseInt(tokens[0])][Integer.parseInt(tokens[1])] = null;
		}
		map[c.getX()][c.getY()] = new Node(c.getX(),c.getY());
		openList.add(map[c.getX()][c.getY()]);

		while (!openList.isEmpty()) {
			Node currentNode = getLowestFInOpen();			
			currentNode.setG(g(currentNode));
			closedList.add(currentNode);
			openList.remove(currentNode);
			
				// Different objects than in the Node matrices
				ArrayList<Node> adjacentNodes = getAdjacentNodes(currentNode);
				for (Node e : adjacentNodes) {
					int ind = openList.indexOf(e);
					int g = g(e);
					if (g <= gMax) {
						if (ind != -1) {//if it's in openList
							Node tmp = openList.get(ind);
							if (g < tmp.getG()) {
								tmp.setParent(e.getParent());
								tmp.setG(g);
							}
						} else {
							int x = e.getX(), y = e.getY();
							map[x][y].setG(g);
							map[x][y].setParent(e.getParent());
							openList.add(map[x][y]);
						}
					}
				}

			
		}
		
		for(Node e : closedList){
			int[] tab = new int[2];
			tab[0]=e.getX();
			tab[1]=e.getY();
			list.add(tab);
		}
		
		return list;
	}
	
	public int f(Node nodeA, Node nodeB) {
		return (h(nodeA, nodeB) + g(nodeA));
	}

	public int h(Node startNode, Node endNode) {
		int distance = (Math.abs(startNode.getX() - endNode.getX()) + Math
				.abs(startNode.getY() - endNode.getY()));

		return (distance * WEIGHT);
	}

	/**
	 * It's supposed that the parent node is adjacent to the node given in
	 * parameter
	 * 
	 * @param node
	 * @return
	 */
	public int g(Node node) {		
		if (node.getParent() == null) {
			return 0;
		}

		Node parent = node.getParent();
		if ((parent.getX() == node.getX() && parent.getY() != node.getY())
				|| (parent.getX() != node.getX() && parent.getY() == node
						.getY())) {
			return parent.getG() + 1 * WEIGHT;
		}

		if (parent.getX() != node.getX() && parent.getY() != node.getY()) {
			return (int) (parent.getG() + 1.414 * WEIGHT);
		}
		return 0;
	}

	private ArrayList<Node> getAdjacentNodes(Node current) {
		ArrayList<Node> list = new ArrayList<Node>();
		int x = current.getX();
		int y = current.getY();

		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i >= 0 && i < map.length)
					if (j >= 0 && j < map[i].length)
						if (map[i][j] != null && (i != x || j != y))
							if (!closedList.contains(map[i][j]))
								list.add(new Node(map[i][j], current));
			}
		}
		return list;
	}

	private Node getLowestFInOpen() {
		Node n = openList.get(0);
		for (int i = 1; i < openList.size(); i++) {
			if (f(n,goal) < f(openList.get(i),goal))
				n = openList.get(i);
		}
		return n;
	}
}