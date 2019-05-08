	package main.java.trees;

import java.util.ArrayList;
import java.util.List;

import main.java.trees.visitors.TreeVisitor;

/**
 * Represents misc or terminal nodes of ParseTree
 * @author Ben
 *
 */
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
	
	/**
	 * Add child node
	 * @param t
	 */
	public void addChild(ParseTreeNode t) {
		children.add(t);
	}
	
	/**
	 * Check if this node has no children
	 * @return
	 */
	public boolean isEmpty() { 
		return children.isEmpty();
	}
	
	/**
	 * Get attribute for this node
	 * @return
	 */
	public String getAttribute() {
		return attribute;		
	}
	
	/**
	 * Get tree node type
	 * @return
	 */
	public TreeNodeType getTreeNodeType() {
		return type;
	}
		
	/**
	 * Get all children
	 * @return
	 */
	public List<ParseTreeNode> getChildren() {
		return children;
	}
	
	/**
	 * Set line number of token this node represents
	 * @param lineNum
	 */
	public void setLineNumber(int lineNum) {
		lineNumber = lineNum;
	}
	
	/**
	 * Set tree type
	 * @param type
	 */
	public void setTreeType(TreeNodeType type) {
		this.type = type;
	}
	
	/** 
	 * Get line number of token this node represents
	 * @return
	 */
	public int getLineNumber() {
		return lineNumber;
	}
	
	/**
	 * Accept visitor class
	 * @param visitor
	 */
	public void accept(TreeVisitor visitor) {
		visitor.visit(this);
		for(ParseTreeNode p : children) {
			p.accept(visitor);
		}
	}
	
	
	
}
