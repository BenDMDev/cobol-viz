package main.java.parsers;

import java.io.IOException;
import main.java.messages.MessageListener;
import main.java.messages.MessageEmitter;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.TokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeFactory;
import main.java.trees.TreeNodeFactoryImpl;

public abstract class Parser implements MessageEmitter {

	protected Scanner scanner;
	protected ParseTreeNode parseTree;
	protected MessageListener listener;
	protected TreeNodeFactory treeNodeFactory;
	protected ParserFactory parserFactory;
	

	public Parser(Scanner scanner) {
		this.scanner = scanner;
		parseTree = null;
		treeNodeFactory = new TreeNodeFactoryImpl();
		parserFactory = new ParserFactoryImpl();
		
	}

	public abstract ParseTreeNode parse(Token t) throws IOException;


	/**
	 * 
	 * @param input
	 * @param expected
	 * @param p
	 * @throws IOException
	 */
	public void match(Token input, TokenType expected, ParseTreeNode p) throws IOException {

		if (input.getType() == expected) {			
			ParseTreeNode node = treeNodeFactory.createTreeNode(input);
			p.addChild(node);
			scanner.scan();			
		}

	}



	/**
	 * 
	 * @param input
	 * @param p
	 * @param tokenTypes
	 * @throws IOException
	 */
	public void matchAlternation(Token input, ParseTreeNode p, TokenType... tokenTypes)
			throws IOException {

				
		for (TokenType t : tokenTypes) {
			if (input.getType() == t) {
				match(input, t, p);
			}
		}
		
	}
	
	public void matchSequence(Token input, ParseTreeNode p, TokenType... tokenTypes) throws IOException {
		for (TokenType type : tokenTypes) {
			match(input, type, p);		
			input = scanner.getCurrentToken();
		}
	}
	
	public void matchRepetition(Token input, ParseTreeNode p, TokenType type) throws IOException {
		while(input.getType() == type) {
			match(input, type, p);
			input = scanner.getCurrentToken();
		}
	}
	

	public void matchRepeatingAlternation(Token input, ParseTreeNode p, TokenType... tokenTypes) throws IOException {
		while(containsToken(input, tokenTypes)) {		
			matchAlternation(input, p, tokenTypes);
			input = scanner.getCurrentToken();
		}
	}
	
	public void matchRepeatingSequence(Token input, ParseTreeNode p, TokenType... tokenTypes) throws IOException {
		while(containsToken(input, tokenTypes)) {
			matchSequence(input, p, tokenTypes);
			input = scanner.getCurrentToken();
		}
	}
	
	
	private boolean containsToken(Token input, TokenType[] types) {
		boolean contains = false;
		for(TokenType t : types) {
			if(input.getType() == t)
				contains = true;
		}		
		return contains;
	}

	@Override
	public void addListener(MessageListener listener) {
		this.listener = listener;
		
	}

	@Override
	public void removeListener(MessageListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(String message) {
		if(listener != null)
			listener.listen(message);
		else 
			System.out.println(message);
		
	}

}
