package main.java.parsers.cobol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import main.java.parsers.Parser;
import main.java.scanners.Scanner;
import main.java.scanners.tokens.Token;
import main.java.scanners.tokens.cobol.COBOLTokenType;
import main.java.scanners.tokens.cobol.EOFToken;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.ProgramNode;

public class COBOLParser extends Parser {

	public static ArrayList<String> REFERENCES = new ArrayList<String>();
	public static HashMap<String, String> SECTION_REFERENCES = new HashMap<String, String>();

	public COBOLParser(Scanner scanner) {
		super(scanner);
	}

	public ParseTreeNode parse(Token inputToken) throws IOException {

		long startTime = System.currentTimeMillis();

		switch ((COBOLTokenType) inputToken.getType()) {
		case IDENTIFICATION:
			break;
		case ENVIRONMENT:
			break;
		case DATA:
			break;
		case PROCEDURE:

			parseTree = new ProgramNode(TreeNodeType.PROGRAM, "PROCEDURE DIVISION");

		
				match(inputToken, COBOLTokenType.PROCEDURE, parseTree);

				inputToken = scanner.getCurrentToken();
				match(inputToken, COBOLTokenType.DIVISION, parseTree);

				inputToken = scanner.getCurrentToken();
				match(inputToken, COBOLTokenType.FULL_STOP, parseTree);

				SectionParser sp = new SectionParser(scanner);
				sp.addListener(listener);
				inputToken = scanner.getCurrentToken();
				while (inputToken.getType() != COBOLTokenType.EOF) {
					parseTree.addChild(sp.parse(inputToken));
					inputToken = scanner.getCurrentToken();
				}

				if (inputToken instanceof EOFToken) {
					if (listener != null) {
						sendMessage("Parse Complete");
						float timeTaken = (System.currentTimeMillis() - startTime) / 1000f;
						sendMessage("Time Taken: " + timeTaken);
					}

				}
			

			break;
		default:
			break;
		}
		return parseTree;

	}

	

}
