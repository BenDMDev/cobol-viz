package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.TokenType;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.scanners.tokens.cobol.NumberToken;
import main.java.scanners.tokens.cobol.StringToken;
import main.java.scanners.tokens.cobol.WordToken;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

/**
 * Parser for Copy Statement
 * 
 * @author Ben
 *
 */
public class CopyStatementParser extends StatementParser {

	public CopyStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		// Match and consume COPY
		match(inputToken, COBOLTokenType.COPY, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume Identifier
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match Alternation OF | IN
		matchAlternation(inputToken, parseTree, COBOLTokenType.OF, COBOLTokenType.IN);
		inputToken = scanner.getCurrentToken();

		// Match and consume Identifier
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume Replacing
		match(inputToken, COBOLTokenType.REPLACING, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match repeated Sequence of user put followed by "BY"
		matchRepeatingSequence(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.STRING_LITERAL,
				COBOLTokenType.INTEGER, COBOLTokenType.REAL, COBOLTokenType.BY);


		return parseTree;
	}

}
