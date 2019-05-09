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
 * Parser for Perform statement
 * 
 * @author Ben
 *
 */
public class PerformStatementParser extends StatementParser {

	public PerformStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {

		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		// Match and consume PERFORM
		match(inputToken, COBOLTokenType.PERFORM, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume procedure name
		// If procedure name then must be outofline perform
		inputToken = scanner.getCurrentToken();
		if (inputToken.getType() == COBOLTokenType.IDENTIFIER) {

			parseOutOfLinePerform(inputToken);
			inputToken = scanner.getCurrentToken();

		} else {

			// Lookahead to see if Times clause
			if (scanner.lookAhead().getType() == COBOLTokenType.TIMES) {
				// Match and consume identifier | integer
				matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER);
				inputToken = scanner.getCurrentToken();
				// Match and consume Times
				match(inputToken, COBOLTokenType.TIMES, parseTree);
				inputToken = scanner.getCurrentToken();

			}

			// Check for With Test clause
			else if (inputToken.getType() == COBOLTokenType.WITH || inputToken.getType() == COBOLTokenType.TEST) {
				parseWithTestClause(inputToken);
				inputToken = scanner.getCurrentToken();
			}

			// Check for Until Clause
			else if (inputToken.getType() == COBOLTokenType.UNTIL) {
				parseUntilCondition(inputToken);
				inputToken = scanner.getCurrentToken();
			}

			// Check for Varying
			if (inputToken.getType() == COBOLTokenType.VARYING) {
				parseVaryingClause(inputToken);
				inputToken = scanner.getCurrentToken();
			}

		}

		match(inputToken, COBOLTokenType.END_PERFORM, parseTree);
		inputToken = scanner.getCurrentToken();

		return parseTree;

	}

	private void parseOutOfLinePerform(Token inputToken) throws IOException {

		parseTree.setTreeType(TreeNodeType.REFERENCE);

		// Add reference node and set attribute to the procedure name
		ParseTreeNode referenceVal = new ParseTreeNode(TreeNodeType.REFERENCE_VALUE, inputToken.getTokenValue());
		scanner.scan();
		parseTree.addChild(referenceVal);

		inputToken = scanner.getCurrentToken();

		// Check for Through clause
		if (inputToken.getType() == COBOLTokenType.THROUGH || inputToken.getType() == COBOLTokenType.THRU) {
			parsePerformThrough(scanner.getCurrentToken());

		}

		// Lookahead for Times clause
		if (scanner.lookAhead().getType() == COBOLTokenType.TIMES) {
			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.TIMES, parseTree);
			inputToken = scanner.getCurrentToken();
		}

		// Check for WITH TEST
		else if (inputToken.getType() == COBOLTokenType.WITH || inputToken.getType() == COBOLTokenType.TEST) {
			parseWithTestClause(inputToken);
			inputToken = scanner.getCurrentToken();
		}

		// Check for Until Clause
		else if (inputToken.getType() == COBOLTokenType.UNTIL) {
			parseUntilCondition(inputToken);
			inputToken = scanner.getCurrentToken();
		}

		// Check for Varying
		if (inputToken.getType() == COBOLTokenType.VARYING) {
			parseVaryingClause(inputToken);
			inputToken = scanner.getCurrentToken();
		}

	}

	private void parsePerformThrough(Token inputToken) throws IOException {

		// Match and consume THROUGH | THRU
		inputToken = scanner.getCurrentToken();
		matchAlternation(inputToken, parseTree, COBOLTokenType.THROUGH, COBOLTokenType.THRU);

		// Match and consume procedure name
		inputToken = scanner.getCurrentToken();
		if (inputToken.getType() == COBOLTokenType.IDENTIFIER) {

			parseTree.setTreeType(TreeNodeType.REFERENCE);
			ParseTreeNode referenceVal = new ParseTreeNode(TreeNodeType.REFERENCE_VALUE, inputToken.getTokenValue());
			scanner.scan();
			parseTree.addChild(referenceVal);
		}
	}

	private void parseUntilCondition(Token inputToken) throws IOException {

		// Set to Loop tree type
		StatementNode loopCondition = new StatementNode(TreeNodeType.LOOP, "LOOP BODY");
		// Match and consume Until
		match(inputToken, COBOLTokenType.UNTIL, loopCondition);
		inputToken = scanner.getCurrentToken();

		// handle condition
		parseCondition(inputToken, loopCondition);
		inputToken = scanner.getCurrentToken();

		// Keep checking for more conditions
		while (inputToken.getType() == COBOLTokenType.AND || inputToken.getType() == COBOLTokenType.OR) {
			matchAlternation(inputToken, loopCondition, COBOLTokenType.AND, COBOLTokenType.OR);
			inputToken = scanner.getCurrentToken();
			parseCondition(inputToken, loopCondition);
			inputToken = scanner.getCurrentToken();
		}

		inputToken = scanner.getCurrentToken();
		// Add statement
		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue().toLowerCase())) {
			System.out.println(inputToken.getTokenValue());
			parseStatements(inputToken);
		}

		parseTree.addChild(loopCondition);

	}

	private void parseWithTestClause(Token inputToken) throws IOException {
		// Match and consume sequence WITH TEST (BEFORE | AFTER)
		matchSequence(inputToken, parseTree, COBOLTokenType.WITH, COBOLTokenType.TEST, COBOLTokenType.BEFORE,
				COBOLTokenType.AFTER);
		inputToken = scanner.getCurrentToken();
		parseUntilCondition(inputToken);

	}

	private void parseVaryingClause(Token inputToken) throws IOException {
		
		// Match and consume Varying
		match(inputToken, COBOLTokenType.VARYING, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume type
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();
		
		// Match and consume FROM
		match(inputToken, COBOLTokenType.FROM, parseTree);
		inputToken = scanner.getCurrentToken();
		
		// Match and consume From Type
		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.STRING_LITERAL,
				COBOLTokenType.INTEGER, COBOLTokenType.REAL, COBOLTokenType.FIGURATIVE_CONSTANT);
		inputToken = scanner.getCurrentToken();

		// Match and consume BY
		match(inputToken, COBOLTokenType.BY, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume alternating type
		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.STRING_LITERAL,
				COBOLTokenType.INTEGER, COBOLTokenType.REAL, COBOLTokenType.FIGURATIVE_CONSTANT);

		inputToken = scanner.getCurrentToken();
		// Check for Until Clause
		if (inputToken.getType() == COBOLTokenType.UNTIL) {
			parseUntilCondition(inputToken);
			inputToken = scanner.getCurrentToken();
		}

		// Handle statements
		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue().toLowerCase())) {
			parseStatements(inputToken);
		}

	}

	private void parseStatements(Token inputToken) throws IOException {

		// keep checking for statements (can be long list in perform statement)
		parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		while (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue().toLowerCase())) {
			StatementParser parser = new StatementParser(scanner);
			parser.addListener(listener);
			parseTree.addChild(parser.parse(inputToken));
			inputToken = scanner.getCurrentToken();
		}

	}

}
