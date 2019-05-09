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
 * Parser for Unstring Statement
 * @author Ben
 *
 */
public class UnstringStatementParser extends StatementParser {

	public UnstringStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		// Match and consume Unstring
		match(inputToken, COBOLTokenType.UNSTRING, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume identifier
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume DELIMITED BY ALL
		matchSequence(inputToken, parseTree, COBOLTokenType.DELIMITED, COBOLTokenType.BY, COBOLTokenType.ALL);
		inputToken = scanner.getCurrentToken();

		// Match and consume alternate types
		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER, COBOLTokenType.REAL,
				COBOLTokenType.STRING_LITERAL);
		inputToken = scanner.getCurrentToken();

		// Match (OR ALL (IDENTIFIER | INTEGER | STRING LITERAL | REAL))*
		matchRepeatingSequence(inputToken, parseTree, COBOLTokenType.OR, COBOLTokenType.ALL, COBOLTokenType.IDENTIFIER,
				COBOLTokenType.INTEGER, COBOLTokenType.STRING_LITERAL, COBOLTokenType.REAL);

		inputToken = scanner.getCurrentToken();

		// Match and consume Into
		match(inputToken, COBOLTokenType.INTO, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match (IDENTIFIER (IN | DELIMTER) IDENTIFIER (COUNT | IN) IDENTIFIER)*
		matchRepeatingSequence(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.IN,
				COBOLTokenType.DELIMITER, COBOLTokenType.IDENTIFIER, COBOLTokenType.COUNT, COBOLTokenType.IN,
				COBOLTokenType.IDENTIFIER);

		inputToken = scanner.getCurrentToken();
		
		// Match WITH POINTER Identifier
		matchSequence(inputToken, parseTree, COBOLTokenType.WITH, COBOLTokenType.POINTER, COBOLTokenType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();
		
		// Match TYLLING IN IDENTIFER
		matchSequence(inputToken, parseTree, COBOLTokenType.TALLYING, COBOLTokenType.IN, COBOLTokenType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();

		// Check for on overflow error
		if (inputToken.getType() == COBOLTokenType.ON || scanner.lookAhead().getType() == COBOLTokenType.OVERFLOW) {
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
			parseOverflowError(inputToken);
			inputToken = scanner.getCurrentToken();
		}

		// Check for Not on overflow error
		if (inputToken.getType() == COBOLTokenType.NOT) {
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
			parseOverflowError(inputToken);
			inputToken = scanner.getCurrentToken();
		}

		// Match and consume closing tag
		match(inputToken, COBOLTokenType.END_UNSTRING, parseTree);

		return parseTree;
	}

	private void parseOverflowError(Token inputToken) throws IOException {

		// Treat error as conditional statement
		StatementNode conditional = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, inputToken.getTokenValue());
		ParseTreeNode condition = new ParseTreeNode(TreeNodeType.CONDITION, inputToken.getTokenValue());
		conditional.addChild(condition);

		// Match condition
		matchSequence(inputToken, condition, COBOLTokenType.NOT, COBOLTokenType.ON, COBOLTokenType.OVERFLOW);
		inputToken = scanner.getCurrentToken();

		// Handle condition statements
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
