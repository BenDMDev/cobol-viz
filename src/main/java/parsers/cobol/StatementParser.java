package main.java.parsers.cobol;

import java.io.IOException;
import java.util.HashSet;

import main.java.parsers.Parser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.TokenType;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;

public class StatementParser extends Parser {

	protected HashSet<TokenType> statementPrefixes;

	public StatementParser(Scanner scanner) {
		super(scanner);

		statementPrefixes = new HashSet<TokenType>();

		statementPrefixes.add(COBOLTokenType.MOVE);
		statementPrefixes.add(COBOLTokenType.ADD);
		statementPrefixes.add(COBOLTokenType.IF);
	}

	@Override
	public ParseTreeNode parse(Token inputToken) throws IOException {

		switch ((COBOLTokenType) inputToken.getType()) {
		case MOVE:
			MoveStatementParser moveParser = new MoveStatementParser(scanner);
			parseTree = moveParser.parse(inputToken);
			break;
		case ADD:
		case SUBTRACT:
		case DIVIDE:
		case MULTIPLY:
			ExpressionParser expression = new ExpressionParser(scanner);
			parseTree = expression.parse(inputToken);
			break;
		case IF:
			ConditionalParser conditional = new ConditionalParser(scanner);
			parseTree = conditional.parse(inputToken);
			break;
		default:
			break;
		}

		return parseTree;
	}

}
