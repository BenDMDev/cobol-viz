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
 * Parser for GO TO Statement
 * @author Ben
 *
 */
public class GoToStatementParser extends StatementParser {

	public GoToStatementParser(Scanner scanner) {
		super(scanner);
		
	}

	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode(TreeNodeType.REFERENCE, inputToken.getTokenValue());
		inputToken = scanner.getCurrentToken();
		
		// Match and consume GO
		match(inputToken, COBOLTokenType.GO, parseTree);
		inputToken = scanner.getCurrentToken();
		
		// Match and consume TO
		match(inputToken, COBOLTokenType.TO, parseTree);
		inputToken = scanner.getCurrentToken();
		
		// Create reference node and use it's attribute to store reference value
		inputToken = scanner.getCurrentToken();
		if(inputToken.getType() == COBOLTokenType.IDENTIFIER) {
			ParseTreeNode referenceVal = new ParseTreeNode(TreeNodeType.REFERENCE_VALUE, inputToken.getTokenValue());
			scanner.scan();
			parseTree.addChild(referenceVal);
		}
		
		
		return parseTree;
	}
	
}
