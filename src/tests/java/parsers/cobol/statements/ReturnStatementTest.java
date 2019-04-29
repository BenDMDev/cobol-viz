package tests.java.parsers.cobol.statements;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import main.java.parsers.cobol.StatementParser;
import main.java.parsers.cobol.statements.ReleaseStatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public class ReturnStatementTest {

	@Test
	public void testParse() {
		String input = "RETURN file-name RECORD INTO id-1\n"
						+ "AT END\n";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new ReleaseStatementParser(l);
		try {
			l.scan();
			
			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			ArrayList<ParseTreeNode> children =  (ArrayList<ParseTreeNode>) pt.getChildren();
			tree.printParseTree();
			assertEquals(4, children.size());
			assertEquals("RELEASE", children.get(0).getAttribute());
			assertEquals(TreeNodeType.KEYWORD, children.get(0).getTreeNodeType());
			assertEquals("my-file", children.get(1).getAttribute());
			assertEquals(TreeNodeType.IDENTIFIER, children.get(1).getTreeNodeType());
			assertEquals("my-id", children.get(3).getAttribute());
			assertEquals(TreeNodeType.IDENTIFIER, children.get(3).getTreeNodeType());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
