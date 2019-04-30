package main.java.trees;

import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.TokenType;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.scanners.tokens.cobol.NumberToken;
import main.java.scanners.tokens.cobol.StringToken;

public class TreeNodeFactoryImpl implements TreeNodeFactory {

	@Override
	public ParseTreeNode createTreeNode(Token inputToken) {
		
		TokenType type = inputToken.getType();
		if(type == COBOLTokenType.IDENTIFIER) {
			return new ParseTreeNode(TreeNodeType.IDENTIFIER, inputToken.getTokenValue());
		} else if (inputToken instanceof NumberToken || inputToken instanceof StringToken) {
			return new ParseTreeNode(TreeNodeType.LITERAL, inputToken.getTokenValue());
		}
		else if(COBOLTokenType.SPECIAL_SYMBOLS.containsKey(inputToken.getTokenValue())) {
			return new ParseTreeNode(TreeNodeType.SPECIAL_SYMBOL, inputToken.getTokenValue());
		} 
		else {
			return new ParseTreeNode(TreeNodeType.KEYWORD, inputToken.getTokenValue());
		}
		
	}

}
