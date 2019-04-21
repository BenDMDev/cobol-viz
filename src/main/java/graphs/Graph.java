package main.java.graphs;

public class Graph {
	
	private Vertex[] vertices;
	private int[][] adjacencyMatrix;
	private int index;
	private int numVertices;
	private int numEdges;
	
	public Graph() {
		this(10);		
	}
	
	public Graph(int numVertices) {
		this.numVertices = 0;
		adjacencyMatrix = new int[numVertices][numVertices];
		vertices = new Vertex[numVertices];		
		index = 0;
		numEdges = 0;
	}
	
	public int addVertices(Vertex v) {
		vertices[index] = v;
		numVertices++;
		return index++;
	}
	
	public void addEdge(int from, int to) {
		adjacencyMatrix[from][to] = 1;
		numEdges++;
		
	}
	
	public void printMatrix() {
		for(int i = 0; i < numVertices; i++) {
			System.out.print("  " + i);			
		}
		
		for(int i = 0; i < numVertices; i++) {
			System.out.print("\n" + i + " ");
			for(int j = 0; j < numVertices; j++) {
				System.out.print(adjacencyMatrix[i][j] + "  ");
			}
			
		}
		
	}
	
	public Vertex getVertex(int index) {
		return vertices[index];
	}
	
	public Vertex[] getVertices() {
		return vertices;
	}
	
	public boolean edgeExists(int from, int to) {
		return adjacencyMatrix[from][to] == 1;
	}
	
	public int inDegree(int vertex) {
		int degree = 0;
		for(int i = 0; i < numVertices; i++) {
			if(adjacencyMatrix[i][vertex] >= 1)
				degree++;
		}
		return degree;
	}
	
	public int outDegree(int vertex) {
		int degree = 0;
		for(int i = 0; i < numVertices; i++) {
			if(adjacencyMatrix[vertex][i] >= 1)
				degree++;
		}
		return degree;
	}
	
	public int getNumberOfVertices() {
		return numVertices;
	}
	
	public int getNumberOfEdges() {
		return numEdges;
	}
	
}
