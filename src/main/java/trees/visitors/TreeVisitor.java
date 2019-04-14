package main.java.trees.visitors;

import main.java.trees.ParseTreeNode;

public interface TreeVisitor {
		
	void visit(ParseTreeNode treeNode);
	
}
