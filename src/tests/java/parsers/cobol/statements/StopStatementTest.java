package tests.java.parsers.cobol.statements;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import main.java.parsers.cobol.StatementParser;
import main.java.parsers.cobol.statements.StartStatementParser;
import main.java.parsers.cobol.statements.StopStatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public class StopStatementTest {

	@Test
	public void testParse() {
		String input = "STOP RUN.";

	BufferedReader in = new BufferedReader(new StringReader(input));
	SourceFile s = new SourceFile(in);
	Scanner l = new Scanner(s);

	StatementParser sp = new  StopStatementParser(l);
	try {
		l.scan();

		ParseTreeNode pt = sp.parse(l.getCurrentToken());
		ParseTree tree = new ParseTree();
		tree.setRoot(pt);
		ArrayList<ParseTreeNode> children = (ArrayList<ParseTreeNode>) pt.getChildren();
		tree.printParseTree();
		assertEquals(2, children.size());
		assertEquals("STOP", children.get(0).getAttribute());
		assertEquals(TreeNodeType.KEYWORD, children.get(0).getTreeNodeType());
		assertEquals("RUN", children.get(1).getAttribute());
		assertEquals(TreeNodeType.KEYWORD, children.get(1).getTreeNodeType());
		

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}
