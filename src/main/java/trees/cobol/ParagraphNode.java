package main.java.trees.cobol;

import main.java.trees.ParseTreeNode;

public class ParagraphNode extends ParseTreeNode {

	String label;
	
	public ParagraphNode(String type, String label) {
		super(type);		
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}

}
