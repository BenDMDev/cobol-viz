package main.java.trees.visitors.cobol;

import main.java.graphs.Graph;
import main.java.graphs.Vertex;
import main.java.graphs.cobol.CallGraphVertex;
import main.java.trees.ParseTreeNode;
import main.java.trees.cobol.ParagraphNode;
import main.java.trees.cobol.SectionNode;
import main.java.trees.cobol.SentenceNode;
import main.java.trees.cobol.StatementNode;
import main.java.trees.visitors.TreeVisitor;

public class COBOLVisitor implements TreeVisitor {

	private Graph g;
	private CallGraphVertex lastSeen;
	
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
		
	
	}
	
	public void visit(SectionNode section) {
		
	}
	
	public void visit(ParagraphNode paragraph) {
		
		
		CallGraphVertex v = new CallGraphVertex(paragraph.getType());
		v.setIndex(g.addVertices(v));
		
		if(lastSeen == null) { 
			lastSeen = v;		
		} else {
			g.addEdge(lastSeen.getIndex(), v.getIndex());
			lastSeen = v;
		}
		
		
	}
	
	public void visit(SentenceNode sentence) {
		
	}
	
	public Graph getGraph() {
		return g;
	}

	

}
