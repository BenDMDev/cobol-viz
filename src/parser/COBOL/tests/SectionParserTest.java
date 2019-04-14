package parser.COBOL.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import lexer.Lexer;
import lexer.SourceFile;
import parser.trees.ParseTreeNode;
import parser.COBOL.SectionParser;

public class SectionParserTest {

	@Test
	public void testTwoSections() {
		String input = "two-sections SECTION.\n" + "para-label.\n" + "MOVE x TO y.\n" + "section-label SECTION.\n"
	+ "END PROGRAM.";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);
		SectionParser sp = new SectionParser(l);

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
	public void testOneSection() {
		String input = "one-sections SECTION.\n" + "para-label.\n" + "MOVE x TO y.\n" + "END PROGRAM.";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);
		SectionParser sp = new SectionParser(l);

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
