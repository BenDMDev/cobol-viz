package main.java.trees.cobol;

import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public class SentenceNode extends ParseTreeNode {

	public SentenceNode(String type) {
		super(type);
		
	}

	public SentenceNode(TreeNodeType type, String attribute) {
		super(type, attribute);
	}
	
}
