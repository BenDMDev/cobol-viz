package parser.trees.nodes.COBOL;

import parser.trees.ParseTreeNode;
import parser.trees.TreeVisitor;

public class COBOLVisitor implements TreeVisitor {

	
	@Override
	public void visit(ParseTreeNode treeNode) {
		
		if(treeNode instanceof StatementNode) {
			visit((StatementNode) treeNode);
		} else if(treeNode instanceof SectionNode) {
			visit((SectionNode) treeNode);
		} else if (treeNode instanceof ParagraphNode) {
			visit((ParagraphNode) treeNode);
		} else if (treeNode instanceof SentenceNode) {
			visit((SentenceNode) treeNode);
		}
		
	}
	
	public void visit(StatementNode statement) {
		System.out.println(statement.getType());
	}
	
	public void visit(SectionNode section) {
		System.out.println(section.getType());
	}
	
	public void visit(ParagraphNode paragraph) {
		System.out.println(paragraph.getType());
	}
	
	public void visit(SentenceNode sentence) {
		System.out.println(sentence.getType());
	}

	

}
