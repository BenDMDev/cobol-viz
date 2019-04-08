package output;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import lexer.Lexer;
import lexer.SourceFile;
import parser.ParseTreeNode;
import parser.Parser;

public class GraphWriterTest {

	@Test
	public void testWriteToFile() {
	String input = "IDENTIFICATION DIVISION.\n" 
				   + "PROGRAM-ID HELLO-WORLD\n"
				   + "PROCEDURE DIVISION\n"
				   + "MOVE one TO two\n"
				   + "MOVE 5126 TO three\n"
				   + "ADD four TO five.\n"
				   + "END PROGRAM";
	BufferedReader in = new BufferedReader(new StringReader(input));
	SourceFile s = new SourceFile(in);
	Lexer l = new Lexer(s);
	Parser p = new Parser(l);
	GraphGenerator gp = new GraphGenerator();
	GraphWriter gW = new GraphWriter(gp);
	
	ParseTreeNode pN;
	try {
		pN = p.parse();	
		
		gW.generate(pN);
		gW.writeToFile();
		System.out.println(gp.getOutputString());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}
