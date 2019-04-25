package main.java.ui.models;

import main.java.graphs.Graph;
import main.java.trees.ParseTree;

public class GraphDataModel {
	
	private Graph graph;	
	private ParseTree tree;	

	
	public GraphDataModel(Graph g) {
		graph = g;
	}
	

	
	public Graph getGraph() {
		return graph;
	}
	
	
	
	
}
