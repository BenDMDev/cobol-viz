package main.java.graphs;

public class Vertex {

	protected int index;
	protected String text;
	
	public Vertex() {
		this(0);
	}
	
	public Vertex(int index) {
		this.index = index;
	}
	
	public Vertex(String text) {
		this.text = text;
	}
	
	public void setIndex(int i) {
		index = i;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getText() {
		return text;
	}
	
	
	
}
