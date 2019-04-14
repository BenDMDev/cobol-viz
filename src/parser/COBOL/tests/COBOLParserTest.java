package parser.COBOL.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import intermediate.graph.Graph;
import lexer.Lexer;
import lexer.SourceFile;
import parser.trees.ParseTreeNode;
import parser.trees.TreeVisitor;
import parser.trees.nodes.COBOL.COBOLVisitor;
import parser.COBOL.COBOLParser;

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
		Lexer l = new Lexer(s);
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
