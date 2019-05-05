package tests.java.parsers.cobol.statements;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import main.java.parsers.cobol.StatementParser;
import main.java.parsers.cobol.statements.SetStatementParser;
import main.java.parsers.cobol.statements.SortStatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public class SortStatementTest {

	@Test
	public void testParse() {
		String input = "SORT file-name\n"
				+ "WITH DUPLICATES IN ORDER\n"
				+ "INPUT PROCEDURE IS proc-1 THRU proc-2\n"
				+ "USING file-2 file-3\n"
				+ "OUTPUT PROCEDURE IS proc-3 THROUGH proc-4\n"
				+ "GIVING file-4 file-5";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new  SortStatementParser(l);
		try {
			l.scan();

			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			ArrayList<ParseTreeNode> children = (ArrayList<ParseTreeNode>) pt.getChildren();
			tree.printParseTree();
			assertEquals(8, children.size());
			assertEquals("SORT", children.get(0).getAttribute());
			assertEquals(TreeNodeType.KEYWORD, children.get(0).getTreeNodeType());
			assertEquals("file-name", children.get(1).getAttribute());
			assertEquals(TreeNodeType.IDENTIFIER, children.get(1).getTreeNodeType());
			assertEquals("OUTPUT", children.get(7).getAttribute());
			assertEquals(TreeNodeType.REFERENCE, children.get(7).getTreeNodeType());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
