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
 * Parser for Delete statement
 * 
 * @author Ben
 *
 */
public class DeleteStatementParser extends StatementParser {

	public DeleteStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {

		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		// Match and Consume Delete
		match(inputToken, COBOLTokenType.DELETE, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and Consume Identifier
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume Record
		match(inputToken, COBOLTokenType.RECORD, parseTree);
		inputToken = scanner.getCurrentToken();

		// Handle Invalid Clause
		if (inputToken.getType() == COBOLTokenType.INVALID) {
			parseInvalidClause(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		inputToken = scanner.getCurrentToken();
		// Handle not invalid clause
		if (inputToken.getType() == COBOLTokenType.NOT) {
			parseInvalidClause(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}

		// Match closing tag
		match(inputToken, COBOLTokenType.END_DELETE, parseTree);
		inputToken = scanner.getCurrentToken();

		return parseTree;
	}

	private void parseInvalidClause(Token inputToken) throws IOException {

		// Treat as if conditional statement
		StatementNode onInvalid = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, inputToken.getTokenValue());

		// Root node for condition body
		ParseTreeNode condition = new ParseTreeNode(TreeNodeType.CONDITION, inputToken.getTokenValue());

		// Match conditions
		matchSequence(inputToken, condition, COBOLTokenType.NOT, COBOLTokenType.INVALID, COBOLTokenType.KEY);
		

		inputToken = scanner.getCurrentToken();

		onInvalid.addChild(condition);

		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue().toLowerCase())) {
			StatementParser sParser = new StatementParser(scanner);
			sParser.addListener(listener);
			ParseTreeNode conditionBody = new ParseTreeNode(TreeNodeType.CONDITION_BODY, inputToken.getTokenValue());
			conditionBody.addChild(sParser.parse(inputToken));
			onInvalid.addChild(conditionBody);
			parseTree.addChild(onInvalid);
		}

	}

}
