package main.java.parsers.cobol.statements;

import java.io.IOException;
import java.util.HashMap;

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

public class InitializeStatementParser extends StatementParser {

	public InitializeStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {

		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		match(inputToken, COBOLTokenType.INITIALIZE, parseTree, TreeNodeType.KEYWORD);
		inputToken = scanner.getCurrentToken();

		matchRepetition(inputToken, TreeNodeType.IDENTIFIER, parseTree, COBOLTokenType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();

		match(inputToken, COBOLTokenType.REPLACING, parseTree, TreeNodeType.KEYWORD);
		inputToken = scanner.getCurrentToken();

		matchAlternation(inputToken, TreeNodeType.KEYWORD, parseTree, COBOLTokenType.ALPHABETIC,
				COBOLTokenType.ALPHANUMERIC, COBOLTokenType.NUMERIC, COBOLTokenType.ALPHANUMERIC_EDITED,
				COBOLTokenType.NUMERIC_EDITED);
		inputToken = scanner.getCurrentToken();

		matchSequence(inputToken, TreeNodeType.KEYWORD, parseTree, COBOLTokenType.DATA, COBOLTokenType.BY);
		inputToken = scanner.getCurrentToken();

		matchRepeatingAlternation(inputToken, parseTree, TOKEN_TO_TREE);



		return parseTree;
	}

}
