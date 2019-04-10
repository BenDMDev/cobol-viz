package parser.COBOL.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import lexer.Lexer;
import lexer.SourceFile;
import parser.ParseTreeNode;
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
				+ "ADD x TO z\n"
				+ "IF x > y.\n"
				+ "sec-two SECTION.\n"
				+ "MOVE five TO six.\n"
				+ "END PROGRAM.";

		BufferedReader in = new BufferedReader(new StringReader(input));
		SourceFile s = new SourceFile(in);
		Lexer l = new Lexer(s);
		COBOLParser cbp = new COBOLParser(l);

		try {
			l.scan();

			ParseTreeNode pt = cbp.parse(l.getCurrentToken());
			cbp.printParseTree(pt);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
