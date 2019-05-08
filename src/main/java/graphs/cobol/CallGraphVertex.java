package main.java.graphs.cobol;

import main.java.graphs.Graph;
import main.java.graphs.Vertex;

/**
 * Vertex used in Graph representing COBOL call Graph
 * @author Ben
 *
 */
public class CallGraphVertex extends Vertex {
		
	private Graph graph;
	private int numberOfLines;
	
	public CallGraphVertex(String label) {
		super(label);				
		graph = new Graph(100);
		numberOfLines = 0;
	}
	
	/**
	 * Returns control flow graph for this Vertex
	 * @return
	 */
	public Graph getGraph() {
		return graph;
	} 
	
	/**
	 * Sets Control flow graph for this vertex
	 * @param g Graph to assign
	 */
	public void setGraph(Graph g) {
		graph = g;
		
	}
	
	/**
	 * Sets number of lines for the procedure this vertex represents
	 * @param numberOfLines
	 */
	public void setNumberOfLines(int numberOfLines) {
		this.numberOfLines = numberOfLines;
	}
	
	/**
	 * Get Number of lines of procedure this vertex represents
	 * @return
	 */
	public int getNumberOfLines() {
		return numberOfLines;
	}
	
	/**
	 * Get cyclomatic complexity of procedure this vertex represents
	 * @return int value for Complexity
	 */
	public int getCyclomaticComplexity() {
		int edges = graph.getNumberOfEdges();
		int vertices = graph.getNumberOfVertices();
		int connected = graph.inDegree(graph.getVertex("EXIT").getIndex());
		return (edges - vertices) + 2 * connected;
		
	}

}
