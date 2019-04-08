package lexer;

import java.io.IOException;

import lexer.tokens.COBOLTokenType;
import lexer.tokens.NumberToken;
import lexer.tokens.SymbolToken;
import lexer.tokens.Token;
import lexer.tokens.WordToken;

public class Lexer {

	private SourceFile source;
	static final char EOL = '\n';
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
		
		// First time scanning, set first character
		if(source.getCurrentChar() == 0)
		source.nextChar();
		
		source.skipWhiteSpace(); // Skips to next valid Character or does nothing if already valid
				
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
		}
		
		
		if(t !=null) {
			currentToken = t;
		}
		
	}
	
	
}
