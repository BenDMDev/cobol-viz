package main.java.parsers;

import java.io.IOException;

import main.java.messages.MessageListener;
import main.java.messages.MessageEmitter;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.TokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public abstract class Parser implements MessageEmitter {

	protected Scanner scanner;
	protected ParseTreeNode parseTree;
	protected MessageListener listener;
	

	public Parser(Scanner scanner) {
		this.scanner = scanner;
		parseTree = null;
		
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
			p.addChild(new ParseTreeNode(input.getTokenValue()));
			scanner.scan();		
			
		}

	}

	/**
	 * 
	 * @param input
	 * @param expected
	 * @param p
	 * @param treeType
	 * @throws IOException
	 */
	public void match(Token input, TokenType expected, ParseTreeNode p, TreeNodeType treeType) throws IOException {

		if (input.getType() == expected) {
			p.addChild(new ParseTreeNode(treeType, input.getTokenValue()));
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
	public void matchList(Token input, ParseTreeNode p, TokenType... tokenTypes) throws IOException {

		boolean found = false;
		for (TokenType t : tokenTypes) {

			if (input.getType() == t)
				found = true;
		}
		if (found) {
			p.addChild(new ParseTreeNode(input.getTokenValue()));
			scanner.scan();
		}
	}

	/**
	 * 
	 * @param input
	 * @param treeType
	 * @param p
	 * @param tokenTypes
	 * @throws IOException
	 */
	public void matchList(Token input, TreeNodeType treeType, ParseTreeNode p, TokenType... tokenTypes)
			throws IOException {

		boolean found = false;
		for (TokenType t : tokenTypes) {

			if (input.getType() == t)
				found = true;
		}
		if (found) {
			p.addChild(new ParseTreeNode(treeType, input.getTokenValue()));
			scanner.scan();
		}
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
		listener.listen(message);
		
	}

}
