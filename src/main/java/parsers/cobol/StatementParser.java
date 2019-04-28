package main.java.parsers.cobol;

import java.io.IOException;
import main.java.parsers.Parser;
import main.java.parsers.cobol.statements.AcceptStatementParser;
import main.java.parsers.cobol.statements.AlterStatementParser;
import main.java.parsers.cobol.statements.CallStatementParser;
import main.java.parsers.cobol.statements.CancelStatementParser;
import main.java.parsers.cobol.statements.CloseStatementParser;
import main.java.parsers.cobol.statements.MoveStatementParser;
import main.java.parsers.cobol.statements.PerformStatementParser;
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
		switch ((COBOLTokenType) inputToken.getType()) {
		case MOVE:
			MoveStatementParser moveParser = new MoveStatementParser(scanner);
			moveParser.addListener(listener);
			parseTree = moveParser.parse(inputToken);
			break;
		case ADD:
		case SUBTRACT:
		case DIVIDE:
		case MULTIPLY:
		case COMPUTE:
			ExpressionParser expression = new ExpressionParser(scanner);
			expression.addListener(listener);
			parseTree = expression.parse(inputToken);
			break;
		case IF:
			ConditionalParser conditional = new ConditionalParser(scanner);
			conditional.addListener(listener);
			parseTree = conditional.parse(inputToken);
			break;
		case PERFORM:
			PerformStatementParser perform = new PerformStatementParser(scanner);
			perform.addListener(listener);
			parseTree = perform.parse(inputToken);
			break;
		case ACCEPT:
			AcceptStatementParser accept = new AcceptStatementParser(scanner);
			accept.addListener(listener);
			parseTree = accept.parse(inputToken);
			break;
		case ALTER:
			AlterStatementParser alter = new AlterStatementParser(scanner);
			alter.addListener(listener);
			parseTree = alter.parse(inputToken);
			break;
		case CALL:
			CallStatementParser call = new CallStatementParser(scanner);
			call.addListener(listener);
			parseTree = call.parse(inputToken);
			break;
		case CLOSE:
			CloseStatementParser close = new CloseStatementParser(scanner);
			close.addListener(listener);
			parseTree = close.parse(inputToken);
			break;
		case CANCEL:
			CancelStatementParser cancel = new CancelStatementParser(scanner);
			cancel.addListener(listener);
			break;
		default:
			parseTree = new ParseTreeNode(TreeNodeType.ERROR, inputToken.getTokenValue());
			break;
		}

	

		if (parseTree.getTreeNodeType() == TreeNodeType.ERROR) {
			sendMessage("MISSING STATEMENT:" + parseTree.getAttribute() + " AT LINE: " + inputToken.getLineNumber());
			findNextValidToken(inputToken);
		}

		parseTree.setLineNumber(lineNumber);
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
