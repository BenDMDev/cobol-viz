package main.java.graphs.cobol;

import main.java.graphs.Graph;
import main.java.graphs.Vertex;

public class CallGraphVertex extends Vertex {
		
	private Graph g;
	private int numberOfLines;
	
	public CallGraphVertex(String label) {
		super(label);				
		g = new Graph(10);
		numberOfLines = 0;
	}
	
	public Graph getGraph() {
		return g;
	} 
	
	public void setNumberOfLines(int numberOfLines) {
		this.numberOfLines = numberOfLines;
	}
	
	public int getNumberOfLines() {
		return numberOfLines;
	}

}
