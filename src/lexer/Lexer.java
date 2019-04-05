package lexer;

import java.io.IOException;

public class Lexer {

	private Source source;
	static final char EOL = '\n';
	private Token currentToken;
	
	
	public Lexer (Source s) {
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
		
		StringBuilder s = new StringBuilder();	
		char c = source.getCurrentChar();
		Token t = null;
		if(Character.isLetter(c)) {
			while(Character.isLetter(c) || c == '-') {
				s.append(c);				
				source.nextChar();
				c = source.getCurrentChar();
			}
			
		t = new Token("WORD", s.toString());
		
		} else if (Character.isDigit(c)) {
			while(Character.isDigit(c) || c == 'v' || c == 'V') {
				s.append(c);				
				source.nextChar();
				c = source.getCurrentChar();
			}
			
		t = new Token("DIGIT", s.toString());
		
		} else if(c == '.') {
			t = new Token("TERMINATE", ".");
			source.nextChar();
		}
		
		
		if(t !=null) {
			currentToken = t;
		}
		
	}
	
	
}
