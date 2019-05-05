package main.java.trees.cobol;

import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public class ProgramNode extends ParseTreeNode {

	
	public ProgramNode(String value) {
		super(value);
		
	}
	
	public ProgramNode(TreeNodeType type, String attribute) {
		super(type, attribute);
		
	}

}
