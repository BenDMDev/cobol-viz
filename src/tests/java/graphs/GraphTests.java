package tests.java.graphs;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.graphs.Graph;
import main.java.graphs.Vertex;

public class GraphTests {

	@Test
	public void test() {
		Graph g = new Graph(10);
		Vertex v1 = new Vertex("V1");
		g.addVertices(v1);
		Vertex v2 = new Vertex("V2");
		g.addVertices(v2);
		Vertex v3 = new Vertex("V3");
		g.addVertices(v3);
		g.addEdge(v3.getIndex(),v2.getIndex());
		g.addEdge(v1.getIndex(),v3.getIndex());
		g.addEdge(v2.getIndex(),v3.getIndex());
		g.printMatrix();
		
		Vertex v4 = g.getVertex(2);
		
		
		assertEquals(2, g.inDegree(v3.getIndex()));
		assertEquals(1, g.outDegree(v3.getIndex()));
		assertTrue(g.edgeExists(v3.getIndex(),v2.getIndex()));
		assertFalse(g.edgeExists(v1.getIndex(),v2.getIndex()));
	}

}
