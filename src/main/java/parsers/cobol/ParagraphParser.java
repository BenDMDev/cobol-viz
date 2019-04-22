package main.java.parsers.cobol;

import java.io.IOException;

import main.java.parsers.Parser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.scanners.tokens.cobol.EOFToken;
import main.java.trees.ParseTreeNode;
import main.java.trees.cobol.ParagraphNode;

public class ParagraphParser extends Parser {

	public ParagraphParser(Scanner scanner) {
		super(scanner);

	}

	@Override
	public ParseTreeNode parse(Token t) throws IOException {
		parseTree = new ParagraphNode("PARAGRAPH", t.getTokenValue());
		if (t.getType() == COBOLTokenType.IDENTIFIER) {
			COBOLParser.REFERENCES.add(t.getTokenValue());
			parseTree.addChild(new ParseTreeNode(t.getTokenValue()));

			scanner.scan(); // Consume Paragraph identifier
			t = scanner.getCurrentToken();
			parseTree.addChild(new ParseTreeNode(t.getTokenValue()));
			scanner.scan(); // Consume '.' terminator
			t = scanner.getCurrentToken();

		}
	
		parseSentences(t);
	

		return parseTree;
	}

	private void parseSentences(Token t) throws IOException {

		while (t.getType() != COBOLTokenType.IDENTIFIER
				&& !(t.getType() == COBOLTokenType.END || t instanceof EOFToken)) {
			SentenceParser senParse = new SentenceParser(scanner);
			parseTree.addChild(senParse.parse(t));
			t = scanner.getCurrentToken();
		}

	}

}
