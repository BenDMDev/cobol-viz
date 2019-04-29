package main.java.parsers;

import main.java.parsers.Parser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;

public interface ParserFactory {

	Parser createParser(Token inputToken,  Scanner scanner);
}
