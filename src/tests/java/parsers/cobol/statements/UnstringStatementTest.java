package tests.java.parsers.cobol.statements;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import main.java.parsers.cobol.StatementParser;
import main.java.parsers.cobol.statements.UnstringStatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public class UnstringStatementTest {

	@Test
	public void testParseOne() {
		String input = "UNSTRING my-id-1";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new UnstringStatementParser(l);
		try {
			l.scan();

			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			ArrayList<ParseTreeNode> children = (ArrayList<ParseTreeNode>) pt.getChildren();
			tree.printParseTree();
			assertEquals(2, children.size());
			assertEquals("UNSTRING", children.get(0).getAttribute());
			assertEquals(TreeNodeType.KEYWORD, children.get(0).getTreeNodeType());
			assertEquals("my-id-1", children.get(1).getAttribute());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testParseTwo() {
		String input = "UNSTRING my-id-1 " + "DELIMITED BY ALL my-delim-1 OR ALL 10 OR ALL my-id";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new UnstringStatementParser(l);
		try {
			l.scan();

			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			ArrayList<ParseTreeNode> children = (ArrayList<ParseTreeNode>) pt.getChildren();
			tree.printParseTree();
			assertEquals(12, children.size());
			assertEquals("UNSTRING", children.get(0).getAttribute());
			assertEquals(TreeNodeType.KEYWORD, children.get(0).getTreeNodeType());
			assertEquals("my-id", children.get(11).getAttribute());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testParseThree() {
		String input = "UNSTRING my-id-1 " + "DELIMITED BY ALL my-delim-1 OR ALL 10 OR ALL my-id\n"
				+ "INTO my-id DELIMITER IN my-id-5 COUNT IN id-7 my-id DELIMITER IN id-7 COUNT IN id-6 ";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new UnstringStatementParser(l);
		try {
			l.scan();

			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			ArrayList<ParseTreeNode> children = (ArrayList<ParseTreeNode>) pt.getChildren();
			tree.printParseTree();
			assertEquals(27, children.size());
			assertEquals("UNSTRING", children.get(0).getAttribute());
			assertEquals(TreeNodeType.KEYWORD, children.get(0).getTreeNodeType());
			assertEquals("my-id", children.get(11).getAttribute());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testParseFOUR() {
		String input = "UNSTRING my-id-1 " + "DELIMITED BY ALL my-delim-1 OR ALL 10 OR ALL my-id\n"
				+ "INTO my-id DELIMITER IN my-id-5 COUNT IN id-7 my-id DELIMITER IN id-7 COUNT IN id-6\n"
				+ "WITH POINTER id-9\n"
				+ "TALLYING IN id-10\n"
			    + "ON OVERFLOW DISPLAY \"ERROR\"";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new UnstringStatementParser(l);
		try {
			l.scan();

			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			ArrayList<ParseTreeNode> children = (ArrayList<ParseTreeNode>) pt.getChildren();
			tree.printParseTree();
			assertEquals(34, children.size());
			assertEquals("UNSTRING", children.get(0).getAttribute());
			assertEquals(TreeNodeType.KEYWORD, children.get(0).getTreeNodeType());
			assertEquals("my-id", children.get(11).getAttribute());
			assertEquals(TreeNodeType.CONDITIONAL_STATEMENT, children.get(33).getTreeNodeType());
			assertEquals(TreeNodeType.COMPOUND_STATEMENT, pt.getTreeNodeType());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
