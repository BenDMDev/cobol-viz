package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class GoToStatementParser extends StatementParser {

	public GoToStatementParser(Scanner scanner) {
		super(scanner);
		
	}

	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode(TreeNodeType.REFERENCE, inputToken.getTokenValue());
		inputToken = scanner.getCurrentToken();
		
		match(inputToken, COBOLTokenType.GO, parseTree);
		inputToken = scanner.getCurrentToken();
		
		if(inputToken.getType() == COBOLTokenType.TO)
			scanner.scan(); // Skip TO 
		
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		
		return parseTree;
	}
	
}
