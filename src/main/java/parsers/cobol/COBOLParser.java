package main.java.parsers.cobol;

import java.io.IOException;
import java.util.ArrayList;

import main.java.parsers.Parser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.scanners.tokens.cobol.EOFToken;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public class COBOLParser extends Parser {

	public static ArrayList<String> REFERENCES = new ArrayList<String>();
	
	public COBOLParser(Scanner scanner) {
		super(scanner);
	}

	public ParseTreeNode parse(Token t) throws IOException {
		
		sendMessage("BEGIN PARSING");
				
		switch ((COBOLTokenType) t.getType()) {
		case IDENTIFICATION:
			break;
		case ENVIRONMENT:
			break;
		case DATA:
			break;
		case PROCEDURE:
			
			
			parseTree = new ParseTreeNode("PROCEDURE DIVISION");
			
			
			match(t, COBOLTokenType.PROCEDURE, parseTree, TreeNodeType.KEYWORD);

			
			t = scanner.getCurrentToken();
			match(t, COBOLTokenType.DIVISION, parseTree, TreeNodeType.KEYWORD);
			

			
			t = scanner.getCurrentToken();			
			match(t, COBOLTokenType.FULL_STOP, parseTree);
			
			SectionParser sp = new SectionParser(scanner);			
			t = scanner.getCurrentToken();		
			while (t.getType() != COBOLTokenType.EOF) {
				parseTree.addChild(sp.parse(t));
				t = scanner.getCurrentToken();
			}
						
			
			if(t instanceof EOFToken)		
				sendMessage("PARSE SUCCESS");
			
			break;
		default:
			break;
		}
		return parseTree;

	}

}
