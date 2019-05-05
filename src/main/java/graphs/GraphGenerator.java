package main.java.graphs;

import main.java.trees.ParseTree;
import main.java.trees.visitors.cobol.CallGraphVisitor;

public class GraphGenerator {

	private ParseTree rootNode;
	
	
	public GraphGenerator(ParseTree root) {
		rootNode = root;
	}
	
	
	public Graph generateGraph() {		
		CallGraphVisitor visitor = new CallGraphVisitor();
		rootNode.accept(visitor);
		Graph graph = visitor.getGraph();		
		return graph;
	}
	
	
	
	
	
	
	
}
