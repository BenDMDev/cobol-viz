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
 * Parser for Return statement
 * @author Ben
 *
 */
public class ReturnStatementParser extends StatementParser {

	public ReturnStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {

		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());
		// Match Return Identifier RECORD INTO Identifier
		matchSequence(inputToken, parseTree, COBOLTokenType.RETURN, COBOLTokenType.IDENTIFIER, COBOLTokenType.RECORD,
				COBOLTokenType.INTO, COBOLTokenType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();

		// Check for at end clause
		if (inputToken.getType() == COBOLTokenType.AT || inputToken.getType() == COBOLTokenType.END) {
			parseAtEndClause(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		// Check for NOT at end clause
		inputToken = scanner.getCurrentToken();
		if (inputToken.getType() == COBOLTokenType.NOT) {
			parseAtEndClause(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}
		
		// Match and consume closing tag
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_RETURN, parseTree);

		return parseTree;
	}

	private void parseAtEndClause(Token inputToken) throws IOException {

		// Treat as conditional statement
		StatementNode node = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, "CONDITIONAL STATEMENT");

		ParseTreeNode conditionNode = new ParseTreeNode(TreeNodeType.CONDITION, "CONDITION");

		node.addChild(conditionNode);
		// Match condition
		matchSequence(inputToken, conditionNode, COBOLTokenType.NOT, COBOLTokenType.AT, COBOLTokenType.END);
		inputToken = scanner.getCurrentToken();

		ParseTreeNode onErrorBody = new ParseTreeNode(TreeNodeType.CONDITION_BODY, "CONDITION BODY");
		node.addChild(onErrorBody);
		// CONSUME STATEMENT
		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue().toLowerCase())) {
			while((COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue().toLowerCase()))) {
			StatementParser statementParser = new StatementParser(scanner);
			statementParser.addListener(listener);
			onErrorBody.addChild(statementParser.parse(inputToken));
			inputToken = scanner.getCurrentToken();
			}
		}

		parseTree.addChild(node);
	}

}
