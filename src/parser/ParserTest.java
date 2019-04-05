package parser;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import lexer.Lexer;
import lexer.Source;
import output.GraphGenerator;

public class ParserTest {

	@Test
	public void testParse() {
		String input = "ID DIVISION\n" 
					   + "PROGRAM-ID HELLO-WORLD\n"
					   + "PROCEDURE DIVISION\n"
					   + "MOVE one TO two\n"
					   + "MOVE 5126 TO three\n"
					   + "MOVE four TO five\n"
					   + "END PROGRAM";
		BufferedReader in = new BufferedReader(new StringReader(input));
		Source s = new Source(in);
		Lexer l = new Lexer(s);
		Parser p = new Parser(l);
		
		ParseTreeNode pN;
		try {
			pN = p.parse();
			pN.traverse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
