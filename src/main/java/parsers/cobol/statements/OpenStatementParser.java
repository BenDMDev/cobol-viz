package main.java.parsers.cobol.statements;

import java.io.IOException;

import main.java.parsers.cobol.StatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.TokenType;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.StatementNode;

/**
 * Parser for Open Statement
 * @author Ben
 *
 */
public class OpenStatementParser extends StatementParser {

	public OpenStatementParser(Scanner scanner) {
		super(scanner);

	}

	public ParseTreeNode parse(Token inputToken) throws IOException {
		parseTree = new StatementNode(TreeNodeType.STATEMENT, inputToken.getTokenValue());

		parseOpenClause(inputToken);
		inputToken = scanner.getCurrentToken();
		
		// Match and consume repeation Open clauses
		TokenType type = inputToken.getType();
		while(type == COBOLTokenType.INPUT || type == COBOLTokenType.OUTPUT || type == COBOLTokenType.I_O || type == COBOLTokenType.EXTEND)
		{
			parseOpenClause(inputToken);
			inputToken = scanner.getCurrentToken();
			type = inputToken.getType();
		}
		
		

		return parseTree;

	}
	
	public void parseOpenClause(Token inputToken) throws IOException {
		
		// Match and consume Open
		match(inputToken, COBOLTokenType.OPEN, parseTree);
		inputToken = scanner.getCurrentToken();

		// Match and consume Open type
		matchAlternation(inputToken, parseTree, COBOLTokenType.INPUT, COBOLTokenType.OUTPUT, COBOLTokenType.I_O,
				COBOLTokenType.EXTEND);
		inputToken = scanner.getCurrentToken();
		
		// Match and consume Identifier
		matchRepetition(inputToken, parseTree, COBOLTokenType.IDENTIFIER);
		inputToken = scanner.getCurrentToken();
	}

}
