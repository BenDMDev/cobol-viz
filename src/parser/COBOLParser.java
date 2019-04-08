package parser;

import java.io.IOException;

import lexer.Lexer;
import lexer.tokens.Token;

public class COBOLParser extends Parser {

	public COBOLParser(Lexer l) {
		super(l);		
	}
	
	
	public ParseTreeNode parse() throws IOException {
		
		lexer.scan();
		Token t = lexer.getCurrentToken();
		
		switch(t.type) {
		case IDENTIFICATION :			
			break;
		case ENVIRONMENT :
			break;
		case DATA:
			break;
		case PROCEDURE:
			break;
		}
		return null;
		
	}

	
	
}
