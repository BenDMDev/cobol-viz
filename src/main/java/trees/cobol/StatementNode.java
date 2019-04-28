package main.java.trees.cobol;

import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public class StatementNode extends ParseTreeNode {

	public StatementNode(String attribute) {
		super(attribute);		
	}
	
	public StatementNode(TreeNodeType type, String attribute) {
		super(type, attribute);
	}
	

}
