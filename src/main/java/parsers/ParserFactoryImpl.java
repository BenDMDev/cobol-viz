package main.java.parsers;

import main.java.parsers.cobol.ConditionalParser;
import main.java.parsers.cobol.ExpressionParser;
import main.java.parsers.cobol.statements.AcceptStatementParser;
import main.java.parsers.cobol.statements.AlterStatementParser;
import main.java.parsers.cobol.statements.CallStatementParser;
import main.java.parsers.cobol.statements.CancelStatementParser;
import main.java.parsers.cobol.statements.CloseStatementParser;
import main.java.parsers.cobol.statements.ContinueStatementParser;
import main.java.parsers.cobol.statements.CopyStatementParser;
import main.java.parsers.cobol.statements.DeleteStatementParser;
import main.java.parsers.cobol.statements.DisplayStatementParser;
import main.java.parsers.cobol.statements.ExitStatementParser;
import main.java.parsers.cobol.statements.GoToStatementParser;
import main.java.parsers.cobol.statements.InitializeStatementParser;
import main.java.parsers.cobol.statements.InspectStatementParser;
import main.java.parsers.cobol.statements.MoveStatementParser;
import main.java.parsers.cobol.statements.OpenStatementParser;
import main.java.parsers.cobol.statements.PerformStatementParser;
import main.java.parsers.cobol.statements.ReadStatementParser;
import main.java.parsers.cobol.statements.ReleaseStatementParser;
import main.java.parsers.cobol.statements.ReturnStatementParser;
import main.java.parsers.cobol.statements.RewriteStatementParser;
import main.java.parsers.cobol.statements.SetStatementParser;
import main.java.parsers.cobol.statements.SortStatementParser;
import main.java.parsers.cobol.statements.StartStatementParser;
import main.java.parsers.cobol.statements.StopStatementParser;
import main.java.parsers.cobol.statements.StringStatementParser;
import main.java.parsers.cobol.statements.UnstringStatementParser;
import main.java.parsers.cobol.statements.WriteStatementParser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;

public class ParserFactoryImpl implements ParserFactory {

	@Override
	public Parser createParser(Token inputToken, Scanner scanner) {
		
		switch ((COBOLTokenType) inputToken.getType()) {
		case MOVE:
			return new MoveStatementParser(scanner);		
		case ADD:
		case SUBTRACT:
		case DIVIDE:
		case MULTIPLY:
		case COMPUTE:
			return new ExpressionParser(scanner);		
		case IF:
			return new ConditionalParser(scanner);		
		case PERFORM:
			return new PerformStatementParser(scanner);		
		case ACCEPT:
			return new AcceptStatementParser(scanner);		
		case ALTER:
			return new AlterStatementParser(scanner);
		case CALL:
			return new CallStatementParser(scanner);
		case CLOSE:
			return new CloseStatementParser(scanner);
		case CANCEL:
			return new CancelStatementParser(scanner);
		case CONTINUE:
			return new ContinueStatementParser(scanner);
		case COPY:
			return new CopyStatementParser(scanner);
		case DELETE:
			return new DeleteStatementParser(scanner);
		case DISPLAY:
			return new DisplayStatementParser(scanner);
		case EXIT:
			return new ExitStatementParser(scanner);
		case GO:
			return new GoToStatementParser(scanner);
		case INITIALIZE:
			return new InitializeStatementParser(scanner);
		case INSPECT:
			return new InspectStatementParser(scanner);
		case OPEN:
			return new OpenStatementParser(scanner);
		case RELEASE:
			return new ReleaseStatementParser(scanner);
		case READ:
			return new ReadStatementParser(scanner);
		case RETURN:
			return new ReturnStatementParser(scanner);
		case REWRITE:
			return new RewriteStatementParser(scanner);
		case SET:
			return new SetStatementParser(scanner);
		case SORT:
			return new SortStatementParser(scanner);
		case START:
			return new StartStatementParser(scanner);
		case STOP:
			return new StopStatementParser(scanner);
		case STRING:
			return new StringStatementParser(scanner);
		case UNSTRING:
			return new UnstringStatementParser(scanner);
		case WRITE:
			return new WriteStatementParser(scanner);
		default:
			return null;
		}
		
		
	}

}
