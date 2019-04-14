package main.java.parsers;

import java.io.IOException;
import java.util.Set;

import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.TokenType;
import main.java.trees.ParseTreeNode;

public abstract class Parser {

	protected Scanner lexer;
	protected ParseTreeNode parseTree;

	public Parser(Scanner l) {
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
	
	public void match(Token input, TokenType expected, ParseTreeNode p) throws IOException {
		
		if(input.getType() == expected) {
			p.addChild(new ParseTreeNode(input.getTokenValue()));
			lexer.scan();
		} 
		
	}
	
	public void matchList(Token input, Set<TokenType> expected, ParseTreeNode p) throws IOException {
		
		if(expected.contains(input.getType())) {
			p.addChild(new ParseTreeNode(input.getTokenValue()));
			lexer.scan();
		}
	}
	
}
