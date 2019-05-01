package main.java.scanners.tokens.cobol;

import java.io.IOException;

import main.java.scanners.SourceFile;
import main.java.scanners.tokens.Token;

public class ErrorToken extends Token {

	public ErrorToken(SourceFile source) {
		super(source);
		
	}

	@Override
	public void extract() throws IOException {
		this.type = COBOLTokenType.ERROR;
		this.value = "Error";
		
	}

}
