package lexer.tokens;

import lexer.tokens.ExtractToken;

public class Token implements ExtractToken {

	protected String text;
	protected String value;
	public TokenType type;
	
	public Token(String type, String value) {
		this.text = type;
		this.value = value;
	}
	
	
	public String getType() {
		return text;		
	}
	
	public String getTokenValue() {
		return value;
	}
	
	public void extract() {
		
	}
}
