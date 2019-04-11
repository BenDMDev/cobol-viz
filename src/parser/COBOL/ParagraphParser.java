package parser.COBOL;

import java.io.IOException;

import lexer.Lexer;
import lexer.tokens.COBOLTokenType;
import lexer.tokens.EOFToken;
import lexer.tokens.Token;
import parser.trees.ParseTreeNode;
import parser.Parser;

public class ParagraphParser extends Parser {

	public ParagraphParser(Lexer l) {
		super(l);

	}

	@Override
	public ParseTreeNode parse(Token t) throws IOException {
		parseTree = new ParseTreeNode("PARAGRAPH");
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
