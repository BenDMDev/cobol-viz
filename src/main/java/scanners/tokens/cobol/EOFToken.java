package main.java.scanners.tokens.cobol;

import java.io.IOException;

import main.java.scanners.SourceFile;
import main.java.scanners.tokens.Token;
/**
 * EOF Token Class
 * @author Ben
 *
 */
public class EOFToken extends Token {

	public EOFToken(SourceFile source) {
		super(source);		
		this.type = COBOLTokenType.EOF;
		this.value = "EOF";
		lineNumber = source.getNumberOfLines();
	}

	@Override
	public void extract() throws IOException {
		
		
	}
	
	

}
