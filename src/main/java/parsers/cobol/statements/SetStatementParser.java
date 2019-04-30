package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class SetStatementParser extends StatementParser {

	public SetStatementParser(Scanner scanner) {
		super(scanner);
	
	}
	
	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());
		
		match(inputToken, COBOLTokenType.SET, parseTree);
		inputToken = scanner.getCurrentToken();
		
		matchRepetition(inputToken, parseTree, COBOLTokenType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();
		
		if(inputToken.getType() == COBOLTokenType.TO){
			match(inputToken, COBOLTokenType.TO, parseTree);
			inputToken = scanner.getCurrentToken();
		} else if(inputToken.getType() == COBOLTokenType.UP){
			matchSequence(inputToken, parseTree, COBOLTokenType.UP, COBOLTokenType.BY);
			inputToken = scanner.getCurrentToken();
		}
		else if(inputToken.getType() == COBOLTokenType.DOWN) {
			matchSequence(inputToken, parseTree, COBOLTokenType.DOWN, COBOLTokenType.BY);
			inputToken = scanner.getCurrentToken();
		}
		
		matchAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.INTEGER);
		
		return parseTree;
	}

}
