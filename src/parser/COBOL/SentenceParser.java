package parser.COBOL;

import java.io.IOException;

import lexer.Lexer;
import lexer.tokens.COBOLTokenType;
import lexer.tokens.Token;
import parser.ParseTreeNode;
import parser.Parser;

public class SentenceParser extends Parser {

	public SentenceParser(Lexer l) {
		super(l);
		
	}

	@Override
	public ParseTreeNode parse() throws IOException {
		
		Token t = lexer.getCurrentToken();
		parseTree = new ParseTreeNode ("SENTENCE");		
		while(t.getType() != COBOLTokenType.FULL_STOP) {
			StatementParser sp = new StatementParser(lexer);
			ParseTreeNode p = new ParseTreeNode("STATEMENT");
			p.addChild(sp.parse());
			parseTree.addChild(p);			
			t = lexer.getCurrentToken();	
		}
		
		parseTree.addChild(new ParseTreeNode(t.getTokenValue()));
		lexer.scan();
		
		return parseTree;
	}

}
