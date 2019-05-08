package main.java.graphs;

/**
 * Simple vertex representing a node in a generic graph
 * @author Ben
 *
 */
public class Vertex {

	protected int index;
	protected String text;
	
	/**
	 * Default constructor
	 */
	public Vertex() {
		this(0);
	}
	
	/**
	 * Constructor with index value
	 * @param index
	 */
	public Vertex(int index) {
		this.index = index;
	}
	
	/**
	 * Constructor with text value
	 * @param text
	 */
	public Vertex(String text) {
		this.text = text;
	}
	
	/**
	 * Set Index position in Graph
	 * @param i
	 */
	public void setIndex(int i) {
		index = i;
	}
	
	/**
	 * Get index position in graph
	 * @return
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Get text label for node in graph
	 * @return
	 */
	public String getText() {
		return text;
	}
	
	
	
}
