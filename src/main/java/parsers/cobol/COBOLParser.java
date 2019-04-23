package main.java.parsers.cobol;

import java.io.IOException;
import java.util.ArrayList;

import main.java.parsers.Parser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.scanners.tokens.cobol.EOFToken;
import main.java.trees.ParseTreeNode;

public class COBOLParser extends Parser {

	public static ArrayList<String> REFERENCES = new ArrayList<String>();
	
	public COBOLParser(Scanner scanner) {
		super(scanner);
	}

	public ParseTreeNode parse(Token t) throws IOException {
		
		
				
		switch ((COBOLTokenType) t.getType()) {
		case IDENTIFICATION:
			break;
		case ENVIRONMENT:
			break;
		case DATA:
			break;
		case PROCEDURE:
			
			
			parseTree = new ParseTreeNode("PROCEDURE DIVISION");
			
			parseTree.addChild(new ParseTreeNode(t.getTokenValue()));

			scanner.scan();
			t = scanner.getCurrentToken();
			parseTree.addChild(new ParseTreeNode(t.getTokenValue()));

			scanner.scan();
			t = scanner.getCurrentToken();
			parseTree.addChild(new ParseTreeNode(t.getTokenValue()));

			SectionParser sp = new SectionParser(scanner);
			scanner.scan();
			t = scanner.getCurrentToken();		
			while (t.getType() != COBOLTokenType.EOF) {
				parseTree.addChild(sp.parse(t));
				t = scanner.getCurrentToken();
			}
						
			
			if(t instanceof EOFToken)		
				System.out.println("Parse Success");
			
			break;
		default:
			break;
		}
		return parseTree;

	}

}
