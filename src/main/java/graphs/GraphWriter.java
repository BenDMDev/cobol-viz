package main.java.graphs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GraphWriter {

	private Graph g;
	private String gString;
	static final String header = "<gexf xmlns=\"http://www.gexf.net/1.2draft\" "
			+ "xmlns:viz=\"http://www.gexf.net/1.1draft/viz\" "
			+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
			+ "xsi:schemaLocation=\"http://www.gexf.net/1.2draft "
			+ "http://www.gexf.net/1.2draft/gexf.xsd\" version=\"1.2\"> \n";
	
	public GraphWriter(Graph g) {
		this.g = g;
	}
	
	public void generate() {
		gString = "<graph defaultedgetype=\"directed\" type=\"static\">\n" 
				+ "<nodes>\n";
		
		String body;
		Vertex[] v = g.getVertices();
		int numVer = g.getNumberOfVertices();
		int numEdges = g.getNumberOfEdges();
		for(int i = 0; i < numVer; i++) {
			gString += "<node id=\"" + i + "\" label=\"" + v[i].getText() + "\" /> \n"; 
		}
		
		gString += "</nodes>\n" 
				+ "<edges>\n";
		int edges = 0;
		for(int i = 0; i < numVer; i++) {
			for(int j = 0; j < v.length; j++){
				if(g.edgeExists(i,j)) {
					gString += "<edge id=\"" + edges + "\" source=\"" + i + "\" target=\"" + j + "\"/>\n";
					edges++;
				}
			}
		}
		
		gString += "</edges>\n"
				+ "</graph>\n" +
				 "</gexf>";
	}
	
	public void write(String fileName) throws IOException {
		File file = new File(fileName);
		
		file.createNewFile();
		
		FileWriter writer = new FileWriter(file);
		
		writer.write(header + gString);
		writer.flush();
		writer.close();
		
		
	}
	
	public void write(File file) throws IOException {
				
		file.createNewFile();
		
		FileWriter writer = new FileWriter(file);
		
		writer.write(header + gString);
		writer.flush();
		writer.close();
		
		
	}
	
}
