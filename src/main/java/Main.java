package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Cobol Viz");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/resources/views/MainView.fxml"));
		Parent root = loader.load(); 
		      	
		primaryStage.setScene(new Scene(root, 1080, 720));
		primaryStage.show();
		
	}

}
