package main.java.parsers.cobol;

import java.io.IOException;

import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class PerformStatementParser extends StatementParser {

	public PerformStatementParser(Scanner scanner) {
		super(scanner);
		
	}
	
	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode("PERFORM STATEMENT");
		
		// Match and consume PERFORM 
		match(inputToken, COBOLTokenType.PERFORM, parseTree, TreeNodeType.KEYWORD);		
		
		
		// Match and consume procedure name
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree, TreeNodeType.IDENTIFIER);
		
		// Match and consume THROUGH | THRU
		inputToken = scanner.getCurrentToken();
		matchList(inputToken, TreeNodeType.KEYWORD, parseTree, COBOLTokenType.THROUGH, COBOLTokenType.THRU);
		
		// Match and consume procedure name
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree, TreeNodeType.IDENTIFIER);
		
		return parseTree;
		
		
	}

}
