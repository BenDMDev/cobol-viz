package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.TokenType;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class SortStatementParser extends StatementParser {

	public SortStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {

		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		match(inputToken, COBOLTokenType.SORT, parseTree);
		inputToken = scanner.getCurrentToken();

		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();

		matchRepeatingSequence(inputToken, parseTree, COBOLTokenType.ON, COBOLTokenType.ASCENDING,
				COBOLTokenType.DESCENDING, COBOLTokenType.KEY, COBOLTokenType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();

		matchSequence(inputToken, parseTree, COBOLTokenType.WITH, COBOLTokenType.DUPLICATES, COBOLTokenType.IN,
				COBOLTokenType.ORDER);
		inputToken = scanner.getCurrentToken();

		// Match INPUT USING
		if (inputToken.getType() == COBOLTokenType.INPUT) {
			parseInputClause(inputToken);
		}
		
		inputToken = scanner.getCurrentToken();

		// MATCH OUTPUT GIVING
		if (inputToken.getType() == COBOLTokenType.OUTPUT) {
			parseOutputClause(inputToken);

		}
		return parseTree;

	}
	
	private void parseOutputClause(Token inputToken) throws IOException {
		
		parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
		
		ParseTreeNode referenceRoot = new StatementNode(TreeNodeType.REFERENCE, inputToken.getTokenValue());
		parseTree.addChild(referenceRoot);
		
		matchSequence(inputToken, referenceRoot, COBOLTokenType.OUTPUT, COBOLTokenType.PROCEDURE, COBOLTokenType.IS);
		inputToken = scanner.getCurrentToken();

		if (inputToken.getType() == COBOLTokenType.IDENTIFIER) {
			ParseTreeNode referenceNode = new ParseTreeNode(TreeNodeType.REFERENCE_VALUE,
					inputToken.getTokenValue());
			scanner.scan();
			inputToken = scanner.getCurrentToken();
			referenceRoot.addChild(referenceNode);
		}
		matchAlternation(inputToken, referenceRoot, COBOLTokenType.THROUGH, COBOLTokenType.THRU);
		inputToken = scanner.getCurrentToken();

		if (inputToken.getType() == COBOLTokenType.IDENTIFIER) {
			ParseTreeNode referenceNode = new ParseTreeNode(TreeNodeType.REFERENCE_VALUE,
					inputToken.getTokenValue());
			scanner.scan();
			inputToken = scanner.getCurrentToken();
			referenceRoot.addChild(referenceNode);
		}
		
		match(inputToken, COBOLTokenType.GIVING, referenceRoot);
		inputToken = scanner.getCurrentToken();

		matchRepetition(inputToken, referenceRoot, COBOLTokenType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();
		
		
	}
	
	private void parseInputClause(Token inputToken) throws IOException {
		parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);

		ParseTreeNode referenceRoot = new StatementNode(TreeNodeType.REFERENCE, inputToken.getTokenValue());
		parseTree.addChild(referenceRoot);
		matchSequence(inputToken, referenceRoot, COBOLTokenType.INPUT, COBOLTokenType.PROCEDURE, COBOLTokenType.IS);
		inputToken = scanner.getCurrentToken();

		if (inputToken.getType() == COBOLTokenType.IDENTIFIER) {
			ParseTreeNode referenceNode = new ParseTreeNode(TreeNodeType.REFERENCE_VALUE,
					inputToken.getTokenValue());
			scanner.scan();
			inputToken = scanner.getCurrentToken();
			referenceRoot.addChild(referenceNode);
		}
		
		matchAlternation(inputToken, referenceRoot, COBOLTokenType.THROUGH, COBOLTokenType.THRU);
		inputToken = scanner.getCurrentToken();
		if (inputToken.getType() == COBOLTokenType.IDENTIFIER) {
			ParseTreeNode referenceNode = new ParseTreeNode(TreeNodeType.REFERENCE_VALUE,
					inputToken.getTokenValue());
			scanner.scan();
			inputToken = scanner.getCurrentToken();
			referenceRoot.addChild(referenceNode);
		}
		
		
		match(inputToken, COBOLTokenType.USING, referenceRoot);
		inputToken = scanner.getCurrentToken();

		matchRepetition(inputToken, referenceRoot, COBOLTokenType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();
		
	}

}
