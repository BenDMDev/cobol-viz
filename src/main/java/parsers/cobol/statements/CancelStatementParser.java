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
 * Parse Cancel statement
 * 
 * @author Ben
 *
 */
public class CancelStatementParser extends StatementParser {

	public CancelStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {

		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		// Match and consume Cancel
		match(inputToken, COBOLTokenType.CANCEL, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match repeating alternation between expected Tokens
		matchRepeatingAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER,
				COBOLTokenType.STRING_LITERAL, COBOLTokenType.REAL);

		return parseTree;
	}

}
