package lexer.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import lexer.Lexer;
import lexer.SourceFile;
import lexer.tokens.COBOLTokenType;
import lexer.tokens.Token;

public class LexerTest {

	@Test
	public void testWordScan() {
		BufferedReader in = new BufferedReader(new StringReader("IDENTIFICATION DIVISION. \n PROGRAM-ID HELLO 45685"));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);
		
		try {
			l.scan();				
			assertEquals("IDENTIFICATION", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.IDENTIFICATION, l.getCurrentToken().getType());
			l.scan();							
			assertEquals("DIVISION", l.getCurrentToken().getTokenValue());
			assertEquals(COBOLTokenType.DIVISION, l.getCurrentToken().getType());
			l.scan();						
			assertEquals(".", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.FULL_STOP, l.getCurrentToken().getType());
			l.scan();						
			assertEquals("PROGRAM-ID", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.PROGRAM_ID, l.getCurrentToken().getType());
			l.scan();				
			assertEquals("HELLO", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.IDENTIFIER, l.getCurrentToken().getType());
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
			assertEquals(COBOLTokenType.INTEGER, l.getCurrentToken().getType());
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
			assertEquals(COBOLTokenType.FULL_STOP, l.getCurrentToken().getType());
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
	
	@Test
	public void testEOF() {
		BufferedReader in = new BufferedReader(new StringReader("test"));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);
		
		try {
			l.scan();				
			assertEquals("test", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.IDENTIFIER, l.getCurrentToken().getType());
			l.scan();						
			assertEquals(COBOLTokenType.EOF, l.getCurrentToken().getType());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testLookAhead() {
		BufferedReader in = new BufferedReader(new StringReader("test DIVISION"));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);
		
		try {
			l.scan();				
			assertEquals("test", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.IDENTIFIER, l.getCurrentToken().getType());
			Token t = l.lookAhead();
			t = l.lookAhead();
			t = l.lookAhead();
			assertEquals(COBOLTokenType.DIVISION, t.getType());
			assertEquals("test", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.IDENTIFIER, l.getCurrentToken().getType());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	public void testNameWithNumbers() {
		BufferedReader in = new BufferedReader(new StringReader("test9-name-0"));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);
		
		try {
			l.scan();				
			assertEquals("test9-name-0", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.IDENTIFIER, l.getCurrentToken().getType());
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	@Test 
	public void testStringLiteral() {
		BufferedReader in = new BufferedReader(new StringReader("IF \"LDA-RC\" NOT = 0\n"));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);
		
		try {
			l.scan();			
			assertEquals("IF", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.IF, l.getCurrentToken().getType());
			l.scan();			
			assertEquals("LDA-RC", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.STRING_LITERAL, l.getCurrentToken().getType());
			l.scan();			
			assertEquals("NOT", l.getCurrentToken().getTokenValue());			
			assertEquals(COBOLTokenType.NOT, l.getCurrentToken().getType());
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	

}
