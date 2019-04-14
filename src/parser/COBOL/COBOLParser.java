package parser.COBOL;

import java.io.IOException;

import lexer.Lexer;
import lexer.tokens.COBOLTokenType;
import lexer.tokens.EOFToken;
import lexer.tokens.Token;
import parser.trees.ParseTreeNode;
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

			SectionParser sp = new SectionParser(lexer);
			lexer.scan();
			t = lexer.getCurrentToken();
			System.out.println(lexer.getCurrentToken().getTokenValue());
			while (t.getType() != COBOLTokenType.END) {
				parseTree.addChild(sp.parse(t));
				t = lexer.getCurrentToken();
			}
			
			parseTree.addChild(new ParseTreeNode(t.getTokenValue())); 
			lexer.scan(); // Consume END token
			t = lexer.getCurrentToken();
			parseTree.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan(); // Consume PROGRAM token
			t = lexer.getCurrentToken();
			parseTree.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan(); // Consume terminator
			t = lexer.getCurrentToken();
			
			if(t instanceof EOFToken)		
				System.out.println("Parse Success");
			
			break;
		default:
			break;
		}
		return parseTree;

	}

}
