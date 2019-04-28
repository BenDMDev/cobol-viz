package main.java.parsers.cobol;

import java.io.IOException;
import main.java.parsers.Parser;
import main.java.parsers.cobol.statements.AcceptStatementParser;
import main.java.parsers.cobol.statements.AlterStatementParser;
import main.java.parsers.cobol.statements.CallStatementParser;
import main.java.parsers.cobol.statements.MoveStatementParser;
import main.java.parsers.cobol.statements.PerformStatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.trees.ParseTreeNode;

public class StatementParser extends Parser {

	

	public StatementParser(Scanner scanner) {
		super(scanner);	
	}

	@Override
	public ParseTreeNode parse(Token inputToken) throws IOException {
		int lineNumber = inputToken.getLineNumber();
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
		case PERFORM:
			PerformStatementParser perform = new PerformStatementParser(scanner);
			parseTree = perform.parse(inputToken);
			break;
		case ACCEPT:
			AcceptStatementParser accept = new AcceptStatementParser(scanner);
			parseTree = accept.parse(inputToken);
			break;
		case ALTER:
			AlterStatementParser alter = new AlterStatementParser(scanner);
			parseTree = alter.parse(inputToken);
			break;
		case CALL:
			CallStatementParser call = new CallStatementParser(scanner);
			parseTree = call.parse(inputToken);
		default:
			break;
		}
		parseTree.setLineNumber(lineNumber);
		return parseTree;
	}

}
