package main.java.scanners.tokens.cobol;

import java.io.IOException;

import main.java.scanners.SourceFile;
import main.java.scanners.tokens.Token;

public class WordToken extends Token {

	public WordToken(SourceFile source) {
		super(source);
	}
	
	

	public void extract() throws IOException {
		
		StringBuilder s = new StringBuilder();
		char c = source.getCurrentChar();
		while(Character.isLetter(c) || c == '-' || Character.isDigit(c)) {
			s.append(c);				
			c = source.nextChar();
			
		}	
		
		this.value = s.toString();
		
		if(COBOLTokenType.RESERVED.contains(value)) {
			this.type = COBOLTokenType.valueOf(value);
		} else if(COBOLTokenType.RESERVED_HYPHENS.containsKey(value)) {
			this.type = COBOLTokenType.RESERVED_HYPHENS.get(value);
		} else {
			type = COBOLTokenType.IDENTIFIER;
		}
		
		lineNumber = source.getNumberOfLines();
	}
	
	
}
