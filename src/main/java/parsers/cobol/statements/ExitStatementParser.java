package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class ExitStatementParser extends StatementParser {

	public ExitStatementParser(Scanner scanner) {
		super(scanner);
		
	}
	
	public ParseTreeNode parse(Token inputToken) throws IOException {
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());
		
		matchSequence(inputToken, parseTree, COBOLTokenType.EXIT, COBOLTokenType.PROGRAM);
	
		
		
		return parseTree;
	}

	
	
	
}
