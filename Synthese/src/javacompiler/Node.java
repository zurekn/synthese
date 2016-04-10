package javacompiler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Node implements Serializable {
	private Node parent = null;
	private ArrayList<Node> children = null;
	private String value;
	private int id;
	//private Node bro = null;
	
	public Node(String value)
	{
	    this.children = new ArrayList<Node>();
	    this.value = value;
	}
	
	/*
	 * Suppression d'un Noeud
	 */
	public void removeNode() 
	{
		if (parent != null) 
			parent.removeChild(this);
	}
	
	/*
	 * Suppression d'un fils spécifique
	 */
	public void removeChild(Node child) 
	{
		if (children.contains(child))
		{	
			//int temp = children.indexOf(child);
			children.remove(child);
			//if(temp <= children.size()-1)
				//children.get(temp).setBro(children.get(temp+1));
		}
	}
	
	/** Replace a child with another node
	 * 
	 * @param child
	 * @param newChild
	 */
	public void replaceChild(Node child, Node newChild)
	{
		int index =0;
		if (children.contains(child))
		{	
			index=children.indexOf(child);
			children.set(index, newChild);
		}
	}
	
	/*
	 * Ajout d'un fils
	 */
	public void addChild(Node child) 
	{
		child.parent = this;
		//if (!children.contains(child))
		//{	
			children.add(child);
			//children.get(children.indexOf(child)-1).setBro(child);
		//}
	}
	

	/*
	 * Transforme un arbre en une liste de Node
	 */
	public ArrayList<Node> TreeToArray(Node node, ArrayList<Node> treeArray)
	{
		treeArray.add(node);
		for(Node child: node.getChildren())
			TreeToArray(child, treeArray);				
		return treeArray;
	}

	/*
	 * distance from root
	 */
	public int getLevel() 
	{
		int level = 0;
		Node p = parent;
		while (p != null) {
			++level;
			p = p.parent;
		}
		return level;
	}
	
	/*
	 * Affichage de toute l'arborescence à partir de ce noeud
	 */
	public void displayTree()
	{
		System.out.println(this.getValue());
		for(Node n : this.getChildren())
			n.displayTree();
	}
	
	public void displayNode()
	{
		System.out.println("parent : "+(hasParent()?this.parent.getValue():"")+" value : "+ this.value);
	}
	
	public boolean hasParent(){
		if(this.parent == null)
			return false;
		else 
			return true;
	}
	
	
	/*
	 * Parcours en largeur de l'arbre.
	 * Pour ce faire il faut mettre les noeuds dans une file. 
	 * Un accès aléatoire (si id == 0) permet de récupérer le Node
	 * Si id > 0 alors on récupère le Noeud 
	 */
	public Node getSubTree(int id)
	{
		System.out.println("\t=== getSubTree ===");
		ArrayList<Node> arrayNode = new ArrayList<Node>();
		Node returNode;
		//arrayNode = TreeToArray(this, arrayNode);
		arrayNode= this.getChildren();
		if(arrayNode != null && arrayNode.size() > 0)
		if(id < 0 || id >= arrayNode.size()){ // Si id n'est pas un des fils, on en prend un aléatoirement
			returNode = arrayNode.get(new Random().nextInt(arrayNode.size())); // Noeud aléatoire parmi les fils direct
			if(returNode.getChildren() != null){
				System.out.println("-noeud avec fils .. 50% de prendre ses fils");
				return (new Random().nextInt(2)==0?returNode:returNode.getSubTree(id)); // 50% de chance de prendre ses fils directs
			}else {
				System.out.println("--noeud sans fils");
				return returNode; // Noeud sans fils
			}
		}else{
			System.out.println("choix du noeud numero "+id);
			return arrayNode.get(id); // On prend le fils id
		}
		else
			return this;
	}
	
	Node lastFounded;
	/** Search a node with given value. if this Node has children, 50% to continue the search.
	 * Return random child if not found valued child.
	 * 
	 * @param value
	 * @return
	 */
	public Node searchSubTreeByValue(String value){
		ArrayList<Node> childNodes = new ArrayList<Node>();
		childNodes = this.getChildren(); // get children
		lastFounded = null;
		for(Node resNode : childNodes){ // for each child
			if(resNode.value.equals(value)){ // if we find the searched value
				if(resNode.getChildren()==null){
					return resNode;// we return the node if it has no children
				}else{// 50% to return the node if it has children
					lastFounded = resNode;// we save the node in case we don't find any matching not later.
					return (new Random().nextInt(2)==0?resNode:resNode.searchSubTreeByValue(value));
				}
					//return resNode;

			}else if(resNode.getChildren()!=null && resNode.getChildren().size()>0){ // if node has children but has not value searched
				return resNode.searchSubTreeByValue(value); // Recursively search value
			}
		}
		if(lastFounded != null)// if we founded matching nodes, we return the last matching node founded
			return lastFounded;
		return this.getSubTree(-1);  // if we didn't find any matching node, we take random node 
	}
	
	public ArrayList<String> TreeToArrayList(ArrayList<String> st)
	{
		if(!this.getValue().contains("run"))
		{	
			
			if(this.getValue().equals("if") || this.getValue().equals("for") || this.getValue().equals("while"))
				st.add(this.getValue()+"("+this.getChildren().get(0).getValue()+")");
			else{
				st.add(this.getValue());
			}
			if(!this.getChildren().isEmpty() )
				st.add("{");
			for(Node n : this.getChildren())
			{	
					if(n.getValue() != this.getChildren().get(0).getValue())
						st = n.TreeToArrayList(st);
			}
			if(!this.getChildren().isEmpty() )
				st.add("}");
		}
		else
		{
			for(Node n : this.getChildren())
						st = n.TreeToArrayList(st);
		}
			
		return st;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Node> children) {
		this.children = children;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
