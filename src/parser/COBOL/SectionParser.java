package parser.COBOL;

import java.io.IOException;

import lexer.Lexer;
import lexer.tokens.COBOLTokenType;
import lexer.tokens.Token;
import parser.trees.ParseTreeNode;
import parser.trees.nodes.COBOL.SectionNode;
import parser.Parser;

public class SectionParser extends Parser {

	public SectionParser(Lexer l) {
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
