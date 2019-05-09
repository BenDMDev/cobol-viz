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

/**
 * Parser for Inspect Statement
 * @author Ben
 *
 */
public class InspectStatementParser extends StatementParser {

	public InspectStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());
		
		// Match and consume Inspect
		match(inputToken, COBOLTokenType.INSPECT, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume Identifier
		match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
		inputToken = scanner.getCurrentToken();

		// Handle type of inspection
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

		// Match and consume sequence Identifier For 
		matchSequence(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.FOR);
		inputToken = scanner.getCurrentToken();

		// Check input token is valid prefix 
		while (validForPrefix(inputToken)) {

			parseCharactersClause(inputToken);
			inputToken = scanner.getCurrentToken();
			parseLeaderFirstClause(inputToken);
			inputToken = scanner.getCurrentToken();
			parseBeforeAfterClause(inputToken);

		}
	}

	private void parseTallyingClause(Token inputToken) throws IOException {
		
		// Match and consume Tallying
		match(inputToken, COBOLTokenType.TALLYING, parseTree);
		inputToken = scanner.getCurrentToken();
		
		// Look ahead and see if FOR Clause is present
		while (inputToken.getType() == COBOLTokenType.IDENTIFIER
				&& scanner.lookAhead().getType() == COBOLTokenType.FOR) {
			parseForClause(inputToken);
			inputToken = scanner.getCurrentToken();
		}

		// Handle Replacing
		if (inputToken.getType() == COBOLTokenType.REPLACING) {
			parseReplacingClause(inputToken);
		}

	}

	private void parseReplacingClause(Token inputToken) throws IOException {

		// Match and consume replacing
		match(inputToken, COBOLTokenType.REPLACING, parseTree);
		inputToken = scanner.getCurrentToken();

		// Handle FOR clause
		while (validForPrefix(inputToken)) {
			parseCharactersClause(inputToken);
			inputToken = scanner.getCurrentToken();
			parseLeaderFirstClause(inputToken);
			inputToken = scanner.getCurrentToken();
			parseBeforeAfterClause(inputToken);

		}

	}

	
	private void parseConvertingClause(Token inputToken) throws IOException {
		
		// Match and consume Converting
		match(inputToken, COBOLTokenType.CONVERTING, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match converting type
		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
				COBOLTokenType.STRING_LITERAL, COBOLTokenType.INTEGER, COBOLTokenType.FIGURATIVE_CONSTANT);
		inputToken = scanner.getCurrentToken();

		// Match converting TO
		match(inputToken, COBOLTokenType.TO, parseTree);
		inputToken = scanner.getCurrentToken();
		
		// Match converting TO type
		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
				COBOLTokenType.STRING_LITERAL, COBOLTokenType.INTEGER, COBOLTokenType.FIGURATIVE_CONSTANT);
		inputToken = scanner.getCurrentToken();
		
		// Handle BeforeAfter Clause
		parseBeforeAfterClause(inputToken);

	}

	private void parseCharactersClause(Token inputToken) throws IOException {

		// Match and consume Characters
		match(inputToken, COBOLTokenType.CHARACTERS, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume BY
		match(inputToken, COBOLTokenType.BY, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match repeating sequence of ((BEFORE | AFTER) INITIAL ( IDENTIFIER | INTEGER | STRING_LITERAL | REAL))+
		matchRepeatingSequence(inputToken, parseTree, COBOLTokenType.BEFORE, COBOLTokenType.AFTER,
				COBOLTokenType.INITIAL, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER,
				COBOLTokenType.STRING_LITERAL, COBOLTokenType.REAL);

	}

	private void parseBeforeAfterClause(Token inputToken) throws IOException {
		
		// Keep looping while tokens are either before or after
		while (inputToken.getType() == COBOLTokenType.BEFORE || inputToken.getType() == COBOLTokenType.AFTER) {
			// Match alternation BEFORE | AFTER
			matchAlternation(inputToken, parseTree, COBOLTokenType.BEFORE, COBOLTokenType.AFTER);
			inputToken = scanner.getCurrentToken();
			// Match INITIAL
			match(inputToken, COBOLTokenType.INITIAL, parseTree);
			inputToken = scanner.getCurrentToken();
			// Match alternation for Type
			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
					COBOLTokenType.STRING_LITERAL, COBOLTokenType.INTEGER, COBOLTokenType.FIGURATIVE_CONSTANT);
			inputToken = scanner.getCurrentToken();

		}
	}

	private void parseLeaderFirstClause(Token inputToken) throws IOException {

		// Match and consume ALL | LEADING | FIRST
		// Annoyingly ALL is both a keyword and a Constant... so have to match both
		matchAlternation(inputToken, parseTree, COBOLTokenType.ALL, COBOLTokenType.FIGURATIVE_CONSTANT, COBOLTokenType.LEADING, COBOLTokenType.FIRST);
		inputToken = scanner.getCurrentToken();

		while (isIdentifierOrLiteral(inputToken)) {
			// Match and consume Type
			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
					COBOLTokenType.STRING_LITERAL, COBOLTokenType.INTEGER, COBOLTokenType.FIGURATIVE_CONSTANT);
			inputToken = scanner.getCurrentToken();

			// Match and consume BY
			match(inputToken, COBOLTokenType.BY, parseTree);
			inputToken = scanner.getCurrentToken();

			// Match and consume Type
			matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL,
					COBOLTokenType.STRING_LITERAL, COBOLTokenType.INTEGER, COBOLTokenType.FIGURATIVE_CONSTANT);
			
			// Handle before After
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
		case FIGURATIVE_CONSTANT:
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
