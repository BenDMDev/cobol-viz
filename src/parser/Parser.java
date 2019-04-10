package parser;

import java.io.IOException;

import lexer.Lexer;
import lexer.tokens.Token;

public abstract class Parser {

	protected Lexer lexer;
	protected ParseTreeNode parseTree;

	public Parser(Lexer l) {
		lexer = l;
		parseTree = null;
	}
	
	public abstract ParseTreeNode parse(Token t) throws IOException;
	
	public void printParseTree(ParseTreeNode p) {
		
		System.out.println(p.getType());
		for(ParseTreeNode c : p.getChildren()) {
			printParseTree(c);
		}
		
	}
	
}
