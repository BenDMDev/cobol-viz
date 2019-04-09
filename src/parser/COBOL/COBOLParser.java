package parser.COBOL;

import java.io.IOException;

import lexer.Lexer;
import lexer.tokens.COBOLTokenType;
import lexer.tokens.Token;
import parser.ParseTreeNode;
import parser.Parser;


public class COBOLParser extends Parser {

	public COBOLParser(Lexer l) {
		super(l);		
	}
	
	
	public ParseTreeNode parse() throws IOException {
		
		lexer.scan();
		Token t = lexer.getCurrentToken();
		COBOLTokenType tokenType = (COBOLTokenType) t.getType();
		
		switch(tokenType) {
		case IDENTIFICATION :			
			break;
		case ENVIRONMENT :
			break;
		case DATA:
			break;
		case PROCEDURE:
			break;
		default:
			break;
		}
		return null;
		
	}

	
	
}
