package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class DeleteStatementParser extends StatementParser {

	public DeleteStatementParser(Scanner scanner) {
		super(scanner);
		
	}

	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());
		
		match(inputToken, COBOLTokenType.DELETE, parseTree);
		inputToken = scanner.getCurrentToken();
		
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();
		
		match(inputToken, COBOLTokenType.RECORD, parseTree);
		inputToken = scanner.getCurrentToken();
		
		if(inputToken.getType() == COBOLTokenType.INVALID) {
			parseInvalidClause(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}
		
		inputToken = scanner.getCurrentToken();
		if(inputToken.getType() == COBOLTokenType.NOT){
			parseInvalidClause(inputToken);
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		}
		
		match(inputToken, COBOLTokenType.END_DELETE, parseTree);
		inputToken = scanner.getCurrentToken();
		
		
		return parseTree;
	}
	
	
	private void parseInvalidClause(Token inputToken) throws IOException {
		StatementNode onInvalid = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, inputToken.getTokenValue());
		ParseTreeNode condition = new ParseTreeNode(TreeNodeType.CONDITION, inputToken.getTokenValue());
		
		match(inputToken, COBOLTokenType.NOT, condition);
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.INVALID, condition);
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.KEY, condition);
		inputToken = scanner.getCurrentToken();
		
		onInvalid.addChild(condition);
		
		StatementParser sParser = new StatementParser(scanner);
		sParser.addListener(listener);
		ParseTreeNode conditionBody = new ParseTreeNode(TreeNodeType.CONDITION_BODY, inputToken.getTokenValue());
		conditionBody.addChild(sParser.parse(inputToken));
		onInvalid.addChild(conditionBody);
		parseTree.addChild(onInvalid);
		
		
	}
	
}
