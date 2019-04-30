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

public class InspectStatementParser extends StatementParser {

	public InspectStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());
		match(inputToken, COBOLTokenType.INSPECT, parseTree);
		inputToken = scanner.getCurrentToken();

		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();

		matchAlternation(inputToken, parseTree, COBOLTokenType.TALLYING, COBOLTokenType.REPLACING);
		inputToken = scanner.getCurrentToken();

		if (inputToken.getType() == COBOLTokenType.IDENTIFIER) {
			parseForClause(inputToken);
			inputToken = scanner.getCurrentToken();
		}

		return parseTree;
	}

	private void parseForClause(Token inputToken) throws IOException {
		matchSequence(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.FOR);
		inputToken = scanner.getCurrentToken();

		while (validForPrefix(inputToken)) {
			if (inputToken.getType() == COBOLTokenType.CHARACTERS) {
				parseCharactersClause(inputToken);
				inputToken = scanner.getCurrentToken();
			} 
		}
	}

	private void parseReplacying(Token inputToken) {

	}

	private void parseCharactersClause(Token inputToken) throws IOException {
		
		match(inputToken, COBOLTokenType.CHARACTERS, parseTree);
		inputToken = scanner.getCurrentToken();

		matchRepeatingSequence(inputToken, parseTree, COBOLTokenType.BEFORE, COBOLTokenType.AFTER,
				COBOLTokenType.INITIAL, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER,
				COBOLTokenType.STRING_LITERAL, COBOLTokenType.REAL);
		
	}
	
	private boolean validForPrefix(Token inputToken) {

		switch ((COBOLTokenType) inputToken.getType()) {
		case CHARACTERS:
		case ALL:
		case LEADING:
		case FIRST:
		case BEFORE:
		case AFTER:
			return true;
		default:
			return false;
		}
	}

}
