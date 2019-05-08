package main.java.graphs;

import main.java.trees.ParseTree;
import main.java.trees.visitors.cobol.CallGraphVisitor;

/**
 * Simple graph generator class
 * Takes a parse tree and generates a graph structure.
 * @author Ben
 *
 */
public class GraphGenerator {

	private ParseTree rootNode;
	
	
	public GraphGenerator(ParseTree root) {
		rootNode = root;
	}
	
	/**
	 * Generate graph from current parse tree
	 * @return
	 */
	public Graph generateGraph() {		
		CallGraphVisitor visitor = new CallGraphVisitor();
		rootNode.accept(visitor);
		Graph graph = visitor.getGraph();		
		return graph;
	}
	
	
	
	
	
	
	
}
