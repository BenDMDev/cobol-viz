package tests.java.parsers.cobol.statements;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import main.java.parsers.cobol.StatementParser;
import main.java.parsers.cobol.statements.UnstringStatementParser;
import main.java.parsers.cobol.statements.WriteStatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public class WriteStatementTest {

	@Test
	public void testParse() {
		String input = "WRITE REPORT-RECORD FROM SECOND-HEADING-LINE AFTER ADVANCING 1 LINE.";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new WriteStatementParser(l);
		try {
			l.scan();

			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			ArrayList<ParseTreeNode> children = (ArrayList<ParseTreeNode>) pt.getChildren();
			tree.printParseTree();
			assertEquals(9, children.size());
			assertEquals("WRITE", children.get(0).getAttribute());
			assertEquals(TreeNodeType.KEYWORD, children.get(0).getTreeNodeType());
			assertEquals("my-id", children.get(3).getAttribute());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
