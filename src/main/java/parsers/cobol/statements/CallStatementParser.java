package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

/**
 * Parse for a Call Statement
 * 
 * @author Ben
 *
 */
public class CallStatementParser extends StatementParser {

	public CallStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {

		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		// Match and consume CALL
		match(inputToken, COBOLTokenType.CALL, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume LHS of call statement
		matchRepeatingAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER,
				COBOLTokenType.REAL, COBOLTokenType.STRING_LITERAL, COBOLTokenType.FIGURATIVE_CONSTANT);
		inputToken = scanner.getCurrentToken();

		// Match and consume USING
		matchSequence(inputToken, parseTree, COBOLTokenType.USING, COBOLTokenType.BY);
		inputToken = scanner.getCurrentToken();

		// Match and consume REFERENCE | CONTENT
		matchAlternation(inputToken, parseTree, COBOLTokenType.REFERENCE, COBOLTokenType.CONTENT);
		inputToken = scanner.getCurrentToken();

		// Match and consume RHS of statement (including comma separator)
		while (inputToken.getType() == COBOLTokenType.IDENTIFIER) {
			match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.COMMA_SYMBOL, parseTree);
			inputToken = scanner.getCurrentToken();
		}

		// Handle on Exception OR Overflow Errors
		if (inputToken.getType() == COBOLTokenType.ON && scanner.lookAhead().getType() == COBOLTokenType.EXCEPTION
				|| inputToken.getType() == COBOLTokenType.EXCEPTION) {
			parseOnException(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		} else if (inputToken.getType() == COBOLTokenType.NOT) {
			parseOnException(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		} else if (inputToken.getType() == COBOLTokenType.ON
				&& scanner.lookAhead().getType() == COBOLTokenType.OVERFLOW) {
			parseOverflow(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		// Match closing tag
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_CALL, parseTree);

		return parseTree;

	}

	/**
	 * Handles overflow errors
	 * 
	 * @param inputToken
	 * @throws IOException
	 */
	private void parseOverflow(Token inputToken) throws IOException {

		// Treat errors as conditional statements
		StatementNode overflow = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, "CONDITIONAL STATEMENT");

		// Root node for the condition 
		ParseTreeNode condition = new ParseTreeNode(TreeNodeType.CONDITION, "CONDITION");
		overflow.addChild(condition);

		// Match condition
		matchSequence(inputToken, condition, COBOLTokenType.ON, COBOLTokenType.OVERFLOW);
		inputToken = scanner.getCurrentToken();

		// Root node for condition body - should be another statement
		ParseTreeNode onOverFlowBody = new ParseTreeNode(TreeNodeType.CONDITION_BODY, "CONDITION BODY");
		overflow.addChild(onOverFlowBody);

		// CONSUME STATEMENT
		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue().toLowerCase())) {
			StatementParser statementParser = new StatementParser(scanner);
			onOverFlowBody.addChild(statementParser.parse(inputToken));
			inputToken = scanner.getCurrentToken();
		}

		// Add to parent tree
		parseTree.addChild(overflow);

	}

	/**
	 * Handle exception errors
	 * @param inputToken
	 * @throws IOException
	 */
	private void parseOnException(Token inputToken) throws IOException {

		// Treat errors as conditional statements
		StatementNode exception = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, "CONDITIONAL STATEMENT");

		// Root node for the condition 
		ParseTreeNode condition = new ParseTreeNode(TreeNodeType.CONDITION, "CONDITION");
		exception.addChild(condition);
		
		// Match condition
		matchSequence(inputToken, condition, COBOLTokenType.NOT, COBOLTokenType.ON, COBOLTokenType.EXCEPTION);
		inputToken = scanner.getCurrentToken();

		// Root node for condition body - should be another statement
		ParseTreeNode onExceptionBody = new ParseTreeNode(TreeNodeType.CONDITION_BODY, "CONDITION BODY");
		exception.addChild(onExceptionBody);

		// CONSUME STATEMENT
		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue().toLowerCase())) {
			StatementParser statementParser = new StatementParser(scanner);
			onExceptionBody.addChild(statementParser.parse(inputToken));
			inputToken = scanner.getCurrentToken();
		}

		// Add to parent tree
		parseTree.addChild(exception);
	}

}
