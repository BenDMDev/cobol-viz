package lexer;

import java.io.IOException;

import lexer.tokens.*;


public class Lexer {

	private SourceFile source;	
	private Token currentToken;
	
	
	public Lexer (SourceFile s) {
		source = s;
		currentToken = null;
	}
	
	public Token getCurrentToken() {
		return currentToken;
	}
		
	
	/**
	 * 
	 * @throws IOException
	 */
	public void scan() throws IOException {
		
				
		skipWhiteSpace(); // Skips to next valid Character or does nothing if already valid
				
		char c = source.getCurrentChar();
		Token t = null;
		if(Character.isLetter(c)) {
			
			t = new WordToken(source);
			t.extract();		
			
		} else if (Character.isDigit(c)) {
						
			t = new NumberToken(source);
			t.extract();
		
		} else if(COBOLTokenType.SPECIAL_SYMBOLS.containsKey(String.valueOf(c))) {
			t = new SymbolToken(source);
			t.extract();
		} else if (c == 0) {
			t = new EOFToken(source);
			
		}
		
		
		if(t !=null) {
			currentToken = t;
		}
		
	}
	
	private void skipWhiteSpace() throws IOException {
		
		while(source.getCurrentChar() == ' ' || source.getCurrentChar() == '\n') {
			source.nextChar();
		}
		
	}
	
	
}
