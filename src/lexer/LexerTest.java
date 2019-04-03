package lexer;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class LexerTest {

	@Test
	public void testScan() {
		BufferedReader in = new BufferedReader(new StringReader("HELLO-WORLD\n 95645\n\n65v56"));
		Source s = new Source(in);
		Lexer l = new Lexer(s);
		
		try {
			l.scan();
			assertEquals("WORD", l.getCurrentToken().getType());	
			assertEquals("HELLO-WORLD", l.getCurrentToken().getTokenValue());
			l.scan();							
			assertEquals("DIGIT", l.getCurrentToken().getType());
			assertEquals("95645", l.getCurrentToken().getTokenValue());
			l.scan();						
			assertEquals("DIGIT", l.getCurrentToken().getType());
			assertEquals("65v56", l.getCurrentToken().getTokenValue());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

}
