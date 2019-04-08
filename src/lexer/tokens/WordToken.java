package lexer.tokens;

public class WordToken extends Token {

	public WordToken(String type, String value) {
		super(type, value);		
	}

	public void extract() {
		if(TokenType.RESERVED.contains(value)) {
			this.type = TokenType.valueOf(value);
		} else if(TokenType.RESERVED_HYPHENS.containsKey(value)) {
			this.type = TokenType.RESERVED_HYPHENS.get(value);
		} else {
			type = TokenType.IDENTIFIER;
		}
	}
	
	
}
