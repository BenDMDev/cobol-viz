package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class CallStatementParser extends StatementParser {

	public CallStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {
		parseTree = new StatementNode("CALL STATEMENT");

		match(inputToken, COBOLTokenType.CALL, parseTree);
		inputToken = scanner.getCurrentToken();

		matchList(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.STRING_LITERAL);
		inputToken = scanner.getCurrentToken();
		
		
		match(inputToken, COBOLTokenType.USING, parseTree);
		inputToken = scanner.getCurrentToken();

		match(inputToken, COBOLTokenType.BY, parseTree);

		inputToken = scanner.getCurrentToken();
		matchList(inputToken, parseTree, COBOLTokenType.REFERENCE, COBOLTokenType.CONTENT);

		inputToken = scanner.getCurrentToken();

		while (inputToken.getType() == COBOLTokenType.IDENTIFIER) {
			match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
			inputToken = scanner.getCurrentToken();
		}

		if (inputToken.getType() == COBOLTokenType.ON && scanner.lookAhead().getType() == COBOLTokenType.EXCEPTION || inputToken.getType() == COBOLTokenType.EXCEPTION)
			parseOnException(inputToken);
		else if (inputToken.getType() == COBOLTokenType.NOT)
			parseOnException(inputToken);
		else if(inputToken.getType() == COBOLTokenType.ON && scanner.lookAhead().getType() == COBOLTokenType.OVERFLOW)
			parseOverflow(inputToken);

		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_CALL, parseTree);
		
		return parseTree;

	}
	
	private void parseOverflow(Token inputToken) throws IOException {
		StatementNode overflow = new StatementNode("CONDITIONAL STATEMENT");
		overflow.setTreeType(TreeNodeType.CONDITIONAL);
		
		ParseTreeNode condition = new ParseTreeNode("CONDITION");
		condition.setTreeType(TreeNodeType.CONDITION);
		overflow.addChild(condition);

		match(inputToken, COBOLTokenType.ON, condition);
		inputToken = scanner.getCurrentToken();

		match(inputToken, COBOLTokenType.OVERFLOW, condition);
		inputToken = scanner.getCurrentToken();
		
		ParseTreeNode onOverFlowBody = new ParseTreeNode("CONDITION BODY");
		onOverFlowBody.setTreeType(TreeNodeType.CONDITION_BODY);
		overflow.addChild(onOverFlowBody);
		// CONSUME STATEMENT
		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue())) {
			StatementParser statementParser = new StatementParser(scanner);
			onOverFlowBody.addChild(statementParser.parse(inputToken));
			inputToken = scanner.getCurrentToken();
		}
		
		parseTree.addChild(overflow);
		
	}

	private void parseOnException(Token inputToken) throws IOException {

		StatementNode exception = new StatementNode("CONDITIONAL STATEMENT");
		exception.setTreeType(TreeNodeType.CONDITIONAL);

		ParseTreeNode condition = new ParseTreeNode("CONDITION");
		condition.setTreeType(TreeNodeType.CONDITION);
		exception.addChild(condition);

		match(inputToken, COBOLTokenType.NOT, condition);
		inputToken = scanner.getCurrentToken();

		match(inputToken, COBOLTokenType.ON, condition);
		inputToken = scanner.getCurrentToken();

		match(inputToken, COBOLTokenType.EXCEPTION, condition);
		inputToken = scanner.getCurrentToken();

		ParseTreeNode onExceptionBody = new ParseTreeNode("CONDITION BODY");
		onExceptionBody.setTreeType(TreeNodeType.CONDITION_BODY);
		exception.addChild(onExceptionBody);
		// CONSUME STATEMENT
		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue())) {
			StatementParser statementParser = new StatementParser(scanner);
			onExceptionBody.addChild(statementParser.parse(inputToken));
			inputToken = scanner.getCurrentToken();
		}

		parseTree.addChild(exception);
	}

}
