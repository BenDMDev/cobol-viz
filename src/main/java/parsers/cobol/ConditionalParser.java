package main.java.parsers.cobol;

import java.io.IOException;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class ConditionalParser extends StatementParser {

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

		// Match and Consume LHS of IF
		matchAlternation(inputToken, condition, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER, COBOLTokenType.REAL,
				COBOLTokenType.STRING_LITERAL);
		inputToken = scanner.getCurrentToken();

		// Parse Condition Body of IF
		parseCondition(inputToken, condition);

		// Get next Token - RHS of IF condition
		inputToken = scanner.getCurrentToken();

		matchAlternation(inputToken, condition, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER, COBOLTokenType.REAL,
				COBOLTokenType.STRING_LITERAL);

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
			while (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue())) {
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
				while (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue())) {

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

	private ParseTreeNode parseCondition(Token inputToken, ParseTreeNode root) throws IOException {

		match(inputToken, COBOLTokenType.IS, root);
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

		return root;
	}

}
