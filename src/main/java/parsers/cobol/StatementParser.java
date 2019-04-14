package main.java.parsers.cobol;

import java.io.IOException;
import java.util.HashSet;

import main.java.parsers.Parser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.TokenType;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.scanners.tokens.cobol.NumberToken;
import main.java.scanners.tokens.cobol.WordToken;
import main.java.trees.ParseTreeNode;

public class StatementParser extends Parser {

	private HashSet<TokenType> userInputAlternatives;
	private HashSet<TokenType> statementPrefixes;

	public StatementParser(Scanner l) {
		super(l);
		userInputAlternatives = new HashSet<TokenType>();
		statementPrefixes = new HashSet<TokenType>();
		
		userInputAlternatives.add(COBOLTokenType.IDENTIFIER);
		userInputAlternatives.add(COBOLTokenType.STRING_LITERAL);
		userInputAlternatives.add(COBOLTokenType.REAL);
		userInputAlternatives.add(COBOLTokenType.INTEGER);
		
		statementPrefixes.add(COBOLTokenType.MOVE);
		statementPrefixes.add(COBOLTokenType.ADD);
		statementPrefixes.add(COBOLTokenType.IF);
	}

	@Override
	public ParseTreeNode parse(Token t) throws IOException {

		ParseTreeNode p = new ParseTreeNode("STATEMENT");

		switch ((COBOLTokenType) t.getType()) {
		case MOVE:
			p.addChild(parseMoveStatement(t));
			break;
		case ADD:
			p.addChild(parseAddStatement(t));
			break;
		case IF:
			p.addChild(parseConditionalStatement(t));
			break;
		default:
			break;
		}

		return p;
	}

	public ParseTreeNode parseMoveStatement(Token inputToken) throws IOException {

		ParseTreeNode p = new ParseTreeNode("MOVE STATEMENT");

		match(inputToken, COBOLTokenType.MOVE, p);
		inputToken = lexer.getCurrentToken();

		HashSet<TokenType> tokens = new HashSet<TokenType>();
		tokens.add(COBOLTokenType.CORRESPONDING);
		tokens.add(COBOLTokenType.CORR);

		matchList(inputToken, tokens, p);
		inputToken = lexer.getCurrentToken();

		if (inputToken instanceof WordToken || inputToken instanceof NumberToken) {
			p.addChild(new ParseTreeNode(inputToken.getTokenValue()));
			lexer.scan();
			inputToken = lexer.getCurrentToken();
		}

		match(inputToken, COBOLTokenType.TO, p);
		inputToken = lexer.getCurrentToken();

		while (inputToken.getType() == COBOLTokenType.IDENTIFIER) {

			match(inputToken, COBOLTokenType.IDENTIFIER, p);
			inputToken = lexer.getCurrentToken();
		}

		return p;

	}

	public ParseTreeNode parseAddStatement(Token inputToken) throws IOException {

		ParseTreeNode root = new ParseTreeNode("ADD STATEMENT");

		// Match and consume ADD
		match(inputToken, COBOLTokenType.ADD, root);
		inputToken = lexer.getCurrentToken();

		while (inputToken.getType() == COBOLTokenType.IDENTIFIER) {

			match(inputToken, COBOLTokenType.IDENTIFIER, root);
			inputToken = lexer.getCurrentToken();
		}

		// Match and consume TO
		match(inputToken, COBOLTokenType.TO, root);
		inputToken = lexer.getCurrentToken();

		// Match and Consume Identifiers and Optional ROUNDED clause
		while (inputToken.getType() == COBOLTokenType.IDENTIFIER) {

			match(inputToken, COBOLTokenType.IDENTIFIER, root);
			inputToken = lexer.getCurrentToken();
			match(inputToken, COBOLTokenType.ROUNDED, root);
			inputToken = lexer.getCurrentToken();

		}

		return root;
	}

	public ParseTreeNode parseConditionalStatement(Token inputToken) throws IOException {

		ParseTreeNode root = new ParseTreeNode("CONDITIONAL STATEMENT");

		
		// Match and consume IF
		match(inputToken, COBOLTokenType.IF, root);
		inputToken = lexer.getCurrentToken();
		

		// Match and Consume LHS of IF
		matchList(inputToken, userInputAlternatives, root);
		inputToken = lexer.getCurrentToken();
		
		// Parse Condition Body of IF
		parseCondition(inputToken, root);

		// Get next Token - RHS of IF condition
		inputToken = lexer.getCurrentToken();
		matchList(inputToken, userInputAlternatives, root);

		inputToken = lexer.getCurrentToken();

		// Recursively call Parse Statement for body of IF
		while(statementPrefixes.contains(inputToken.getType())){
			root.addChild(parse(inputToken));
			inputToken = lexer.getCurrentToken();
		}

		inputToken = lexer.getCurrentToken();

		if (inputToken.getType() == COBOLTokenType.ELSE) {
			match(inputToken, COBOLTokenType.ELSE, root);
			inputToken = lexer.getCurrentToken();
			root.addChild(parse(inputToken)); // Recursively call more
												// statements
		}
		
		return root;

	}

	private ParseTreeNode parseCondition(Token inputToken, ParseTreeNode root) throws IOException {

		match(inputToken, COBOLTokenType.IS, root);
		match(lexer.getCurrentToken(), COBOLTokenType.NOT, root);
		match(lexer.getCurrentToken(), COBOLTokenType.GREATER, root);
		match(lexer.getCurrentToken(), COBOLTokenType.THAN, root);
		match(lexer.getCurrentToken(), COBOLTokenType.OR, root);
		match(lexer.getCurrentToken(), COBOLTokenType.EQUAL, root);
		match(lexer.getCurrentToken(), COBOLTokenType.TO, root);
		match(lexer.getCurrentToken(), COBOLTokenType.GREATER_THAN, root);
		match(lexer.getCurrentToken(), COBOLTokenType.LESS, root);
		match(lexer.getCurrentToken(), COBOLTokenType.THAN, root);
		match(lexer.getCurrentToken(), COBOLTokenType.OR, root);
		match(lexer.getCurrentToken(), COBOLTokenType.EQUAL, root);
		match(lexer.getCurrentToken(), COBOLTokenType.TO, root);
		match(lexer.getCurrentToken(), COBOLTokenType.LESS_THAN, root);
		match(lexer.getCurrentToken(), COBOLTokenType.EQUAL, root);
		match(lexer.getCurrentToken(), COBOLTokenType.TO, root);
		match(lexer.getCurrentToken(), COBOLTokenType.EQUALS, root);

		
		return root;
	}

}
