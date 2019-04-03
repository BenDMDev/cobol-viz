import java.io.BufferedReader;
import java.io.IOException;

public class Source {

	private BufferedReader input;
	private char currentChar;
	private int numLines;

	public Source(BufferedReader in) {
		input = in;
		currentChar = 0;
		numLines = 0;
	}

	public char getNextChar() throws IOException {
		currentChar = (char) input.read();		
		return currentChar;
	}

}
