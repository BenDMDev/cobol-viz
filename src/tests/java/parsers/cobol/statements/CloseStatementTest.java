package tests.java.parsers.cobol.statements;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import main.java.parsers.cobol.StatementParser;
import main.java.parsers.cobol.statements.CloseStatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;

public class CloseStatementTest {

	@Test
	public void testParse() {
		String input = "CLOSE file-1 file-2 file-3\n";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new CloseStatementParser(l);
		try {
			l.scan();
			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			tree.printParseTree();
			assertEquals(4, pt.getChildren().size());
			assertEquals("file-3", pt.getChildren().get(3).getAttribute());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
