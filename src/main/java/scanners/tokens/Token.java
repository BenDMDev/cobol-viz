package main.java.scanners.tokens;

import java.io.IOException;

import main.java.scanners.SourceFile;

public abstract class Token  {

	
	protected String value;
	protected SourceFile source;
	protected TokenType type;
	
	public Token(SourceFile source) {
		this.source = source;
	}	
	
	
	public TokenType getType() {
		return type;		
	}
	
	public String getTokenValue() {
		return value;
	}
	
	public abstract void extract() throws IOException;
}
