package main.java.ui.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import main.java.graphs.Graph;
import main.java.graphs.GraphGenerator;
import main.java.graphs.GraphWriter;
import main.java.parsers.Parser;
import main.java.parsers.cobol.COBOLParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.visitors.cobol.COBOLVisitor;

public class GraphDataModel {
	
	private File currentFile;
	private String projectDir;
	private String inputFileName;
	
	private GraphGenerator graphGenerator;
	private Parser parser;
	private Scanner scanner;
	private SourceFile source;
	private ParseTree tree;
	
	public GraphDataModel(File file) {		
		currentFile = file;
		inputFileName = file.getName().replaceFirst("[.][^.]+$",  "");
		
	}
	
	public void setWorkingDir(String dirName) {
		projectDir = dirName;
	}
	
	
	public Graph generateGraph() {
		
		Graph g = null;
		
		try {
			COBOLVisitor visitor = new COBOLVisitor();
			tree.accept(visitor);
			g = visitor.getGraph();
			GraphWriter writer = new GraphWriter(g);
			writer.generate();
			writer.write(projectDir + "\\" + inputFileName + ".gexf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return g;
	}
	
	
	public void parse() {
		try {
			initSource();
			initScanner();
			initParser();
			scanner.scan();
			tree = new ParseTree();
			tree.setRoot(parser.parse(scanner.getCurrentToken()));
			
			source.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public String getOutputFileName() {
		return projectDir + "\\" + inputFileName + ".gexf";
	}
	
	private void initParser() {
		if(scanner != null) 
			parser = new COBOLParser(scanner);
	}
	
	private void initScanner() {
		if(source != null)
			scanner = new Scanner(source);
	}
	
	private void initSource() {
		
		try {			
			FileReader reader = new FileReader(currentFile);
			BufferedReader input = new BufferedReader(reader);
			source = new SourceFile(input);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
