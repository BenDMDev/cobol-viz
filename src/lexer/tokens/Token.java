package lexer.tokens;

import java.io.IOException;

import lexer.SourceFile;
import lexer.tokens.TokenType;

public class Token implements TokenType {

	
	protected String value;
	protected SourceFile source;
	public COBOLTokenType type;
	
	public Token(SourceFile source) {
		this.source = source;
	}	
	
	
	public TokenType getType() {
		return type;		
	}
	
	public String getTokenValue() {
		return value;
	}
	
	public void extract() throws IOException {
		
	}
}
