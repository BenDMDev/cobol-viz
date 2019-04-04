package parser;

import java.io.IOException;

import lexer.Lexer;
import lexer.Token;
import output.GraphPrinter;

public class Parser {

	private Lexer lexer;
	private GraphPrinter gPrinter;
	private ParseTreeNode parseTree;
	
	public Parser(Lexer l) {
		lexer = l;
		parseTree = null;
	}
	
	
	public ParseTreeNode parse() throws IOException {
		parseProgram();
		return parseTree;
	}
	
	private void parseProgram() throws IOException {
		parseTree = new ParseTreeNode("PROGRAM");
		parseIdentificationDivision(parseTree);
		parseProcedureDivision(parseTree);
	}
	
	private void parseIdentificationDivision(ParseTreeNode p) throws IOException {
		ParseTreeNode p1 = new ParseTreeNode("RULE: IDENTIFICATION DIVISION");
		p.addChild(p1);
		Token t;
		lexer.scan();
		t = lexer.getCurrentToken();
		if(t.getTokenValue().equals("IDENTIFICATION") || t.getTokenValue().equals("ID")) {
			p1.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan();
			t = lexer.getCurrentToken();
			if(t.getTokenValue().equals("DIVISION"))
				p1.addChild(new ParseTreeNode(t.getTokenValue()));			
			parseProgramID(p1);
			
		} else {
			System.out.println("Invalid Token");
		}
		
	}
	
	private void parseProgramID(ParseTreeNode p) throws IOException {
		Token t;
		lexer.scan();
		t = lexer.getCurrentToken();
		ParseTreeNode p1 = new ParseTreeNode("RULE: PROGRAM-ID");
		p.addChild(p1);
		if(t.getTokenValue().equals("PROGRAM-ID"))
			p1.addChild(new ParseTreeNode("PROGRAM-ID"));
		lexer.scan();
		t = lexer.getCurrentToken();
		if(t.getType().equals("WORD"))
			p1.addChild(new ParseTreeNode(t.getTokenValue()));			

	}
	
	
	private void parseProcedureDivision(ParseTreeNode p) throws IOException {
		lexer.scan();
		Token t;
		t = lexer.getCurrentToken();
		ParseTreeNode p1 = new ParseTreeNode("RULE: Procedure Division");
		p.addChild(p1);
		if(t.getTokenValue().equals("PROCEDURE")) {
			p1.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan();
			t = lexer.getCurrentToken();
			if(t.getTokenValue().equals("DIVISION")) {
				p1.addChild(new ParseTreeNode(t.getTokenValue()));
				parseParagraph(p1);
			}
		} else {
			System.out.println("PROCEDURE DIVISION ERROR");
		
		}
		
		
	}
	
	private void parseParagraph(ParseTreeNode p) throws IOException {
		ParseTreeNode p1 = new ParseTreeNode("RULE: Paragraph");
		p.addChild(p1);
		parseSentence(p1);
	}
	
	private void parseSentence(ParseTreeNode p) throws IOException {
		ParseTreeNode p1 = new ParseTreeNode("RULE: Sentence");
		p.addChild(p1);
		parseStatementList(p1);
	}
	
	private void parseStatementList(ParseTreeNode p) throws IOException {
		ParseTreeNode p1 = new ParseTreeNode("RULE: Statement List");
		p.addChild(p1);
		lexer.scan();		
		while(!lexer.getCurrentToken().getTokenValue().equals("END")) {
			parseStatement(p1);
		}
		}
		
	private void parseStatement(ParseTreeNode p) throws IOException { 
		ParseTreeNode p1 = new ParseTreeNode("RULE: Statement");
		p.addChild(p1);
		Token t = lexer.getCurrentToken();
		if(t.getTokenValue().equals("MOVE")) {
			p1.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan();			
		}
		
		t = lexer.getCurrentToken();
		
		if(t.getType().equals("WORD") || t.getType().equals("DIGIT")) {
			p1.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan();
		} 
		
		t = lexer.getCurrentToken();
		if(t.getTokenValue().equals("TO")) {
			p1.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan();
		}
		
		t = lexer.getCurrentToken();
		if(t.getType().equals("WORD")){
			p1.addChild(new ParseTreeNode(t.getTokenValue()));
			lexer.scan();
		}
	}
	
	
	
}
