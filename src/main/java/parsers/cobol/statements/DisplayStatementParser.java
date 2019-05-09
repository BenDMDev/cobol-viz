package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.TokenType;
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

		// List of Token types that are valid, used to clean up while loop condition
		TokenType[] tokenList = { COBOLTokenType.IDENTIFIER, 
				COBOLTokenType.FIGURATIVE_CONSTANT,				
				COBOLTokenType.STRING_LITERAL,
				COBOLTokenType.INTEGER,
				COBOLTokenType.REAL };
		
		while (containsToken(inputToken, tokenList)) {
			// Match and consume Alternating input
			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER,
					COBOLTokenType.REAL, COBOLTokenType.STRING_LITERAL, COBOLTokenType.FIGURATIVE_CONSTANT);

			inputToken = scanner.getCurrentToken();

		}
		
		// Match and consume UPON
		matchSequence(inputToken, parseTree, COBOLTokenType.UPON, COBOLTokenType.CONSOLE);
		inputToken = scanner.getCurrentToken();

		// Match and consume Identifier
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume Sequence WITH NO ADVANCING
		matchSequence(inputToken, parseTree, COBOLTokenType.WITH, COBOLTokenType.NO, COBOLTokenType.ADVANCING);

		// Match and consume closing Tag
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_DISPLAY, parseTree);

		return parseTree;
	}

}
