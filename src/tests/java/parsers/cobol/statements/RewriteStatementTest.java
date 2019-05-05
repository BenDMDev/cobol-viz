package tests.java.parsers.cobol.statements;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import main.java.parsers.cobol.StatementParser;
import main.java.parsers.cobol.statements.ReleaseStatementParser;
import main.java.parsers.cobol.statements.RewriteStatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public class RewriteStatementTest {

	@Test
	public void testParse() {
		String input = "REWRITE file-name FROM id-1\n" + "INVALID KEY\n" + "DISPLAY HELLO";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new RewriteStatementParser(l);
		try {
			l.scan();

			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			ArrayList<ParseTreeNode> children = (ArrayList<ParseTreeNode>) pt.getChildren();
			tree.printParseTree();
			assertEquals(5, children.size());
			assertEquals("REWRITE", children.get(0).getAttribute());
			assertEquals(TreeNodeType.KEYWORD, children.get(0).getTreeNodeType());
			assertEquals("file-name", children.get(1).getAttribute());
			assertEquals(TreeNodeType.IDENTIFIER, children.get(1).getTreeNodeType());
			assertEquals("id-1", children.get(3).getAttribute());
			assertEquals(TreeNodeType.IDENTIFIER, children.get(3).getTreeNodeType());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
