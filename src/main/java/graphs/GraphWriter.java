package main.java.graphs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import main.java.graphs.cobol.CallGraphVertex;

public class GraphWriter {

	public static final int CONTROL_GRAPH = 0;
	public static final int CALL_GRAPH = 1;
	private String outputString;	
	static final String header = "<gexf xmlns=\"http://www.gexf.net/1.2draft\" "
			+ "xmlns:viz=\"http://www.gexf.net/1.1draft/viz\" "
			+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
			+ "xsi:schemaLocation=\"http://www.gexf.net/1.2draft "
			+ "http://www.gexf.net/1.2draft/gexf.xsd\" version=\"1.2\"> \n";
	
	public GraphWriter() {
		
	}
	
	public String generate(int type, Graph graph) {	
		if(type == CALL_GRAPH)
			generateCallGraph(graph);
		else if (type == CONTROL_GRAPH) 
			generateControlGraph(graph);
		
		return outputString;
		
	}
	
	
	
	public void generateControlGraph(Graph controlGraph) {
		outputString = "<graph defaultedgetype=\"directed\" type=\"static\">\n"
							+ "<nodes>\n";
		
		Vertex[] v = controlGraph.getVertices();
		int numVer = controlGraph.getNumberOfVertices();
		
		for(int i = 0; i < numVer; i++) {
			String label = v[i].getText();
			
			label = label.replace("<","less than");
			
			outputString += "<node id=\"" + i + "\" label=\"" + label + "\"/> \n";
		}
	
		outputString += "</nodes>\n" 
				+ "<edges>\n";
		
		int edges = 0;
		for(int i = 0; i < numVer; i++) {
			for(int j = 0; j < v.length; j++){
				if(controlGraph.edgeExists(i,j)) {
					outputString += "<edge id=\"" + edges + "\" source=\"" + i + "\" target=\"" + j + "\"/>\n";
					edges++;
				}
			}
		}
		
		outputString += "</edges>\n"
				+ "</graph>\n" +
				 "</gexf>";
		

	}
	
	public void generateCallGraph(Graph graph) {
		
		outputString = "<graph defaultedgetype=\"directed\" type=\"static\">\n" 
				+ "<attributes class=\"node\">\n" 
				+ "<attribute id=\"loc\" title=\"linesOfCode\" type=\"int\" />"
				+ "<attribute id=\"in\" title=\"inDegree\" type=\"int\" />"
				+ "<attribute id=\"out\" title=\"outDegree\" type=\"int\" />"
				+ "</attributes >"	
				+ "<nodes>\n";
		
		
		Vertex[] v = graph.getVertices();
		int numVer = graph.getNumberOfVertices();
		
		for(int i = 0; i < numVer; i++) {
			outputString += "<node id=\"" + i + "\" label=\"" + v[i].getText() + "\"> \n";
			outputString += "<attvalues>\n" 
						+ "<attvalue for=\"loc\" value=\"" +
						((CallGraphVertex)v[i]).getNumberOfLines() + "\"/> \n"						
						+ "<attvalue for=\"in\" value=\"" +
						graph.inDegree(i) + "\"/> \n"
						+ "<attvalue for=\"out\" value=\"" +
						graph.outDegree(i) + "\"/> \n"
						+"</attvalues>\n"
						+"</node>";
		}
		
		outputString += "</nodes>\n" 
				+ "<edges>\n";
		int edges = 0;
		for(int i = 0; i < numVer; i++) {
			for(int j = 0; j < v.length; j++){
				if(graph.edgeExists(i,j)) {
					int weight = graph.getEdgeWeight(i,j);
					outputString += "<edge id=\"" + edges + "\" source=\"" + i + "\" target=\"" + j + "\" "
							+ "weight=\"" + weight + "\" />\n";
					edges++;
				}
			}
		}
		
		outputString += "</edges>\n"
				+ "</graph>\n" +
				 "</gexf>";
	}
	
	public void write(String fileName) throws IOException {
		File file = new File(fileName);
		write(file);
	
		
		
	}
	
	public void write(File file) throws IOException {
				
		file.createNewFile();
		
		FileWriter writer = new FileWriter(file);
		
		writer.write(header + outputString);		
		writer.flush();		
		writer.close();		
		
	}
	
	public void write(File file, String output) throws IOException {
		file.createNewFile();
		
		FileWriter writer = new FileWriter(file);
		
		writer.write(header + output);		
		writer.flush();		
		writer.close();	
	}
	
}
