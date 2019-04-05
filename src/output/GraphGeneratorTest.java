package output;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import lexer.Lexer;
import lexer.Source;
import parser.ParseTreeNode;
import parser.Parser;

public class GraphGeneratorTest {

	@Test
	public void testProcess() {
		String input = "IDENTIFICATION DIVISION\n" 
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
	GraphGenerator gp = new GraphGenerator();
	
	ParseTreeNode pN;
	try {
		pN = p.parse();	
		
		gp.process(pN);
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
	}

}
