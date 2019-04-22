package tests.java.parsers.cobol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import main.java.parsers.cobol.SentenceParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;

public class SentenceParserTest {
	
	@Test
	public void testStatementParser() {
		
		String input =  "MOVE x TO z\n"						
						+ "IF x GREATER THAN sy\n" 
						+ "MOVE y TO z\n"
						+ "ADD x TO two.";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);
		SentenceParser sp = new SentenceParser(l);
		
		try {
			l.scan();
			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			tree.printParseTree();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
