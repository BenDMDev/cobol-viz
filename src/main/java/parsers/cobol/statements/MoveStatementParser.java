package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.cobol.StatementNode;

public class MoveStatementParser extends StatementParser {

	public MoveStatementParser(Scanner scanner) {
		super(scanner);
		
	}
	
	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode("MOVE STATEMENT");

		match(inputToken, COBOLTokenType.MOVE, parseTree);
		inputToken = scanner.getCurrentToken();

		matchList(inputToken, parseTree, COBOLTokenType.CORRESPONDING, COBOLTokenType.CORR);
		inputToken = scanner.getCurrentToken();

		
		matchList(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER, COBOLTokenType.REAL);
		inputToken = scanner.getCurrentToken();		
	

		match(inputToken, COBOLTokenType.TO, parseTree);
		inputToken = scanner.getCurrentToken();

		while (inputToken.getType() == COBOLTokenType.IDENTIFIER) {

			match(inputToken, COBOLTokenType.IDENTIFIER, parseTree);
			inputToken = scanner.getCurrentToken();
		}

		return parseTree;
	}
	
	

}
