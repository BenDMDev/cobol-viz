package lexer.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import lexer.SourceFile;

public class SourceFileTest {

	@Test
	public void testNextChar() {
		BufferedReader in = new BufferedReader(new StringReader("Hello\nTest \n"));
		SourceFile s = new SourceFile(in);
		try {			
			assertEquals('H', s.nextChar());			
			assertEquals('e', s.nextChar());			
			assertEquals('l', s.nextChar());
			assertEquals('l', s.nextChar());			
			assertEquals('o', s.nextChar());			
			assertEquals('\n', s.nextChar());
			assertEquals('T', s.nextChar());			
			assertEquals('e', s.nextChar());			
			assertEquals('s', s.nextChar());
			assertEquals('t', s.nextChar());	
			assertEquals(' ', s.nextChar());
			assertEquals('\n', s.nextChar());
			in.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		
	}	
	
	
	@Test
	public void testPeak() {
		BufferedReader in = new BufferedReader(new StringReader("Hello"));
		SourceFile s = new SourceFile(in);
		try {		
			assertEquals('H', s.getCurrentChar());	
			assertEquals('e', s.peek());
			in.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	@Test
	public void testNumberOfLines() {
		BufferedReader in = new BufferedReader(new StringReader("\n\n\n"));
		SourceFile s = new SourceFile(in);
		try {		
			assertEquals('\n', s.getCurrentChar());	
			assertEquals('\n', s.nextChar());
			assertEquals('\n', s.nextChar());
			assertEquals(3, s.getNumberOfLines());
			in.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testEndOfFile() {
		BufferedReader in = new BufferedReader(new StringReader("E\n"));
		SourceFile s = new SourceFile(in);
		try {		
			assertEquals('E', s.getCurrentChar());
			assertEquals('\n', s.nextChar());
			assertEquals(0, s.nextChar());
			
			in.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}
	
	@Test 
	public void testPeekEndOfFile() {
		BufferedReader in = new BufferedReader(new StringReader("45.5"));
		SourceFile s = new SourceFile(in);
		try {		
			assertEquals('4', s.getCurrentChar());
			assertEquals('5', s.nextChar());
			assertEquals('.', s.nextChar());
			assertEquals('5', s.nextChar());
			assertEquals('\n', s.peek());
			
			in.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		
	}

}
