package tests.java.parsers.cobol.statements;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import main.java.parsers.cobol.StatementParser;
import main.java.parsers.cobol.statements.StringStatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public class StringStatementTest {

	@Test
	public void testParse() {
		String input = "STRING my-val my-val-2 DELIMITED BY SIZE\n" +
						"my-val-3 my-val-4 DELIMITED BY my-del\n";

	BufferedReader in = new BufferedReader(new StringReader(input));
	SourceFile s = new SourceFile(in);
	Scanner l = new Scanner(s);

	StatementParser sp = new StringStatementParser(l);
	try {
		l.scan();

		ParseTreeNode pt = sp.parse(l.getCurrentToken());
		ParseTree tree = new ParseTree();
		tree.setRoot(pt);
		ArrayList<ParseTreeNode> children = (ArrayList<ParseTreeNode>) pt.getChildren();
		tree.printParseTree();
		assertEquals(11, children.size());
		assertEquals("STRING", children.get(0).getAttribute());
		assertEquals(TreeNodeType.KEYWORD, children.get(0).getTreeNodeType());
		assertEquals("my-val", children.get(1).getAttribute());
		assertEquals(TreeNodeType.IDENTIFIER, children.get(1).getTreeNodeType());
		assertEquals("SIZE", children.get(5).getAttribute());
		assertEquals(TreeNodeType.KEYWORD, children.get(4).getTreeNodeType());
	

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	@Test
	public void testTwoParse() {
		String input = "STRING my-val my-val-2 DELIMITED BY SIZE\n" +
						"my-val-3 my-val-4 DELIMITED BY my-del\n"
				+ "INTO my-id WITH POINTER id-4";

	BufferedReader in = new BufferedReader(new StringReader(input));
	SourceFile s = new SourceFile(in);
	Scanner l = new Scanner(s);

	StatementParser sp = new StringStatementParser(l);
	try {
		l.scan();

		ParseTreeNode pt = sp.parse(l.getCurrentToken());
		ParseTree tree = new ParseTree();
		tree.setRoot(pt);
		ArrayList<ParseTreeNode> children = (ArrayList<ParseTreeNode>) pt.getChildren();
		tree.printParseTree();
		assertEquals(16, children.size());
		assertEquals("STRING", children.get(0).getAttribute());
		assertEquals(TreeNodeType.KEYWORD, children.get(0).getTreeNodeType());
		assertEquals("my-val", children.get(1).getAttribute());
		assertEquals(TreeNodeType.IDENTIFIER, children.get(1).getTreeNodeType());
		assertEquals("SIZE", children.get(5).getAttribute());
		assertEquals(TreeNodeType.KEYWORD, children.get(4).getTreeNodeType());
	

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	
	@Test
	public void testThreeParse() {
		String input = "STRING my-val my-val-2 DELIMITED BY SIZE\n" +
						"my-val-3 my-val-4 DELIMITED BY my-del\n"
				+ "INTO my-id WITH POINTER id-4\n"
						+"ON OVERFLOW DISPLAY \"ERROR\"";

	BufferedReader in = new BufferedReader(new StringReader(input));
	SourceFile s = new SourceFile(in);
	Scanner l = new Scanner(s);

	StatementParser sp = new StringStatementParser(l);
	try {
		l.scan();

		ParseTreeNode pt = sp.parse(l.getCurrentToken());
		ParseTree tree = new ParseTree();
		tree.setRoot(pt);
		ArrayList<ParseTreeNode> children = (ArrayList<ParseTreeNode>) pt.getChildren();
		tree.printParseTree();
		assertEquals(17, children.size());
		assertEquals("STRING", children.get(0).getAttribute());
		assertEquals(TreeNodeType.KEYWORD, children.get(0).getTreeNodeType());
		assertEquals("my-val", children.get(1).getAttribute());
		assertEquals(TreeNodeType.IDENTIFIER, children.get(1).getTreeNodeType());
		assertEquals("SIZE", children.get(5).getAttribute());
		assertEquals(TreeNodeType.KEYWORD, children.get(4).getTreeNodeType());
		assertEquals(TreeNodeType.COMPOUND_STATEMENT, pt.getTreeNodeType());
	

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	
	

}
