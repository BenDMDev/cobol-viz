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
 * Parser for Stop statement
 * @author Ben
 *
 */
public class StopStatementParser extends StatementParser {

	public StopStatementParser(Scanner scanner) {
		super(scanner);
		
	}
	
	
	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode(TreeNodeType.END, inputToken.getTokenValue());
		
		match(inputToken, COBOLTokenType.STOP, parseTree);
		inputToken = scanner.getCurrentToken();
		
		matchAlternation(inputToken, parseTree, COBOLTokenType.RUN, COBOLTokenType.STRING_LITERAL, COBOLTokenType.INTEGER, COBOLTokenType.IDENTIFIER);
		
		
		return parseTree;
		
	}

	
	
}
