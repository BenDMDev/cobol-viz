package parser.COBOL;

import java.io.IOException;

import lexer.Lexer;
import lexer.tokens.COBOLTokenType;
import parser.ParseTreeNode;
import parser.Parser;

public class StatementParser extends Parser {

	public StatementParser(Lexer l) {
		super(l);
		
	}

	@Override
	public ParseTreeNode parse() throws IOException {
		ParseTreeNode p = null;
		lexer.scan();
		if(lexer.getCurrentToken().getType() == COBOLTokenType.MOVE) {
		p = new ParseTreeNode("MOVE STATEMENT");
			parseMoveStatement(p);			
		}
		return p;
	}
	
	public ParseTreeNode parseMoveStatement(ParseTreeNode p) throws IOException {
		if(lexer.getCurrentToken().getType() == COBOLTokenType.MOVE)
			p.addChild(new ParseTreeNode(lexer.getCurrentToken().getTokenValue()));
		
		lexer.scan();
		
		if(lexer.getCurrentToken().getType() == COBOLTokenType.IDENTIFIER)
			p.addChild(new ParseTreeNode(lexer.getCurrentToken().getTokenValue())); 
		
		lexer.scan();
		
		if(lexer.getCurrentToken().getType() == COBOLTokenType.TO)
			p.addChild(new ParseTreeNode(lexer.getCurrentToken().getTokenValue()));
		
		lexer.scan();
		
		
		if(lexer.getCurrentToken().getType() == COBOLTokenType.IDENTIFIER) {
			p.addChild(new ParseTreeNode(lexer.getCurrentToken().getTokenValue())); 
			lexer.scan();
					
		}	
			
		
		return p;	
		
	}

}
