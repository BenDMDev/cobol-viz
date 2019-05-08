package main.java.graphs.cobol;

import main.java.graphs.Vertex;

/**
 * Simple class representing vertex in control graph
 * @author Ben
 *
 */
public class ControlGraphVertex extends Vertex {

	
	public ControlGraphVertex(String label) {
		super(label);
	}
	
	public void setText(String text) {
		super.text = text;
	}
	
}
