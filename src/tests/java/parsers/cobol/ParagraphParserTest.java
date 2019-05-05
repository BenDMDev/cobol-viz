package tests.java.parsers.cobol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import main.java.parsers.cobol.ParagraphParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;

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
		Scanner l = new Scanner(s);
		ParagraphParser pp = new ParagraphParser(l);

		try {
			l.scan();

			ParseTreeNode pt = pp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			tree.printParseTree();

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
				   + "MOVE two TO three\n"
			       + "END-IF.\n"
			       + "para-name.";
				   

	BufferedReader in = new BufferedReader(new StringReader(input));
	SourceFile s = new SourceFile(in);
	Scanner l = new Scanner(s);
	ParagraphParser pp = new ParagraphParser(l);

	try {
		l.scan();

		ParseTreeNode pt = pp.parse(l.getCurrentToken());
		ParseTree tree = new ParseTree();
		tree.setRoot(pt);
		tree.printParseTree();

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}

}
