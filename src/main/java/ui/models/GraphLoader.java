package main.java.ui.models;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.preview.PreviewControllerImpl;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

public class GraphLoader {

	private double edgeOpacity;
	private boolean showLabels;

	private Map<String, Workspace> renderedGraphs;
	private PreviewController previewController;
	private ProjectController projectController;
	private ImportController importController;
	private PreviewSketch previewSketch;

	public GraphLoader() {
		edgeOpacity = 100.0;
		showLabels = false;
		renderedGraphs = new HashMap<String, Workspace>();
		previewController = Lookup.getDefault().lookup(PreviewController.class);
		projectController = Lookup.getDefault().lookup(ProjectController.class);
		importController = Lookup.getDefault().lookup(ImportController.class);
	}

	public JPanel initProject(File file) {

		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		Workspace activeWorkspace = projectController.duplicateWorkspace(workspace);

		// Import file
		Container container;

		try {
			container = importController.importFile(file);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		// Append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), activeWorkspace);

		runLayout();
		// Preview configuration		
		PreviewModel previewModel = previewController.getModel();
		previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, showLabels);
		// previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR,
		// new DependantOriginalColor(Color.BLACK));
		// previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED,
		// Boolean.FALSE);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, edgeOpacity);
		// previewModel.getProperties().putValue(PreviewProperty.EDGE_THICKNESS,
		// 5);
		// previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS,
		// 5);
		// previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR,
		// Color.WHITE);

		// New Processing target, get the PApplet
		G2DTarget target = (G2DTarget) previewController.getRenderTarget(RenderTarget.G2D_TARGET);
		previewSketch = new PreviewSketch(target);
		previewController.refreshPreview();

		// Add the applet to a JFrame and display
		JPanel frame = new JPanel();
		frame.setLayout(new BorderLayout());
		frame.add(previewSketch, BorderLayout.CENTER);
		frame.setVisible(true);
		renderedGraphs.put(file.getName(), activeWorkspace);
		return frame;

	}

	public void switchWorkSpace(String key) {
		Workspace ws = renderedGraphs.get(key);
		projectController.openWorkspace(ws);

	}

	public void reload() {
		PreviewModel previewModel = previewController.getModel();
		previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, showLabels);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, edgeOpacity);
		previewController.refreshPreview();
		previewSketch.refresh();
	}

	public void setEdgeOpacity(double opacity) {
		edgeOpacity = opacity;
		
	}

	public void setShowLabels(boolean show) {
		showLabels = show;
	}

	public void runLayout() {
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();
		YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
		layout.setGraphModel(graphModel);
		layout.initAlgo();
		layout.resetPropertiesValues();

		layout.setOptimalDistance(500f);

		for (int i = 0; i < 100 && layout.canAlgo(); i++) {
			layout.goAlgo();
		}
		layout.endAlgo();
	}

}
