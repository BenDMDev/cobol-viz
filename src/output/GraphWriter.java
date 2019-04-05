package output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import parser.ParseTreeNode;

public class GraphWriter{

	private GraphGenerator generator;
	
	public GraphWriter(GraphGenerator generator) {
		this.generator = generator;
	}
	
	public void generate(ParseTreeNode p) {
		generator.process(p);
	}
	
	public void writeToFile() throws IOException {
		File file = new File("Test.gv");
		
		file.createNewFile();
		
		FileWriter writer = new FileWriter(file);
		writer.write(generator.getOutputString());
		writer.flush();
		writer.close();
		
	}
	
	
	
}
