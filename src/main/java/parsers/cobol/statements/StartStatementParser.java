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
 * Parser for Start Statement
 * @author Ben
 *
 */
public class StartStatementParser extends StatementParser {

	public StartStatementParser(Scanner scanner) {
		super(scanner);
		
	}
	
	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());
		// Match and consume Start
		match(inputToken, COBOLTokenType.START, parseTree);
		inputToken = scanner.getCurrentToken();
		
		// Match and consume Identifier
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();
		
		// check for Key clause
		if(inputToken.getType() == COBOLTokenType.KEY) {
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);		
			
			matchCondition();
			inputToken = scanner.getCurrentToken();
			
			// Check for invalid clause
			if(inputToken.getType() == COBOLTokenType.INVALID) {
				parseKeyError(inputToken);
				inputToken = scanner.getCurrentToken();
			}
			
			//Check for Not invalid clause
			if(inputToken.getType() == COBOLTokenType.NOT) {
				parseKeyError(inputToken);
				inputToken = scanner.getCurrentToken();
			}
		}
		
		// Match and consume closing tag
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_START, parseTree);
		
		
		
		return parseTree;
	}
	
	
	private void matchCondition() throws IOException {
		
		matchSequence(scanner.getCurrentToken(), parseTree, COBOLTokenType.KEY, COBOLTokenType.IS);
		matchSequence(scanner.getCurrentToken(), parseTree, COBOLTokenType.EQUAL, COBOLTokenType.TO);
		match(scanner.getCurrentToken(), COBOLTokenType.EQUALS_SYMBOL, parseTree);
		matchSequence(scanner.getCurrentToken(), parseTree,  COBOLTokenType.GREATER, COBOLTokenType.THAN);
		match(scanner.getCurrentToken(), COBOLTokenType.GREATER_THAN_SYMBOL, parseTree);
		matchSequence(scanner.getCurrentToken(), parseTree,  COBOLTokenType.NOT, COBOLTokenType.LESS, COBOLTokenType.THAN);
		matchSequence(scanner.getCurrentToken(), parseTree,  COBOLTokenType.NOT, COBOLTokenType.GREATER_THAN_SYMBOL);
		matchSequence(scanner.getCurrentToken(), parseTree,  COBOLTokenType.GREATER, COBOLTokenType.THAN, COBOLTokenType.OR, COBOLTokenType.EQUAL, COBOLTokenType.TO);
		match(scanner.getCurrentToken(), COBOLTokenType.GREATER_THAN_EQUALS, parseTree);
		match(scanner.getCurrentToken(), COBOLTokenType.IDENTIFIER, parseTree);
		
		
	}
	
	private void parseKeyError(Token inputToken) throws IOException {
		
		// Treat error as conditional statement
		StatementNode node = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, "CONDITIONAL STATEMENT");

		ParseTreeNode conditionNode = new ParseTreeNode(TreeNodeType.CONDITION, "CONDITION");

		node.addChild(conditionNode);
		// Match condition
		matchSequence(inputToken, conditionNode, COBOLTokenType.NOT, COBOLTokenType.INVALID, COBOLTokenType.KEY);
		inputToken = scanner.getCurrentToken();

		// Handle error statements
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
