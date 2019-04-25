package main.java.ui.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import main.java.graphs.Graph;
import main.java.graphs.Vertex;
import main.java.messages.MessageListener;
import main.java.ui.models.GraphDataModel;
import main.java.ui.models.GraphLoader;
import main.java.ui.models.Project;

public class MainController implements MessageListener {

	final FileChooser fileChooser = new FileChooser();
	final DirectoryChooser dirChooser = new DirectoryChooser();
	final GraphLoader graphLoader = new GraphLoader();
	private Project currentProject;

	@FXML
	private BorderPane mainPane;
	@FXML
	private TabPane previewTabs;
	@FXML
	private TextArea console;
	@FXML
	private MenuItem importMenuItem;
	@FXML
	private Button parseSourceButton;
	@FXML
	private CheckBox showLabelsChkBox;
	@FXML
	private Slider edgeOpacitySlider;
	@FXML
	private TreeView<String> projectNavigator;

	// Static Tree Nodes in TreeView
	private TreeItem<String> projectRootNode;
	private TreeItem<String> sourceRootNode;
	private TreeItem<String> graphRootNode;

	@FXML
	private void importSourceFile() {

		File file = fileChooser.showOpenDialog(null);

		if (file != null) {
			String sourceFile = file.getName();
			currentProject.addSourceFile(file);
			sourceRootNode.getChildren().add(new TreeItem<String>(sourceFile));

		}
	}

	@FXML
	private void parseSourceFile() {
		String fileName = getSelectedFile();
		File file = currentProject.getSourceFile(fileName);
		currentProject.parse(file);
		String outputFileName = file.getName().replaceFirst("[.][^.]+$", ".gexf");
		GraphDataModel m = currentProject.getModel(outputFileName);
		Graph g = m.getGraph();
		TreeItem<String> graphNode = new TreeItem<String>(outputFileName);
		for (int i = 0; i < g.getNumberOfVertices(); i++) {
			Vertex[] vert = g.getVertices();
			graphNode.getChildren().add(new TreeItem<String>((vert[i].getText())));

		}
		graphRootNode.getChildren().add(graphNode);
		createGraphPreviewContent(outputFileName);
	}

	@FXML
	private void createProject() throws IOException {

		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Create New Project");

		ButtonType confirmButton = new ButtonType("Create", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/resources/views/CreateProjectView.fxml"));
		Pane gridPane = loader.load();
		dialog.getDialogPane().setContent(gridPane);

		TextField projectNameField = (TextField) gridPane.lookup("#projectName");
		TextField directoryNameField = (TextField) gridPane.lookup("#projectDirectory");
		Button selectButton = (Button) gridPane.lookup("#selectDirectory");

		selectButton.setOnAction(e -> {
			File projectDir = dirChooser.showDialog(null);
			directoryNameField.setText(projectDir.getAbsolutePath());
		});

		dialog.setResultConverter(btn -> {
			if (btn == confirmButton)
				return new Pair<>(projectNameField.getText(), directoryNameField.getText());
			else
				return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();
		result.ifPresent(dirDetails -> {
			currentProject = new Project(dirDetails.getKey(), dirDetails.getValue());
			projectNavigatorInit(currentProject.getProjectName());
			importMenuItem.setDisable(false);
			currentProject.addListener(this);
		});

	}

	@FXML
	private void updateGraph() {
		graphLoader.setEdgeOpacity(edgeOpacitySlider.getValue());
		graphLoader.setShowLabels(showLabelsChkBox.selectedProperty().get());
		graphLoader.reload();
	}

	private String getSelectedFile() {
		return projectNavigator.getSelectionModel().getSelectedItem().getValue();
	}

	private void createGraphPreviewContent(String fileName) {

		JPanel frame = graphLoader.initProject(currentProject.getOutputFile(fileName));
		SwingNode swing = new SwingNode();
		createSwingContent(swing, frame);
		addGraphPreviewTab(swing, fileName);

	}

	private void createSwingContent(final SwingNode swingNode, JPanel frame) {
		SwingUtilities.invokeLater(() -> {

			swingNode.setContent(frame);
			// preview.resetZoom();
		});
	}

	private void addGraphPreviewTab(SwingNode node, String fileName) {

		AnchorPane anchor = new AnchorPane();
		anchor.getChildren().clear();
		anchor.getChildren().add(node);
		Tab tab = previewTabs.getTabs().get(0);
		tab.setText("Preview: " + fileName);

		tab.setContent(anchor);
		tab.setClosable(false);
		previewTabs.getSelectionModel().select(tab);
		AnchorPane.setBottomAnchor(node, 0.0d);
		AnchorPane.setTopAnchor(node, 0.0d);
		AnchorPane.setRightAnchor(node, 0.0d);
		AnchorPane.setLeftAnchor(node, 0.0d);

	}

	private void addSourcePreviewTab(String fileName) {
		Tab tab = new Tab(fileName);
		tab.setId(fileName);
		TextArea area = new TextArea();
		area.setEditable(false);
		File file = currentProject.getSourceFile(fileName);
		loadFileToTextArea(area, file);
		tab.setContent(area);

		previewTabs.getTabs().add(tab);
		previewTabs.getSelectionModel().select(tab);

	}

	private void loadFileToTextArea(TextArea area, File file) {
		try {
			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				area.appendText(line + "\n");
			}

			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void projectNavigatorInit(String projectName) {

		projectRootNode = new TreeItem<String>(projectName);
		sourceRootNode = new TreeItem<String>("Source Files");
		graphRootNode = new TreeItem<String>("Graphs");
		projectNavigator.setRoot(projectRootNode);
		projectRootNode.getChildren().add(sourceRootNode);
		projectRootNode.getChildren().add(graphRootNode);

		projectNavigator.setOnMouseClicked(e -> {
			TreeItem<String> item = projectNavigator.getSelectionModel().getSelectedItem();
			if (e.getClickCount() == 2) {
				handleNavigationDoubleClick();
				if (item != null && item.getParent() == sourceRootNode)
					parseSourceButton.setDisable(false);
				else
					parseSourceButton.setDisable(true);
			}
		});

	}

	private void handleNavigationDoubleClick() {

		TreeItem<String> item = projectNavigator.getSelectionModel().getSelectedItem();
		if (item != null) {
			if (item.getParent() == graphRootNode) {
				graphLoader.switchWorkSpace(item.getValue());
				graphLoader.reload();
				
			} else if (item.getParent() == sourceRootNode) {
				addSourcePreviewTab(item.getValue());
			}
		}

	}

	@Override
	public void listen(String input) {
		console.appendText(input + "\n");

	}

}
