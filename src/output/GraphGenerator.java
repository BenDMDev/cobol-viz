package output;

import parser.ParseTreeNode;

public class GraphGenerator {
	
	private String outputString;
	
	private int labelIncrementer = 0;
	
	public GraphGenerator() {
		outputString = "digraph Output {\n"
					  + "a0 [label=\"entry\"]\n";
	}
	
	public void process(ParseTreeNode t) { 			
		
		System.out.println(t.getType());
		if(t.getType().contains("RULE: Move")) {
			String label = "";
			labelIncrementer++;
			for(ParseTreeNode p1  : t.getChildren()) {
				label += p1.getType() + " ";
			}
			outputString += "a" + labelIncrementer + "[label=\"" + label + "\"]\n";
			outputString += "a" + (labelIncrementer - 1) + "->" + "a" + labelIncrementer + ";\n";
		} else {
			for(ParseTreeNode p : t.getChildren()) {
				process(p);
			}
		}
		
	
		
	}
	
	public String getOutputString() {
		return outputString += " }";
	}

}
