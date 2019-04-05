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
	
	public String getType() {
		return type;		
	}
	
	public void traverse() {
				
		for(ParseTreeNode c : children) {
			if(c.type.contains("RULE"))
				System.out.println("\n" + c.type + " ");
			else
				System.out.print(c.type + " ");
			c.traverse();			
			
		}
	}
		
	
	public List<ParseTreeNode> getChildren() {
		return children;
	}
	
	
	
}
