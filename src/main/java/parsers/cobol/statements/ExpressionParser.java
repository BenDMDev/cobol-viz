package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.TokenType;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

/**
 * Expression parser, handles ADD SUBCRACT, DIVIDE, MULTIPLY and COMPUTE Statements
 * @author Ben
 *
 */
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

	/**
	 * Parse an ADD statement
	 * @param inputToken first token in Scanner buffer
	 * @throws IOException
	 */
	private void parseAddStatement(Token inputToken) throws IOException {

		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		// Match and consume ADD
		match(inputToken, COBOLTokenType.ADD, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume CORR | CORRESPONDING
		matchAlternation(inputToken, parseTree, COBOLTokenType.CORR, COBOLTokenType.CORRESPONDING);
		inputToken = scanner.getCurrentToken();

		// Match and consume LHS of expression
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

		// Match and consume RHS of expression 
		matchRepeatingSequence(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
				COBOLTokenType.INTEGER, COBOLTokenType.ROUNDED);
		inputToken = scanner.getCurrentToken();

		if (inputToken.getType() == COBOLTokenType.GIVING) {
			parseGivingClause(inputToken);
		}

		// Check for On Size error 
		inputToken = scanner.getCurrentToken();
		if (inputToken.getType() == COBOLTokenType.ON || inputToken.getType() == COBOLTokenType.SIZE) {
			parseOnSizeError(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		inputToken = scanner.getCurrentToken();

		// If NOT then must be NOT on Size error
		if (inputToken.getType() == COBOLTokenType.NOT) {
			parseOnSizeError(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		// Match closing tag
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_ADD, parseTree);

	}

	/**
	 * Parse Subract Statement
	 * @param inputToken first token in scanner buffer
	 * @throws IOException
	 */
	private void parseSubtractStatement(Token inputToken) throws IOException {

		parseTree = new StatementNode(TreeNodeType.STATEMENT, "SUBTRACT STATEMENT");

		// Match and consume SUBTRACT
		match(inputToken, COBOLTokenType.SUBTRACT, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume LHS of expression
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

		// Match and consume RHS of expression
		matchRepeatingSequence(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
				COBOLTokenType.INTEGER, COBOLTokenType.ROUNDED);
		inputToken = scanner.getCurrentToken();

		// Handle GIVING Clause
		if (inputToken.getType() == COBOLTokenType.GIVING) {
			parseGivingClause(inputToken);
		}

		inputToken = scanner.getCurrentToken();

		// Handle On size error
		if (inputToken.getType() == COBOLTokenType.ON || inputToken.getType() == COBOLTokenType.SIZE) {
			parseOnSizeError(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		inputToken = scanner.getCurrentToken();

		// Handle Not On size error
		if (inputToken.getType() == COBOLTokenType.NOT) {
			parseOnSizeError(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		// Match and consume closing tag
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_SUBTRACT, parseTree);

	}

	/**
	 * Parse Divide statement
	 * @param inputToken first token in scanner buffer
	 * @throws IOException
	 */
	private void parseDivideStatement(Token inputToken) throws IOException {
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		// Match and consume DIVIDE
		match(inputToken, COBOLTokenType.DIVIDE, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume LHS of Expression
		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
		inputToken = scanner.getCurrentToken();

		// Match and consume INTO | BY
		matchAlternation(inputToken, parseTree, COBOLTokenType.INTO, COBOLTokenType.BY);
		inputToken = scanner.getCurrentToken();

		// Match and consume RHS of expression
		matchRepeatingSequence(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
				COBOLTokenType.INTEGER, COBOLTokenType.ROUNDED);
		inputToken = scanner.getCurrentToken();

		// Handle GIVING Clause
		if (inputToken.getType() == COBOLTokenType.GIVING) {
			parseGivingClause(inputToken);
		}

		inputToken = scanner.getCurrentToken();

		// Match and consume REMAINDER if present
		match(inputToken, COBOLTokenType.REMAINDER, parseTree);
		inputToken = scanner.getCurrentToken();

		// MATCH identifier if present
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
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

		// Match and consume closing tag
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_DIVIDE, parseTree);

	}

	/**
	 * Parse Multiple Statement
	 * @param inputToken first token in scanner buffer
	 * @throws IOException
	 */
	private void parseMultiplyStatement(Token inputToken) throws IOException {
		parseTree = new StatementNode(TreeNodeType.STATEMENT, "MULTIPLY STATEMENT");

		// Match and consume MULTIPLY
		match(inputToken, COBOLTokenType.MULTIPLY, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match LHS of expression
		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
		inputToken = scanner.getCurrentToken();

		// Match and consume BY
		match(inputToken, COBOLTokenType.BY, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match RHS of expression
		matchRepeatingSequence(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
				COBOLTokenType.INTEGER, COBOLTokenType.ROUNDED);
		inputToken = scanner.getCurrentToken();

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

		// Consume closing tag
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_MULTIPLY, parseTree);

	}

	/**
	 * Parse Compute statement
	 * @param inputToken first token in scanner buffer
	 * @throws IOException
	 */
	private void parseComputeStatement(Token inputToken) throws IOException {

		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		// Match and consume Computer
		match(inputToken, COBOLTokenType.COMPUTE, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume LHS of expression
		matchRepeatingSequence(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.ROUNDED);
		inputToken = scanner.getCurrentToken();

		// Matcha and consume Equals symbol
		match(inputToken, COBOLTokenType.EQUALS_SYMBOL, parseTree);
		inputToken = scanner.getCurrentToken();

		// While the input token is either an operand (i.e number, identifier, literal) Or an operator
		// i.e + - ( ) etc repeated parse the input token
		while (isOperand(inputToken) || isOperator(inputToken)) {
			parseArithmeticExpression(parseTree, inputToken);
			inputToken = scanner.getCurrentToken();
		}

		inputToken = scanner.getCurrentToken();

		// Handle on size error
		if (inputToken.getType() == COBOLTokenType.ON || inputToken.getType() == COBOLTokenType.SIZE) {
			parseOnSizeError(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT); // Treat size error as its own statement so switch to compound statement
		}

		inputToken = scanner.getCurrentToken();

		// handle not on size error
		if (inputToken.getType() == COBOLTokenType.NOT) {
			parseOnSizeError(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT); // Same as on size error - treat as compound statement
		}
		
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_COMPUTE, parseTree);

	}

	/**
	 * Parse on Size error 
	 * @param inputToken first token in scanner buffer
	 * @throws IOException
	 */
	private void parseOnSizeError(Token inputToken) throws IOException {

		// On Error is effectively a conditional, so create new child statement to contain it
		StatementNode node = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, "CONDITIONAL STATEMENT");

		// sub tree to hold the condition 
		ParseTreeNode conditionNode = new ParseTreeNode(TreeNodeType.CONDITION, "CONDITION");
		node.addChild(conditionNode);
		
		// Match and consume condition. Any in sequence not present are ignored
		matchSequence(inputToken, conditionNode, COBOLTokenType.NOT, COBOLTokenType.ROUNDED, COBOLTokenType.ON,
				COBOLTokenType.SIZE, COBOLTokenType.ERROR);
		inputToken = scanner.getCurrentToken();

		// sub tree to hold condition body - should be another statement
		ParseTreeNode onErrorBody = new ParseTreeNode(TreeNodeType.CONDITION_BODY, "CONDITION BODY");
		node.addChild(onErrorBody);
		// CONSUME STATEMENT
		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue().toLowerCase())) {
			StatementParser statementParser = new StatementParser(scanner);
			statementParser.addListener(listener);
			onErrorBody.addChild(statementParser.parse(inputToken));
			inputToken = scanner.getCurrentToken();
		}

		// Add entire tree to the parent
		parseTree.addChild(node);
	}

	/**
	 * Handle Giving clause 
	 * @param inputToken
	 * @throws IOException
	 */
	private void parseGivingClause(Token inputToken) throws IOException {

		// CONSUME GIVING
		match(inputToken, COBOLTokenType.GIVING, parseTree);
		inputToken = scanner.getCurrentToken();
		
		// Match RHS of Giving - could be any time of identifier or literal..
		matchRepeatingSequence(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
				COBOLTokenType.INTEGER, COBOLTokenType.ROUNDED);

	}

}
