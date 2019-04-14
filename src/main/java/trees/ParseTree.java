package main.java.trees;

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

}
