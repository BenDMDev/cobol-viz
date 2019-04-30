package main.java.trees.cobol;

import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public class SectionNode extends ParseTreeNode {

	public SectionNode(String type) {
		super(type);		
	}

	public SectionNode(TreeNodeType type, String attribute) {
		super(type, attribute);
	}
	
}
