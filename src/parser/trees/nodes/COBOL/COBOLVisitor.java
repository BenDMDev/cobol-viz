package parser.trees.nodes.COBOL;

import intermediate.graph.Graph;
import intermediate.graph.Vertex;
import intermediate.graph.COBOL.CallGraphVertex;
import parser.trees.ParseTreeNode;
import parser.trees.TreeVisitor;

public class COBOLVisitor implements TreeVisitor {

	private Graph g;
	private Vertex lastSeen;
	
	public COBOLVisitor() {
		g = new Graph(10);
		lastSeen = null;
	}
	
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
		
		Vertex v = new CallGraphVertex(paragraph.getType());
		v.setIndex(g.addVertices(v));
		
		if(lastSeen == null) { 
			lastSeen = v;		
		} else {
			g.addEdge(lastSeen.getIndex(), v.getIndex());
			lastSeen = v;
		}
		
		
	}
	
	public void visit(SentenceNode sentence) {
		System.out.println(sentence.getType());
	}
	
	public Graph getGraph() {
		return g;
	}

	

}
