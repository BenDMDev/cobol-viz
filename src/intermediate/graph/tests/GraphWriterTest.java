package intermediate.graph.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import intermediate.GraphWriter;
import intermediate.graph.Graph;
import intermediate.graph.Vertex;
import intermediate.graph.COBOL.CallGraphVertex;

public class GraphWriterTest {

	@Test
	public void test() {
		Graph g = new Graph(10);
		Vertex v1 = new Vertex("V1");
		v1.setIndex(g.addVertices(v1));
		Vertex v2 = new Vertex("V2");
		v2.setIndex(g.addVertices(v2));
		Vertex v3 = new Vertex("V3");
		v3.setIndex(g.addVertices(v3));
		g.addEdge(v3.getIndex(),v2.getIndex());
		g.addEdge(v1.getIndex(),v3.getIndex());
		g.addEdge(v1.getIndex(),v2.getIndex());
		
		GraphWriter gr = new GraphWriter(g);
		gr.generate();
		
		
		try {
			gr.write("Test.gexf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
