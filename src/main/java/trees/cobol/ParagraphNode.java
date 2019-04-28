package main.java.trees.cobol;

import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public class ParagraphNode extends ParseTreeNode {

	String label;
	
	public ParagraphNode(String type, String label) {
		super(type);		
		this.label = label;
	}
	
	public ParagraphNode(TreeNodeType type, String attribute) {
		super(type, attribute);
	}
	
	public String getLabel() {
		return label;
	}

}
