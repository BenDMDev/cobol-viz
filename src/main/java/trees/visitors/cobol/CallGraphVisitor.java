package main.java.trees.visitors.cobol;

import main.java.graphs.Graph;
import main.java.graphs.cobol.CallGraphVertex;
import main.java.parsers.cobol.COBOLParser;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.ParagraphNode;
import main.java.trees.cobol.SectionNode;
import main.java.trees.cobol.SentenceNode;
import main.java.trees.cobol.StatementNode;
import main.java.trees.visitors.TreeVisitor;

public class CallGraphVisitor implements TreeVisitor {

	private Graph g;
	private CallGraphVertex lastSeen;
	private Graph controlGraph;
	private int lineBegin;
	private int lineEnd;

	public CallGraphVisitor() {
		g = new Graph(50);
		lastSeen = null;
	}

	@Override
	public void visit(ParseTreeNode treeNode) {

		if (treeNode instanceof StatementNode) {
		
		} else if (treeNode instanceof SectionNode) {
			visit((SectionNode) treeNode);
		} else if (treeNode instanceof ParagraphNode) {
			visit((ParagraphNode) treeNode);
		} else if (treeNode instanceof SentenceNode) {
			
		}

	}

	public void visit(StatementNode statement) {
		lineEnd = statement.getLineNumber();

		if (statement.getTreeNodeType() == TreeNodeType.REFERENCE) {
			processPerformStatement(statement);
		} 
		
		if(statement.getTreeNodeType() == TreeNodeType.CONDITIONAL_STATEMENT) {
			processConditionalStatement(statement);
		}
		
		
		for (ParseTreeNode n : statement.getChildren()) {
			if (n instanceof StatementNode) {
				visit((StatementNode) n);
			} 			
		}

	}

	public void visit(SectionNode section) {

	}

	public void visit(ParagraphNode paragraph) {
		ControlGraphVisitor controlVisitor = new ControlGraphVisitor();
		paragraph.accept(controlVisitor);
		controlGraph = controlVisitor.getGraph();
		CallGraphVertex vertex = (CallGraphVertex) g.getVertex(paragraph.getAttribute());
		lineBegin = lineEnd = paragraph.getLineNumber();
		if (vertex == null) {
			vertex = new CallGraphVertex(paragraph.getAttribute());
			g.addVertices(vertex);
			vertex.setGraph(controlGraph);
			if (lastSeen == null)
				lastSeen = vertex;
			else {
				g.addEdge(lastSeen.getIndex(), vertex.getIndex());
				lastSeen = vertex;
			}
			for (ParseTreeNode n : paragraph.getChildren()) {
				if (n instanceof SentenceNode)
					visit((SentenceNode) n);
			}
			int diff = lineEnd - lineBegin;
			lastSeen.setNumberOfLines(diff);

		} else {
			CallGraphVertex holder = lastSeen;
			lastSeen = vertex;
			for (ParseTreeNode n : paragraph.getChildren()) {
				if (n instanceof SentenceNode)
					visit((SentenceNode) n);
			}
			int diff = lineEnd - lineBegin;
			lastSeen.setNumberOfLines(diff);
			lastSeen.setGraph(controlGraph);
			lastSeen = holder;
		}

	}

	public void visit(SentenceNode sentence) {
		for (ParseTreeNode n : sentence.getChildren()) {
			if (n instanceof StatementNode)
				visit((StatementNode) n);
		}
	}

	public Graph getGraph() {
		return g;
	}

	private void processPerformStatement(StatementNode statement) {

		int beginIndex = 0;
		int endIndex = 0;
		for (int i = 0; i < COBOLParser.REFERENCES.size(); i++) {
			String ref = COBOLParser.REFERENCES.get(i);
			String labelLHS = statement.getChildren().get(1).getAttribute();
			if (labelLHS.equals(ref)) {
				beginIndex = i;
			}
			if (statement.getChildren().size() >= 4) {
				String labelRHS = statement.getChildren().get(3).getAttribute();
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

			g.addWeightedEdge(lastSeen.getIndex(), v.getIndex(), 2);

		}

	}
	
	
	private void processConditionalStatement(StatementNode statement) {
		
		for(ParseTreeNode n : statement.getChildren()) {
			if(n.getTreeNodeType() == TreeNodeType.CONDITION_BODY) {
				for(ParseTreeNode c : n.getChildren()) {
					if(c instanceof StatementNode)
						visit((StatementNode) c);
				}
			}
		}
		
	}


}
