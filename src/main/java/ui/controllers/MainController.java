package main.java.ui.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
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
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import main.java.graphs.Graph;
import main.java.graphs.Vertex;
import main.java.messages.MessageListener;
import main.java.ui.models.GraphDataModel;
import main.java.ui.models.GraphLoader;
import main.java.ui.models.Project;

/**
 * Main Controller for UI
 * @author Ben
 *
 */
public class MainController implements MessageListener {

	final FileChooser fileChooser = new FileChooser();
	final DirectoryChooser dirChooser = new DirectoryChooser();
	final GraphLoader graphLoader = new GraphLoader();

	private Project currentProject;

	// Main view
	@FXML
	private BorderPane mainPane;
	@FXML
	private TabPane previewTabs;
	@FXML
	private Tab previewOptionsTab;
	@FXML
	private TextArea console;

	// Buttons and Menu Items
	@FXML
	private MenuItem importSourceMenuItem;
	@FXML
	private MenuItem importGraphMenuItem;
	@FXML
	private Button parseSourceButton;

	// Preview Options Tab
	@FXML
	private CheckBox showLabelsChkBox;
	@FXML
	private Slider edgeOpacitySlider;
	@FXML
	private TreeView<String> projectNavigator;
	@FXML
	private TextField edgeThicknessField;
	@FXML
	private CheckBox curvedEdgesChkBox;
	@FXML
	private TextField edgeRadiusField;
	@FXML
	private ColorPicker backgroundColourPicker;
	@FXML
	private ColorPicker edgeColourPicker;
	@FXML
	private ColorPicker labelColourPicker;
	@FXML
	private ChoiceBox<String> rankOptionsBox;
	@FXML
	private ChoiceBox<String> rankOptionsColourBox;

	@FXML
	private TextArea graphStatsTextArea;
	@FXML
	private TextArea nodeStatsTextArea;

	// Static Tree Nodes in TreeView
	private TreeItem<String> projectRootNode;
	private TreeItem<String> sourceRootNode;
	private TreeItem<String> graphRootNode;

	/**
	 * Import Source File via menu item
	 */
	@FXML
	private void importSourceFile() {

		File file = fileChooser.showOpenDialog(null);

		if (file != null) {
			String sourceFile = file.getName();
			currentProject.addSourceFile(file);
			addSourceNavigation(sourceFile);

		}
	}

	/**
	 * Import Graph File via menu item
	 */
	@FXML
	private void importGraphFile() {
		File file = fileChooser.showOpenDialog(null);

		if (file != null) {
			String outputFile = file.getName();
			currentProject.addOutputFile(file);
			graphRootNode.getChildren().add(new TreeItem<String>(outputFile));
			createGraphPreviewContent(currentProject.getOutputFile(outputFile));

		}
	}

	/**
	 * Parse currently selected file in tree view
	 */
	@FXML
	private void parseSourceFile() {

		String fileName = getSelectedFile();
		File file = currentProject.getSourceFile(fileName);
		currentProject.parse(file);
		String outputFileName = file.getName().replaceFirst("[.][^.]+$", ".gexf");
		initGraphNavigation(outputFileName);

	}

	/**
	 * Open Dialog window to create new project
	 * 
	 * @throws IOException
	 */
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
			if (projectDir != null) {
				directoryNameField.setText(projectDir.getAbsolutePath());
			}
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
			initProjectNavigation(currentProject.getProjectName());
			enableImportMenuItems();
			currentProject.addListener(this);
			graphLoader.addListener(this);
			createTabPaneListeners();
		});

	}

	/**
	 * Open Existing project
	 */
	@FXML
	private void openProject() {
		File projectDir = dirChooser.showDialog(null);

		File[] projectFile = projectDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {

				return name.startsWith("project") && name.endsWith("xml");
			}

		});

		if (projectFile.length > 0) {

			currentProject = new Project(projectFile[0]);
			initProjectNavigation(currentProject.getProjectName());
			loadSourceFiles();
			loadGraphFiles();
			enableImportMenuItems();
			currentProject.addListener(this);
			graphLoader.addListener(this);
			createTabPaneListeners();
		}
	}

	/**
	 * Handle update graph button in preview options tab
	 */
	@FXML
	private void updateGraph() {
		if (rankOptionsBox.getValue() != null)
			graphLoader.setRankingOptions(rankOptionsBox.getValue().toString());
		
		if(rankOptionsColourBox.getValue() != null)
			graphLoader.setRankingOptionsColour(rankOptionsColourBox.getValue().toString());

		graphLoader.setEdgeOpacity(edgeOpacitySlider.getValue());
		graphLoader.setShowLabels(showLabelsChkBox.selectedProperty().get());
		graphLoader.setEdgeThickness(Double.parseDouble(edgeThicknessField.getText()));
		graphLoader.setCurvedEdges(curvedEdgesChkBox.selectedProperty().getValue());
		graphLoader.setEdgeRadius(Double.parseDouble(edgeRadiusField.getText()));
		Color color = backgroundColourPicker.getValue();
		graphLoader.setBackgroundColour(color.getRed(), color.getGreen(), color.getBlue());
		color = edgeColourPicker.getValue();
		graphLoader.setEdgeColor(color.getRed(), color.getGreen(), color.getBlue());
		color = labelColourPicker.getValue();
		graphLoader.setLabelColor(color.getRed(), color.getGreen(), color.getBlue());
		graphLoader.reload();
	}

	/**
	 * Runs currently selected layout
	 */
	@FXML
	private void runLayout() {
		graphLoader.runLayout();
	}

	/**
	 * Get current selected file from navigator tree
	 * 
	 * @return
	 */
	private String getSelectedFile() {
		return projectNavigator.getSelectionModel().getSelectedItem().getValue();
	}

	/**
	 * Initialise a Gephi project and create preview content
	 * 
	 * @param file
	 *            to load
	 */
	private void createGraphPreviewContent(File file) {

		JPanel frame = graphLoader.initProject(file);
		SwingNode swing = new SwingNode();
		createSwingContent(swing, frame);
		addGraphPreviewTab(swing, file.getName());

		if (previewOptionsTab.isDisabled()) {
			previewOptionsTab.setDisable(false);
			addRankChoiceOptions();
		}

		updateGraphPreviewOptionsTab();
	}

	/**
	 * Load existing source files
	 */
	private void loadSourceFiles() {
		Map<String, File> sourceFiles = currentProject.getSourceFiles();

		for (String s : sourceFiles.keySet()) {
			addSourceNavigation(s);
		}

	}

	/**
	 * Load existing graph files
	 */
	private void loadGraphFiles() {
		Map<String, File> outputFiles = currentProject.getOutputFiles();

		for (String s : outputFiles.keySet()) {
			currentProject.parse(currentProject.getOutputFile(s));
			initGraphNavigation(s);
		}
	}

	/**
	 * Create swing content
	 * 
	 * @param swingNode
	 *            parent node to wrap swing content
	 * @param frame
	 *            Frame created via Gephi to be wrapped
	 */
	private void createSwingContent(final SwingNode swingNode, JPanel frame) {
		SwingUtilities.invokeLater(() -> {

			swingNode.setContent(frame);

		});
	}

	/**
	 * Initialise ranking options
	 */
	private void addRankChoiceOptions() {
		rankOptionsBox.getItems().add("LOC");
		rankOptionsBox.getItems().add("IN DEGREE");
		rankOptionsBox.getItems().add("OUT DEGREE");
		rankOptionsBox.getItems().add("COMPLEXITY");
		rankOptionsBox.getItems().add("CENTRALITY");
		
		rankOptionsColourBox.getItems().add("LOC");
		rankOptionsColourBox.getItems().add("IN DEGREE");
		rankOptionsColourBox.getItems().add("OUT DEGREE");
		rankOptionsColourBox.getItems().add("COMPLEXITY");
		rankOptionsColourBox.getItems().add("CENTRALITY");

	}

	/**
	 * Set current graphloaders preview options based on preview options tab
	 */
	private void updateGraphPreviewOptionsTab() {

		edgeThicknessField.setText(Double.toString(graphLoader.getEdgeThickness()));
		edgeRadiusField.setText(Double.toString(graphLoader.getEdgeRadius()));
		edgeOpacitySlider.adjustValue(graphLoader.getEdgeOpacity());
		int[] rgb = graphLoader.getBackgroundColour();
		backgroundColourPicker.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));
		rgb = graphLoader.getEdgeColour();
		edgeColourPicker.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));
		rgb = graphLoader.getLabelColour();
		labelColourPicker.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));

	}

	/**
	 * Add a preview tab
	 * 
	 * @param node
	 *            Gephi content to be added
	 * @param fileName
	 *            file name of GEXF file
	 */
	private void addGraphPreviewTab(SwingNode node, String fileName) {

		AnchorPane anchor = new AnchorPane();
		anchor.getChildren().clear();
		anchor.getChildren().add(node);
		Tab tab = new Tab();

		tab.setText(fileName);
		tab.setId(fileName);

		tab.setOnCloseRequest(e -> {
			graphLoader.removeWorkspace(fileName);
		});

		tab.setContent(anchor);
		tab.setClosable(true);
		previewTabs.getTabs().add(tab);
		previewTabs.getSelectionModel().select(tab);
		AnchorPane.setBottomAnchor(node, 0.0d);
		AnchorPane.setTopAnchor(node, 0.0d);
		AnchorPane.setRightAnchor(node, 0.0d);
		AnchorPane.setLeftAnchor(node, 0.0d);

	}

	/**
	 * Opens and previews a source file
	 * 
	 * @param fileName
	 */
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

	/**
	 * Enables import after project created
	 */
	private void enableImportMenuItems() {
		importSourceMenuItem.setDisable(false);
		importGraphMenuItem.setDisable(false);
	}

	/**
	 * Loads source file to text area
	 * 
	 * @param area
	 *            to be loaded into
	 * @param file
	 *            to be loaded
	 */
	private void loadFileToTextArea(TextArea area, File file) {
		try {
			Scanner scanner = new Scanner(file);
			int lineNum = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				area.appendText(lineNum + ": " + line + "\n");
				lineNum++;
			}

			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialise the project nav tree
	 * 
	 * @param projectName
	 */
	private void initProjectNavigation(String projectName) {

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

			} else {
				if (item != null && item.getParent() == sourceRootNode)
					parseSourceButton.setDisable(false);
				else
					parseSourceButton.setDisable(true);
			}
		});

	}

	/**
	 * Add source file to navigation tree
	 * @param fileName
	 */
	private void addSourceNavigation(String fileName) {

		sourceRootNode.getChildren().add(new TreeItem<String>(fileName));

	}

	/**
	 * Initialise the graph tree when loading GEXF files
	 * 
	 * @param graphName
	 *            name of GEXF file
	 */
	private void initGraphNavigation(String graphName) {

		GraphDataModel dataModel = currentProject.getModel(graphName);
		Graph graph = dataModel.getGraph();
		TreeItem<String> graphNode = new TreeItem<String>(graphName);
		for (int i = 0; i < graph.getNumberOfVertices(); i++) {
			Vertex[] vert = graph.getVertices();
			graphNode.getChildren().add(new TreeItem<String>((vert[i].getText())));

		}
		graphRootNode.getChildren().add(graphNode);
		createGraphPreviewContent(currentProject.getOutputFile(graphName));

	}

	/**
	 * Handle clicks in navigation view
	 */
	private void handleNavigationDoubleClick() {

		TreeItem<String> item = projectNavigator.getSelectionModel().getSelectedItem();
		if (item != null) {
			if (item.getParent() == graphRootNode) {
				Tab activeTab = getActiveTab(item.getValue());

				if (activeTab != null) {
					previewTabs.getSelectionModel().select(activeTab);
				} else {
					createGraphPreviewContent(currentProject.getOutputFile(item.getValue()));
				}
				graphLoader.switchWorkSpace(item.getValue());
				graphLoader.reload();
				updateGraphStatsView();

			} else if (item.getParent() == sourceRootNode) {
				addSourcePreviewTab(item.getValue());
			} else if (item.getParent().getParent() == graphRootNode) {
				createGraphPreviewContent(currentProject.getOutputFile(item.getValue() + ".gexf"));
			}
		}

	}

	/**
	 * Update stats view for node
	 * 
	 * @param graphLabel
	 *            current graph selected
	 */
	private void updateNodeStatsView(String graphLabel) {

		String activeGraph = previewTabs.getSelectionModel().getSelectedItem().getId();
		GraphDataModel model = currentProject.getModel(activeGraph);

		nodeStatsTextArea.clear();
		nodeStatsTextArea.appendText("Node: " + graphLabel + "\n");
		nodeStatsTextArea.appendText("Lines of Code: " + model.getLinesOfCode(graphLabel) + "\n");
		nodeStatsTextArea.appendText("Cyclomatic Complexity: " + model.getCyclomaticComplexity(graphLabel) + "\n");
		nodeStatsTextArea.appendText("In Degree: " + model.getInDegree(graphLabel) + "\n");
		nodeStatsTextArea.appendText("Out Degree: " + model.getOutDegree(graphLabel) + "\n");

	}

	/**
	 * Update graph view
	 */
	private void updateGraphStatsView() {

		Tab selectedItem = previewTabs.getSelectionModel().getSelectedItem();
		String activeGraph = selectedItem.getId();
		GraphDataModel model = currentProject.getModel(activeGraph);

		graphStatsTextArea.clear();
		if (model != null) {
			graphStatsTextArea.appendText("Number Of Nodes " + model.getGraph().getNumberOfVertices() + "\n");
			graphStatsTextArea.appendText("Number Of Eges " + model.getGraph().getNumberOfEdges() + "\n");
			graphStatsTextArea.appendText("Total Lines Of Code " + model.getTotalLinesOfCode() + "\n");
		}

	}

	/**
	 * Create listeners for Tab PAne
	 */
	private void createTabPaneListeners() {
		previewTabs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				if (newValue != null && newValue.getId().contains(".gexf")) {
					graphLoader.switchWorkSpace(newValue.getId());
					graphLoader.reload();
					updateGraphStatsView();
				}
			}

		});

	}

	/**
	 * Get currently active tab
	 * @param id of Tab 
	 * @return
	 */
	private Tab getActiveTab(String id) {
		Tab activeTab = null;
		for (Tab t : previewTabs.getTabs()) {
			if (t.getId().equals(id))
				activeTab = t;
		}
		return activeTab;
	}

	@Override
	public void listen(String input) {
		console.appendText(input + "\n");

	}

	@Override
	public void listen(float x, float y) {
		console.appendText("X Pos: " + x + "Y Pos: " + y + "\n");

	}

	@Override
	public void listenForNodeClicks(String nodeLabel) {

		updateNodeStatsView(nodeLabel);

	}

}
