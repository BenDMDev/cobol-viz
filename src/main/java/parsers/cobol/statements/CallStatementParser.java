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

		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		match(inputToken, COBOLTokenType.CALL, parseTree);
		inputToken = scanner.getCurrentToken();

		matchRepeatingAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER, COBOLTokenType.REAL, COBOLTokenType.STRING_LITERAL, COBOLTokenType.FIGURATIVE_CONSTANT);
		inputToken = scanner.getCurrentToken();

		
		matchSequence(inputToken, parseTree, COBOLTokenType.USING, COBOLTokenType.BY);
		inputToken = scanner.getCurrentToken();

		matchAlternation(inputToken, parseTree, COBOLTokenType.REFERENCE, COBOLTokenType.CONTENT);
		inputToken = scanner.getCurrentToken();

		while (inputToken.getType() == COBOLTokenType.IDENTIFIER) {
			match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.COMMA_SYMBOL, parseTree);
			inputToken = scanner.getCurrentToken();
		}

		if (inputToken.getType() == COBOLTokenType.ON && scanner.lookAhead().getType() == COBOLTokenType.EXCEPTION
				|| inputToken.getType() == COBOLTokenType.EXCEPTION)
			parseOnException(inputToken);
		else if (inputToken.getType() == COBOLTokenType.NOT)
			parseOnException(inputToken);
		else if (inputToken.getType() == COBOLTokenType.ON && scanner.lookAhead().getType() == COBOLTokenType.OVERFLOW)
			parseOverflow(inputToken);

		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_CALL, parseTree);

		return parseTree;

	}

	private void parseOverflow(Token inputToken) throws IOException {
		StatementNode overflow = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, "CONDITIONAL STATEMENT");

		ParseTreeNode condition = new ParseTreeNode(TreeNodeType.CONDITION, "CONDITION");
		overflow.addChild(condition);
		
		matchSequence(inputToken, condition, COBOLTokenType.ON, COBOLTokenType.OVERFLOW);
		inputToken = scanner.getCurrentToken();

		ParseTreeNode onOverFlowBody = new ParseTreeNode(TreeNodeType.CONDITION_BODY, "CONDITION BODY");
		overflow.addChild(onOverFlowBody);
		
		// CONSUME STATEMENT
		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue().toLowerCase())) {
			StatementParser statementParser = new StatementParser(scanner);
			onOverFlowBody.addChild(statementParser.parse(inputToken));
			inputToken = scanner.getCurrentToken();
		}

		parseTree.addChild(overflow);

	}

	private void parseOnException(Token inputToken) throws IOException {

		StatementNode exception = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, "CONDITIONAL STATEMENT");

		ParseTreeNode condition = new ParseTreeNode(TreeNodeType.CONDITION, "CONDITION");
		exception.addChild(condition);


		matchSequence(inputToken, condition, COBOLTokenType.NOT, COBOLTokenType.ON, COBOLTokenType.EXCEPTION);
		inputToken = scanner.getCurrentToken();
		
		ParseTreeNode onExceptionBody = new ParseTreeNode(TreeNodeType.CONDITION_BODY, "CONDITION BODY");
		exception.addChild(onExceptionBody);
		
		// CONSUME STATEMENT
		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue().toLowerCase())) {
			StatementParser statementParser = new StatementParser(scanner);
			onExceptionBody.addChild(statementParser.parse(inputToken));
			inputToken = scanner.getCurrentToken();
		}

		parseTree.addChild(exception);
	}

}
