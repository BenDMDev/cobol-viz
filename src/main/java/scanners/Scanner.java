package main.java.scanners;

import java.io.IOException;

import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.scanners.tokens.cobol.EOFToken;
import main.java.scanners.tokens.cobol.NumberToken;
import main.java.scanners.tokens.cobol.StringToken;
import main.java.scanners.tokens.cobol.SymbolToken;
import main.java.scanners.tokens.cobol.WordToken;

public class Scanner {

	private SourceFile source;
	private Token currentToken;
	private Token lookAheadToken;

	public Scanner(SourceFile s) {
		source = s;
		currentToken = null;
		lookAheadToken = null;
	}

	/**
	 * Returns
	 * 
	 * @return current Token
	 */
	public Token getCurrentToken() {
		return currentToken;
	}

	/**
	 * Scan through SourceFile Determines what Character was read and passes to
	 * Token Class to handle extraction Each Token class encapsulates the
	 * extract algorithm for that type
	 * 
	 * @throws IOException
	 */
	public void scan() throws IOException {

		skipWhiteSpace(); // Skips to next valid Character or does nothing if
						  // already valid
		
		if(lookAheadToken == null)
			currentToken = nextToken();
		else {
			currentToken = lookAheadToken;
			lookAheadToken = null;
		}
		
	}
	
	private Token nextToken() throws IOException {
		
		char c = source.getCurrentChar();
		Token t = null;
		if (Character.isLetter(c)) {

			t = new WordToken(source);
			t.extract();

		} else if (Character.isDigit(c)) {

			t = new NumberToken(source);
			t.extract();

		} else if(c == '\"' || c == '\'') {
			
			t = new StringToken(source);
			t.extract();
		}
		else if (COBOLTokenType.SPECIAL_SYMBOLS.containsKey(String.valueOf(c))) {
			t = new SymbolToken(source);
			t.extract();
		} else if (c == 0) {
			t = new EOFToken(source);

		}
		return t;
	}
	
	public Token lookAhead() throws IOException {		
		skipWhiteSpace();
		if(lookAheadToken == null)
			lookAheadToken = nextToken();
		
		return lookAheadToken;
	}

	/**
	 * Skips spaces and newline
	 * 
	 * @throws IOException
	 */
	private void skipWhiteSpace() throws IOException {

		while (source.getCurrentChar() == ' ' || source.getCurrentChar() == '\n' || source.getCurrentChar() == '\t') {
			source.nextChar();
		}

	}

}
