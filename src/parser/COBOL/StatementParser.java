package parser.COBOL;

import java.io.IOException;
import java.util.HashSet;

import lexer.Lexer;
import lexer.tokens.COBOLTokenType;
import lexer.tokens.NumberToken;
import lexer.tokens.Token;
import lexer.tokens.TokenType;
import lexer.tokens.WordToken;
import parser.ParseTreeNode;
import parser.Parser;

public class StatementParser extends Parser {

	private HashSet<TokenType> userInputAlternatives;
	
	public StatementParser(Lexer l) {
		super(l);
		userInputAlternatives = new HashSet<TokenType>();
		userInputAlternatives.add(COBOLTokenType.IDENTIFIER);
		userInputAlternatives.add(COBOLTokenType.STRING_LITERAL);
		userInputAlternatives.add(COBOLTokenType.REAL);
		userInputAlternatives.add(COBOLTokenType.INTEGER);
	}

	@Override
	public ParseTreeNode parse(Token t) throws IOException {

		ParseTreeNode p = new ParseTreeNode("STATEMENT");

		if (t.getType() == COBOLTokenType.MOVE) {
			p.addChild(parseMoveStatement(t));
		} else

		if (t.getType() == COBOLTokenType.ADD) {
			p.addChild(parseAddStatement(t));
		} else

		if (t.getType() == COBOLTokenType.IF) {
			p.addChild(parseConditionalStatement(t));
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
		root.addChild(parse(inputToken));

		inputToken = lexer.getCurrentToken();
		
		if (inputToken.getType() == COBOLTokenType.ELSE) {			
			match(inputToken, COBOLTokenType.ELSE, root);
			inputToken = lexer.getCurrentToken();
			root.addChild(parse(inputToken)); // Recursively call more statements
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
