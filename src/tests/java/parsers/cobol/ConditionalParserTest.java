package tests.java.parsers.cobol;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;

public class ConditionalParserTest {

	@Test
	public void testParseEndIf() {
		String input = "IF EMPNO >= 9999\n" +
						"ADD 10 TO five\n" +
						"ELSE\n" +
			"MOVE five TO ten\n" +
			"ADD ten TO six\n" +
			"PERFORM PARA-NAME-SIX\n" +
			"END-IF.";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new StatementParser(l);
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
	
	@Test
	public void testParseNoEndIf() {
		String input = "if country-record(1:1) > '#'\n"
				+ "MOVE ten TO ten.\n";
				

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new StatementParser(l);
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
