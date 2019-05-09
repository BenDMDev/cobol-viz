package tests.java.parsers.cobol.statements;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import main.java.parsers.cobol.StatementParser;
import main.java.parsers.cobol.statements.AcceptStatementParser;
import main.java.parsers.cobol.statements.CancelStatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;

/**
 * 
 * @author Ben
 *
 */
public class CancelStatementTest {

	@Test
	public void testParse() {
		String input = "CANCEL var-1 \"MY FILE\" var-2";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new CancelStatementParser(l);
		try {
			l.scan();
			
			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			ArrayList<ParseTreeNode> children =  (ArrayList<ParseTreeNode>) pt.getChildren();
			tree.printParseTree();
			assertEquals(4, children.size());
			assertEquals("MY FILE", children.get(2).getAttribute());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
