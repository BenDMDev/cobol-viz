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

/**
 * Base class for parse
 * Includes helper methods to match tokens
 * @author Ben
 *
 */
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
	 * Match single input with expected and add to parse tree
	 * Simply adds token to tree and consumes
	 * Does nothing if no match
	 * @param input token to be checked
	 * @param expected the expected token 
	 * @param p the parse tree to add this token to, if matched
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
	 * Match input against list of alternate tokens and adds to tree if matched
	 * Using varargs as could be maybe alternatives
	 * Adds token to tree and consumes if match
	 * Does nothing if no match
	 * @param input token to be checked
	 * @param p Parse tree to add to if matched
	 * @param tokenTypes varargs of token types to check against
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
	
	/**
	 * Matches a sequence of tokens, starting from the first token provided
	 * Calls Match (which consumes the current token) as it looks through sequence
	 * @param input first token in the sequence currently in the scanner
	 * @param p parse tree to add matched tokens to
	 * @param tokenTypes varargs of sequence
	 * @throws IOException
	 */
	public void matchSequence(Token input, ParseTreeNode p, TokenType... tokenTypes) throws IOException {
		for (TokenType type : tokenTypes) {
			match(input, type, p);		
			input = scanner.getCurrentToken();
		}
	}
	
	/**
	 * Matches repetition of a single token type
	 * Will continue until token does not match type
	 * @param input input token to be checked
	 * @param p parse tree to add to if matched
	 * @param type repeating type to be checked against
	 * @throws IOException
	 */
	public void matchRepetition(Token input, ParseTreeNode p, TokenType type) throws IOException {
		while(input.getType() == type) {
			match(input, type, p);
			input = scanner.getCurrentToken();
		}
	}
	

	/**
	 * Matches a repeating alternation of token types i.e (IDENTIFIER | INTEGER)+
	 * Starts with the input token and consumes as it repeats
	 * @param input first token to be checked
	 * @param p parse tree to add to
	 * @param tokenTypes varargs of token types
	 * @throws IOException
	 */
	public void matchRepeatingAlternation(Token input, ParseTreeNode p, TokenType... tokenTypes) throws IOException {
		while(containsToken(input, tokenTypes)) {		
			matchAlternation(input, p, tokenTypes);
			input = scanner.getCurrentToken();
		}
	}
	
	/**
	 * Matches a repeating sequence of token types i.e (IDENTIFIER ROUNDED)+
	 * @param input the first token to be checked
	 * @param p parse tree to add to
	 * @param tokenTypes varages of token types
	 * @throws IOException
	 */
	public void matchRepeatingSequence(Token input, ParseTreeNode p, TokenType... tokenTypes) throws IOException {
		while(containsToken(input, tokenTypes)) {
			matchSequence(input, p, tokenTypes);
			input = scanner.getCurrentToken();
		}
	}
	
	/**
	 * Helper method to clean up IF statements when using many alternations 
	 * @param input Token to be checked
	 * @param types array of types to be checked against
	 * @return true if any match
	 */
	protected boolean containsToken(Token input, TokenType[] types) {
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
	
	@Override
	public void sendMessage(float x, float y) {
		// TODO Auto-generated method stub
		
	}

}
