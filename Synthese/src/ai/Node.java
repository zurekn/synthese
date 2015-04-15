package ai;

public class Node {
	private Node parent;
	private int NodeX;
	private int NodeY;

	private int gScore;
	private int hScore;
	private int fScore;

	public Node(int NodeX, int NodeY) {
		this.parent = null;
		this.NodeX = NodeX;
		this.NodeY = NodeY;
		this.gScore = 0 ;
		this.hScore = 0 ;
		this.fScore = 0 ;
	}
	
	public Node(int NodeX, int NodeY, Node parent) {
		this.parent = parent;
		this.NodeX = NodeX;
		this.NodeY = NodeY;
		this.gScore = 0 ;
		this.hScore = 0 ;
		this.fScore = 0 ;
	}
	
	public Node(Node node, Node parent){
		this.parent = parent;
		this.NodeX = node.getX();
		this.NodeY = node.getY();
		this.gScore = node.getG() ;
		this.hScore = node.getH() ;
		this.fScore = node.getF() ;
	}

	public int compareTo(Node n) {
		if (fScore > n.getF())
			return 1;
		if (fScore < n.getF())
			return -1;

		return 0;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getX() {
		return NodeX;
	}

	public int getY() {
		return NodeY;
	}

	public int getG() {
		return gScore;
	}

	public void setG(int gScore) {
		this.gScore = gScore;
	}

	public int getH() {
		return hScore;
	}

	public void setH(int hScore) {
		this.hScore = hScore;
	}

	public int getF() {
		return fScore;
	}

	public void setF(int fScore) {
		this.fScore = fScore;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Node other = (Node) obj;
		if (NodeX != other.NodeX) {
			return false;
		}
		if (NodeY != other.NodeY) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Node [NodeX=" + NodeX + ", NodeY=" + NodeY + ", gScore="
				+ gScore + ", hScore=" + hScore + ", fScore=" + fScore + "]";
	}
}