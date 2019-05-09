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
 * Parser for Rewrite Statement
 * @author Ben
 *
 */
public class RewriteStatementParser extends StatementParser {

	public RewriteStatementParser(Scanner scanner) {
		super(scanner);
		
	}

	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());
		// Match and consume Rewrite
		match(inputToken, COBOLTokenType.REWRITE, parseTree);
		inputToken = scanner.getCurrentToken();
		
		// Match IDENTIFIER FROM IDENTIFIER
		matchSequence(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.FROM, COBOLTokenType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();
		
		// Check for invalid clause
		if(inputToken.getType() == COBOLTokenType.INVALID) {
			parseInvalidClause(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}
		
		// Check for not invalid clause
		inputToken = scanner.getCurrentToken();
		if(inputToken.getType() == COBOLTokenType.NOT) {
			parseInvalidClause(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}
		
		// Match and consume closing tag
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_REWRITE, parseTree);
		return parseTree;
	}
	
	
	private void parseInvalidClause(Token inputToken) throws IOException{ 
		
		// Treat error as Conditional statement
		StatementNode node = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, "CONDITIONAL STATEMENT");

		ParseTreeNode conditionNode = new ParseTreeNode(TreeNodeType.CONDITION, "CONDITION");

		node.addChild(conditionNode);
	
		// Match condition
		matchSequence(inputToken, conditionNode,  COBOLTokenType.NOT,  COBOLTokenType.INVALID, COBOLTokenType.KEY);
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
