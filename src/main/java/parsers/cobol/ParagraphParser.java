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

	public ParagraphParser(Scanner l) {
		super(l);

	}

	@Override
	public ParseTreeNode parse(Token t) throws IOException {
		parseTree = new ParagraphNode("PARAGRAPH");
		if (t.getType() == COBOLTokenType.IDENTIFIER) {

			parseTree.addChild(new ParseTreeNode(t.getTokenValue()));

			lexer.scan(); // Consume Paragraph identifier
			t = lexer.getCurrentToken();
			parseTree.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan(); // Consume '.' terminator
			t = lexer.getCurrentToken();

		}

		switch ((COBOLTokenType) t.getType()) {
		case ADD:
		case MOVE:
		case IF:
			parseSentences(t);
			break;
		default:
			break;
		}

		return this.parseTree;
	}

	private void parseSentences(Token t) throws IOException {

		while (t.getType() != COBOLTokenType.IDENTIFIER
				&& !(t.getType() == COBOLTokenType.END || t instanceof EOFToken)) {
			SentenceParser senParse = new SentenceParser(lexer);
			parseTree.addChild(senParse.parse(t));
			t = lexer.getCurrentToken();
		}

	}

}
