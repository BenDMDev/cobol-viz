package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class UnstringStatementParser extends StatementParser {

	public UnstringStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		match(inputToken, COBOLTokenType.UNSTRING, parseTree);
		inputToken = scanner.getCurrentToken();

		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();

		matchSequence(inputToken, parseTree, COBOLTokenType.DELIMITED, COBOLTokenType.BY, COBOLTokenType.ALL);
		inputToken = scanner.getCurrentToken();

		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER, COBOLTokenType.REAL,
				COBOLTokenType.STRING_LITERAL);
		inputToken = scanner.getCurrentToken();

		matchRepeatingSequence(inputToken, parseTree, COBOLTokenType.OR, COBOLTokenType.ALL, COBOLTokenType.IDENTIFIER,
				COBOLTokenType.INTEGER, COBOLTokenType.STRING_LITERAL, COBOLTokenType.REAL);

		inputToken = scanner.getCurrentToken();

		match(inputToken, COBOLTokenType.INTO, parseTree);
		inputToken = scanner.getCurrentToken();

		matchRepeatingSequence(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.IN,
				COBOLTokenType.DELIMITER, COBOLTokenType.IDENTIFIER, COBOLTokenType.COUNT, COBOLTokenType.IN,
				COBOLTokenType.IDENTIFIER);

		inputToken = scanner.getCurrentToken();
		matchSequence(inputToken, parseTree, COBOLTokenType.WITH, COBOLTokenType.POINTER, COBOLTokenType.IDENTIFIER);

		inputToken = scanner.getCurrentToken();
		matchSequence(inputToken, parseTree, COBOLTokenType.TALLYING, COBOLTokenType.IN, COBOLTokenType.IDENTIFIER);

		inputToken = scanner.getCurrentToken();

		if (inputToken.getType() == COBOLTokenType.ON || scanner.lookAhead().getType() == COBOLTokenType.OVERFLOW) {
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
			parseOverflowError(inputToken);
			inputToken = scanner.getCurrentToken();
		}

		if (inputToken.getType() == COBOLTokenType.NOT) {
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
			parseOverflowError(inputToken);
			inputToken = scanner.getCurrentToken();
		}

		match(inputToken, COBOLTokenType.END_UNSTRING, parseTree);

		return parseTree;
	}

	private void parseOverflowError(Token inputToken) throws IOException {

		StatementNode conditional = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, inputToken.getTokenValue());
		ParseTreeNode condition = new ParseTreeNode(TreeNodeType.CONDITION, inputToken.getTokenValue());
		conditional.addChild(condition);

		matchSequence(inputToken, condition, COBOLTokenType.NOT, COBOLTokenType.ON, COBOLTokenType.OVERFLOW);
		inputToken = scanner.getCurrentToken();

		ParseTreeNode onOverflowBody = new ParseTreeNode(TreeNodeType.CONDITION_BODY, "CONDITION BODY");
		conditional.addChild(onOverflowBody);
		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue().toLowerCase())) {
			StatementParser statementParser = new StatementParser(scanner);
			statementParser.addListener(listener);
			onOverflowBody.addChild(statementParser.parse(inputToken));
			inputToken = scanner.getCurrentToken();
		}

		parseTree.addChild(conditional);

	}

}
