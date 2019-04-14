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
		gString = "<graph>\n" 
				+ "<nodes>\n";
		
		String body;
		Vertex[] v = g.getVertices();
		int numVer = g.getNumberOfVertices();
		for(int i = 0; i < numVer; i++) {
			gString += "<node id=\"" + i + "\" label=\"" + v[i].getText() + "\" /> \n"; 
		}
		
		gString += "</nodes>\n" 
				+ "<edges>\n";
		
		for(int i = 0; i < numVer; i++) {
			for(int j = 0; j < v.length; j++){
				if(g.edgeExists(i,j)) {
					gString += "<edge id=\"" + i + "\" source=\"" + i + "\" target=\"" + j + "\"/>\n";
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
	
}
