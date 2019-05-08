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

/**
 * Call graph visitor
 * Descends through paragraphs and constructurs references between procedures.
 * @author Ben
 *
 */
public class CallGraphVisitor implements TreeVisitor {

	private Graph g;
	private CallGraphVertex lastSeen;
	private Graph controlGraph;
	private int lineBegin;
	private int lineEnd;
	private boolean endNodeSeen;

	public CallGraphVisitor() {
		g = new Graph(100);
		lastSeen = null;
		endNodeSeen = false;
	}

	@Override
	public void visit(ParseTreeNode treeNode) {

		if (treeNode instanceof SectionNode) {
			visit((SectionNode) treeNode);
		} else if (treeNode instanceof ParagraphNode) {
			visit((ParagraphNode) treeNode);
		}

	}

	/**
	 * Visit statement nodes
	 * Handle specific cases
	 * @param statement
	 */
	public void visit(StatementNode statement) {
		lineEnd = statement.getLineNumber();

		if (statement.getTreeNodeType() == TreeNodeType.REFERENCE) {
			processReferenceStatement(statement);
		}

		if (statement.getTreeNodeType() == TreeNodeType.CONDITIONAL_STATEMENT) {
			processConditionalStatement(statement);
		}
		
		if (statement.getTreeNodeType() == TreeNodeType.END) {
			endNodeSeen = true;
		}

		for (ParseTreeNode n : statement.getChildren()) {
			if (n instanceof StatementNode) {
				visit((StatementNode) n);
			}
		}

	}

	public void visit(SectionNode section) {
		
	}

	/**
	 * Visit paragraph - represents a procedure in COBOL
	 * @param paragraph
	 */
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
				if(!endNodeSeen) {
				g.addEdge(lastSeen.getIndex(), vertex.getIndex());
				
				}
				lastSeen = vertex;
			}
			visitChildren(paragraph);

			 int diff = lineEnd - lineBegin;
			lastSeen.setNumberOfLines(paragraph.getNumberOfLines());

		} else {
			CallGraphVertex holder = lastSeen;
			lastSeen = vertex;

			visitChildren(paragraph);

			int diff = lineEnd - lineBegin;
			lastSeen.setNumberOfLines(paragraph.getNumberOfLines());
			lastSeen.setGraph(controlGraph);
			lastSeen = holder;
		}

	}

	/**
	 * Visito children of Node
	 * @param paragraph
	 */
	private void visitChildren(ParagraphNode paragraph) {
		for (ParseTreeNode n : paragraph.getChildren()) {
			if (n instanceof SentenceNode)
				visit((SentenceNode) n);
		}
	}

	/**
	 * Visit Sentence Node
	 * @param sentence
	 */
	public void visit(SentenceNode sentence) {
		for (ParseTreeNode n : sentence.getChildren()) {
			if (n instanceof StatementNode)
				visit((StatementNode) n);
		}
	}

	/**
	 * Return graph constructed by this visitor
	 * 
	 */
	public Graph getGraph() {
		return g;
	}

	/**
	 * Process reference statements
	 * I.e PERFORM proc-1 THRU proc-2
	 * @param statement
	 */
	private void processReferenceStatement(StatementNode statement) {

		int indexList[] = new int[COBOLParser.REFERENCES.size()];
		int curPos = -1;
		for (ParseTreeNode n : statement.getChildren()) {
			if (n.getTreeNodeType() == TreeNodeType.REFERENCE_VALUE) {
				int index = getReferenceIndex(n.getAttribute());
				if (index >= 0) {
					curPos++;
					indexList[curPos] = index;
				}
			}
		}
		if (curPos >= 0) {
			if (indexList[curPos] >= indexList[0])
				addReferencedVertex(indexList[0], indexList[curPos]);
			
		}

	}

	/**
	 * Add referenced nodes to graph ready to be connected
	 * @param begin
	 * @param end
	 */
	private void addReferencedVertex(int begin, int end) {

		for (int i = begin; i <= end; i++) {
			CallGraphVertex v = (CallGraphVertex) g.getVertex(COBOLParser.REFERENCES.get(i));

			if (v == null) {
				v = new CallGraphVertex(COBOLParser.REFERENCES.get(i));
				g.addVertices(v);

			}

			g.addWeightedEdge(lastSeen.getIndex(), v.getIndex(), 1);

		}

	}

	/**
	 * process conditional statements - expected to have nested compound statements
	 * @param statement
	 */
	private void processConditionalStatement(StatementNode statement) {

		for (ParseTreeNode n : statement.getChildren()) {
			if (n.getTreeNodeType() == TreeNodeType.CONDITION_BODY) {
				for (ParseTreeNode c : n.getChildren()) {
					if (c instanceof StatementNode)
						visit((StatementNode) c);
				}
			}
		}

	}
	

	/**
	 * Get index of reference
	 * @param ref
	 * @return
	 */
	private int getReferenceIndex(String ref) {
		
		if(COBOLParser.SECTION_REFERENCES.containsKey(ref)){
			ref = COBOLParser.SECTION_REFERENCES.get(ref);
		}
		
		int index = -1;
		for (int i = 0; i < COBOLParser.REFERENCES.size(); i++) {
			
			if (COBOLParser.REFERENCES.get(i).equals(ref))
				index = i;
		}

		return index;
	}

}
