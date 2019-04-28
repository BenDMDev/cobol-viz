package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class AcceptStatementParser extends StatementParser {

	public AcceptStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {

		parseTree = new StatementNode(TreeNodeType.STATEMENT, "ACCEPT STATEMENT");
		
		
		// Consume ACCEPT
		match(inputToken, COBOLTokenType.ACCEPT, parseTree, TreeNodeType.KEYWORD);
		inputToken = scanner.getCurrentToken();

		// Consume Identifier
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree, TreeNodeType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();

		if (inputToken.getType() == COBOLTokenType.FROM) {
			// Consume FROM
			match(inputToken, COBOLTokenType.FROM, parseTree, TreeNodeType.KEYWORD);
			inputToken = scanner.getCurrentToken();
			
			if (inputToken.getType() == COBOLTokenType.IDENTIFIER) {
				// Consume Identifier
				match(inputToken, COBOLTokenType.IDENTIFIER, parseTree, TreeNodeType.IDENTIFIER);
			} else {
				// Consume DATE/DAY/DAY-OF-WEEK/TIME
				matchAlternation(inputToken, TreeNodeType.KEYWORD, parseTree, COBOLTokenType.DATE, COBOLTokenType.DAY,
						COBOLTokenType.DAY_OF_WEEK, COBOLTokenType.TIME);
			}
		}
		return parseTree;
	}

}
