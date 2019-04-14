package main.java.parsers.cobol;

import java.io.IOException;

import main.java.parsers.Parser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.cobol.SectionNode;

public class SectionParser extends Parser {

	public SectionParser(Scanner l) {
		super(l);
		
	}

	@Override
	public ParseTreeNode parse(Token t) throws IOException {
		
		parseTree = new SectionNode("SECTION");
				
		if(t.getType() == COBOLTokenType.IDENTIFIER && lexer.lookAhead().getType() == COBOLTokenType.SECTION) {
			parseTree.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan(); // Consume section label
			t = lexer.getCurrentToken();
			parseTree.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan(); // Consume SECTION keyword
			t = lexer.getCurrentToken();
			parseTree.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan(); // Consume terminator
			t = lexer.getCurrentToken();
		}
		
		ParagraphParser pp = new ParagraphParser(lexer);
		
		while(!(t.getType() == COBOLTokenType.END)
				&& !(t.getType() == COBOLTokenType.IDENTIFIER 
				&& lexer.lookAhead().getType() == COBOLTokenType.SECTION)) {			
			parseTree.addChild(pp.parse(t));
			t = lexer.getCurrentToken();
			
			
		}
		
		
		return parseTree;
	}
	


}
