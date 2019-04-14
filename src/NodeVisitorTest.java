import static org.junit.Assert.*;

import org.junit.Test;

import parser.trees.ParseTreeNode;
import parser.trees.nodes.COBOL.COBOLVisitor;
import parser.trees.nodes.COBOL.ParagraphNode;
import parser.trees.nodes.COBOL.SectionNode;
import parser.trees.nodes.COBOL.SentenceNode;
import parser.trees.nodes.COBOL.StatementNode;

public class NodeVisitorTest {

	@Test
	public void test() {
		ParseTreeNode root = new ParseTreeNode("ROOT");
		root.addChild(new StatementNode("STATEMENT"));
		root.addChild(new SectionNode("SECTION TWO"));
		root.addChild(new SentenceNode("SUBROOT"));
		ParseTreeNode node = new ParagraphNode("ROOT TWO");
		node.addChild(new StatementNode("STATEMENT TWO"));
		root.addChild(node);
		
		root.accept(new COBOLVisitor());
	}

}
