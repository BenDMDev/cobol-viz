package main.java.trees;

import main.java.trees.visitors.TreeVisitor;

public class ParseTree {

	private ParseTreeNode root;

	public ParseTree() {
		root = null;
	}

	public void setRoot(ParseTreeNode root) {
		this.root = root;
	}

	public ParseTreeNode getRoot() {
		return root;
	}

	public void printParseTree() {	
		print(root);		
	}
	
	private void print(ParseTreeNode treeNode) {
		
		System.out.println(treeNode.getAttribute());
		
		
		for (ParseTreeNode childNode : treeNode.getChildren()) {
			print(childNode);
		}
	}
	
	public void accept(TreeVisitor visitor) {
		root.accept(visitor);
	}

}
