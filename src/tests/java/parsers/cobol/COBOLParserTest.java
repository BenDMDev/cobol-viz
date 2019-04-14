package tests.java.parsers.cobol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import main.java.graphs.Graph;
import main.java.parsers.cobol.COBOLParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTreeNode;
import main.java.trees.visitors.cobol.COBOLVisitor;

public class COBOLParserTest {

	@Test
	public void testParse() {
		String input = "PROCEDURE DIVISION.\n"
				+ "sec-one SECTION.\n"
				+ "PARA-NAME.\n" 
				+ "MOVE x TO Y\n"
				+ "ADD x TO z\n"
				+ "IF x > y.\n"
				+ "PARA-NAME-TWO.\n"
				+ "ADD two TO three\n"
				+ "IF one > five.\n"
				+ "PARA-NAME-THREE.\n"
				+ "ADD two TO three\n"
				+ "IF one > five\n"
				+ "MOVE x TO Y\n"
				+ "ELSE\n"
				+ "IF x > y\n"
				+ "MOVE y TO x.\n"
				+ "sec-two SECTION.\n"
				+ "Para-name-four.\n"
				+ "Para-name-five.\n"
				+ "MOVE five TO six.\n"
				+ "END PROGRAM.";

		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(new File("src/test.txt")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);
		COBOLParser cbp = new COBOLParser(l);

		try {
			l.scan();

			ParseTreeNode pt = cbp.parse(l.getCurrentToken());
			cbp.printParseTree(pt);
			COBOLVisitor tv = new COBOLVisitor();
			pt.accept(tv);
			Graph g = tv.getGraph();
			g.printMatrix();
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
