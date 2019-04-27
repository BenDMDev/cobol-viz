package tests.java.graphs;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import main.java.graphs.Graph;
import main.java.graphs.GraphWriter;
import main.java.graphs.Vertex;

public class GraphWriterTest {

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
		g.addEdge(v1.getIndex(),v2.getIndex());
		
		GraphWriter gr = new GraphWriter();
		gr.generate(GraphWriter.CALL_GRAPH, g);
		
		
		try {
			gr.write("Test.gexf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
