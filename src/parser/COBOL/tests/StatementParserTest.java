package parser.COBOL.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import lexer.Lexer;
import lexer.SourceFile;
import parser.ParseTreeNode;
import parser.COBOL.StatementParser;

public class StatementParserTest {

	@Test
	public void testParseMoveStatement() {
		String input = "MOVE one TO two";
				   
	BufferedReader in = new BufferedReader(new StringReader(input));
	SourceFile s = new SourceFile(in);
	Lexer l = new Lexer(s);	
	
	StatementParser sp = new StatementParser(l);
	try {
		ParseTreeNode pt = sp.parse();
		sp.printParseTree(pt);
	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	}

}
