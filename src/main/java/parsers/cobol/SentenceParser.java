package main.java.parsers.cobol;

import java.io.IOException;

import main.java.parsers.Parser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.SentenceNode;

public class SentenceParser extends Parser {

	public SentenceParser(Scanner scanner) {
		super(scanner);
		
	}

	@Override
	public ParseTreeNode parse(Token inputToken) throws IOException {
				
		parseTree = new SentenceNode (TreeNodeType.STATEMENT_BLOCK, "SENTENCE");		
		while(inputToken.getType() != COBOLTokenType.FULL_STOP) {
			StatementParser sp = new StatementParser(scanner);	
			sp.addListener(listener);
			parseTree.addChild(sp.parse(inputToken));	
			System.out.println(inputToken.getTokenValue());
			inputToken = scanner.getCurrentToken();	
		}
		
		// Consume '.' terminating sentence
		if(inputToken.getType() != COBOLTokenType.FULL_STOP) {
			sendMessage("ERROR MISSING TERMINATOR AT " + inputToken.getLineNumber());
			findNextValidToken(inputToken);
		} else {
			
			match(inputToken, COBOLTokenType.FULL_STOP, parseTree);
			
		}
		
		return parseTree;
	}

	
	private void findNextValidToken(Token inputToken) throws IOException {
		while(inputToken.getType() != COBOLTokenType.FULL_STOP) {
			scanner.scan();
			inputToken = scanner.getCurrentToken();
		}
	}
}
