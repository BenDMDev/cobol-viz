package parser.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import parser.ParseTreeNode;

public class ParseTreeNodeTest {

	@Test
	public void testTraverse() {
		ParseTreeNode t1 = new ParseTreeNode("PROGRAM");
		ParseTreeNode c1 = new ParseTreeNode("IDENTIFICATION-RULE");
		ParseTreeNode gc1 = new ParseTreeNode("IDENTIFICATION");
		ParseTreeNode gc2 = new ParseTreeNode("DIVISION");
		c1.addChild(gc1);
		c1.addChild(gc2);
		ParseTreeNode c2 = new ParseTreeNode("PROGRAM-ID-RULE");
		ParseTreeNode gc21 = new ParseTreeNode("PROGRAM-ID");
		ParseTreeNode gc22 = new ParseTreeNode("test");
		c2.addChild(gc21);
		c2.addChild(gc22);
		t1.addChild(c1);
		t1.addChild(c2);
		
		
	}
	
	@Test
	public void testIsEmpty() {
		
		
	}

}
