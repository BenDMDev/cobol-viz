package parser.COBOL;

import java.io.IOException;

import lexer.Lexer;
import lexer.tokens.COBOLTokenType;
import lexer.tokens.Token;
import parser.ParseTreeNode;
import parser.Parser;

public class StatementParser extends Parser {

	public StatementParser(Lexer l) {
		super(l);

	}

	@Override
	public ParseTreeNode parse() throws IOException {
		ParseTreeNode p = null;
		
		if (lexer.getCurrentToken().getType() == COBOLTokenType.MOVE) {
			p = new ParseTreeNode("MOVE STATEMENT");
			parseMoveStatement(p);
		} else

		if (lexer.getCurrentToken().getType() == COBOLTokenType.ADD) {
			p = new ParseTreeNode("ADD STATEMENT");
			parseAddStatement(p);
		} else

		if (lexer.getCurrentToken().getType() == COBOLTokenType.IF) {
			p = new ParseTreeNode("CONDITIONAL STATEMENT");
			parseConditionalStatement(p);
		}
		return p;
	}

	public ParseTreeNode parseMoveStatement(ParseTreeNode parentNode) throws IOException {

		Token t = lexer.getCurrentToken();
		parentNode.addChild(new ParseTreeNode(t.getTokenValue()));

		lexer.scan();
		t = lexer.getCurrentToken();

		if (t.getType() == COBOLTokenType.CORRESPONDING || t.getType() == COBOLTokenType.CORR) {
			parentNode.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan();
			t = lexer.getCurrentToken();
		}

		if (t.getType() == COBOLTokenType.IDENTIFIER) {
			parentNode.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan();
			t = lexer.getCurrentToken();
		}
		

		if (t.getType() == COBOLTokenType.TO) {
			parentNode.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan();
			t = lexer.getCurrentToken();
		}
		

		if (t.getType() == COBOLTokenType.IDENTIFIER) {
			
			while (t.getType() == COBOLTokenType.IDENTIFIER) {
				parentNode.addChild(new ParseTreeNode(t.getTokenValue()));
				lexer.scan();
				t = lexer.getCurrentToken();
			}

		}

		return parentNode;

	}

	public ParseTreeNode parseAddStatement(ParseTreeNode parentNode) throws IOException {

		Token t = lexer.getCurrentToken();
		parentNode.addChild(new ParseTreeNode(t.getTokenValue()));

		lexer.scan();
		t = lexer.getCurrentToken();

		if (t.getType() == COBOLTokenType.IDENTIFIER) {
			while (t.getType() == COBOLTokenType.IDENTIFIER) {
				parentNode.addChild(new ParseTreeNode(t.getTokenValue()));
				lexer.scan();
				t = lexer.getCurrentToken();
			}
		}

		if (t.getType() == COBOLTokenType.TO)
			parentNode.addChild(new ParseTreeNode(t.getTokenValue()));

		lexer.scan();
		t = lexer.getCurrentToken();

		if (t.getType() == COBOLTokenType.IDENTIFIER) {

			while (t.getType() == COBOLTokenType.IDENTIFIER) {
				parentNode.addChild(new ParseTreeNode(t.getTokenValue()));
				lexer.scan();
				t = lexer.getCurrentToken();
				if (t.getType() == COBOLTokenType.ROUNDED) {
					parentNode.addChild(new ParseTreeNode(t.getTokenValue()));
					lexer.scan();
					t = lexer.getCurrentToken();
				}
			}
		}
		
		return parentNode;
	}

	public ParseTreeNode parseConditionalStatement(ParseTreeNode parentNode) throws IOException {

		Token t = lexer.getCurrentToken();
		parentNode.addChild(new ParseTreeNode(t.getTokenValue()));

		lexer.scan();
		t = lexer.getCurrentToken();

		if (t.getType() == COBOLTokenType.IDENTIFIER) {
			parentNode.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan();
			t = lexer.getCurrentToken();
		}

		COBOLTokenType type = (COBOLTokenType) t.getType();
		while (t.getType() != COBOLTokenType.IDENTIFIER) {
			switch (type) {
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
				parentNode.addChild(new ParseTreeNode(t.getTokenValue()));
				break;
			default:
				break;
			}

			lexer.scan();
			t = lexer.getCurrentToken();
		}

		parentNode.addChild(new ParseTreeNode(t.getTokenValue()));
		lexer.scan();

		return parentNode;

	}

}
