package lexer.tokens;

import java.io.IOException;

import lexer.SourceFile;

public class NumberToken extends Token {

	public NumberToken(SourceFile source) {
		super(source);
		
	}
	
	public void extract() throws IOException {
		
		StringBuilder s = new StringBuilder();
		char c = source.getCurrentChar();
		this.type = COBOLTokenType.INTEGER;
		
		while(Character.isDigit(c)) {
			s.append(c);
			if(source.peek() == '.') {
				this.type = COBOLTokenType.REAL;
				s.append(source.nextChar());
			}
			c = source.nextChar();			
		}	
		
		this.value = s.toString();
		
		
	}

}
