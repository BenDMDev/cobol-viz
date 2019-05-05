package main.java.messages;

public interface MessageListener {

	void listen(String input);
	void listen(float x, float y);
	void listenForNodeClicks(String nodeLabel);
	
}
