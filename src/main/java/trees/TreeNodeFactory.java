package main.java.trees;

import main.java.scanners.tokens.Token;

public interface TreeNodeFactory {

	ParseTreeNode createTreeNode(Token inputToken);
	
}
