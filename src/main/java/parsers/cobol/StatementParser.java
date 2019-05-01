package main.java.parsers.cobol;

import java.io.IOException;
import main.java.parsers.Parser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.TokenType;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public class StatementParser extends Parser {

	
	public StatementParser(Scanner scanner) {
		super(scanner);
		
	}

	@Override
	public ParseTreeNode parse(Token inputToken) throws IOException {
		int lineNumber = inputToken.getLineNumber();
		
		Parser parser = parserFactory.createParser(inputToken, scanner);
		
	

		if (parser == null) {
			sendMessage("UNEXPECTED TOKEN: " + inputToken.getTokenValue() + " AT LINE: " + inputToken.getLineNumber());
			parseTree = new ParseTreeNode(TreeNodeType.ERROR, "ERROR NODE");
			findNextValidToken(inputToken);
		} else {
			parser.addListener(listener);
			parseTree = parser.parse(inputToken);
			parseTree.setLineNumber(lineNumber);
		}
			
		
		
		return parseTree;
	}

	private void findNextValidToken(Token inputToken) throws IOException {
		while (!COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue())
				&& inputToken.getType() != COBOLTokenType.FULL_STOP) {
			scanner.scan();
			inputToken = scanner.getCurrentToken();
		}
	}
	
	
	protected ParseTreeNode parseCondition(Token inputToken, ParseTreeNode root) throws IOException {

		matchRepetition(inputToken, root, COBOLTokenType.LEFT_PAREN);
		matchAlternation(scanner.getCurrentToken(), root, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER,
				COBOLTokenType.REAL, COBOLTokenType.STRING_LITERAL, COBOLTokenType.FIGURATIVE_CONSTANT);
		parseArithmeticExpression(root, scanner.getCurrentToken());
		match(scanner.getCurrentToken(), COBOLTokenType.IS, root);
		match(scanner.getCurrentToken(), COBOLTokenType.NOT, root);
		match(scanner.getCurrentToken(), COBOLTokenType.GREATER, root);
		match(scanner.getCurrentToken(), COBOLTokenType.THAN, root);
		match(scanner.getCurrentToken(), COBOLTokenType.OR, root);
		match(scanner.getCurrentToken(), COBOLTokenType.EQUAL, root);
		match(scanner.getCurrentToken(), COBOLTokenType.TO, root);
		match(scanner.getCurrentToken(), COBOLTokenType.GREATER_THAN_SYMBOL, root);
		match(scanner.getCurrentToken(), COBOLTokenType.LESS, root);
		match(scanner.getCurrentToken(), COBOLTokenType.THAN, root);
		match(scanner.getCurrentToken(), COBOLTokenType.OR, root);
		match(scanner.getCurrentToken(), COBOLTokenType.EQUAL, root);
		match(scanner.getCurrentToken(), COBOLTokenType.TO, root);
		match(scanner.getCurrentToken(), COBOLTokenType.LESS_THAN_SYMBOL, root);
		match(scanner.getCurrentToken(), COBOLTokenType.EQUAL, root);
		match(scanner.getCurrentToken(), COBOLTokenType.TO, root);
		match(scanner.getCurrentToken(), COBOLTokenType.EQUALS_SYMBOL, root);
		match(scanner.getCurrentToken(), COBOLTokenType.NUMERIC, root);
		match(scanner.getCurrentToken(), COBOLTokenType.ALPHANUMERIC, root);
		match(scanner.getCurrentToken(), COBOLTokenType.ALPHABETIC, root);
		match(scanner.getCurrentToken(), COBOLTokenType.ALPHABETIC_LOWER, root);
		match(scanner.getCurrentToken(), COBOLTokenType.ALPHABETIC_UPPER, root);
		
		matchAlternation(scanner.getCurrentToken(), root, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER,
				COBOLTokenType.REAL, COBOLTokenType.STRING_LITERAL, COBOLTokenType.FIGURATIVE_CONSTANT);

		matchRepetition(scanner.getCurrentToken(), root, COBOLTokenType.RIGHT_PAREN);

		return root;
	}

	protected void parseArithmeticExpression(ParseTreeNode root, Token inputToken) throws IOException {

		TokenType[] validOperands = { COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER, COBOLTokenType.REAL,
				COBOLTokenType.ADDITION_SYMBOL, COBOLTokenType.SUBTRACTION_SYMBOL, COBOLTokenType.MULTIPLICATION_SYMBOL,
				COBOLTokenType.EXPONENTIATION_SYMBOL, COBOLTokenType.DIVISION_SYMBOL, COBOLTokenType.LEFT_PAREN,
				COBOLTokenType.RIGHT_PAREN };
		
		while (isOperand(inputToken) || isOperator(inputToken)) {
			matchAlternation(inputToken, root, validOperands);
			inputToken = scanner.getCurrentToken();
		}

		

	}

	protected boolean isOperator(Token inputToken) {

		switch ((COBOLTokenType) inputToken.getType()) {
		case ADDITION_SYMBOL:
		case SUBTRACTION_SYMBOL:
		case MULTIPLICATION_SYMBOL:
		case EXPONENTIATION_SYMBOL:
		case DIVISION_SYMBOL:
		case LEFT_PAREN:
		case RIGHT_PAREN:
			return true;
		default:
			return false;
		}

	}
	
	protected boolean isOperand(Token inputToken) {
		TokenType type = inputToken.getType();
		if (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {
			return true;
		} else
			return false;
	}
	

}
