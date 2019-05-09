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
 * Conditional Parser, handles parsing conditional statements I.E IF
 * @author Ben
 *
 */
public class ConditionalParser extends StatementParser {

	/**
	 * 
	 * @param scanner
	 */
	public ConditionalParser(Scanner scanner) {
		super(scanner);
	}

	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode("CONDITIONAL STATEMENT");
		parseTree.setTreeType(TreeNodeType.CONDITIONAL_STATEMENT);

		ParseTreeNode condition = new ParseTreeNode("CONDITIONAL");
		condition.setTreeType(TreeNodeType.CONDITION);

		// Match and consume IF
		match(inputToken, COBOLTokenType.IF, condition);
		inputToken = scanner.getCurrentToken();

		// Parse Condition Body of IF
		parseCondition(inputToken, condition);
		inputToken = scanner.getCurrentToken();

		while (inputToken.getType() == COBOLTokenType.AND || inputToken.getType() == COBOLTokenType.OR) {
			matchAlternation(inputToken, condition, COBOLTokenType.AND, COBOLTokenType.OR);
			inputToken = scanner.getCurrentToken();
			parseCondition(inputToken, condition);
			inputToken = scanner.getCurrentToken();
		}

		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.THEN, condition);

		parseTree.addChild(condition);
		inputToken = scanner.getCurrentToken();

		if (inputToken.getType() == COBOLTokenType.NEXT) {
			// Consume NEXT
			match(inputToken, COBOLTokenType.NEXT, parseTree);
			inputToken = scanner.getCurrentToken();
			// Consume SENTENCE
			match(inputToken, COBOLTokenType.SENTENCE, parseTree);

		} else {

			ParseTreeNode conditionBodyIF = new ParseTreeNode("IF STATEMENTS");
			conditionBodyIF.setTreeType(TreeNodeType.CONDITION_BODY);
			// Parse Statement(s) of IF body
			while (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue().toLowerCase())) {
				StatementParser parser = new StatementParser(scanner);
				parser.addListener(listener);
				conditionBodyIF.addChild(parser.parse(inputToken));
				inputToken = scanner.getCurrentToken();
			}

			parseTree.addChild(conditionBodyIF);
		}

		inputToken = scanner.getCurrentToken();

		// Handle ELSE
		if (inputToken.getType() == COBOLTokenType.ELSE) {
			// match(inputToken, COBOLTokenType.ELSE, parseTree);

			scanner.scan();
			inputToken = scanner.getCurrentToken();

			if (inputToken.getType() == COBOLTokenType.NEXT) {
				// Consume NEXT
				match(inputToken, COBOLTokenType.NEXT, parseTree);
				inputToken = scanner.getCurrentToken();
				// Consume SENTENCE
				match(inputToken, COBOLTokenType.SENTENCE, parseTree);

			} else {
				ParseTreeNode conditionBodyElse = new ParseTreeNode("ELSE STATEMENTS");
				conditionBodyElse.setTreeType(TreeNodeType.CONDITION_BODY);
				parseTree.addChild(conditionBodyElse);
				while (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue().toLowerCase())) {

					StatementParser parser = new StatementParser(scanner);
					parser.addListener(listener);
					conditionBodyElse.addChild(parser.parse(inputToken));
					inputToken = scanner.getCurrentToken();
				}

			}

		}

		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.END_IF, parseTree);

		return parseTree;

	}

//	private ParseTreeNode parseCondition(Token inputToken, ParseTreeNode root) throws IOException {
//
//		matchRepetition(inputToken, root, COBOLTokenType.LEFT_PAREN);
//		matchAlternation(scanner.getCurrentToken(), root, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER,
//				COBOLTokenType.REAL, COBOLTokenType.STRING_LITERAL);
//		parseArithmeticExpression(root, scanner.getCurrentToken());
//		match(scanner.getCurrentToken(), COBOLTokenType.IS, root);
//		match(scanner.getCurrentToken(), COBOLTokenType.NOT, root);
//		match(scanner.getCurrentToken(), COBOLTokenType.GREATER, root);
//		match(scanner.getCurrentToken(), COBOLTokenType.THAN, root);
//		match(scanner.getCurrentToken(), COBOLTokenType.OR, root);
//		match(scanner.getCurrentToken(), COBOLTokenType.EQUAL, root);
//		match(scanner.getCurrentToken(), COBOLTokenType.TO, root);
//		match(scanner.getCurrentToken(), COBOLTokenType.GREATER_THAN_SYMBOL, root);
//		match(scanner.getCurrentToken(), COBOLTokenType.LESS, root);
//		match(scanner.getCurrentToken(), COBOLTokenType.THAN, root);
//		match(scanner.getCurrentToken(), COBOLTokenType.OR, root);
//		match(scanner.getCurrentToken(), COBOLTokenType.EQUAL, root);
//		match(scanner.getCurrentToken(), COBOLTokenType.TO, root);
//		match(scanner.getCurrentToken(), COBOLTokenType.LESS_THAN_SYMBOL, root);
//		match(scanner.getCurrentToken(), COBOLTokenType.EQUAL, root);
//		match(scanner.getCurrentToken(), COBOLTokenType.TO, root);
//		match(scanner.getCurrentToken(), COBOLTokenType.EQUALS_SYMBOL, root);
//
//		matchAlternation(scanner.getCurrentToken(), root, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER,
//				COBOLTokenType.REAL, COBOLTokenType.STRING_LITERAL);
//
//		matchRepetition(scanner.getCurrentToken(), root, COBOLTokenType.RIGHT_PAREN);
//
//		return root;
//	}
//
//	private void parseArithmeticExpression(ParseTreeNode root, Token inputToken) throws IOException {
//
//		TokenType[] validOperands = { COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER, COBOLTokenType.REAL,
//				COBOLTokenType.ADDITION_SYMBOL, COBOLTokenType.SUBTRACTION_SYMBOL, COBOLTokenType.MULTIPLICATION_SYMBOL,
//				COBOLTokenType.EXPONENTIATION_SYMBOL, COBOLTokenType.DIVISION_SYMBOL, COBOLTokenType.LEFT_PAREN,
//				COBOLTokenType.RIGHT_PAREN };
//		
//		while (isOperand(inputToken) || isOperator(inputToken)) {
//			matchAlternation(inputToken, parseTree, validOperands);
//			inputToken = scanner.getCurrentToken();
//		}
//
//		
//
//	}
//
//	private boolean isOperator(Token inputToken) {
//
//		switch ((COBOLTokenType) inputToken.getType()) {
//		case ADDITION_SYMBOL:
//		case SUBTRACTION_SYMBOL:
//		case MULTIPLICATION_SYMBOL:
//		case EXPONENTIATION_SYMBOL:
//		case DIVISION_SYMBOL:
//		case LEFT_PAREN:
//		case RIGHT_PAREN:
//			return true;
//		default:
//			return false;
//		}
//
//	}
//	
//	private boolean isOperand(Token inputToken) {
//		TokenType type = inputToken.getType();
//		if (type == COBOLTokenType.IDENTIFIER || type == COBOLTokenType.REAL || type == COBOLTokenType.INTEGER) {
//			return true;
//		} else
//			return false;
//	}
//	

}
