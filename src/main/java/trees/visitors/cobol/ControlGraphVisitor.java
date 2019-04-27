package main.java.trees.visitors.cobol;

import java.util.ArrayList;
import java.util.Stack;

import main.java.graphs.Graph;
import main.java.graphs.cobol.ControlGraphVertex;
import main.java.trees.ParseTreeNode;
import main.java.trees.cobol.ParagraphNode;
import main.java.trees.cobol.SentenceNode;
import main.java.trees.cobol.StatementNode;
import main.java.trees.visitors.TreeVisitor;

public class ControlGraphVisitor implements TreeVisitor {

	Graph graph;
	ControlGraphVertex lastSeen;
	ControlGraphVertex entry;
	ControlGraphVertex exit;
	Stack<ControlGraphVertex> exitList;

	public ControlGraphVisitor() {
		graph = new Graph(100);
		entry = new ControlGraphVertex("ENTRY");
		exit = new ControlGraphVertex("EXIT");
		graph.addVertices(entry);
		lastSeen = entry;
		exitList = new Stack<ControlGraphVertex>();
	}

	@Override
	public void visit(ParseTreeNode treeNode) {
		if (treeNode instanceof ParagraphNode) {			
			
			visit((ParagraphNode) treeNode);
			graph.addVertices(exit);
			graph.addEdge(lastSeen.getIndex(), exit.getIndex());
			while(!exitList.isEmpty()) {
				graph.addEdge(exitList.pop().getIndex(), exit.getIndex());
			}
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

		StringBuilder builder = new StringBuilder();
		ArrayList<StatementNode> childNodes = new ArrayList<StatementNode>();
		for (ParseTreeNode n : node.getChildren()) {
			if (n instanceof StatementNode) {
				childNodes.add((StatementNode) n);				
			}
			else
				builder.append(n.getType() + " ");
		}

		ControlGraphVertex vertex = new ControlGraphVertex(builder.toString());
		graph.addVertices(vertex);
		graph.addEdge(lastSeen.getIndex(), vertex.getIndex());
		if(!exitList.isEmpty()) {
			graph.addEdge(exitList.pop().getIndex(), vertex.getIndex());
			
		}
		if(builder.toString().contains("IF")) {
			ControlGraphVertex holder = vertex;
			lastSeen = vertex;
			for (StatementNode child : childNodes) {
				visit(child);
			}
			exitList.push(lastSeen);
			lastSeen = holder;
		} else {
			lastSeen = vertex;
		}
		
		

	}

	public Graph getGraph() {

		return graph;
	}

}
