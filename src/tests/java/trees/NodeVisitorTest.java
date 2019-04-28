package tests.java.trees;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.trees.ParseTreeNode;
import main.java.trees.cobol.ParagraphNode;
import main.java.trees.cobol.SectionNode;
import main.java.trees.cobol.SentenceNode;
import main.java.trees.cobol.StatementNode;
import main.java.trees.visitors.cobol.CallGraphVisitor;

public class NodeVisitorTest {

	@Test
	public void test() {
		ParseTreeNode root = new ParseTreeNode("ROOT");
		root.addChild(new StatementNode("STATEMENT"));
		root.addChild(new SectionNode("SECTION TWO"));
		root.addChild(new SentenceNode("SUBROOT"));
		ParseTreeNode node = new ParagraphNode("ROOT TWO", "LABEL ONE");
		node.addChild(new StatementNode("STATEMENT TWO"));
		root.addChild(node);
		
		root.accept(new CallGraphVisitor());
		
	}

}
