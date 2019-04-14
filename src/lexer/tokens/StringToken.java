package lexer.tokens;

import java.io.IOException;

import lexer.SourceFile;

public class StringToken extends Token {

	public StringToken(SourceFile source) {
		super(source);		
	}

	@Override
	public void extract() throws IOException {
		
		StringBuilder s = new StringBuilder();
		char c = source.nextChar();
					
		while(c != '\"' && c != '\'') {
			
			s.append(c);
			c = source.nextChar();
		}
		source.nextChar();
		this.value = s.toString();
		
		this.type = COBOLTokenType.STRING_LITERAL;
	}

}
