package main.java.ui.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import main.java.graphs.Graph;
import main.java.graphs.GraphGenerator;
import main.java.graphs.GraphWriter;
import main.java.graphs.cobol.CallGraphVertex;
import main.java.messages.MessageListener;
import main.java.parsers.Parser;
import main.java.parsers.cobol.COBOLParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTree;

/**
 * Represents current project
 * Holds references to data model, source files out output files
 * @author Ben
 *
 */
public class Project {

	private Map<String, GraphDataModel> dataModels;
	private Map<String, File> sourceFiles;
	private Map<String, File> outputFiles;	
	private String dirPath;
	private String projectName;
	private Parser parser;
	private Scanner scanner;
	private SourceFile source;
	private MessageListener listener;
	private String callGraphDirPath;
	private String controlGraphDirPath;
	private String sourceFileDirPath;
	private Document projectFile;

	public Project(String projectName, String dirPath) {
		System.out.println(dirPath);
		this.dirPath = dirPath;
		this.projectName = projectName;
		dataModels = new HashMap<String, GraphDataModel>();		
		sourceFiles = new HashMap<String, File>();
		outputFiles = new HashMap<String, File>();
		generateSubDirectories(dirPath);
		generateProjectFile();

	}
	
	public Project(File projectFile) {
		
		dataModels = new HashMap<String, GraphDataModel>();		
		sourceFiles = new HashMap<String, File>();
		outputFiles = new HashMap<String, File>();
		dirPath = projectFile.getParent();
		generateSubDirectories(dirPath);
		loadProjectFile(projectFile);
	}

	/**
	 * Add graph model
	 * @param key
	 * @param model
	 */
	public void addDataModel(String key, GraphDataModel model) {
		dataModels.put(key, model);
	}

	/**
	 * Get specific graph model
	 * @param key
	 * @return
	 */
	public GraphDataModel getModel(String key) {
		return dataModels.get(key);
	}

	/**
	 * Get specific output file
	 * @param key
	 * @return
	 */
	public File getOutputFile(String key) {
		return outputFiles.get(key);
	}

	/**
	 * Get specific source file
	 * @param key
	 * @return
	 */
	public File getSourceFile(String key) {
		return sourceFiles.get(key);
	}

	/**
	 * Add source file and save to current directory
	 * @param file
	 */
	public void addSourceFile(File file) {
		sourceFiles.put(file.getName(), file);
		saveSourceFile(file);
		addSourceToProjectFile(file.getName());
	}

	/**
	 * Add output file
	 * @param file
	 */
	public void addOutputFile(File file) {
		outputFiles.put(file.getName(), file);
	}
	
	/**
	 * Return all source files
	 * @return Map of source files
	 */
	public Map<String, File> getSourceFiles() {
		return sourceFiles;
	}
	
	/**
	 * Return all outputfiles
	 * @return map of output files
	 */
	public Map<String, File> getOutputFiles() {
		return outputFiles;
	}
 	

	/**
	 * Get Current projects directory
	 * @return
	 */
	public String getDirectoryPath() {
		return dirPath;
	}

	/**
	 * Get current project name
	 * @return
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * Parse file
	 * @param file
	 */
	public void parse(File file) {
		try {
			initSource(file);
			initScanner();
			initParser();
			parser.addListener(listener);
			scanner.scan();
			ParseTree tree = new ParseTree();
			tree.setRoot(parser.parse(scanner.getCurrentToken()));
			source.close();			
			generateModel(tree, file);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	/**
	 * Copy source from source directory to project directory
	 * @param file
	 */
	private void saveSourceFile(File file) {
		
		try {
			Path source = Paths.get(file.toURI());
			Path output = Paths.get(sourceFileDirPath + "\\" + file.getName());
			Files.copy(source, output, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}

	/**
	 * Generate graph model
	 * @param tree
	 * @param sourceFile
	 */
	private void generateModel(ParseTree tree, File sourceFile) {
		
		GraphGenerator graphGen = new GraphGenerator(tree);
		String modelName = sourceFile.getName().replaceFirst("[.][^.]+$", ".gexf");
		Graph graph = graphGen.generateGraph();
		GraphDataModel model = new GraphDataModel(graph, sourceFile);
		addDataModel(modelName, model);

		generateCallGraphOutput(graph, modelName);
		generateControlGraphOutput(graph);

	}

	
	/**
	 * Generate call graph
	 * @param graph
	 * @param fileName
	 */
	private void generateCallGraphOutput(Graph graph, String fileName) {
		
		GraphWriter writer = new GraphWriter();
		
		String output = writer.generate(GraphWriter.CALL_GRAPH, graph);
		saveToFile(writer, callGraphDirPath, fileName, output);
			

	}
	
	/**
	 * Generate control graphs
	 * @param graph
	 */
	private void generateControlGraphOutput(Graph graph) {
		GraphWriter writer = new GraphWriter();
		String output = "";
		for (int i = 0; i < graph.getNumberOfVertices(); i++) {
			CallGraphVertex v = (CallGraphVertex) graph.getVertex(i);
			output = writer.generate(GraphWriter.CONTROL_GRAPH, v.getGraph());
			saveToFile(writer, controlGraphDirPath, v.getText() + ".gexf", output);
			
		}
		
	}

	/**
	 * Helper method to save string output to file
	 * @param writer
	 * @param dirPath
	 * @param fileName
	 * @param output
	 */
	private void saveToFile(GraphWriter writer, String dirPath, String fileName, String output) {
		try {
			File file = new File(dirPath + "\\" + fileName);
			writer.write(file, output);
			addOutputFile(file);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}

	/**
	 * Initialise Parser
	 */
	private void initParser() {
		if (scanner != null)
			parser = new COBOLParser(scanner);
	}

	/**
	 * Initialise Scanner
	 */
	private void initScanner() {
		if (source != null)
			scanner = new Scanner(source);
	}

	/**
	 * Initialise source file
	 * @param currentFile
	 */
	private void initSource(File currentFile) {

		try {
			FileReader reader = new FileReader(currentFile);
			BufferedReader input = new BufferedReader(reader);
			source = new SourceFile(input);
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param listener
	 */
	public void addListener(MessageListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Generate new project XML file
	 */
	private void generateProjectFile() {
		
		
		try {
			DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = docBuildFactory.newDocumentBuilder();
			projectFile = builder.newDocument();
			
			Element project = projectFile.createElement("project");
			Attr attr = projectFile.createAttribute("name");
			attr.setValue(projectName);
			project.setAttributeNode(attr);
			projectFile.appendChild(project);
			
			Element sourceNode = projectFile.createElement("sourcefiles");
			Element graphNode = projectFile.createElement("callgraphs");
			Element controlNode = projectFile.createElement("controlgraphs");
			project.appendChild(sourceNode);
			project.appendChild(graphNode);
			project.appendChild(controlNode);
			
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
			DOMSource source = new DOMSource(projectFile);
			StreamResult result = new StreamResult(new File(dirPath + "\\project.xml"));
			transformer.transform(source, result);
					
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	

	/** 
	 * Append source to project file
	 * @param sourceName
	 */
	private void addSourceToProjectFile(String sourceName) {
		
		Node sourceRoot = projectFile.getElementsByTagName("sourcefiles").item(0);
		
		Element newSource = projectFile.createElement("source");
		Attr attr = projectFile.createAttribute("name");
		attr.setValue(sourceName);
		newSource.setAttributeNode(attr);
		sourceRoot.appendChild(newSource);
		
		

		try {

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer;
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(projectFile);
			StreamResult result = new StreamResult(new File(dirPath + "\\project.xml"));
			transformer.transform(source, result);
			
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Load source file 
	 * @param fileName
	 */
	private void loadSourceFile(String fileName) {
		File file = new File(sourceFileDirPath + "\\" + fileName);
		sourceFiles.put(file.getName(), file);
		
		
	}
	
	/**
	 * Load project XMl file
	 * @param file
	 */
	private void loadProjectFile(File file) {
		
		try {
			DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = docBuildFactory.newDocumentBuilder();
			projectFile = builder.parse(file);
			projectFile.getDocumentElement().normalize();
			Node sourceRoot = projectFile.getElementsByTagName("project").item(0);
			Element sourceElement = (Element) sourceRoot;
			projectName = sourceElement.getAttribute("name");
			
			
			NodeList nodes = projectFile.getElementsByTagName("source");
			
			for(int i = 0; i < nodes.getLength(); i++) {
				Node n = nodes.item(i);
				if(n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) n;
					System.out.println("Source: " + e.getAttribute("name"));
					loadSourceFile(e.getAttribute("name"));
				}
			}
			
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	/**
	 * Generate sub directories (if they don't exit
	 * @param dirPath
	 */
	private void generateSubDirectories(String dirPath) {
		
		new File(dirPath + "\\call_graphs").mkdir();
		callGraphDirPath = dirPath + "\\call_graphs";

		new File(dirPath + "\\control_graphs").mkdir();
		controlGraphDirPath = dirPath + "\\control_graphs";
		
		new File(dirPath + "\\source_files").mkdir();
		sourceFileDirPath = dirPath + "\\source_files";

	}

}
