package lexer;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import lexer.tokens.TokenType;

public class LexerTest {

	@Test
	public void testScan() {
		BufferedReader in = new BufferedReader(new StringReader("IDENTIFICATION DIVISION.\n PROGRAM-ID HELLO"));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);
		
		try {
			l.scan();
			assertEquals("WORD", l.getCurrentToken().getType());	
			assertEquals("IDENTIFICATION", l.getCurrentToken().getTokenValue());
			l.getCurrentToken().extract();
			assertEquals(TokenType.IDENTIFICATION, l.getCurrentToken().type);
			l.scan();							
			assertEquals("WORD", l.getCurrentToken().getType());
			assertEquals("DIVISION", l.getCurrentToken().getTokenValue());
			l.scan();						
			assertEquals("TERMINATE", l.getCurrentToken().getType());
			assertEquals(".", l.getCurrentToken().getTokenValue());
			l.scan();						
			assertEquals("WORD", l.getCurrentToken().getType());
			assertEquals("PROGRAM-ID", l.getCurrentToken().getTokenValue());
			l.getCurrentToken().extract();
			assertEquals(TokenType.PROGRAM_ID, l.getCurrentToken().type);
			l.scan();	
			assertEquals("WORD", l.getCurrentToken().getType());
			assertEquals("HELLO", l.getCurrentToken().getTokenValue());
			l.getCurrentToken().extract();
			assertEquals(TokenType.IDENTIFIER, l.getCurrentToken().type);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

}
