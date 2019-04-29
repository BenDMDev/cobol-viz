package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class MoveStatementParser extends StatementParser {

	public MoveStatementParser(Scanner scanner) {
		super(scanner);
		
	}
	
	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		match(inputToken, COBOLTokenType.MOVE, parseTree);
		inputToken = scanner.getCurrentToken();

		matchAlternation(inputToken, parseTree, COBOLTokenType.CORRESPONDING, COBOLTokenType.CORR);
		inputToken = scanner.getCurrentToken();

		
		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER, COBOLTokenType.REAL, COBOLTokenType.STRING_LITERAL);
		inputToken = scanner.getCurrentToken();
		match(inputToken, COBOLTokenType.FIGURATIVE_CONSTANT, parseTree);
	

		match(inputToken, COBOLTokenType.TO, parseTree);
		inputToken = scanner.getCurrentToken();

		
		matchRepetition(inputToken, parseTree, COBOLTokenType.IDENTIFIER);


		return parseTree;
	}
	
	

}
