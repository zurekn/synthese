package ai;

import java.util.ArrayList;

public class TreeNode {
	private String command="";
	private float heuristic=0.0f;
	private TreeNode father;
	private ArrayList<TreeNode> sons;
	private boolean maxDepthReached =false;
	private int depth;
	
	public TreeNode(TreeNode father, String command, int depth) {
		this.father =father;
		sons = new ArrayList<TreeNode>();
		this.command = command;
		this.depth = depth;
	}
	
	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = (depth < 0) ? 0 : depth;
	}

	public TreeNode(TreeNode father, String command, int depth, float heuristic) {
		this(father, command, depth);
		this.heuristic = heuristic;
	}
	
	public void addSon(TreeNode son){
		son.setFather(this);
		if(!sons.contains(son))
			sons.add(son);
	}
	
	public void cut(TreeNode son){
		sons.remove(son);
		System.gc();
	}
	
	public void cutAll(){
		sons.clear();
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreeNode other = (TreeNode) obj;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		return true;
	}

	public TreeNode getFather() {
		return father;
	}

	public void setFather(TreeNode father) {
		this.father = father;
	}

	public ArrayList<TreeNode> getSons() {
		return sons;
	}

	public void setSons(ArrayList<TreeNode> sons) {
		this.sons = sons;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public float getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(float heuristic) {
		this.heuristic = heuristic;
	}

	public boolean isMaxDepthReached() {
		return maxDepthReached;
	}

	public void setMaxDepthReached(boolean maxDepthReached) {
		this.maxDepthReached = maxDepthReached;
	}
	
	public void setRoot(){
		this.command = "root";
		this.father = null;
		this.heuristic = 0.f;
		this.maxDepthReached = false;
		this.depth = 0;
		for(TreeNode n : sons)
			reduceDepth(n);
	}
	
	private void reduceDepth(TreeNode node){
		node.setDepth(node.getDepth()-1);
		for(TreeNode n : node.getSons())
			reduceDepth(n);
	}
}
