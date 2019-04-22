package main.java.graphs.cobol;

import main.java.graphs.Graph;
import main.java.graphs.Vertex;

public class CallGraphVertex extends Vertex {
		
	private Graph g;
	
	public CallGraphVertex(String label) {
		super(label);				
		g = new Graph(10);
	}
	
	public Graph getGraph() {
		return g;
	} 

}
