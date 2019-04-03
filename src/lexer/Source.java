package lexer;

import java.io.BufferedReader;
import java.io.IOException;

public class Source {

	private BufferedReader input;
	private char currentChar;
	private int numLines;
	private char EOL = '\n';

	/**
	 * 
	 * @param in
	 */
	public Source(BufferedReader in) {
		input = in;
		currentChar = 0;
		numLines = 0;
	}
    
	/**
	 * 
	 * @return
	 */
	public char getCurrentChar() {
		return currentChar;
	}
	
	public int getNumberOfLines() {
		return numLines;
	}
	
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public void nextChar() throws IOException {
		currentChar = (char) input.read();
		while(Character.isWhitespace(currentChar)) {
			if(currentChar == EOL)
				numLines++;			
			currentChar = (char) input.read();
		}
		
	}
	
	
	
	public char peek() throws IOException {
		input.mark(1);
		char c = (char) input.read();
		input.reset();
		return c;
	}

}
