package tests.java.parsers.cobol.statements;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import main.java.parsers.cobol.StatementParser;
import main.java.parsers.cobol.statements.MoveStatementParser;
import main.java.parsers.cobol.statements.PerformStatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;

public class PerformStatementTest {

	@Test
	public void testParse() {
		String input = "PERFORM proc-1 UNTIL 5 > 0";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new PerformStatementParser(l);
		try {
			l.scan();

			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			ArrayList<ParseTreeNode> children = (ArrayList<ParseTreeNode>) pt.getChildren();
			tree.printParseTree();
			assertEquals(6, children.size());
			assertEquals("proc-1", children.get(1).getAttribute());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
