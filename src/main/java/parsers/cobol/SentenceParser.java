package main.java.parsers.cobol;

import java.io.IOException;

import main.java.parsers.Parser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.cobol.SentenceNode;

public class SentenceParser extends Parser {

	public SentenceParser(Scanner scanner) {
		super(scanner);
		
	}

	@Override
	public ParseTreeNode parse(Token t) throws IOException {
				
		parseTree = new SentenceNode ("SENTENCE");		
		while(t.getType() != COBOLTokenType.FULL_STOP) {
			StatementParser sp = new StatementParser(scanner);		
			parseTree.addChild(sp.parse(t));			
			t = scanner.getCurrentToken();	
		}
		
		// Consume '.' terminating sentence
		parseTree.addChild(new ParseTreeNode(t.getTokenValue()));
		scanner.scan();
		
		return parseTree;
	}

}
