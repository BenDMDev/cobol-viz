package parser;

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
	
	public void traverse() {
		System.out.println(type);
		for(ParseTreeNode c : children) {
			c.traverse();
		}
	}
	
}
