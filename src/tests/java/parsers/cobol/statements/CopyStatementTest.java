package tests.java.parsers.cobol.statements;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import main.java.parsers.cobol.StatementParser;
import main.java.parsers.cobol.statements.AlterStatementParser;
import main.java.parsers.cobol.statements.CopyStatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.ParseTreeNode;

public class CopyStatementTest {

	@Test
	public void testParse() {
		String input = "COPY my-var IN my-var-2\n"
						+"REPLACING my-car BY my-van ";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);

		StatementParser sp = new CopyStatementParser(l);
		try {
			l.scan();
			ParseTreeNode pt = sp.parse(l.getCurrentToken());
			ParseTree tree = new ParseTree();
			tree.setRoot(pt);
			tree.printParseTree();
			assertEquals(8, pt.getChildren().size());
			assertEquals("my-var-2", pt.getChildren().get(3).getAttribute());
			assertEquals("my-van", pt.getChildren().get(7).getAttribute());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
