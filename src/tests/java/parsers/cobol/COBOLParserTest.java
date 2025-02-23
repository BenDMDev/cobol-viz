package tests.java.parsers.cobol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;

import main.java.graphs.Graph;
import main.java.parsers.cobol.COBOLParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.visitors.cobol.CallGraphVisitor;

public class COBOLParserTest {

	@Test
	public void testParse() {		

		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(new File("src/resources/sample_3.cbl")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SourceFile s = new SourceFile(in);
		Scanner l = new Scanner(s);
		COBOLParser cbp = new COBOLParser(l);

		try {
			l.scan();

			ParseTree tree = new ParseTree();
			tree.setRoot(cbp.parse(l.getCurrentToken()));
			tree.printParseTree();
			CallGraphVisitor tv = new CallGraphVisitor();
			tree.accept(tv);
			Graph g = tv.getGraph();
			g.printMatrix();
			
			
			
			s.close();
			
		
	
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
