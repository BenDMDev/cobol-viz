package main.java.scanners.tokens.cobol;

import java.io.IOException;

import main.java.scanners.SourceFile;
import main.java.scanners.tokens.Token;

/**
 * Number token
 * Match integer or real
 * @author Ben
 *
 */
public class NumberToken extends Token {

	public NumberToken(SourceFile source) {
		super(source);
		
	}
	
	public void extract() throws IOException {
		
		StringBuilder s = new StringBuilder();
		char c = source.getCurrentChar();
		this.type = COBOLTokenType.INTEGER;
		charPos = source.getCharPos();
		while(Character.isDigit(c)) {
			s.append(c);
			c = source.nextChar();	
			if(c == '.' && Character.isDigit(source.peek()) && type != COBOLTokenType.REAL) {
				this.type = COBOLTokenType.REAL;
				s.append(c);
				c = source.nextChar();
			}
					
		}	
		
		this.value = s.toString();
		lineNumber = source.getNumberOfLines();
		
		
	}

}
