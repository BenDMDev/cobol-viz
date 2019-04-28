package main.java.trees.visitors.cobol;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import main.java.graphs.Graph;
import main.java.graphs.Vertex;
import main.java.graphs.cobol.ControlGraphVertex;
import main.java.trees.ParseTreeNode;
import main.java.trees.TreeNodeType;
import main.java.trees.cobol.ParagraphNode;
import main.java.trees.cobol.SentenceNode;
import main.java.trees.cobol.StatementNode;
import main.java.trees.visitors.TreeVisitor;

public class ControlGraphVisitor implements TreeVisitor {

	Graph graph;
	ControlGraphVertex lastSeen;
	ControlGraphVertex entry;
	ControlGraphVertex exit;
	

	public ControlGraphVisitor() {
		graph = new Graph(100);
		entry = new ControlGraphVertex("ENTRY");
		exit = new ControlGraphVertex("EXIT");
		graph.addVertices(entry);
		lastSeen = entry;
		
	}

	@Override
	public void visit(ParseTreeNode treeNode) {
		if (treeNode instanceof ParagraphNode) {
			visit((ParagraphNode) treeNode);
			graph.addVertices(exit);
			graph.addEdge(lastSeen.getIndex(), exit.getIndex());	
		}
	}

	public void visit(ParagraphNode treeNode) {
		for (ParseTreeNode n : treeNode.getChildren()) {
			if (n instanceof SentenceNode)
				visit((SentenceNode) n);
		}
	}

	public void visit(SentenceNode treeNode) {
		for (ParseTreeNode n : treeNode.getChildren()) {
			if (n instanceof StatementNode)
				visit((StatementNode) n);
		}
	}

	public void visit(StatementNode node) {
		if (node.getTreeNodeType() == TreeNodeType.CONDITIONAL_STATEMENT) {
			processConditional(node);
		} else if(node.getTreeNodeType() == TreeNodeType.COMPOUND_STATEMENT){
			processCompoundStatement(node);
		} else {
			processStatement(node);
		}

	}

	/**
	 * Process an Imperative Statement
	 * Assumes child nodes are all terminals representing Statement Body
	 * @param root
	 */
	private void processStatement(ParseTreeNode root) {

		StringBuilder builder = new StringBuilder();
		List<StatementNode> childStatements = new ArrayList<StatementNode>();
		for (ParseTreeNode p : root.getChildren()) {
			if (p instanceof StatementNode)
				childStatements.add((StatementNode) p);
			else
				builder.append(p.getAttribute() + " ");
		}

		ControlGraphVertex rootVer = new ControlGraphVertex(builder.toString());
		graph.addVertices(rootVer);
		graph.addEdge(lastSeen.getIndex(), rootVer.getIndex());
		lastSeen = rootVer;
		for (StatementNode s : childStatements) {
			visit(s);
		}

	}
	
	/**
	 * Process a compound statement
	 * assumes child nodes are mix of terminals (representing Statement Body)
	 * and other StatementNodes representing nested statements
	 * @param root Root node for this Statement
	 */
	private void processCompoundStatement(ParseTreeNode root) {
		
		StringBuilder builder = new StringBuilder();
		List<StatementNode> childStatements = new ArrayList<StatementNode>();
		for (ParseTreeNode p : root.getChildren()) {
			if (p instanceof StatementNode)
				childStatements.add((StatementNode) p);
			else
				builder.append(p.getAttribute() + " ");
		}

		ControlGraphVertex rootVer = new ControlGraphVertex(builder.toString());
		graph.addVertices(rootVer);
		graph.addEdge(lastSeen.getIndex(), rootVer.getIndex());
		lastSeen = rootVer;
		for (StatementNode s : childStatements) {
			visit(s);
		}
	}

	/**
	 * Process a Conditional statement in the form
	 * CONDITION, CONDITION_BODY (i.e IF), (Optional) CONDITION_BODY (i.e ELSE)
	 * Expects nodes to be in the above order.
	 * @param root
	 */
	private void processConditional(ParseTreeNode root) {

		StringBuilder builder = new StringBuilder();
		List<ParseTreeNode> nodes = root.getChildren().get(0).getChildren();
		for (ParseTreeNode p : nodes) {
			builder.append(p.getAttribute() + " ");
		}

		ControlGraphVertex rootVer = new ControlGraphVertex(builder.toString());
		ControlGraphVertex holder = rootVer;
		ControlGraphVertex end = new ControlGraphVertex("END");

		graph.addVertices(rootVer);
		graph.addEdge(lastSeen.getIndex(), rootVer.getIndex());
		lastSeen = rootVer;
		graph.addVertices(end);

		// Handle IF BLOCK
		nodes = root.getChildren().get(1).getChildren();
		for (ParseTreeNode p : nodes) {
			if(p instanceof StatementNode)
				visit((StatementNode) p);
		}

		graph.addEdge(lastSeen.getIndex(), end.getIndex());
		lastSeen = holder;

		// HANDLE ELSE BLOCK
		if (root.getChildren().size() == 3) {
			nodes = root.getChildren().get(2).getChildren();
			for (ParseTreeNode p : nodes) {
				visit((StatementNode) p);
			}
			graph.addEdge(lastSeen.getIndex(), end.getIndex());
		} else {
			graph.addEdge(holder.getIndex(), end.getIndex());
		}

		lastSeen = end;

	}

	public Graph getGraph() {

		return graph;
	}

}
