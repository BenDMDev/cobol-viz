package main.java.graphs;

/**
 * Generic Graph class used as to represent both Call and Control graphs 
 * @author Ben
 *
 */
public class Graph {
	
	private Vertex[] vertices;
	private int[][] adjacencyMatrix;
	private int index;
	private int numVertices;
	private int numEdges;
	
	/**
	 * Default constructor
	 */
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
	
	/**
	 * Add vertex to Graph
	 * @param v Vertex to add
	 */
	public void addVertices(Vertex v) {
		vertices[index] = v;
		numVertices++;
		v.setIndex(index);
		index++;
		
	}
	
	/**
	 * Add edge to Graph
	 * @param from Edge
	 * @param to Edge
	 */
	public void addEdge(int from, int to) {
		if(!edgeExists(from, to)) {
		adjacencyMatrix[from][to] = 1;
		numEdges++;
		}
		
	}
	
	/**
	 * Add Weighted edge
	 * @param from Edge
	 * @param to Edge
	 * @param weight Weight of edge
	 */
	public void addWeightedEdge(int from, int to, int weight) {
		if(!edgeExists(from, to)) {
		adjacencyMatrix[from][to] = weight;
		numEdges++;
		}
		
	}
	
	/**
	 * Get edge weight for this edge
	 * @param from Edge
	 * @param to Edge
	 * @return int Weight
	 */
	public int getEdgeWeight(int from, int to) {
		return adjacencyMatrix[from][to];
	}
	
	/**
	 * Help method to print adjacency matrix
	 */
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
		System.out.println();
		
	}
	
	/**
	 * Get Vertex by index position
	 * @param index position
	 * @return Vertex
	 */
	public Vertex getVertex(int index) {
		return vertices[index];
	}
	
	/**
	 * Get Vertex by vertex label
	 * @param text Vertex label
	 * @return Vertex
	 */
	public Vertex getVertex(String text) {
		Vertex vertex = null;
		for(int i = 0; i < numVertices; i++) {
			if(vertices[i].getText().equals(text))
				vertex = vertices[i];
		}
		return vertex;
	}
	
	/**
	 * Get all vertices
	 * @return
	 */
	public Vertex[] getVertices() {
		return vertices;
	}
	
	/**
	 * Check edge exists
	 * @param from Edge
	 * @param to Edge
	 * @return boolean exists
	 */
	public boolean edgeExists(int from, int to) {
		return adjacencyMatrix[from][to] > 0;
	}
	
	/**
	 * Get in degree of vertex
	 * @param vertex index
	 * @return int In Degree
	 */
	public int inDegree(int vertex) {
		int degree = 0;
		for(int i = 0; i < numVertices; i++) {
			if(adjacencyMatrix[i][vertex] >= 1)
				degree++;
		}
		return degree;
	}
	
	/**
	 * Get out degree of vertex
	 * @param vertex index
	 * @return int Out Degree
	 */
	public int outDegree(int vertex) {
		int degree = 0;
		for(int i = 0; i < numVertices; i++) {
			if(adjacencyMatrix[vertex][i] >= 1)
				degree++;
		}
		return degree;
	}
	
	/**
	 * Get number of vertices for this graph
	 * @return
	 */
	public int getNumberOfVertices() {
		return numVertices;
	}
	
	/**
	 * Get number of edges for this graph
	 * @return
	 */
	public int getNumberOfEdges() {
		return numEdges;
	}
	
}
