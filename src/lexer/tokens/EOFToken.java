package lexer.tokens;

import java.io.IOException;

import lexer.SourceFile;

public class EOFToken extends Token {

	public EOFToken(SourceFile source) {
		super(source);		
		this.type = COBOLTokenType.EOF;
	}

	@Override
	public void extract() throws IOException {
		
		
	}
	
	

}
