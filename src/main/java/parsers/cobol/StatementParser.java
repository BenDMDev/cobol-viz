package main.java.parsers.cobol;

import java.io.IOException;
import main.java.parsers.Parser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;

public class StatementParser extends Parser {

	
	public StatementParser(Scanner scanner) {
		super(scanner);
		
	}

	@Override
	public ParseTreeNode parse(Token inputToken) throws IOException {
		int lineNumber = inputToken.getLineNumber();
		
		Parser parser = parserFactory.createParser(inputToken, scanner);
		
	

		if (parser == null) {
			sendMessage("UNEXPECTED TOKEN:" + inputToken.getTokenValue() + " AT LINE: " + inputToken.getLineNumber());
			parseTree = new ParseTreeNode(TreeNodeType.ERROR, "ERROR NODE");
			findNextValidToken(inputToken);
		} else {
			parser.addListener(listener);
			parseTree = parser.parse(inputToken);
			parseTree.setLineNumber(lineNumber);
		}
			
		
		
		return parseTree;
	}

	private void findNextValidToken(Token inputToken) throws IOException {
		while (!COBOLTokenType.STATEMENT_PREFIXES.contains(inputToken.getTokenValue())
				&& inputToken.getType() != COBOLTokenType.FULL_STOP) {
			scanner.scan();
			inputToken = scanner.getCurrentToken();
		}
	}

}
