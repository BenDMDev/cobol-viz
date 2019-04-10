package lexer.tokens;

import java.io.IOException;

import lexer.SourceFile;

public class SymbolToken extends Token {

	public SymbolToken(SourceFile source) {
		super(source);

	}

	public void extract() throws IOException {

		StringBuilder s = new StringBuilder();
		char c = source.getCurrentChar();

		switch (c) {
		case '.':
		case '+':
		case '-':
		case '/':
		case '=': {
			s.append(c);
			this.type = COBOLTokenType.SPECIAL_SYMBOLS.get(s.toString());
			source.nextChar();
			break;
		}
		case '>': {
			s.append(c);
			if (source.peek() == '=') {
				s.append(source.nextChar());
			}
			this.type = COBOLTokenType.SPECIAL_SYMBOLS.get(s.toString());
			break;

		}
		case '<': {
			s.append(c);
			if (source.peek() == '=') {
				s.append(source.nextChar());
			}
			this.type = COBOLTokenType.SPECIAL_SYMBOLS.get(s.toString());
			break;
		}
		case '*': {
			s.append(c);
			if (source.peek() == '*') {
				s.append(source.nextChar());
			}
			this.type = COBOLTokenType.SPECIAL_SYMBOLS.get(s.toString());
			break;
		}
		}

		this.value = s.toString();
		source.nextChar();
	}

}
