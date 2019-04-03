package lexer;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class SourceTest {

	@Test
	public void testNextChar() {
		BufferedReader in = new BufferedReader(new StringReader("Hello"));
		Source s = new Source(in);
		try {
			s.nextChar();
			assertEquals('H', s.getCurrentChar());
			s.nextChar();
			assertEquals('e', s.getCurrentChar());
			s.nextChar();
			assertEquals('l', s.getCurrentChar());
			s.nextChar();
			assertEquals('l', s.getCurrentChar());
			s.nextChar();
			assertEquals('o', s.getCurrentChar());
			in.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void testNextCharNumLine() {
		BufferedReader in = new BufferedReader(new StringReader("H\nE\n"));
		Source s = new Source(in);
		try {
			s.nextChar();	
			s.nextChar();
			assertEquals(1, s.getNumberOfLines());		
			assertEquals('E',s.getCurrentChar());
			s.nextChar();
			assertEquals(2, s.getNumberOfLines());
			in.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	

	
	@Test
	public void testPeak() {
		BufferedReader in = new BufferedReader(new StringReader("Hello"));
		Source s = new Source(in);
		try {
			s.nextChar();			
			assertEquals('e', s.peek());
			assertEquals('H', s.getCurrentChar());		
			in.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}

}
