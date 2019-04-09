package parser;

import java.io.IOException;

import lexer.Lexer;

public abstract class Parser {

	protected Lexer lexer;
	protected ParseTreeNode parseTree;

	public Parser(Lexer l) {
		lexer = l;
		parseTree = null;
	}
	
	public abstract ParseTreeNode parse() throws IOException;
	
	public void printParseTree(ParseTreeNode p) {
		
		System.out.println(p.getType());
		for(ParseTreeNode c : p.getChildren()) {
			printParseTree(c);
		}
		
	}
	
}
