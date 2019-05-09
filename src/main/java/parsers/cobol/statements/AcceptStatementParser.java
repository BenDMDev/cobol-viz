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
 * Parser for an Accept Statement
 * @author Ben
 *
 */
public class AcceptStatementParser extends StatementParser {

	public AcceptStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {

		parseTree = new StatementNode(TreeNodeType.STATEMENT, "ACCEPT STATEMENT");
		
		
		// Consume ACCEPT
		match(inputToken, COBOLTokenType.ACCEPT, parseTree);
		inputToken = scanner.getCurrentToken();

		// Consume Identifier
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();

		if (inputToken.getType() == COBOLTokenType.FROM) {
			// Consume FROM
			match(inputToken, COBOLTokenType.FROM, parseTree);
			inputToken = scanner.getCurrentToken();
			
			if (inputToken.getType() == COBOLTokenType.IDENTIFIER) {
				// Consume Identifier
				match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
			} else {
				// Consume DATE/DAY/DAY-OF-WEEK/TIME
				matchAlternation(inputToken, parseTree, COBOLTokenType.DATE, COBOLTokenType.DAY, COBOLTokenType.DAY_OF_WEEK,
						COBOLTokenType.TIME);
			}
		}
		// Match and consume closing tag
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_ACCEPT, parseTree);
		return parseTree;
	}

}
