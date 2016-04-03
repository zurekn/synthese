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
	private void removeChild(Node child) 
	{
		if (children.contains(child))
		{	
			//int temp = children.indexOf(child);
			children.remove(child);
			//if(temp <= children.size()-1)
				//children.get(temp).setBro(children.get(temp+1));
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
		System.out.println("parent : "+(hasParent(this)?this.parent.getValue():"")+" value : "+ this.value);
	}
	
	public boolean hasParent(Node node){
		if(node.parent == null)
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
		ArrayList<Node> arrayNode = new ArrayList<Node>();
		//arrayNode = TreeToArray(this, arrayNode);
		arrayNode= this.getChildren();
		if(id < 0 || id >= arrayNode.size())
			return arrayNode.get(new Random().nextInt(arrayNode.size()));
		else{
			return arrayNode.get(id);
		}
	}
	
	public ArrayList<String> TreeToArrayList(ArrayList<String> st)
	{
		if(!this.getValue().contains("run"))
		{	
			
			if(this.getValue() == "if" || this.getValue() == "for" || this.getValue() == "while")
				st.add(this.getValue()+"("+this.getChildren().get(0).getValue()+")");
			else
				st.add(this.getValue());
			
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
