package main.java.ui.models;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import main.java.graphs.Graph;
import main.java.graphs.cobol.CallGraphVertex;


public class GraphDataModel {

	private Graph graph;
	


	public GraphDataModel(Graph g) {
		graph = g;
	}
	
	public GraphDataModel(Graph g, File source) {
		graph = g;	
	}


	
	public Graph getGraph() {
		return graph;
	}
	

	

	public int getLinesOfCode(String graphLabel) {

		CallGraphVertex v = (CallGraphVertex) graph.getVertex(graphLabel);		
		return v.getNumberOfLines();

	}
	
	public int getTotalLinesOfCode() {

		int sum = 0;
		for(int i = 0; i < graph.getNumberOfVertices(); i++) {
			sum += ((CallGraphVertex) graph.getVertex(i)).getNumberOfLines();
		}

		return sum;

	}

	public int getCyclomaticComplexity(String graphLabel) {

		CallGraphVertex v = (CallGraphVertex) graph.getVertex(graphLabel);

		return v.getCyclomaticComplexity();

	}
	
	
	public int getInDegree(String graphLabel) {

		CallGraphVertex v = (CallGraphVertex) graph.getVertex(graphLabel);

		return graph.inDegree(v.getIndex());

	}
	
	
	
	public int getOutDegree(String graphLabel) {

		CallGraphVertex v = (CallGraphVertex) graph.getVertex(graphLabel);

		return graph.outDegree(v.getIndex());

	}
	

}
