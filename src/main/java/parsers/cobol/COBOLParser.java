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

	public ParseTreeNode parse(Token t) throws IOException {

		long startTime = System.currentTimeMillis();

		switch ((COBOLTokenType) t.getType()) {
		case IDENTIFICATION:
			break;
		case ENVIRONMENT:
			break;
		case DATA:
			break;
		case PROCEDURE:

			parseTree = new ProgramNode(TreeNodeType.PROGRAM, "PROCEDURE DIVISION");

		
				match(t, COBOLTokenType.PROCEDURE, parseTree);

				t = scanner.getCurrentToken();
				match(t, COBOLTokenType.DIVISION, parseTree);

				t = scanner.getCurrentToken();
				match(t, COBOLTokenType.FULL_STOP, parseTree);

				SectionParser sp = new SectionParser(scanner);
				sp.addListener(listener);
				t = scanner.getCurrentToken();
				while (t.getType() != COBOLTokenType.EOF) {
					parseTree.addChild(sp.parse(t));
					t = scanner.getCurrentToken();
				}

				if (t instanceof EOFToken) {
					if (listener != null) {
						sendMessage("PARSE SUCCESS");
						float timeTaken = (System.currentTimeMillis() - startTime) / 1000f;
						sendMessage("PARSE TIME: " + timeTaken);
					}

				}
			

			break;
		default:
			break;
		}
		return parseTree;

	}

	

}
