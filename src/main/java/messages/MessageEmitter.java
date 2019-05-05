package main.java.messages;

public interface MessageEmitter {

	void addListener(MessageListener listener);
	void removeListener(MessageListener listener);
	void sendMessage(String message);
	void sendMessage(float x, float y);
	
}
