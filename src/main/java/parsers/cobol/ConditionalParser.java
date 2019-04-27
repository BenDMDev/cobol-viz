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
		ParseTreeNode root = new StatementNode("CONDITIONAL STATEMENT");
		root.setTreeType(TreeNodeType.CONDITIONAL);

		// Match and consume IF
		match(inputToken, COBOLTokenType.IF, root);
		inputToken = scanner.getCurrentToken();

		// Match and Consume LHS of IF
		matchList(inputToken, root, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER, COBOLTokenType.REAL,
				COBOLTokenType.STRING_LITERAL);
		inputToken = scanner.getCurrentToken();

		// Parse Condition Body of IF
		parseCondition(inputToken, root);

		// Get next Token - RHS of IF condition
		inputToken = scanner.getCurrentToken();
		
		matchList(inputToken, root, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER, COBOLTokenType.REAL,
				COBOLTokenType.STRING_LITERAL);
		

		inputToken = scanner.getCurrentToken();

		// Parse Statement(s) of IF body
		while (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue())) {
			StatementParser parser = new StatementParser(scanner);
			root.addChild(parser.parse(inputToken));
			inputToken = scanner.getCurrentToken();
		}

		if(inputToken.getType() == COBOLTokenType.NEXT) {
			//Consume NEXT
			match(inputToken, COBOLTokenType.NEXT, root);
			inputToken = scanner.getCurrentToken();
			//Consume SENTENCE
			match(inputToken, COBOLTokenType.SENTENCE, root);
			
		} 
		
		inputToken = scanner.getCurrentToken();

		if (inputToken.getType() == COBOLTokenType.ELSE) {
			match(inputToken, COBOLTokenType.ELSE, root);
			inputToken = scanner.getCurrentToken();

			while (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue())) {
				
				StatementParser parser = new StatementParser(scanner);
				root.addChild(parser.parse(inputToken));
				inputToken = scanner.getCurrentToken();
			}
			
			if(inputToken.getType() == COBOLTokenType.NEXT) {
				//Consume NEXT
				match(inputToken, COBOLTokenType.NEXT, root);
				inputToken = scanner.getCurrentToken();
				//Consume SENTENCE
				match(inputToken, COBOLTokenType.SENTENCE, root);
				
			} 
						

		}
				
		inputToken = scanner.getCurrentToken();	
		match(inputToken, COBOLTokenType.END_IF, root);

		return root;

	}

	private ParseTreeNode parseCondition(Token inputToken, ParseTreeNode root) throws IOException {

		match(inputToken, COBOLTokenType.IS, root);
		match(scanner.getCurrentToken(), COBOLTokenType.NOT, root);
		match(scanner.getCurrentToken(), COBOLTokenType.GREATER, root);
		match(scanner.getCurrentToken(), COBOLTokenType.THAN, root);
		match(scanner.getCurrentToken(), COBOLTokenType.OR, root);
		match(scanner.getCurrentToken(), COBOLTokenType.EQUAL, root);
		match(scanner.getCurrentToken(), COBOLTokenType.TO, root);
		match(scanner.getCurrentToken(), COBOLTokenType.GREATER_THAN, root);
		match(scanner.getCurrentToken(), COBOLTokenType.LESS, root);
		match(scanner.getCurrentToken(), COBOLTokenType.THAN, root);
		match(scanner.getCurrentToken(), COBOLTokenType.OR, root);
		match(scanner.getCurrentToken(), COBOLTokenType.EQUAL, root);
		match(scanner.getCurrentToken(), COBOLTokenType.TO, root);
		match(scanner.getCurrentToken(), COBOLTokenType.LESS_THAN, root);
		match(scanner.getCurrentToken(), COBOLTokenType.EQUAL, root);
		match(scanner.getCurrentToken(), COBOLTokenType.TO, root);
		match(scanner.getCurrentToken(), COBOLTokenType.EQUALS, root);

		return root;
	}

}
