package main.java.ui.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import main.java.graphs.Graph;
import main.java.graphs.GraphGenerator;
import main.java.graphs.GraphWriter;
import main.java.messages.MessageListener;
import main.java.parsers.Parser;
import main.java.parsers.cobol.COBOLParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;
import main.java.trees.visitors.cobol.COBOLVisitor;

public class Project {

	private Map<String, GraphDataModel> dataModels;
	private Map<String, File> sourceFiles;
	private Map<String, File> outputFiles;
	private String dirPath;
	
	private String projectName;
	
	private Parser parser;
	private Scanner scanner;
	private SourceFile source;
	private ParseTree tree;
	private MessageListener listener;
	

	public Project(String projectName, String dirPath) {
		this.dirPath = dirPath;
		this.projectName = projectName;
		dataModels = new HashMap<String, GraphDataModel>();
		sourceFiles = new HashMap<String, File>();
		outputFiles = new HashMap<String, File>();
	}


	
	public void addDataModel(String key, GraphDataModel model ) {
		dataModels.put(key, model);
	}
	
	public GraphDataModel getModel(String key) {
		return dataModels.get(key);
	}
	
	public File getOutputFile(String key) {
		return outputFiles.get(key);
	}
	
	public File getSourceFile(String key) {
		return sourceFiles.get(key);
	}
	
	public void addSourceFile(File file) {		
		sourceFiles.put(file.getName(), file);
	}
	
	public void addOutputFile(File file) {
		outputFiles.put(file.getName(), file);
	}
	
	public String getDirectoryPath() {
		return dirPath;
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public void parse(File file) {
		try {
			initSource(file);
			initScanner();
			initParser();
			parser.addListener(listener);
			scanner.scan();
			tree = new ParseTree();
			tree.setRoot(parser.parse(scanner.getCurrentToken()));			
			source.close();			
			String fileName = file.getName().replaceFirst("[.][^.]+$",  ".gexf");
			generateModel(tree, fileName);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	private void generateModel(ParseTree tree, String fileName) {
		GraphGenerator graphGen = new GraphGenerator(tree);
		Graph graph = graphGen.generateGraph();
		GraphDataModel model = new GraphDataModel(graph);
		addDataModel(fileName, model);
		GraphWriter writer = new GraphWriter(graph);
		writer.generate();
		saveToFile(writer, fileName);
		
	}
	
	private void saveToFile(GraphWriter writer, String fileName) {
		try {
			File file = new File(dirPath + "\\" + fileName);
			writer.write(file);
			addOutputFile(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void initParser() {
		if(scanner != null) 
			parser = new COBOLParser(scanner);
	}
	
	private void initScanner() {
		if(source != null)
			scanner = new Scanner(source);
	}
	
	private void initSource(File currentFile) {
		
		try {			
			FileReader reader = new FileReader(currentFile);
			BufferedReader input = new BufferedReader(reader);
			source = new SourceFile(input);
			//source.addListener(listener);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addListener(MessageListener listener) {
		this.listener = listener;
	}
	
	
	
	
}
