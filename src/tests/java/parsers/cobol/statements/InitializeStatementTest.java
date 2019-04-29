package tests.java.parsers.cobol.statements;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import main.java.parsers.cobol.StatementParser;
import main.java.parsers.cobol.statements.GoToStatementParser;
import main.java.parsers.cobol.statements.InitializeStatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;

public class InitializeStatementTest {

	@Test
	public void testParse() {
		String input = "INITIALIZE proc-1 proc-2\n"
				+ "REPLACING NUMERIC DATA BY 10 my-val";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new InitializeStatementParser(l);
		try {
			l.scan();
			
			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			ArrayList<ParseTreeNode> children =  (ArrayList<ParseTreeNode>) pt.getChildren();
			tree.printParseTree();
			assertEquals(9, children.size());
			assertEquals("proc-1", children.get(1).getAttribute());
			assertEquals("10", children.get(7).getAttribute());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
