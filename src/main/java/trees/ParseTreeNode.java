	package main.java.trees;

import java.util.ArrayList;
import java.util.List;

import main.java.trees.visitors.TreeVisitor;

public class ParseTreeNode {

	protected String attribute;
	protected TreeNodeType type;
	protected int lineNumber;
	protected List<ParseTreeNode> children; 
	
	public ParseTreeNode(String value) {
		this.attribute = value;
		children = new ArrayList<ParseTreeNode>();
	}
	
	public ParseTreeNode(TreeNodeType type, String value) {
		this.attribute = value;
		this.type = type;
		children = new ArrayList<ParseTreeNode>();
	}
	
	public void addChild(ParseTreeNode t) {
		children.add(t);
	}
	
	public boolean isEmpty() { 
		return children.isEmpty();
	}
	
	public String getAttribute() {
		return attribute;		
	}
	
	public TreeNodeType getTreeNodeType() {
		return type;
	}
		
	
	public List<ParseTreeNode> getChildren() {
		return children;
	}
	
	public void setLineNumber(int lineNum) {
		lineNumber = lineNum;
	}
	
	public void setTreeType(TreeNodeType type) {
		this.type = type;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public void accept(TreeVisitor visitor) {
		visitor.visit(this);
		for(ParseTreeNode p : children) {
			p.accept(visitor);
		}
	}
	
	
	
}
