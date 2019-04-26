package main.java.scanners.tokens.cobol;

import java.io.IOException;

import main.java.scanners.SourceFile;
import main.java.scanners.tokens.Token;

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
		lineNumber = source.getNumberOfLines();
		
	}

}
