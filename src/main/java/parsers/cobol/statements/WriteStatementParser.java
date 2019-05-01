package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class WriteStatementParser extends StatementParser {

	public WriteStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		match(inputToken, COBOLTokenType.WRITE, parseTree);
		inputToken = scanner.getCurrentToken();

		matchSequence(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.FROM, COBOLTokenType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();

		if (inputToken.getType() == COBOLTokenType.BEFORE || inputToken.getType() == COBOLTokenType.AFTER) {
			parseBeforeAfter(inputToken);
			inputToken = scanner.getCurrentToken();
		}

		if (inputToken.getType() == COBOLTokenType.AT || scanner.lookAhead().getType() == COBOLTokenType.END_OF_PAGE
				|| scanner.lookAhead().getType() == COBOLTokenType.EOP) {
			parseEndOfPage(inputToken);
			inputToken = scanner.getCurrentToken();
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		if (inputToken.getType() == COBOLTokenType.NOT) {
			parseEndOfPage(inputToken);
			inputToken = scanner.getCurrentToken();
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}
		
		
		if (inputToken.getType() == COBOLTokenType.INVALID) {
			parseInvalidKey(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
			inputToken = scanner.getCurrentToken();
		}

		inputToken = scanner.getCurrentToken();
		if (inputToken.getType() == COBOLTokenType.NOT) {
			parseInvalidKey(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		match(inputToken, COBOLTokenType.END_WRITE, parseTree);

		return parseTree;
	}

	private void parseBeforeAfter(Token inputToken) throws IOException {
		matchAlternation(inputToken, parseTree, COBOLTokenType.BEFORE, COBOLTokenType.AFTER);
		inputToken = scanner.getCurrentToken();
		
		match(inputToken, COBOLTokenType.ADVANCING, parseTree);
		inputToken = scanner.getCurrentToken();
		
		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER);
		inputToken = scanner.getCurrentToken();
		matchAlternation(inputToken, parseTree, COBOLTokenType.LINE, COBOLTokenType.LINES);
		inputToken = scanner.getCurrentToken();
		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.PAGE);

	}

	
	/**
	 * 
	 * @param inputToken
	 * @throws IOException
	 */
	private void parseEndOfPage(Token inputToken) throws IOException {
		StatementNode node = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, "CONDITIONAL STATEMENT");

		ParseTreeNode conditionNode = new ParseTreeNode(TreeNodeType.CONDITION, "CONDITION");

		node.addChild(conditionNode);

		matchSequence(inputToken, conditionNode, COBOLTokenType.NOT, COBOLTokenType.AT);
		inputToken = scanner.getCurrentToken();
		matchAlternation(inputToken, conditionNode, COBOLTokenType.END_OF_PAGE, COBOLTokenType.EOP);
		inputToken = scanner.getCurrentToken();
		
		ParseTreeNode onErrorBody = new ParseTreeNode(TreeNodeType.CONDITION_BODY, "CONDITION BODY");
		node.addChild(onErrorBody);
		// CONSUME STATEMENT
		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue().toLowerCase())) {
			StatementParser statementParser = new StatementParser(scanner);
			statementParser.addListener(listener);
			onErrorBody.addChild(statementParser.parse(inputToken));
			inputToken = scanner.getCurrentToken();
		}

		parseTree.addChild(node);
	}
	
	/**
	 * 
	 * @param inputToken
	 * @throws IOException
	 */
	private void parseInvalidKey(Token inputToken) throws IOException {

		StatementNode node = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, "CONDITIONAL STATEMENT");

		ParseTreeNode conditionNode = new ParseTreeNode(TreeNodeType.CONDITION, "CONDITION");

		node.addChild(conditionNode);

		matchSequence(inputToken, conditionNode, COBOLTokenType.NOT, COBOLTokenType.INVALID, COBOLTokenType.KEY);
		inputToken = scanner.getCurrentToken();

		ParseTreeNode onErrorBody = new ParseTreeNode(TreeNodeType.CONDITION_BODY, "CONDITION BODY");
		node.addChild(onErrorBody);
		// CONSUME STATEMENT
		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue().toLowerCase())) {
			StatementParser statementParser = new StatementParser(scanner);
			statementParser.addListener(listener);
			onErrorBody.addChild(statementParser.parse(inputToken));
			inputToken = scanner.getCurrentToken();
		}

		parseTree.addChild(node);

	}

}
