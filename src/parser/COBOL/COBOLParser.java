package parser.COBOL;

import java.io.IOException;

import lexer.Lexer;
import lexer.tokens.COBOLTokenType;
import lexer.tokens.Token;
import parser.ParseTreeNode;
import parser.Parser;

public class COBOLParser extends Parser {

	public COBOLParser(Lexer l) {
		super(l);
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

			lexer.scan();
			t = lexer.getCurrentToken();
			parseTree.addChild(new ParseTreeNode(t.getTokenValue()));

			lexer.scan();
			t = lexer.getCurrentToken();
			parseTree.addChild(new ParseTreeNode(t.getTokenValue()));

			SentenceParser sp = new SentenceParser(lexer);
			lexer.scan();
			t = lexer.getCurrentToken();
			while (t.getType() != COBOLTokenType.END) {
				parseTree.addChild(sp.parse(t));
				t = lexer.getCurrentToken();
			}
			break;
		default:
			break;
		}
		return parseTree;

	}

}
