package parser.COBOL.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import lexer.Lexer;
import lexer.SourceFile;
import lexer.tokens.COBOLTokenType;
import parser.trees.ParseTreeNode;
import parser.COBOL.StatementParser;

public class StatementParserTest {

	@Test
	public void testParseMoveStatement() {
		String input = "MOVE CORRESPONDING one TO two three four";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);

		StatementParser sp = new StatementParser(l);
		try {
			l.scan();
			
			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			sp.printParseTree(pt);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testParseMoveStatementTwo() {
		String input = "MOVE EMPNO TO EMPNO-D";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);

		StatementParser sp = new StatementParser(l);
		try {
			l.scan();
			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			sp.printParseTree(pt);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void testParseAddStatement() {
		String input = "ADD xy zw TO one two ROUNDED three.";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);

		StatementParser sp = new StatementParser(l);
		try {
			l.scan();
			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			sp.printParseTree(pt);
			assertEquals(COBOLTokenType.FULL_STOP, l.getCurrentToken().getType());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testParseConditionalStatement() {
		String input = "IF lda-rc NOT = y\n" 
				+ "MOVE ten TO five\n";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);

		StatementParser sp = new StatementParser(l);
		try {
			l.scan();			
			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			sp.printParseTree(pt);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
