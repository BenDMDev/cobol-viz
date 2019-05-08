package main.java.scanners.tokens.cobol;

import java.io.IOException;
import java.util.HashSet;

import org.apache.bcel.Constants;

import main.java.scanners.SourceFile;
import main.java.scanners.tokens.Token;

/**
 * Word Token
 * Matches either reserved words or user input identifiers
 * @author Ben
 *
 */
public class WordToken extends Token {

	static final HashSet<String> CONSTANTS = new HashSet<String>();
	static {
		CONSTANTS.add("HIGH-VALUES".toLowerCase());
		CONSTANTS.add("HIGH-VALUE".toLowerCase());
		CONSTANTS.add("LOW-VALUES".toLowerCase());
		CONSTANTS.add("LOW-VALUE".toLowerCase());
		CONSTANTS.add("ZERO".toLowerCase());
		CONSTANTS.add("ZEROS".toLowerCase());
		CONSTANTS.add("ZEROES".toLowerCase());
		CONSTANTS.add("SPACES".toLowerCase());
		CONSTANTS.add("SPACE".toLowerCase());
		CONSTANTS.add("QUOTES".toLowerCase());
		CONSTANTS.add("QUOTE".toLowerCase());
		CONSTANTS.add("ALL".toLowerCase());
	}

	public WordToken(SourceFile source) {
		super(source);
	}

	public void extract() throws IOException {

		StringBuilder s = new StringBuilder();
		char c = source.getCurrentChar();
		charPos = source.getCharPos();
		while (Character.isLetter(c) || c == '-' || Character.isDigit(c) || c == '(' || c == ')' || c == ':') {
			s.append(c);
			c = source.nextChar();

		}

		this.value = s.toString();

		if (CONSTANTS.contains(value.toLowerCase())) {
			this.type = COBOLTokenType.FIGURATIVE_CONSTANT;
		} else if (COBOLTokenType.RESERVED.contains(value.toLowerCase())) {
			this.type = COBOLTokenType.valueOf(value.toUpperCase());
		} else if (COBOLTokenType.RESERVED_HYPHENS.containsKey(value.toLowerCase())) {
			this.type = COBOLTokenType.RESERVED_HYPHENS.get(value.toLowerCase());
		} else if (COBOLTokenType.STATEMENT_PREFIXES.contains(value.toLowerCase())) {
			this.type = COBOLTokenType.valueOf(value.toUpperCase());
		} else {
			type = COBOLTokenType.IDENTIFIER;
		}

		lineNumber = source.getNumberOfLines();
	}

}
