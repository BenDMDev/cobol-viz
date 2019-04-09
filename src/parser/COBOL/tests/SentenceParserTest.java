package parser.COBOL.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import lexer.Lexer;
import lexer.SourceFile;
import parser.ParseTreeNode;
import parser.COBOL.SentenceParser;

public class SentenceParserTest {
	
	@Test
	public void testStatementParser() {
		
		String input =  "MOVE x TO z\n"						
						+ "IF x GREATER THAN sy\n" 
						+ "MOVE y TO z\n"
						+ "ADD x TO two.";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);
		SentenceParser sp = new SentenceParser(l);
		
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
