package main.java.trees;

import main.java.trees.visitors.TreeVisitor;

/**
 * Parse Tree root node, acts an interface into
 * underlying nodes
 * @author Ben
 *
 */
public class ParseTree {

	private ParseTreeNode root;

	public ParseTree() {
		root = null;
	}

	/**
	 * Set root of this tree
	 * @param root
	 */
	public void setRoot(ParseTreeNode root) {
		this.root = root;
	}

	/**
	 * Get root of this tree
	 * @return
	 */
	public ParseTreeNode getRoot() {
		return root;
	}

	/**
	 * Help method to print parse tree for debug
	 */
	public void printParseTree() {	
		print(root);		
	}
	
	private void print(ParseTreeNode treeNode) {
		
		System.out.println(treeNode.getTreeNodeType() + " : " + treeNode.getAttribute());
		
		
		for (ParseTreeNode childNode : treeNode.getChildren()) {
			print(childNode);
		}
	}
	
	/**
	 * Accept visitor class
	 * @param visitor
	 */
	public void accept(TreeVisitor visitor) {
		root.accept(visitor);
	}

}
