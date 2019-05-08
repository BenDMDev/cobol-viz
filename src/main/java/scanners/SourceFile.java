package main.java.scanners;

import java.io.BufferedReader;
import java.io.IOException;

import main.java.messages.MessageListener;
import main.java.messages.MessageEmitter;

/**
 * SourceFile to wrap COBOL input file
 * Handles reading line and moving through input characters.
 * @author Ben
 *
 */
public class SourceFile implements MessageEmitter {

	private BufferedReader input;
	private String inputBuffer;	
	private int numLines;
	private int charPos;
	private int markerPos;
	private MessageListener listener;
	private boolean isCommentLine;
	static char EOL = '\n';
	static char EOF = 0; 

	/**
	 * Input to be wrapped
	 * @param in
	 */
	public SourceFile(BufferedReader in) {
		input = in;		
		numLines = 0;
		charPos = -2;
		isCommentLine = false;
	}
    
	/**
	 * Returns current char in buffer
	 * @return
	 * @throws IOException 
	 */
	public char getCurrentChar() throws IOException {
		
		if(charPos == -2) {			
			return nextChar();
		}
		
		if(charPos == -1) {		
			charPos++;
			inputBuffer = input.readLine();	
			if(!isCommentLine)
				numLines++;
			
		}
		
		
		if(inputBuffer == null) {
			
			return EOF;
			
		} else if(charPos == inputBuffer.length()) {
			
			charPos = -2;
		
			return EOL;		
			
		} else
			return inputBuffer.charAt(charPos);
		
		
	}
	
	/**
	 * Returns number of lines fed in so far
	 * @return in Number of Lines
	 */
	public int getNumberOfLines() {
		return numLines;
	}
	
	
	/**
	 *  Gets next char in buffer
	 * @return next Char
	 * @throws IOException
	 */
	public char nextChar() throws IOException {		
		charPos++;		
		return getCurrentChar();
		
	}
	
	/**
	 * Unused
	 * @throws IOException
	 */
	public void skipWhiteSpace() throws IOException {
		
	}
	
	/**
	 * Set a marker to save current position
	 */
	public void setMarker() {
		markerPos = charPos;
	}
	
	/**
	 * Reset to previous marker
	 */
	public void resetMarker() { 
		charPos = markerPos;
	}
	
	/**
	 * Peak next char without consuming
	 * @return peek char
	 * @throws IOException
	 */
	public char peek() throws IOException {	
		
		if(inputBuffer == null) 
			return EOF;
		else if(charPos < inputBuffer.length()-1)
			return inputBuffer.charAt(charPos + 1);
		else
			return EOL;
	}
	
	/**
	 * Close input
	 */
	public void close() {
		try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Get current char position
	 * @return
	 */
	public int getCharPos() {
		return charPos;
	}
	
	/**
	 * Toggle comment line to ignore
	 * @param isComment
	 */
	public void setIsCommentLine(boolean isComment) {
		isCommentLine = isComment;
	}

	@Override
	public void addListener(MessageListener listener) {
		this.listener = listener;
		
	}

	@Override
	public void removeListener(MessageListener listener) {
		this.listener = null;
		
	}

	@Override
	public void sendMessage(String message) {
		listener.listen(message);
		
	}

	@Override
	public void sendMessage(float x, float y) {
		// TODO Auto-generated method stub
		
	}

}
