package main.java.parsers.cobol;

import java.io.IOException;

import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.TokenType;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class ExpressionParser extends StatementParser {

	public ExpressionParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {

		switch ((COBOLTokenType) inputToken.getType()) {
		case ADD:
			parseAddStatement(inputToken);
			break;
		case SUBTRACT:
			parseSubtractStatement(inputToken);
			break;
		case DIVIDE:
			parseDivideStatement(inputToken);
			break;
		case MULTIPLY:
			parseMultiplyStatement(inputToken);
			break;
		default:
			break;
		}

		return parseTree;
	}

	private void parseAddStatement(Token inputToken) throws IOException {

		parseTree = new StatementNode("ADD STATEMENT");

		// Match and consume ADD
		match(inputToken, COBOLTokenType.ADD, parseTree);
		inputToken = scanner.getCurrentToken();

		TokenType type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchList(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

		// Match and consume TO
		match(inputToken, COBOLTokenType.TO, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and Consume Identifiers and Optional ROUNDED clause
		type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchList(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.ROUNDED, parseTree);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

		if(inputToken.getType() == COBOLTokenType.NOT || scanner.lookAhead().getType() == COBOLTokenType.SIZE)
			parseOnSizeError(inputToken);

	}

	private void parseSubtractStatement(Token inputToken) throws IOException {

		parseTree = new StatementNode("SUBTRACT STATEMENT");

		// Match and consume SUBTRACT
		match(inputToken, COBOLTokenType.SUBTRACT, parseTree);
		inputToken = scanner.getCurrentToken();

		TokenType type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchList(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

		// Match and consume FROM
		match(inputToken, COBOLTokenType.FROM, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and Consume Identifiers and Optional ROUNDED clause
		type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchList(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.ROUNDED, parseTree);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

	}

	private void parseDivideStatement(Token inputToken) throws IOException {
		parseTree = new StatementNode("DIVIDE STATEMENT");

		// Match and consume DIVIDE
		match(inputToken, COBOLTokenType.DIVIDE, parseTree);
		inputToken = scanner.getCurrentToken();

		matchList(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
		inputToken = scanner.getCurrentToken();

		// Match and consume INTO
		match(inputToken, COBOLTokenType.INTO, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and Consume Identifiers and Optional ROUNDED clause
		TokenType type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchList(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.ROUNDED, parseTree);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

	}

	private void parseMultiplyStatement(Token inputToken) throws IOException {
		parseTree = new StatementNode("MULTIPLY STATEMENT");

		// Match and consume MULTIPLY
		match(inputToken, COBOLTokenType.MULTIPLY, parseTree);
		inputToken = scanner.getCurrentToken();

		matchList(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
		inputToken = scanner.getCurrentToken();

		// Match and consume BY
		match(inputToken, COBOLTokenType.BY, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and Consume Identifiers and Optional ROUNDED clause
		TokenType type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchList(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.ROUNDED, parseTree);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

	}

	private void parseOnSizeError(Token inputToken) throws IOException {

		StatementNode node = new StatementNode("CONDITIONAL STATEMENT");
		node.setTreeType(TreeNodeType.CONDITIONAL);
		
		// Consume NOT
		match(inputToken, COBOLTokenType.NOT, node);
		inputToken = scanner.getCurrentToken();

		// Consume Rounded
		match(inputToken, COBOLTokenType.ROUNDED, node);
		inputToken = scanner.getCurrentToken();

		// Consume ON
		match(inputToken, COBOLTokenType.ON, node);
		inputToken = scanner.getCurrentToken();

		// Consume SIZE
		match(inputToken, COBOLTokenType.SIZE, node);
		inputToken = scanner.getCurrentToken();

		// Consume ERROR
		match(inputToken, COBOLTokenType.ERROR, node);
		inputToken = scanner.getCurrentToken();

		// CONSUME STATEMENT
		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue())) {
			StatementParser statementParser = new StatementParser(scanner);
			node.addChild(statementParser.parse(inputToken));
			inputToken = scanner.getCurrentToken();
		}

		parseTree.addChild(node);
	}
}
