package intermediate.graph;

public class Vertex {

	private int index;
	private String text;
	
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
