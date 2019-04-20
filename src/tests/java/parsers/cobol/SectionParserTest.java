package tests.java.parsers.cobol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import main.java.parsers.cobol.SectionParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTreeNode;

public class SectionParserTest {

	@Test
	public void testTwoSections() {
		String input = "two-sections SECTION.\n" + "para-label.\n" + "MOVE x TO y.\n" + "section-label SECTION.\n";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);
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
		String input = "one-sections SECTION.\n" + "para-label.\n" + "MOVE x TO y.\n";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);
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
