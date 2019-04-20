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
import main.java.trees.cobol.StatementNode;

public class StatementParser extends Parser {

	
	private HashSet<TokenType> statementPrefixes;

	public StatementParser(Scanner l) {
		super(l);
		
		statementPrefixes = new HashSet<TokenType>();	
		
		statementPrefixes.add(COBOLTokenType.MOVE);
		statementPrefixes.add(COBOLTokenType.ADD);
		statementPrefixes.add(COBOLTokenType.IF);
	}

	@Override
	public ParseTreeNode parse(Token t) throws IOException {

		parseTree = new StatementNode("STATEMENT");

		switch ((COBOLTokenType) t.getType()) {
		case MOVE:
			parseTree.addChild(parseMoveStatement(t));
			break;
		case ADD:
			parseTree.addChild(parseAddStatement(t));
			break;
		case IF:
			parseTree.addChild(parseConditionalStatement(t));
			break;
		default:
			break;
		}

		return parseTree;
	}

	public ParseTreeNode parseMoveStatement(Token inputToken) throws IOException {

		ParseTreeNode p = new StatementNode("MOVE STATEMENT");

		match(inputToken, COBOLTokenType.MOVE, p);
		inputToken = lexer.getCurrentToken();

		matchList(inputToken, p, COBOLTokenType.CORRESPONDING, COBOLTokenType.CORR);
		inputToken = lexer.getCurrentToken();

		
		matchList(inputToken, p, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER, COBOLTokenType.REAL);
		inputToken = lexer.getCurrentToken();		
	

		match(inputToken, COBOLTokenType.TO, p);
		inputToken = lexer.getCurrentToken();

		while (inputToken.getType() == COBOLTokenType.IDENTIFIER) {

			match(inputToken, COBOLTokenType.IDENTIFIER, p);
			inputToken = lexer.getCurrentToken();
		}

		return p;

	}

	public ParseTreeNode parseAddStatement(Token inputToken) throws IOException {

		ParseTreeNode root = new StatementNode("ADD STATEMENT");

		// Match and consume ADD
		match(inputToken, COBOLTokenType.ADD, root);
		inputToken = lexer.getCurrentToken();

		TokenType type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchList(inputToken, root, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
			inputToken = lexer.getCurrentToken();
			type = inputToken.getType();
		}

		// Match and consume TO
		match(inputToken, COBOLTokenType.TO, root);
		inputToken = lexer.getCurrentToken();

		// Match and Consume Identifiers and Optional ROUNDED clause
		type = inputToken.getType();
		while (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {

			matchList(inputToken, root, COBOLTokenType.IDENTIFIER, COBOLTokenType.REAL, COBOLTokenType.INTEGER);
			inputToken = lexer.getCurrentToken();
			match(inputToken, COBOLTokenType.ROUNDED, root);
			inputToken = lexer.getCurrentToken();
			type = inputToken.getType();
		}

		return root;
	}

	public ParseTreeNode parseConditionalStatement(Token inputToken) throws IOException {

		ParseTreeNode root = new StatementNode("CONDITIONAL STATEMENT");

		
		// Match and consume IF
		match(inputToken, COBOLTokenType.IF, root);
		inputToken = lexer.getCurrentToken();
		

		// Match and Consume LHS of IF
		matchList(inputToken, root, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER, COBOLTokenType.REAL, COBOLTokenType.STRING_LITERAL );
		inputToken = lexer.getCurrentToken();
		
		// Parse Condition Body of IF
		parseCondition(inputToken, root);

		// Get next Token - RHS of IF condition
		inputToken = lexer.getCurrentToken();
		matchList(inputToken, root, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER, COBOLTokenType.REAL, COBOLTokenType.STRING_LITERAL );

		inputToken = lexer.getCurrentToken();

		// Parse Statement(s) of IF body
		while(statementPrefixes.contains(inputToken.getType())){
			StatementParser parser = new StatementParser(lexer);			
			root.addChild(parser.parse(inputToken));
			inputToken = lexer.getCurrentToken();
		}

		inputToken = lexer.getCurrentToken();

		if (inputToken.getType() == COBOLTokenType.ELSE) {
			match(inputToken, COBOLTokenType.ELSE, root);
			inputToken = lexer.getCurrentToken();
			
			while(statementPrefixes.contains(inputToken.getType())){
				StatementParser parser = new StatementParser(lexer);			
				root.addChild(parser.parse(inputToken));
				inputToken = lexer.getCurrentToken();
			}
			
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
