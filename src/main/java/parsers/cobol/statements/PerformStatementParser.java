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
		
		if(scanner.lookAhead().getType() == COBOLTokenType.TIMES) {
			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.TIMES, parseTree);
			inputToken = scanner.getCurrentToken();
		} else 	if (inputToken.getType() == COBOLTokenType.WITH || inputToken.getType() == COBOLTokenType.TEST) {
			parseWithTestClause(inputToken);
			inputToken = scanner.getCurrentToken();
		} else if (inputToken.getType() == COBOLTokenType.UNTIL) {
			parseUntilCondition(inputToken);
			inputToken = scanner.getCurrentToken();
		}
		
		if(inputToken.getType() == COBOLTokenType.VARYING) {
			parseVaryingClause(inputToken);
			inputToken = scanner.getCurrentToken();
		}
		
		match(inputToken, COBOLTokenType.END_PERFORM, parseTree);
		inputToken = scanner.getCurrentToken();
		
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
		StatementNode loopCondition = new StatementNode(TreeNodeType.LOOP, "LOOP BODY");
		match(inputToken, COBOLTokenType.UNTIL, loopCondition);
		inputToken = scanner.getCurrentToken();

		parseCondition(inputToken, loopCondition);
		
		parseTree.addChild(loopCondition);

	}

	private void parseWithTestClause(Token inputToken) throws IOException {
		matchSequence(inputToken, parseTree, COBOLTokenType.WITH, COBOLTokenType.TEST, COBOLTokenType.BEFORE,
				COBOLTokenType.AFTER);
		inputToken = scanner.getCurrentToken();
		parseUntilCondition(inputToken);

	}

	private void parseVaryingClause(Token inputToken) throws IOException {
		match(inputToken, COBOLTokenType.VARYING, parseTree);
		inputToken = scanner.getCurrentToken();

		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.FROM, parseTree);
		inputToken = scanner.getCurrentToken();
		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.STRING_LITERAL,
				COBOLTokenType.INTEGER, COBOLTokenType.REAL, COBOLTokenType.FIGURATIVE_CONSTANT);
		inputToken = scanner.getCurrentToken();
		
		match(inputToken, COBOLTokenType.BY, parseTree);
		inputToken = scanner.getCurrentToken();
		
		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.STRING_LITERAL,
				COBOLTokenType.INTEGER, COBOLTokenType.REAL, COBOLTokenType.FIGURATIVE_CONSTANT);
		
		inputToken = scanner.getCurrentToken();
		if(inputToken.getType() == COBOLTokenType.UNTIL) {
			parseUntilCondition(inputToken);
			inputToken = scanner.getCurrentToken();
		}
				
		
	}
	

}
