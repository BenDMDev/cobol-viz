package parser.COBOL;

import java.io.IOException;

import lexer.Lexer;
import lexer.tokens.COBOLTokenType;
import lexer.tokens.Token;
import parser.trees.ParseTreeNode;
import parser.trees.nodes.COBOL.SentenceNode;
import parser.Parser;

public class SentenceParser extends Parser {

	public SentenceParser(Lexer l) {
		super(l);
		
	}

	@Override
	public ParseTreeNode parse(Token t) throws IOException {
				
		parseTree = new SentenceNode ("SENTENCE");		
		while(t.getType() != COBOLTokenType.FULL_STOP) {
			StatementParser sp = new StatementParser(lexer);
			//ParseTreeNode p = new ParseTreeNode("STATEMENT");
			//p.addChild(sp.parse());
			parseTree.addChild(sp.parse(t));			
			t = lexer.getCurrentToken();	
		}
		
		parseTree.addChild(new ParseTreeNode(t.getTokenValue()));
		lexer.scan();
		
		return parseTree;
	}

}
