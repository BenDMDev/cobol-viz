package lexer.tokens;

import java.io.IOException;

import lexer.SourceFile;

public class WordToken extends Token {

	public WordToken(SourceFile source) {
		super(source);
	}
	
	

	public void extract() throws IOException {
		
		StringBuilder s = new StringBuilder();
		char c = source.getCurrentChar();
		while(Character.isLetter(c) || c == '-') {
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
	}
	
	
}
