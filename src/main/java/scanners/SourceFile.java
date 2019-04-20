package main.java.scanners;

import java.io.BufferedReader;
import java.io.IOException;

public class SourceFile {

	private BufferedReader input;
	private String inputBuffer;	
	private int numLines;
	private int charPos;
	private int markerPos;
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
		}
		
		
		if(inputBuffer == null) {
			
			return EOF;
			
		} else if(charPos == inputBuffer.length()) {
			
			charPos = -2;
			numLines++;
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

}
