package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class RewriteStatementParser extends StatementParser {

	public RewriteStatementParser(Scanner scanner) {
		super(scanner);
		
	}

	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());
		match(inputToken, COBOLTokenType.REWRITE, parseTree);
		inputToken = scanner.getCurrentToken();
		matchSequence(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.FROM, COBOLTokenType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();
		
		if(inputToken.getType() == COBOLTokenType.INVALID) {
			parseInvalidClause(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}
		
		inputToken = scanner.getCurrentToken();
		if(inputToken.getType() == COBOLTokenType.NOT) {
			parseInvalidClause(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}
		
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_REWRITE, parseTree);
		return parseTree;
	}
	
	
	private void parseInvalidClause(Token inputToken) throws IOException{ 
		StatementNode node = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, "CONDITIONAL STATEMENT");

		ParseTreeNode conditionNode = new ParseTreeNode(TreeNodeType.CONDITION, "CONDITION");

		node.addChild(conditionNode);
	
		matchSequence(inputToken, conditionNode,  COBOLTokenType.NOT,  COBOLTokenType.INVALID, COBOLTokenType.KEY);
		inputToken = scanner.getCurrentToken();



		ParseTreeNode onErrorBody = new ParseTreeNode(TreeNodeType.CONDITION_BODY, "CONDITION BODY");
		node.addChild(onErrorBody);
		// CONSUME STATEMENT
		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue())) {
			StatementParser statementParser = new StatementParser(scanner);
			statementParser.addListener(listener);
			onErrorBody.addChild(statementParser.parse(inputToken));
			inputToken = scanner.getCurrentToken();
		}

		parseTree.addChild(node);
		
	}
	
	
	
}
