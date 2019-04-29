package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class AlterStatementParser extends StatementParser {

	public AlterStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		// Match and Consume ALTER
		match(inputToken, COBOLTokenType.ALTER, parseTree);
		inputToken = scanner.getCurrentToken();

		while (inputToken.getType() == COBOLTokenType.IDENTIFIER) {
			// Match and Consume procedure name
			match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
			inputToken = scanner.getCurrentToken();

			matchSequence(inputToken, parseTree, COBOLTokenType.TO, COBOLTokenType.PROCEED, COBOLTokenType.TO);

			inputToken = scanner.getCurrentToken();

			// Match and Consume procedure name
			match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
			inputToken = scanner.getCurrentToken();
		}

		return parseTree;

	}

}
