package main.java.trees.visitors.cobol;

import main.java.graphs.Graph;
import main.java.graphs.cobol.CallGraphVertex;
import main.java.parsers.cobol.COBOLParser;
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
		g = new Graph(50);
		lastSeen = null;

	}

	@Override
	public void visit(ParseTreeNode treeNode) {

		if (treeNode instanceof StatementNode) {
			visit((StatementNode) treeNode);
		} else if (treeNode instanceof SectionNode) {
			visit((SectionNode) treeNode);
		} else if (treeNode instanceof ParagraphNode) {
			visit((ParagraphNode) treeNode);
		} else if (treeNode instanceof SentenceNode) {
			visit((SentenceNode) treeNode);
		}

	}

	public void visit(StatementNode statement) {

		if (statement.getType().contains("PERFORM")) {
			processPerformStatement(statement);
		}

	}

	public void visit(SectionNode section) {

	}

	public void visit(ParagraphNode paragraph) {

		CallGraphVertex v = (CallGraphVertex) g.getVertex(paragraph.getLabel());

		if (v == null) {
			v = new CallGraphVertex(paragraph.getLabel());
			g.addVertices(v);
			if (lastSeen == null)
				lastSeen = v;
			else {
				g.addEdge(lastSeen.getIndex(), v.getIndex());
				lastSeen = v;
			}
		} else
			lastSeen = v;
	

	}

	public void visit(SentenceNode sentence) {

	}

	public Graph getGraph() {
		return g;
	}

	private void processPerformStatement(StatementNode statement) {

		int beginIndex = 0;
		int endIndex = 0;
		for (int i = 0; i < COBOLParser.REFERENCES.size(); i++) {
			String ref = COBOLParser.REFERENCES.get(i);
			String labelLHS = statement.getChildren().get(1).getType();
			if (labelLHS.equals(ref)) {
				beginIndex = i;
			}
			if (statement.getChildren().size() >= 4) {
				String labelRHS = statement.getChildren().get(3).getType();
				if (labelRHS.equals(ref)) {
					endIndex = i;
				}
			} else {
				endIndex = beginIndex;
			}

		}

		addPerformVertices(beginIndex, endIndex);

	}

	private void addPerformVertices(int begin, int end) {

		for (int i = begin; i <= end; i++) {
			CallGraphVertex v = (CallGraphVertex) g.getVertex(COBOLParser.REFERENCES.get(i));

			if (v == null) {
				v = new CallGraphVertex(COBOLParser.REFERENCES.get(i));
				g.addVertices(v);
			}

			g.addEdge(lastSeen.getIndex(), v.getIndex());

		}

	}

}
