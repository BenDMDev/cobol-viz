package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.scanners.tokens.cobol.NumberToken;
import main.java.scanners.tokens.cobol.StringToken;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class DisplayStatementParser extends StatementParser {

	public DisplayStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {

		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		match(inputToken, COBOLTokenType.DISPLAY, parseTree);
		inputToken = scanner.getCurrentToken();

		while (inputToken.getType() == COBOLTokenType.IDENTIFIER || inputToken instanceof NumberToken
				|| inputToken instanceof StringToken) {

			switch ((COBOLTokenType) inputToken.getType()) {
			case IDENTIFIER:
				match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
				break;
			case INTEGER:
			case REAL:
			case STRING_LITERAL:
				matchAlternation(inputToken, parseTree, COBOLTokenType.INTEGER, COBOLTokenType.REAL,
						COBOLTokenType.STRING_LITERAL);
				break;
			default:
				break;
			}
			inputToken = scanner.getCurrentToken();

		}
		matchSequence(inputToken, parseTree, COBOLTokenType.UPON, COBOLTokenType.CONSOLE);
		inputToken = scanner.getCurrentToken();

		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();

		matchSequence(inputToken, parseTree, COBOLTokenType.WITH, COBOLTokenType.NO, COBOLTokenType.ADVANCING);

		return parseTree;
	}

}
