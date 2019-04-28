package main.java.scanners.tokens.cobol;

import java.io.IOException;
import java.util.HashSet;

import org.apache.bcel.Constants;

import main.java.scanners.SourceFile;
import main.java.scanners.tokens.Token;

public class WordToken extends Token {

	static final HashSet<String> CONSTANTS = new HashSet<String>(); 
	static {
		CONSTANTS.add("HIGH-VALUES");
		CONSTANTS.add("LOW-VALUES");
		CONSTANTS.add("ZERO");
		CONSTANTS.add("ZEROES");
		CONSTANTS.add("SPACES");
		CONSTANTS.add("QUOTES");
		CONSTANTS.add("ALL");
	}
	
	public WordToken(SourceFile source) {
		super(source);
	}
	
	

	public void extract() throws IOException {
		
		StringBuilder s = new StringBuilder();
		char c = source.getCurrentChar();
		charPos = source.getCharPos();
		while(Character.isLetter(c) || c == '-' || Character.isDigit(c)) {
			s.append(c);				
			c = source.nextChar();
			
		}	
		
		this.value = s.toString();
		
		if(COBOLTokenType.RESERVED.contains(value)) {
			this.type = COBOLTokenType.valueOf(value);
		} else if(CONSTANTS.contains(value)) {
			this.type = COBOLTokenType.FIGURATIVE_CONSTANT;
		} else if(COBOLTokenType.RESERVED_HYPHENS.containsKey(value)) {
			this.type = COBOLTokenType.RESERVED_HYPHENS.get(value);
		} else if (COBOLTokenType.STATEMENT_PREFIXES.contains(value)){
			this.type = COBOLTokenType.valueOf(value);
		} else {
			type = COBOLTokenType.IDENTIFIER;
		}
		
		lineNumber = source.getNumberOfLines();
	}
	
	
}
