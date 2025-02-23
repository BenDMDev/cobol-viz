package main.java.parsers.cobol;

import java.io.IOException;

import main.java.parsers.Parser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.SectionNode;

public class SectionParser extends Parser {

	public SectionParser(Scanner scanner) {
		super(scanner);
		
	}

	@Override
	public ParseTreeNode parse(Token t) throws IOException {
		
		parseTree = new SectionNode(TreeNodeType.PROCEDURE_BLOCK, "SECTION");
		String sectionRef = "";
				
		if(t.getType() == COBOLTokenType.IDENTIFIER && scanner.lookAhead().getType() == COBOLTokenType.SECTION) {
			sectionRef = t.getTokenValue();
			matchSequence(t, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.SECTION, COBOLTokenType.FULL_STOP);
			t = scanner.getCurrentToken();
			
		}
		
		ParagraphParser paragraphParser = new ParagraphParser(scanner);
		paragraphParser.addListener(listener);
		
		while(!(t.getType() == COBOLTokenType.EOF)
				&& !(t.getType() == COBOLTokenType.IDENTIFIER 
				&& scanner.lookAhead().getType() == COBOLTokenType.SECTION)) {			
			parseTree.addChild(paragraphParser.parse(t));
			t = scanner.getCurrentToken();
			
			
		}
		if(parseTree.getChildren().size() > 3) {
			COBOLParser.SECTION_REFERENCES.put(sectionRef, parseTree.getChildren().get(3).getAttribute());
			System.out.println(sectionRef +  " : " + COBOLParser.SECTION_REFERENCES.get(sectionRef));
		}
		
		return parseTree;
	}
	


}
