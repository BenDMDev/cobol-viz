package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

/**
 * Parser for String Statement
 * @author Ben
 *
 */
public class StringStatementParser extends StatementParser {

	public StringStatementParser(Scanner scanner) {
		super(scanner);
		
	}
	
	
	public ParseTreeNode parse(Token inputToken) throws IOException {
		
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());
		
		// Match and consume String
		match(inputToken, COBOLTokenType.STRING, parseTree);
		inputToken = scanner.getCurrentToken();
		
		// Match and consume type
		matchRepeatingAlternation(inputToken, parseTree, COBOLTokenType.IDENTIFIER, COBOLTokenType.STRING_LITERAL, COBOLTokenType.INTEGER,
				COBOLTokenType.REAL, COBOLTokenType.DELIMITED, COBOLTokenType.BY, COBOLTokenType.SIZE, COBOLTokenType.SPACE);
		
		inputToken = scanner.getCurrentToken();
		
		// Match INTO Identifier
		matchSequence(inputToken, parseTree, COBOLTokenType.INTO, COBOLTokenType.IDENTIFIER);		
		inputToken = scanner.getCurrentToken();
		
		// Match WITH POINTER Identifier
		matchSequence(inputToken, parseTree, COBOLTokenType.WITH, COBOLTokenType.POINTER, COBOLTokenType.IDENTIFIER);		
		inputToken = scanner.getCurrentToken();
		
		// Check for On Overflow Erro
		if(inputToken.getType() == COBOLTokenType.ON || scanner.lookAhead().getType() == COBOLTokenType.OVERFLOW) {
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
			parseOverflowError(inputToken);
			inputToken = scanner.getCurrentToken();
		}
		
		// Check for Not Overflow Error 
		if(inputToken.getType() == COBOLTokenType.NOT) {
			parseTree.setTreeType(TreeNodeType.COMPOUND_STATEMENT);
			parseOverflowError(inputToken);
			inputToken = scanner.getCurrentToken();
		}
		
		// Match and consume closing tag
		match(inputToken, COBOLTokenType.END_STRING, parseTree);
		
		return parseTree;
	}
	
	
	private void parseOverflowError(Token inputToken) throws IOException {
		
		// Treat error as conditional statement
		StatementNode conditional = new StatementNode(TreeNodeType.CONDITIONAL_STATEMENT, inputToken.getTokenValue());
		ParseTreeNode condition = new ParseTreeNode(TreeNodeType.CONDITION, inputToken.getTokenValue());
		conditional.addChild(condition);
		
		// Match condition
		matchSequence(inputToken, condition, COBOLTokenType.NOT, COBOLTokenType.ON, COBOLTokenType.OVERFLOW);
		inputToken = scanner.getCurrentToken();
		
		// handle condition statements
		ParseTreeNode onOverflowBody = new ParseTreeNode(TreeNodeType.CONDITION_BODY, "CONDITION BODY");
		conditional.addChild(onOverflowBody);
		if (COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue().toLowerCase())) {
			StatementParser statementParser = new StatementParser(scanner);
			statementParser.addListener(listener);
			onOverflowBody.addChild(statementParser.parse(inputToken));
			inputToken = scanner.getCurrentToken();
		}
		
		parseTree.addChild(conditional);
		
	}

	
	
}
