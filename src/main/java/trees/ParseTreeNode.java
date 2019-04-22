	package main.java.trees;

import java.util.ArrayList;
import java.util.List;

import main.java.trees.visitors.TreeVisitor;

public class ParseTreeNode {

	private String value;
	private TreeNodeType type;
	
	private List<ParseTreeNode> children; 
	
	public ParseTreeNode(String value) {
		this.value = value;
		children = new ArrayList<ParseTreeNode>();
	}
	
	public ParseTreeNode(TreeNodeType type, String value) {
		this.value = value;
		this.type = type;
		children = new ArrayList<ParseTreeNode>();
	}
	
	public void addChild(ParseTreeNode t) {
		children.add(t);
	}
	
	public boolean isEmpty() { 
		return children.isEmpty();
	}
	
	public String getType() {
		return value;		
	}
	
	public TreeNodeType getTreeNodeType() {
		return type;
	}
		
	
	public List<ParseTreeNode> getChildren() {
		return children;
	}
	
	public void accept(TreeVisitor visitor) {
		visitor.visit(this);
		for(ParseTreeNode p : children) {
			p.accept(visitor);
		}
	}
	
	
	
}
