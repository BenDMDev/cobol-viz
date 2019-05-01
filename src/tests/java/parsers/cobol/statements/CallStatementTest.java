package tests.java.parsers.cobol.statements;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import main.java.parsers.cobol.StatementParser;
import main.java.parsers.cobol.statements.AcceptStatementParser;
import main.java.parsers.cobol.statements.CallStatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

public class CallStatementTest {

	@Test
	public void testCallSimple() {
		String input = "CALL \"OLOG\" USING LDA HDA USER-ID USER-ID-L PSW PSW-L CONN CONN-L CONN-MODE.\n";
	 		

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new CallStatementParser(l);
		try {
			l.scan();
			
			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			ArrayList<ParseTreeNode> children =  (ArrayList<ParseTreeNode>) pt.getChildren();
			tree.printParseTree();
			assertEquals(12, children.size());
			assertEquals("CONN-MODE", children.get(11).getAttribute());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCallSimpleTwo() {
		String input = "call 'printrunreport' using print-run-control end-call";		 		

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new CallStatementParser(l);
		try {
			l.scan();
			
			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			ArrayList<ParseTreeNode> children = (ArrayList<ParseTreeNode>) pt.getChildren();
			tree.printParseTree();
			assertEquals(5, children.size());
			assertEquals("printrunreport", children.get(1).getAttribute());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	@Test
	public void testCallException() {
		String input = "CALL var-1 USING BY CONTENT var-2, var-3\n"
				+ "ON EXCEPTION\n"
				+ "MOVE x TO y";
				 		

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new CallStatementParser(l);
		try {
			l.scan();
			
			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			ArrayList<ParseTreeNode> children =  (ArrayList<ParseTreeNode>) pt.getChildren();
			tree.printParseTree();
			assertEquals(9, children.size());
			StatementNode stmt = (StatementNode) children.get(8);
			assertEquals(TreeNodeType.CONDITIONAL_STATEMENT, stmt.getTreeNodeType());
			assertEquals(2, stmt.getChildren().size());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testCallOverflow() {
		String input = "CALL var-1 USING BY CONTENT var-2 var-3\n"
				+ "ON OVERFLOW\n"
				+ "MOVE x TO y";
				 		

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new CallStatementParser(l);
		try {
			l.scan();
			
			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			ArrayList<ParseTreeNode> children =  (ArrayList<ParseTreeNode>) pt.getChildren();
			tree.printParseTree();
			assertEquals(8, children.size());
			StatementNode stmt = (StatementNode) children.get(7);
			assertEquals(TreeNodeType.CONDITIONAL_STATEMENT, stmt.getTreeNodeType());
			assertEquals(2, stmt.getChildren().size());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
