package tests.java.parsers.cobol.statements;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import main.java.parsers.cobol.StatementParser;
import main.java.parsers.cobol.statements.InitializeStatementParser;
import main.java.parsers.cobol.statements.OpenStatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public class OpenStatementTest {

	@Test
	public void testParse() {
		String input = "OPEN INPUT my-file my-file-2 OUPUT my-file-3 EXTEND my-file-4";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new OpenStatementParser(l);
		try {
			l.scan();
			
			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			ArrayList<ParseTreeNode> children =  (ArrayList<ParseTreeNode>) pt.getChildren();
			tree.printParseTree();
			assertEquals(8, children.size());
			assertEquals("INPUT", children.get(1).getAttribute());
			assertEquals("my-file-2", children.get(3).getAttribute());
			assertEquals("EXTEND", children.get(6).getAttribute());
			assertEquals(TreeNodeType.KEYWORD, children.get(6).getTreeNodeType());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
