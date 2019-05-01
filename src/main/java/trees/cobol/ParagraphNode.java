package main.java.trees.cobol;

import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public class ParagraphNode extends ParseTreeNode {

	protected int numberOfLines;
	
	public ParagraphNode(TreeNodeType type, String attribute) {
		super(type, attribute);
		numberOfLines = 0;
	}
	
	
	public void setNumberOfLines(int num) {
		numberOfLines = num;
	}
	
	public int getNumberOfLines() {
		return numberOfLines;
	}

}
