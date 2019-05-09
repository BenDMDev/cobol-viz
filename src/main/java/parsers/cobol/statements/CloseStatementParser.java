package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

/**
 * Parse for Close statement
 * @author Ben
 *
 */
public class CloseStatementParser extends StatementParser {

	public CloseStatementParser(Scanner scanner) {
		super(scanner);
		
	}
	
	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());
		// Match and consume CLOSE
		match(inputToken, COBOLTokenType.CLOSE, parseTree);
		inputToken = scanner.getCurrentToken();
		
		// Match repeating Identifiers
		matchRepetition(inputToken, parseTree, COBOLTokenType.IDENTIFIER);
		
		return parseTree;
	}

}
