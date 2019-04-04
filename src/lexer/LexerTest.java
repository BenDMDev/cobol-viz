package lexer;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class LexerTest {

	@Test
	public void testScan() {
		BufferedReader in = new BufferedReader(new StringReader("IDENTIFICATION DIVISION\n PROGAM-ID HELLO"));
		Source s = new Source(in);
		Lexer l = new Lexer(s);
		
		try {
			l.scan();
			assertEquals("WORD", l.getCurrentToken().getType());	
			assertEquals("IDENTIFICATION", l.getCurrentToken().getTokenValue());
			l.scan();							
			assertEquals("WORD", l.getCurrentToken().getType());
			assertEquals("DIVISION", l.getCurrentToken().getTokenValue());
			l.scan();						
			assertEquals("WORD", l.getCurrentToken().getType());
			assertEquals("PROGAM-ID", l.getCurrentToken().getTokenValue());
			l.scan();	
			assertEquals("WORD", l.getCurrentToken().getType());
			assertEquals("HELLO", l.getCurrentToken().getTokenValue());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

}
