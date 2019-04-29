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
		case COMPUTE:
			parseComputeStatement(inputToken);
		default:
			break;
		}

		return parseTree;
	}

	private void parseAddStatement(Token inputToken) throws IOException {

		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		// Match and consume ADD
		match(inputToken, COBOLTokenType.ADD, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume CORR | CORRESPONDING
		matchAlternation(inputToken, parseTree, COBOLTokenType.CORR, COBOLTokenType.CORRESPONDING);
		inputToken = scanner.getCurrentToken();

		TokenType type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
					COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

		// Match and consume TO
		match(inputToken, COBOLTokenType.TO, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and Consume Identifiers and Optional ROUNDED clause
		type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {
			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
					COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.ROUNDED, parseTree);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

		if (inputToken.getType() == COBOLTokenType.GIVING) {
			parseGivingClause(inputToken);
		}

		inputToken = scanner.getCurrentToken();
		if (inputToken.getType() == COBOLTokenType.ON || inputToken.getType() == COBOLTokenType.SIZE) {
			parseOnSizeError(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		inputToken = scanner.getCurrentToken();

		if (inputToken.getType() == COBOLTokenType.NOT) {
			parseOnSizeError(inputToken);
		}

		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_ADD, parseTree, TreeNodeType.KEYWORD);

	}

	private void parseSubtractStatement(Token inputToken) throws IOException {

		parseTree = new StatementNode(TreeNodeType.STATEMENT, "SUBTRACT STATEMENT");

		// Match and consume SUBTRACT
		match(inputToken, COBOLTokenType.SUBTRACT, parseTree);
		inputToken = scanner.getCurrentToken();

		TokenType type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
					COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

		// Match and consume FROM
		match(inputToken, COBOLTokenType.FROM, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and Consume Identifiers and Optional ROUNDED clause
		type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
					COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.ROUNDED, parseTree);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

		// Handle GIVING Clause
		if (inputToken.getType() == COBOLTokenType.GIVING) {
			parseGivingClause(inputToken);
		}

		inputToken = scanner.getCurrentToken();

		// HANDLE ON SIZE ERROR
		if (inputToken.getType() == COBOLTokenType.ON || inputToken.getType() == COBOLTokenType.SIZE) {
			parseOnSizeError(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		inputToken = scanner.getCurrentToken();

		// HANDLE NOT ON SIZE ERROR
		if (inputToken.getType() == COBOLTokenType.NOT) {
			parseOnSizeError(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_SUBTRACT, parseTree, TreeNodeType.KEYWORD);

	}

	private void parseDivideStatement(Token inputToken) throws IOException {
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		// Match and consume DIVIDE
		match(inputToken, COBOLTokenType.DIVIDE, parseTree, TreeNodeType.KEYWORD);
		inputToken = scanner.getCurrentToken();

		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
		inputToken = scanner.getCurrentToken();

		// Match and consume INTO | BY
		matchAlternation(inputToken, TreeNodeType.KEYWORD, parseTree, COBOLTokenType.INTO, COBOLTokenType.BY);
		inputToken = scanner.getCurrentToken();

		// Match and Consume Identifiers and Optional ROUNDED clause
		TokenType type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
					COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.ROUNDED, parseTree);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

		// Handle GIVING Clause
		if (inputToken.getType() == COBOLTokenType.GIVING) {
			parseGivingClause(inputToken);
		}

		inputToken = scanner.getCurrentToken();

		// Match and consume REMAINDER
		match(inputToken, COBOLTokenType.REMAINDER, parseTree, TreeNodeType.KEYWORD);
		inputToken = scanner.getCurrentToken();

		// MATCH identifier
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree, TreeNodeType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();

		// HANDLE ON SIZE ERROR
		if (inputToken.getType() == COBOLTokenType.ON || inputToken.getType() == COBOLTokenType.SIZE) {
			parseOnSizeError(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		inputToken = scanner.getCurrentToken();

		// HANDLE NOT ON SIZE ERROR
		if (inputToken.getType() == COBOLTokenType.NOT) {
			parseOnSizeError(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_DIVIDE, parseTree, TreeNodeType.KEYWORD);

	}

	private void parseMultiplyStatement(Token inputToken) throws IOException {
		parseTree = new StatementNode(TreeNodeType.STATEMENT, "MULTIPLY STATEMENT");

		// Match and consume MULTIPLY
		match(inputToken, COBOLTokenType.MULTIPLY, parseTree);
		inputToken = scanner.getCurrentToken();

		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
		inputToken = scanner.getCurrentToken();

		// Match and consume BY
		match(inputToken, COBOLTokenType.BY, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and Consume Identifiers and Optional ROUNDED clause
		TokenType type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
					COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.ROUNDED, parseTree);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

		// Handle GIVING Clause
		if (inputToken.getType() == COBOLTokenType.GIVING) {
			parseGivingClause(inputToken);
		}

		inputToken = scanner.getCurrentToken();

		// HANDLE ON SIZE ERROR
		if (inputToken.getType() == COBOLTokenType.ON || inputToken.getType() == COBOLTokenType.SIZE) {
			parseOnSizeError(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		inputToken = scanner.getCurrentToken();

		// HANDLE NOT ON SIZE ERROR
		if (inputToken.getType() == COBOLTokenType.NOT) {
			parseOnSizeError(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_MULTIPLY, parseTree, TreeNodeType.KEYWORD);

	}

	private void parseComputeStatement(Token inputToken) throws IOException {

		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		match(inputToken, COBOLTokenType.COMPUTE, parseTree, TreeNodeType.KEYWORD);
		inputToken = scanner.getCurrentToken();

		// Match and Consume Identifiers and Optional ROUNDED clause
		TokenType type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER) {

			match(inputToken, COBOLTokenType.IDENTIFIER, parseTree, TreeNodeType.KEYWORD);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.ROUNDED, parseTree, TreeNodeType.KEYWORD);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

		match(inputToken, COBOLTokenType.EQUALS_SYMBOL, parseTree, TreeNodeType.SPECIAL_SYMBOL);
		inputToken = scanner.getCurrentToken();

		while (isOperand(inputToken) || isOperator(inputToken)) {
			parseArithmeticExpression(inputToken);
			inputToken = scanner.getCurrentToken();
		}

		inputToken = scanner.getCurrentToken();

		if (inputToken.getType() == COBOLTokenType.ON || inputToken.getType() == COBOLTokenType.SIZE) {
			parseOnSizeError(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		inputToken = scanner.getCurrentToken();

		if (inputToken.getType() == COBOLTokenType.NOT) {
			parseOnSizeError(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

	}

	private void parseOnSizeError(Token inputToken) throws IOException {

		StatementNode node = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, "CONDITIONAL STATEMENT");

		ParseTreeNode conditionNode = new ParseTreeNode(TreeNodeType.CONDITION, "CONDITION");

		node.addChild(conditionNode);
		matchSequence(inputToken, TreeNodeType.KEYWORD, conditionNode, COBOLTokenType.NOT, COBOLTokenType.ROUNDED,
				COBOLTokenType.ON, COBOLTokenType.SIZE, COBOLTokenType.ERROR);
		inputToken = scanner.getCurrentToken();
		

		ParseTreeNode onErrorBody = new ParseTreeNode(TreeNodeType.CONDITION_BODY, "CONDITION BODY");
		node.addChild(onErrorBody);
		// CONSUME STATEMENT
		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue())) {
			StatementParser statementParser = new StatementParser(scanner);
			statementParser.addListener(listener);
			onErrorBody.addChild(statementParser.parse(inputToken));
			inputToken = scanner.getCurrentToken();
		}

		parseTree.addChild(node);
	}

	private void parseGivingClause(Token inputToken) throws IOException {

		// CONSUME GIVING
		match(inputToken, COBOLTokenType.GIVING, parseTree);
		inputToken = scanner.getCurrentToken();
		// Match and Consume Identifiers and Optional ROUNDED clause
		TokenType type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
					COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.ROUNDED, parseTree);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

	}

	private void parseArithmeticExpression(Token inputToken) throws IOException {

		switch ((COBOLTokenType) inputToken.getType()) {
		case IDENTIFIER:
			match(inputToken, COBOLTokenType.IDENTIFIER, parseTree, TreeNodeType.IDENTIFIER);
			break;
		case INTEGER:
		case REAL:
			matchAlternation(inputToken, TreeNodeType.LITERAL, parseTree, COBOLTokenType.INTEGER, COBOLTokenType.REAL);
			break;
		default:
			matchAlternation(inputToken, TreeNodeType.SPECIAL_SYMBOL, parseTree, COBOLTokenType.ADDITION_SYMBOL,
					COBOLTokenType.SUBTRACTION_SYMBOL, COBOLTokenType.MULTIPLICATION_SYMBOL,
					COBOLTokenType.EXPONENTIATION_SYMBOL, COBOLTokenType.DIVISION_SYMBOL, COBOLTokenType.LEFT_PAREN,
					COBOLTokenType.RIGHT_PAREN);
			break;
		}
	}

	private boolean isOperand(Token inputToken) {
		TokenType type = inputToken.getType();
		if (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {
			return true;
		} else
			return false;
	}

	private boolean isOperator(Token inputToken) {
		if (COBOLTokenType.SPECIAL_SYMBOLS.containsKey(inputToken.getTokenValue())) {
			return true;
		} else
			return false;
	}
}
