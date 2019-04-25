package main.java.graphs;

import main.java.trees.ParseTree;
import main.java.trees.visitors.cobol.COBOLVisitor;

public class GraphGenerator {

	ParseTree rootNode;
	
	public GraphGenerator(ParseTree root) {
		rootNode = root;
	}
	
	
	public Graph generateGraph() {		
		COBOLVisitor visitor = new COBOLVisitor();
		rootNode.accept(visitor);
		Graph graph = visitor.getGraph();
		return graph;
	}
	
	
	
	
	
	
	
}
