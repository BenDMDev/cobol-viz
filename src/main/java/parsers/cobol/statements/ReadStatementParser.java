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
 * Parser for Read statement
 * @author Ben
 *
 */
public class ReadStatementParser extends StatementParser {

	public ReadStatementParser(Scanner scanner) {
		super(scanner);
		
	}

	
	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());
		
		// Match and consume Read statement
		match(inputToken, COBOLTokenType.READ, parseTree);
		inputToken = scanner.getCurrentToken();
		
		// Match and consume Type
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();
		
		// Match and consume NEXT RECORD INTO
		matchSequence(inputToken, parseTree, COBOLTokenType.NEXT, COBOLTokenType.RECORD, COBOLTokenType.INTO);
		inputToken = scanner.getCurrentToken();
		
		// Match and consume type
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();
		
		// Match and consume KEY IS
		matchSequence(inputToken, parseTree, COBOLTokenType.KEY, COBOLTokenType.IS);
		inputToken = scanner.getCurrentToken();
		
		// Match and consume Type
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();
		
		// Handle Valid and At End clause
		if(inputToken.getType() == COBOLTokenType.AT || inputToken.getType() == COBOLTokenType.END){
			parseAtEndClause(inputToken);
			inputToken = scanner.getCurrentToken();
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		} else if(inputToken.getType() == COBOLTokenType.INVALID){
			parseValidClause(inputToken);
			inputToken = scanner.getCurrentToken();
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}
		
		// Handle Not valid clause
		if(inputToken.getType() == COBOLTokenType.NOT){
			parseValidClause(inputToken);
			inputToken = scanner.getCurrentToken();
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}
		
		// Match and consume end tag
		match(inputToken, COBOLTokenType.END_READ, parseTree);
		return parseTree;
	}
	
	
	private void parseValidClause(Token inputToken) throws IOException{ 
		
		// Treat error as condition statement
		StatementNode node = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, "CONDITIONAL STATEMENT");

		ParseTreeNode conditionNode = new ParseTreeNode(TreeNodeType.CONDITION, "CONDITION");

		node.addChild(conditionNode);
	
		// Match condition
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
	
	private void parseAtEndClause(Token inputToken) throws IOException{ 
		
		// Treat Error as conditional statement
		StatementNode node = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, "CONDITIONAL STATEMENT");

		ParseTreeNode conditionNode = new ParseTreeNode(TreeNodeType.CONDITION, "CONDITION");

		node.addChild(conditionNode);
	
		// Match condition
		matchSequence(inputToken, conditionNode, COBOLTokenType.AT, COBOLTokenType.END);
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
