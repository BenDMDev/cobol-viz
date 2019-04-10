package parser.COBOL;

import java.io.IOException;

import lexer.Lexer;
import lexer.tokens.COBOLTokenType;
import lexer.tokens.NumberToken;
import lexer.tokens.Token;
import lexer.tokens.WordToken;
import parser.ParseTreeNode;
import parser.Parser;

public class StatementParser extends Parser {

	public StatementParser(Lexer l) {
		super(l);

	}

	@Override
	public ParseTreeNode parse(Token t) throws IOException {
		
		ParseTreeNode p = new ParseTreeNode("STATEMENT");
		
		if (t.getType() == COBOLTokenType.MOVE) {			
			p.addChild(parseMoveStatement(t));
		} else

		if (t.getType() == COBOLTokenType.ADD) {			
			p.addChild(parseAddStatement(t));
		} else

		if (t.getType() == COBOLTokenType.IF) {			
			p.addChild(parseConditionalStatement(t));
		}
		return p;
	}

	public ParseTreeNode parseMoveStatement(Token t) throws IOException {

		ParseTreeNode p = new ParseTreeNode("MOVE STATEMENT");
		p.addChild(new ParseTreeNode(t.getTokenValue()));

		lexer.scan();
		t = lexer.getCurrentToken();

		if (t.getType() == COBOLTokenType.CORRESPONDING || t.getType() == COBOLTokenType.CORR) {
			p.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan();
			t = lexer.getCurrentToken();
		}

		
		if (t instanceof WordToken || t instanceof NumberToken) {
			p.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan();
			t = lexer.getCurrentToken();
		}
		

		if (t.getType() == COBOLTokenType.TO) {
			p.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan();
			t = lexer.getCurrentToken();
		}
		

		if (t.getType() == COBOLTokenType.IDENTIFIER) {
			
			while (t.getType() == COBOLTokenType.IDENTIFIER) {
				p.addChild(new ParseTreeNode(t.getTokenValue()));
				lexer.scan();
				t = lexer.getCurrentToken();
			}

		}

		return p;

	}

	public ParseTreeNode parseAddStatement(Token t) throws IOException {

		ParseTreeNode p = new ParseTreeNode("ADD STATEMENT");
		p.addChild(new ParseTreeNode(t.getTokenValue()));

		lexer.scan();
		t = lexer.getCurrentToken();

		if (t.getType() == COBOLTokenType.IDENTIFIER) {
			while (t.getType() == COBOLTokenType.IDENTIFIER) {
				p.addChild(new ParseTreeNode(t.getTokenValue()));
				lexer.scan();
				t = lexer.getCurrentToken();
			}
		}

		if (t.getType() == COBOLTokenType.TO)
			p.addChild(new ParseTreeNode(t.getTokenValue()));

		lexer.scan();
		t = lexer.getCurrentToken();

		if (t.getType() == COBOLTokenType.IDENTIFIER) {

			while (t.getType() == COBOLTokenType.IDENTIFIER) {
				p.addChild(new ParseTreeNode(t.getTokenValue()));
				lexer.scan();
				t = lexer.getCurrentToken();
				if (t.getType() == COBOLTokenType.ROUNDED) {
					p.addChild(new ParseTreeNode(t.getTokenValue()));
					lexer.scan();
					t = lexer.getCurrentToken();
				}
			}
		}
		
		return p;
	}

	public ParseTreeNode parseConditionalStatement(Token t) throws IOException {

		ParseTreeNode p = new ParseTreeNode("CONDITIONAL STATEMENT");
		p.addChild(new ParseTreeNode(t.getTokenValue()));

		lexer.scan();
		t = lexer.getCurrentToken();

		if (t.getType() == COBOLTokenType.IDENTIFIER || t instanceof NumberToken) {
			p.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan();
			t = lexer.getCurrentToken();
		}

		
		while (t.getType() != COBOLTokenType.IDENTIFIER && !(t instanceof NumberToken)) {
			switch ((COBOLTokenType) t.getType()) {
			case GREATER:
			case LESS:
			case THAN:
			case EQUAL:
			case OR:
			case TO:
			case IS:
			case GREATER_THAN:
			case LESS_THAN:
			case LESS_THAN_EQUALS:
			case GREATER_THAN_EQUALS:
			case EQUALS:
			case NOT:
				p.addChild(new ParseTreeNode(t.getTokenValue()));
				break;
			default:
				break;
			}

			lexer.scan();
			t = lexer.getCurrentToken();
		}

		p.addChild(new ParseTreeNode(t.getTokenValue()));
		lexer.scan();
		

		return p;

	}

}
