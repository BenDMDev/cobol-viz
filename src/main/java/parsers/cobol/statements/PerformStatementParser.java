package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

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
		inputToken = scanner.getCurrentToken();
		if (inputToken.getType() == COBOLTokenType.IDENTIFIER) {
			
			parseTree.setTreeType(TreeNodeType.REFERENCE);
			ParseTreeNode referenceVal = new ParseTreeNode(TreeNodeType.REFERENCE_VALUE, inputToken.getTokenValue());
			scanner.scan();
			parseTree.addChild(referenceVal);
		}

		inputToken = scanner.getCurrentToken();

		if (inputToken.getType() == COBOLTokenType.THROUGH || inputToken.getType() == COBOLTokenType.THRU) {
			parsePerformThrough(scanner.getCurrentToken());

		} else if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue())) {
			inputToken = scanner.getCurrentToken();
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
			while (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue())) {
				StatementParser parser = new StatementParser(scanner);
				parser.addListener(listener);
				parseTree.addChild(parser.parse(inputToken));
				inputToken = scanner.getCurrentToken();
			}
		}

		inputToken = scanner.getCurrentToken();
		if (inputToken.getType() == COBOLTokenType.UNTIL) {
			parseUntilCondition(inputToken);
		}

		return parseTree;

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

		match(inputToken, COBOLTokenType.UNTIL, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and Consume LHS of IF
		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER, COBOLTokenType.REAL,
				COBOLTokenType.STRING_LITERAL);
		inputToken = scanner.getCurrentToken();

		// Parse Condition Body of IF
		parseCondition(inputToken, parseTree);

		// Get next Token - RHS of IF condition
		inputToken = scanner.getCurrentToken();
		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER, COBOLTokenType.REAL,
				COBOLTokenType.STRING_LITERAL);

	}



}
