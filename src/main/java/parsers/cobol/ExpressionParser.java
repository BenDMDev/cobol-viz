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

		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		// Match and consume ADD
		match(inputToken, COBOLTokenType.ADD, parseTree);
		inputToken = scanner.getCurrentToken();
		
		// Match and consume CORR | CORRESPONDING
		matchAlternation(inputToken, parseTree, COBOLTokenType.CORR, COBOLTokenType.CORRESPONDING);
		inputToken = scanner.getCurrentToken();
		
		
		TokenType type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

		// Match and consume TO
		match(inputToken, COBOLTokenType.TO, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and Consume Identifiers and Optional ROUNDED clause
		type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {
			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.ROUNDED, parseTree);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

		if(inputToken.getType() == COBOLTokenType.GIVING) {
			parseGivingClause(inputToken);
		}
		
		inputToken = scanner.getCurrentToken();
		if (inputToken.getType() == COBOLTokenType.ON || inputToken.getType() == COBOLTokenType.SIZE) {
			parseOnSizeError(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}
		
		inputToken = scanner.getCurrentToken();
				
		if(inputToken.getType() == COBOLTokenType.NOT ){
			parseOnSizeError(inputToken);
		}
		
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_ADD, parseTree);

	}

	private void parseSubtractStatement(Token inputToken) throws IOException {

		parseTree = new StatementNode(TreeNodeType.STATEMENT, "SUBTRACT STATEMENT");

		// Match and consume SUBTRACT
		match(inputToken, COBOLTokenType.SUBTRACT, parseTree);
		inputToken = scanner.getCurrentToken();

		TokenType type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

		// Match and consume FROM
		match(inputToken, COBOLTokenType.FROM, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and Consume Identifiers and Optional ROUNDED clause
		type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.ROUNDED, parseTree);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

	}

	private void parseDivideStatement(Token inputToken) throws IOException {
		parseTree = new StatementNode(TreeNodeType.STATEMENT, "DIVIDE STATEMENT");

		// Match and consume DIVIDE
		match(inputToken, COBOLTokenType.DIVIDE, parseTree);
		inputToken = scanner.getCurrentToken();

		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
		inputToken = scanner.getCurrentToken();

		// Match and consume INTO
		match(inputToken, COBOLTokenType.INTO, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and Consume Identifiers and Optional ROUNDED clause
		TokenType type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.ROUNDED, parseTree);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

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

			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.ROUNDED, parseTree);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

	}

	private void parseOnSizeError(Token inputToken) throws IOException {

		StatementNode node = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, "CONDITIONAL STATEMENT");
		
		ParseTreeNode conditionNode = new ParseTreeNode(TreeNodeType.CONDITION, "CONDITION");
		
		
		node.addChild(conditionNode);
		// Consume NOT
		match(inputToken, COBOLTokenType.NOT, conditionNode);
		inputToken = scanner.getCurrentToken();

		// Consume Rounded
		match(inputToken, COBOLTokenType.ROUNDED, conditionNode);
		inputToken = scanner.getCurrentToken();

		// Consume ON
		match(inputToken, COBOLTokenType.ON, conditionNode);
		inputToken = scanner.getCurrentToken();

		// Consume SIZE
		match(inputToken, COBOLTokenType.SIZE, conditionNode);
		inputToken = scanner.getCurrentToken();

		// Consume ERROR
		match(inputToken, COBOLTokenType.ERROR, conditionNode);
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

			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.ROUNDED, parseTree);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}

	}
}
