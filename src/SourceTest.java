import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class SourceTest {

	@Test
	public void testGetNextChar() {
		BufferedReader in = new BufferedReader(new StringReader("Hello"));
		Source s = new Source(in);
		try {
			assertEquals('H', s.getNextChar());
			assertEquals('e', s.getNextChar());
			assertEquals('l', s.getNextChar());
			assertEquals('l', s.getNextChar());
			assertEquals('o', s.getNextChar());
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}

}
