package main.java.scanners;

import java.io.BufferedReader;
import java.io.IOException;

import main.java.messages.MessageListener;
import main.java.messages.MessageEmitter;

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
	 * 
	 * @param in
	 */
	public SourceFile(BufferedReader in) {
		input = in;		
		numLines = 0;
		charPos = -2;
		isCommentLine = false;
	}
    
	/**
	 * 
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
	
	
	public int getNumberOfLines() {
		return numLines;
	}
	
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public char nextChar() throws IOException {		
		charPos++;		
		return getCurrentChar();
		
	}
	
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
	
	public char peek() throws IOException {	
		
		if(inputBuffer == null) 
			return EOF;
		else if(charPos < inputBuffer.length()-1)
			return inputBuffer.charAt(charPos + 1);
		else
			return EOL;
	}
	
	public void close() {
		try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getCharPos() {
		return charPos;
	}
	
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

}
