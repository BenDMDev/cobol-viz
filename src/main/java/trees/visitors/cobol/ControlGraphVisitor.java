package main.java.trees.visitors.cobol;

import java.util.ArrayList;

import main.java.graphs.Graph;
import main.java.graphs.cobol.ControlGraphVertex;
import main.java.trees.ParseTreeNode;
import main.java.trees.cobol.StatementNode;
import main.java.trees.visitors.TreeVisitor;

public class ControlGraphVisitor implements TreeVisitor {
	
	Graph graph;
	ControlGraphVertex lastSeen;
	public ControlGraphVisitor() {
		graph = new Graph(100);
		
	}
	
	
	@Override
	public void visit(ParseTreeNode treeNode) {
		if(treeNode instanceof StatementNode)
			visit((StatementNode) treeNode);
	}
	
	public void visit(StatementNode node) {
		
		StringBuilder builder = new StringBuilder();
		ArrayList<StatementNode> childNodes = new ArrayList<StatementNode>();
		for(ParseTreeNode n : node.getChildren()) {
			if(n instanceof StatementNode)
				childNodes.add((StatementNode)n);
			else
				builder.append(n.getType() + " ");								
		}
		
		ControlGraphVertex vertex = new ControlGraphVertex(builder.toString());
		graph.addVertices(vertex);
		if(lastSeen == null)
			lastSeen = vertex;
		else
			graph.addEdge(lastSeen.getIndex(), vertex.getIndex());
		
		for(StatementNode child : childNodes) {
			visit(child);
		}
		
	}
	
	public Graph getGraph() {
		return graph;
	}

}
