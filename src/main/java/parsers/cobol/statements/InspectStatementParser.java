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

		if (inputToken.getType() == COBOLTokenType.CONVERTING) {
			parseConvertingClause(inputToken);
			inputToken = scanner.getCurrentToken();
		} else if (inputToken.getType() == COBOLTokenType.TALLYING) {
			parseTallyingClause(inputToken);
			inputToken = scanner.getCurrentToken();
		} else if (inputToken.getType() == COBOLTokenType.REPLACING) {
			parseReplacingClause(inputToken);
			inputToken = scanner.getCurrentToken();
		}

		return parseTree;
	}

	private void parseForClause(Token inputToken) throws IOException {

		matchSequence(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.FOR);
		inputToken = scanner.getCurrentToken();

		while (validForPrefix(inputToken)) {

			parseCharactersClause(inputToken);
			inputToken = scanner.getCurrentToken();
			parseLeaderFirstClause(inputToken);
			inputToken = scanner.getCurrentToken();
			parseBeforeAfterClause(inputToken);

		}
	}

	private void parseTallyingClause(Token inputToken) throws IOException {
		match(inputToken, COBOLTokenType.TALLYING, parseTree);
		inputToken = scanner.getCurrentToken();
		while (inputToken.getType() == COBOLTokenType.IDENTIFIER
				&& scanner.lookAhead().getType() == COBOLTokenType.FOR) {
			parseForClause(inputToken);
			inputToken = scanner.getCurrentToken();
		}

		if (inputToken.getType() == COBOLTokenType.REPLACING) {
			parseReplacingClause(inputToken);
		}

	}

	private void parseReplacingClause(Token inputToken) throws IOException {

		match(inputToken, COBOLTokenType.REPLACING, parseTree);
		inputToken = scanner.getCurrentToken();

		while (validForPrefix(inputToken)) {
			parseCharactersClause(inputToken);
			inputToken = scanner.getCurrentToken();
			parseLeaderFirstClause(inputToken);
			inputToken = scanner.getCurrentToken();
			parseBeforeAfterClause(inputToken);

		}

	}

	private void parseConvertingClause(Token inputToken) throws IOException {
		match(inputToken, COBOLTokenType.CONVERTING, parseTree);
		inputToken = scanner.getCurrentToken();

		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
				COBOLTokenType.STRING_LITERAL, COBOLTokenType.INTEGER, COBOLTokenType.FIGURATIVE_CONSTANT);
		inputToken = scanner.getCurrentToken();

		match(inputToken, COBOLTokenType.TO, parseTree);
		inputToken = scanner.getCurrentToken();
		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
				COBOLTokenType.STRING_LITERAL, COBOLTokenType.INTEGER, COBOLTokenType.FIGURATIVE_CONSTANT);
		inputToken = scanner.getCurrentToken();
		parseBeforeAfterClause(inputToken);

	}

	private void parseCharactersClause(Token inputToken) throws IOException {

		match(inputToken, COBOLTokenType.CHARACTERS, parseTree);
		inputToken = scanner.getCurrentToken();

		match(inputToken, COBOLTokenType.BY, parseTree);
		inputToken = scanner.getCurrentToken();

		matchRepeatingSequence(inputToken, parseTree, COBOLTokenType.BEFORE, COBOLTokenType.AFTER,
				COBOLTokenType.INITIAL, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER,
				COBOLTokenType.STRING_LITERAL, COBOLTokenType.REAL);

	}

	private void parseBeforeAfterClause(Token inputToken) throws IOException {
		while (inputToken.getType() == COBOLTokenType.BEFORE || inputToken.getType() == COBOLTokenType.AFTER) {
			matchAlternation(inputToken, parseTree, COBOLTokenType.BEFORE, COBOLTokenType.AFTER);
			inputToken = scanner.getCurrentToken();
			match(inputToken, COBOLTokenType.INITIAL, parseTree);
			inputToken = scanner.getCurrentToken();
			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
					COBOLTokenType.STRING_LITERAL, COBOLTokenType.INTEGER, COBOLTokenType.FIGURATIVE_CONSTANT);
			inputToken = scanner.getCurrentToken();

		}
	}

	private void parseLeaderFirstClause(Token inputToken) throws IOException {

		matchAlternation(inputToken, parseTree, COBOLTokenType.ALL, COBOLTokenType.LEADING, COBOLTokenType.FIRST);
		inputToken = scanner.getCurrentToken();

		while (isIdentifierOrLiteral(inputToken)) {
			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
					COBOLTokenType.STRING_LITERAL, COBOLTokenType.INTEGER, COBOLTokenType.FIGURATIVE_CONSTANT);
			inputToken = scanner.getCurrentToken();

			match(inputToken, COBOLTokenType.BY, parseTree);
			inputToken = scanner.getCurrentToken();

			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
					COBOLTokenType.STRING_LITERAL, COBOLTokenType.INTEGER, COBOLTokenType.FIGURATIVE_CONSTANT);
			parseBeforeAfterClause(inputToken);
			inputToken = scanner.getCurrentToken();
		}

	}

	private boolean validForPrefix(Token inputToken) {

		switch ((COBOLTokenType) inputToken.getType()) {
		case CHARACTERS:
		case LEADING:
		case FIRST:
		case ALL:
		case BEFORE:
		case AFTER:
			return true;
		default:
			return false;
		}
	}

	private boolean isIdentifierOrLiteral(Token inputToken) {
		switch ((COBOLTokenType) inputToken.getType()) {
		case IDENTIFIER:
		case INTEGER:
		case REAL:
		case STRING_LITERAL:
		case FIGURATIVE_CONSTANT:
			return true;
		default:
			return false;
		}
	}

}
