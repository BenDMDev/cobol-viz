package main.java.parsers.cobol;

import java.io.IOException;

import main.java.parsers.Parser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.scanners.tokens.cobol.EOFToken;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.ParagraphNode;

public class ParagraphParser extends Parser {

	public ParagraphParser(Scanner scanner) {
		super(scanner);

	}

	@Override
	public ParseTreeNode parse(Token t) throws IOException {
		
		if (t.getType() == COBOLTokenType.IDENTIFIER) {
			parseTree = new ParagraphNode(TreeNodeType.PROCEDURE, t.getTokenValue());
			
			COBOLParser.REFERENCES.add(t.getTokenValue());	
			matchSequence(t, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.FULL_STOP);
			t = scanner.getCurrentToken();
		} else {
			parseTree = new ParagraphNode(TreeNodeType.PROCEDURE, "");
		}
	
		parseTree.setLineNumber(t.getLineNumber());
		parseSentences(t);
	

		return parseTree;
	}

	private void parseSentences(Token t) throws IOException {

		while (t.getType() != COBOLTokenType.IDENTIFIER
				&& !(t.getType() == COBOLTokenType.END || t instanceof EOFToken)) {
			SentenceParser senParse = new SentenceParser(scanner);
			senParse.addListener(listener);
			parseTree.addChild(senParse.parse(t));
			t = scanner.getCurrentToken();
		}

	}

}
