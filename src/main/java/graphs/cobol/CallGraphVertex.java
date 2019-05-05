package main.java.graphs.cobol;

import main.java.graphs.Graph;
import main.java.graphs.Vertex;

public class CallGraphVertex extends Vertex {
		
	private Graph graph;
	private int numberOfLines;
	
	public CallGraphVertex(String label) {
		super(label);				
		graph = new Graph(100);
		numberOfLines = 0;
	}
	
	public Graph getGraph() {
		return graph;
	} 
	
	public void setGraph(Graph g) {
		graph = g;
		
	}
	
	public void setNumberOfLines(int numberOfLines) {
		this.numberOfLines = numberOfLines;
	}
	
	public int getNumberOfLines() {
		return numberOfLines;
	}
	
	public int getCyclomaticComplexity() {
		int edges = graph.getNumberOfEdges();
		int vertices = graph.getNumberOfVertices();
		int connected = graph.inDegree(graph.getVertex("EXIT").getIndex());
		return (edges - vertices) + 2 * connected;
		
	}

}
