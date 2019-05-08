package main.java.scanners.tokens;

import java.io.IOException;

import main.java.scanners.SourceFile;

/**
 * Base class for all tokens
 * @author Ben
 *
 */
public abstract class Token  {

	
	protected String value;
	protected SourceFile source;
	protected TokenType type;
	protected int lineNumber;
	protected int charPos;
	
	public Token(SourceFile source) {
		this.source = source;
	}	
	
	/**
	 * Get Token type
	 * @return TokenType type
	 */
	public TokenType getType() {
		return type;		
	}
	
	/**
	 * Get Token value
	 * @return String token value
	 */
	public String getTokenValue() {
		return value;
	}
	
	/**
	 * Get line number Token was seen
	 * @return int Line number
	 */
	public int getLineNumber() {
		return lineNumber;
	}
	
	/** 
	 * Get char pos first char of token was found
	 * @return
	 */
	public int getCharPos() {
		return charPos;
	}
	
	public abstract void extract() throws IOException;
}
