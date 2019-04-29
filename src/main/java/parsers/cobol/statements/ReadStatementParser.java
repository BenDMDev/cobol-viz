package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class ReadStatementParser extends StatementParser {

	public ReadStatementParser(Scanner scanner) {
		super(scanner);
		
	}

	
	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());
		match(inputToken, COBOLTokenType.READ, parseTree, TreeNodeType.KEYWORD);
		inputToken = scanner.getCurrentToken();
		
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree, TreeNodeType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();
		
		matchSequence(inputToken, TreeNodeType.KEYWORD, parseTree, COBOLTokenType.NEXT, COBOLTokenType.RECORD, COBOLTokenType.INTO);
		inputToken = scanner.getCurrentToken();
		
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree, TreeNodeType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();
		
		matchSequence(inputToken, TreeNodeType.KEYWORD, parseTree, COBOLTokenType.KEY, COBOLTokenType.IS);
		inputToken = scanner.getCurrentToken();
		
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree, TreeNodeType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();
		
		
		if(inputToken.getType() == COBOLTokenType.AT || inputToken.getType() == COBOLTokenType.END){
			parseAtEndClause(inputToken);
			inputToken = scanner.getCurrentToken();
		} else if(inputToken.getType() == COBOLTokenType.INVALID){
			parseValidClause(inputToken);
			inputToken = scanner.getCurrentToken();
		}
		
		if(inputToken.getType() == COBOLTokenType.NOT){
			parseValidClause(inputToken);
			inputToken = scanner.getCurrentToken();
		}
		
		match(inputToken, COBOLTokenType.END_READ, parseTree, TreeNodeType.KEYWORD);
		return parseTree;
	}
	
	
	private void parseValidClause(Token inputToken) throws IOException{ 
		StatementNode node = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, "CONDITIONAL STATEMENT");

		ParseTreeNode conditionNode = new ParseTreeNode(TreeNodeType.CONDITION, "CONDITION");

		node.addChild(conditionNode);
	
		matchSequence(inputToken, TreeNodeType.KEYWORD, conditionNode, COBOLTokenType.NOT, COBOLTokenType.INVALID, COBOLTokenType.KEY);
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
	
	private void parseAtEndClause(Token inputToken) throws IOException{ 
		StatementNode node = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, "CONDITIONAL STATEMENT");

		ParseTreeNode conditionNode = new ParseTreeNode(TreeNodeType.CONDITION, "CONDITION");

		node.addChild(conditionNode);
	
		matchSequence(inputToken, TreeNodeType.KEYWORD, conditionNode, COBOLTokenType.AT, COBOLTokenType.END);
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
