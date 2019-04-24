package main.java.ui.controllers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeView;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import main.java.graphs.Graph;
import main.java.graphs.Vertex;
import main.java.ui.models.GraphDataModel;
import main.java.ui.models.PreviewSketch;
import main.java.ui.models.Project;

public class MainController {

	private GraphDataModel dataModel;
	

	@FXML
	private BorderPane mainPane;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	TreeView<String> treeView;

	String sourceFile;
	String outputFile;
	String projectName;
	File projectDir;

	final FileChooser chooser = new FileChooser();
	final DirectoryChooser dirChooser = new DirectoryChooser();

	@FXML
	private void openFile() {

		File file = chooser.showOpenDialog(null);
		sourceFile = file.getName();
		if (file != null) {

			dataModel = new GraphDataModel(file);
			dataModel.setWorkingDir(projectDir.getAbsolutePath());
			dataModel.parse();
			Graph g = dataModel.generateGraph();
			outputFile = dataModel.getOutputFileName();
			// script();
			System.out.println(outputFile);
			
			
			TreeItem<String> sourceNode = new TreeItem<String>("Source File");
			treeView.getRoot().getChildren().add(sourceNode);
			sourceNode.getChildren().add(new TreeItem<String>(sourceFile));
			TreeItem<String> graphs = new TreeItem<String>("Graphs");
			TreeItem<String> graphNode = new TreeItem<String>(outputFile);
			
			for (int i = 0; i < g.getNumberOfVertices(); i++) {
				Vertex[] vert = g.getVertices();
				graphNode.getChildren().add(new TreeItem<String>((vert[i].getText())));

			}
			graphs.getChildren().add(graphNode);
			treeView.getRoot().getChildren().add(graphs);
			
			treeView.setOnMouseClicked(e -> {
				if(e.getClickCount() == 2) {
					TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
					if(item.getValue().contains(".gexf"))	
						script();
						
				}
			});
		}
	}

	@FXML
	private void createProject() throws IOException {

		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Create New Project");

		ButtonType confirmButton = new ButtonType("Create", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);
		
		Pane gridPane = FXMLLoader.load(getClass().getResource("../views/CreateProjectView.fxml"));
		dialog.getDialogPane().setContent(gridPane);
		
		
		TextField projectNameField = (TextField) gridPane.lookup("#projectName");
		TextField directoryNameField = (TextField) gridPane.lookup("#projectDirectory");
		Button selectButton = (Button) gridPane.lookup("#selectDirectory");
		

		selectButton.setOnAction(e -> {
			projectDir = dirChooser.showDialog(null);
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
			projectName = dirDetails.getKey();
		});
		
		
		TreeItem<String> rootNode = new TreeItem<String>(projectName);
		rootNode.setExpanded(true);
		treeView.setRoot(rootNode);

	}

	public void printMouseXY(MouseEvent e) {
		System.out.println(e.getX() + " : " + e.getY());
	}

	public void script() {
		// Init a project - and therefore a workspace
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		// Import file
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		Container container;

		try {
			File file = new File(outputFile);	
			
			container = importController.importFile(file);
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		// Append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), workspace);

		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();
		DirectedGraph graph = graphModel.getDirectedGraph();

		YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
		layout.setGraphModel(graphModel);
		layout.initAlgo();
		layout.resetPropertiesValues();

		layout.setOptimalDistance(500f);

		for (int i = 0; i < 100 && layout.canAlgo(); i++) {
			layout.goAlgo();
		}
		layout.endAlgo();
		

		// Preview configuration
		PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
		PreviewModel previewModel = previewController.getModel();
		previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.FALSE);
		previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR,
				new DependantOriginalColor(Color.BLACK));
		previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 100);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, 5);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 5);
		previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.WHITE);

		// New Processing target, get the PApplet
		G2DTarget target = (G2DTarget) previewController.getRenderTarget(RenderTarget.G2D_TARGET);
		final PreviewSketch previewSketch = new PreviewSketch(target);
		previewController.refreshPreview();

		// Add the applet to a JFrame and display
		JPanel frame = new JPanel();
		frame.setLayout(new BorderLayout());

		frame.add(previewSketch, BorderLayout.CENTER);
	
		frame.setVisible(true);		
		SwingNode swing = new SwingNode();		
		createSwingContent(swing, frame, previewSketch);
		anchorPane.getChildren().add(swing);
		AnchorPane.setBottomAnchor(swing, 0.0d);
		AnchorPane.setTopAnchor(swing, 0.0d);
		AnchorPane.setRightAnchor(swing, 0.0d);
		AnchorPane.setLeftAnchor(swing, 0.0d);
	

	}
	


	private void createSwingContent(final SwingNode swingNode, JPanel frame, PreviewSketch preview) {
		SwingUtilities.invokeLater(() -> {
			swingNode.setContent(frame);
			preview.resetZoom();
		});
	}

}
