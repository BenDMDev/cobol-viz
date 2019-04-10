package parser.COBOL.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import lexer.Lexer;
import lexer.SourceFile;
import parser.ParseTreeNode;
import parser.COBOL.ParagraphParser;

public class ParagraphParserTest {

	@Test
	public void testWithParaLabel() {
		String input = "PARA-NAME.\n"
					+ "MOVE x TO Y\n"
					+ "ADD x TO z\n"
				    + "IF x > y.\n" 
					+ "MOVE two TO three.\n"
				    + "para-name-two.\n";
					   

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);
		ParagraphParser pp = new ParagraphParser(l);

		try {
			l.scan();

			ParseTreeNode pt = pp.parse(l.getCurrentToken());
			pp.printParseTree(pt);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestWithoutParaLabel() {
		String input = "MOVE x TO Y\n"
				   + "ADD x TO z\n"
			       + "IF x > y.\n" 
				   + "MOVE two TO three.\n"
			       + "para-name.";
				   

	BufferedReader in = new BufferedReader(new StringReader(input));
	SourceFile s = new SourceFile(in);
	Lexer l = new Lexer(s);
	ParagraphParser pp = new ParagraphParser(l);

	try {
		l.scan();

		ParseTreeNode pt = pp.parse(l.getCurrentToken());
		pp.printParseTree(pt);

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}

}
