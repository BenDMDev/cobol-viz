package tests.java.parsers.cobol;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;

public class StatementParserTest {

	@Test
	public void testParseMoveStatement() {
		String input = "MOVE CORRESPONDING 2.2 TO two three four";

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
	public void testParseMoveStatementTwo() {
		String input = "MOVE EMPNO TO EMPNO-D";

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
	public void testParseAddStatement() {
		String input = "ADD CORRESPONDING 10 zw 2.2 TO one two ROUNDED three\n"
				+ "GIVING my-val ROUNDED\n"
				+ "ON SIZE ERROR\n" +
				 "MOVE x TO m-val.";

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
			assertEquals(COBOLTokenType.FULL_STOP, l.getCurrentToken().getType());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testParseConditionalStatement() {
		String input = "IF lda-rc NOT = y\n" 
				+ "MOVE ten TO five\n"
				+ "ELSE \n" 
				+ "ADD ten TO five\n"
				+ "MOVE five TO six.\n";
				

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
