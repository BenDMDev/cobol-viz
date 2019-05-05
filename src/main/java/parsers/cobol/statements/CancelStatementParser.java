package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class CancelStatementParser extends StatementParser {

	public CancelStatementParser(Scanner scanner) {
		super(scanner);
		
	}
	
	
	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());
		
		match(inputToken, COBOLTokenType.CANCEL, parseTree);
		inputToken = scanner.getCurrentToken();
		
		boolean validToken = true; 
		while(validToken) {
			switch((COBOLTokenType) inputToken.getType()) {
			case IDENTIFIER:
				match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
				inputToken = scanner.getCurrentToken();
				break;
			case STRING_LITERAL: case INTEGER: case REAL:
				matchAlternation(inputToken, parseTree, COBOLTokenType.INTEGER, COBOLTokenType.STRING_LITERAL, COBOLTokenType.REAL);
				inputToken = scanner.getCurrentToken();
				break;
			default:
				validToken = false;
				break;
			}
				
		}
		
		return parseTree;
	}

}
