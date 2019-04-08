package lexer;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import lexer.tokens.COBOLTokenType;

public class LexerTest {

	@Test
	public void testWordScan() {
		BufferedReader in = new BufferedReader(new StringReader("IDENTIFICATION DIVISION. \n PROGRAM-ID HELLO 45685"));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);
		
		try {
			l.scan();				
			assertEquals("IDENTIFICATION", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.IDENTIFICATION, l.getCurrentToken().type);
			l.scan();							
			assertEquals("DIVISION", l.getCurrentToken().getTokenValue());
			assertEquals(COBOLTokenType.DIVISION, l.getCurrentToken().type);
			l.scan();						
			assertEquals(".", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.FULL_STOP, l.getCurrentToken().type);
			l.scan();						
			assertEquals("PROGRAM-ID", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.PROGRAM_ID, l.getCurrentToken().type);
			l.scan();				
			assertEquals("HELLO", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.IDENTIFIER, l.getCurrentToken().type);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testNumberScan() {
		BufferedReader in = new BufferedReader(new StringReader("45685 56.54"));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);
		
		try {
			l.scan();				
			assertEquals("45685", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.INTEGER, l.getCurrentToken().type);
			l.scan();							
			assertEquals("56.54", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.REAL, l.getCurrentToken().getType());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSymbols() {
		BufferedReader in = new BufferedReader(new StringReader(". > >= ** * + - <= ="));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);
		
		try {
			l.scan();				
			assertEquals(".", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.FULL_STOP, l.getCurrentToken().type);
			l.scan();							
			assertEquals(">", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.GREATER_THAN, l.getCurrentToken().getType());
			l.scan();							
			assertEquals(">=", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.GREATER_THAN_EQUALS, l.getCurrentToken().getType());
			l.scan();							
			assertEquals("**", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.EXPONENTIATION, l.getCurrentToken().getType());
			l.scan();							
			assertEquals("*", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.MULTIPLICATION, l.getCurrentToken().getType());
			l.scan();							
			assertEquals("+", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.ADDITION, l.getCurrentToken().getType());
			l.scan();							
			assertEquals("-", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.SUBTRACTION, l.getCurrentToken().getType());
			l.scan();							
			assertEquals("<=", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.LESS_THAN_EQUALS, l.getCurrentToken().getType());
			l.scan();							
			assertEquals("=", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.EQUALS, l.getCurrentToken().getType());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	

}
