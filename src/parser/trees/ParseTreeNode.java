package parser.trees;

import java.util.ArrayList;
import java.util.List;

public class ParseTreeNode {

	private String type;
	
	private List<ParseTreeNode> children; 
	
	public ParseTreeNode(String type) {
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
