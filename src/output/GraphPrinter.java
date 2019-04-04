package output;

import parser.ParseTreeNode;

public class GraphPrinter {
	
	private String outputString;
	
	private int labelIncrementer = 0;
	
	public GraphPrinter() {
		outputString = "digraph Output {\n"
					  + "a0 [label=\"entry\"]\n";
	}
	
	public void process(ParseTreeNode t) { 	
		
		
		if(!t.getType().contains("RULE")) {
			String label = "";
			labelIncrementer++;
			for(ParseTreeNode p  : t.getChildren()) {
				label += p.getType() + " ";
			}
			outputString += "a" + labelIncrementer + "[label=\"" + label + "\"]\n";
			outputString += "a" + (labelIncrementer - 1) + "->" + "a" + labelIncrementer + ";\n";
		}
		
	}
	
	public String getOutputString() {
		return outputString += " }";
	}

}
