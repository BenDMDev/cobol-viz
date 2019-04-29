package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.TokenType;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.scanners.tokens.cobol.NumberToken;
import main.java.scanners.tokens.cobol.StringToken;
import main.java.scanners.tokens.cobol.WordToken;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class CopyStatementParser extends StatementParser {

	public CopyStatementParser(Scanner scanner) {
		super(scanner);
		
	}
	
	public ParseTreeNode parse(Token inputToken) throws IOException {
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());
		
		match(inputToken, COBOLTokenType.COPY, parseTree, TreeNodeType.KEYWORD);
		inputToken = scanner.getCurrentToken();
		
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree, TreeNodeType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();
		
		matchAlternation(inputToken, TreeNodeType.KEYWORD, parseTree, COBOLTokenType.OF, COBOLTokenType.IN);
		inputToken = scanner.getCurrentToken();
		
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree, TreeNodeType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();
		
		match(inputToken, COBOLTokenType.REPLACING, parseTree, TreeNodeType.KEYWORD);
		inputToken = scanner.getCurrentToken();
		
		TokenType type = inputToken.getType();
		while(type == COBOLTokenType.IDENTIFIER || inputToken instanceof NumberToken || inputToken instanceof StringToken) {
			switch((COBOLTokenType) inputToken.getType()) {
			case IDENTIFIER:
				match(inputToken, COBOLTokenType.IDENTIFIER, parseTree, TreeNodeType.IDENTIFIER);
				break;
			case STRING_LITERAL: case INTEGER: case REAL:
				matchAlternation(inputToken, TreeNodeType.LITERAL, parseTree, COBOLTokenType.STRING_LITERAL, COBOLTokenType.INTEGER, COBOLTokenType.REAL);
				break;
			default:
				break;
			}
			
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.BY, parseTree, TreeNodeType.KEYWORD);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}
		
		
		return parseTree;
	}

	
	
	
}
