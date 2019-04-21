package main.java.parsers;

import java.io.IOException;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.TokenType;
import main.java.trees.ParseTreeNode;

public abstract class Parser {

	protected Scanner scanner;
	protected ParseTreeNode parseTree;

	public Parser(Scanner scanner) {
		this.scanner = scanner;
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
			scanner.scan();
		} 
		
	}
	
	public void matchList(Token input, ParseTreeNode p, TokenType... tokenTypes) throws IOException {
		
		boolean found = false;
		for(TokenType t : tokenTypes) {
			
			if(input.getType() == t)
				found = true;			
		}
		if(found) {
			p.addChild(new ParseTreeNode(input.getTokenValue()));
			scanner.scan();
		}
	}
	
}
