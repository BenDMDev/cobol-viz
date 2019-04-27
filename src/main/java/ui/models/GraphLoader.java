package main.java.ui.models;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.gephi.appearance.api.AppearanceController;
import org.gephi.appearance.api.AppearanceModel;
import org.gephi.appearance.api.Function;
import org.gephi.appearance.plugin.RankingElementColorTransformer;
import org.gephi.appearance.plugin.RankingNodeSizeTransformer;
import org.gephi.graph.api.Column;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

public class GraphLoader {

	private double edgeOpacity;
	private double edgeThickness;
	private boolean showLabels;
	private boolean curvedEdges;
	private double edgeRadius;
	private Color backgroundColour;
	private Color edgeColour;
	private Color labelColour;

	private Map<String, Workspace> renderedGraphs;
	private PreviewController previewController;
	private ProjectController projectController;
	private ImportController importController;
	private PreviewSketch previewSketch;

	public GraphLoader() {
		edgeOpacity = 100.0;
		showLabels = false;
		edgeThickness = 5.0;
		curvedEdges = false;
		edgeRadius = 5.0;
		backgroundColour = Color.WHITE;
		edgeColour = Color.BLACK;
		labelColour = Color.BLACK;
		renderedGraphs = new HashMap<String, Workspace>();
		previewController = Lookup.getDefault().lookup(PreviewController.class);
		projectController = Lookup.getDefault().lookup(ProjectController.class);
		importController = Lookup.getDefault().lookup(ImportController.class);
	}

	public JPanel initProject(File file) {

		projectController.newProject();
		Workspace workspace = projectController.getCurrentWorkspace();
		Workspace activeWorkspace = projectController.duplicateWorkspace(workspace);

		AppearanceController appearanceController = Lookup.getDefault().lookup(AppearanceController.class);
		AppearanceModel appearanceModel = appearanceController.getModel();

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
//		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();
//		DirectedGraph graph = graphModel.getDirectedGraph();
//
//		Column outDegreeCol = graphModel.getNodeTable().getColumn("out");
//		Function degreeRanking = appearanceModel.getNodeFunction(graph, outDegreeCol,
//				RankingElementColorTransformer.class);
//		RankingElementColorTransformer degreeTransformer = (RankingElementColorTransformer) degreeRanking
//				.getTransformer();
//		degreeTransformer.setColors(new Color[] { new Color(0xFEF0D9), new Color(0xB30000) });
//		degreeTransformer.setColorPositions(new float[] { 0f, 1f });
//		appearanceController.transform(degreeRanking);
////
////		// Get Centrality
////		GraphDistance distance = new GraphDistance();
////		distance.setDirected(true);
////		distance.execute(graphModel);
////
//		// Rank size by centrality
//		Column centralityColumn = graphModel.getNodeTable().getColumn("loc");
//		
//		Function centralityRanking = appearanceModel.getNodeFunction(graph, centralityColumn,
//				RankingNodeSizeTransformer.class);
//		RankingNodeSizeTransformer centralityTransformer = (RankingNodeSizeTransformer) centralityRanking
//				.getTransformer();
//		centralityTransformer.setMinSize(10);
//		centralityTransformer.setMaxSize(50);
//		appearanceController.transform(centralityRanking);

		// New Processing target, get the PApplet
		G2DTarget target = (G2DTarget) previewController.getRenderTarget(RenderTarget.G2D_TARGET);
		previewSketch = new PreviewSketch(target);
		reload();

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
		previewModel.getProperties().putValue(PreviewProperty.DIRECTED, true);
		previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, showLabels);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, edgeOpacity);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, edgeThickness);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, edgeRadius);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, curvedEdges);
		previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, backgroundColour);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(edgeColour));
		previewModel.getProperties().putValue(PreviewProperty.NODE_OPACITY, 50);
		previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR,
				new DependantOriginalColor(labelColour));
		previewController.refreshPreview();
		previewSketch.refresh();
	}

	public void setEdgeOpacity(double opacity) {
		edgeOpacity = opacity;

	}

	public void setEdgeThickness(double thickness) {
		edgeThickness = thickness;
	}

	public void setShowLabels(boolean show) {
		showLabels = show;
	}

	public void setCurvedEdges(boolean curved) {
		curvedEdges = curved;
	}

	public void setEdgeRadius(double radius) {
		edgeRadius = radius;
	}

	public void setBackgroundColour(double red, double green, double blue) {

		backgroundColour = new Color((float) red, (float) green, (float) blue, 1.0f);
	}

	public void setEdgeColor(double red, double green, double blue) {

		edgeColour = new Color((float) red, (float) green, (float) blue, 1.0f);

	}

	public void setLabelColor(double red, double green, double blue) {

		labelColour = new Color((float) red, (float) green, (float) blue, 1.0f);
	}

	public int[] getBackgroundColour() {
		int[] rgb = { backgroundColour.getRed(), backgroundColour.getGreen(), backgroundColour.getBlue() };
		return rgb;
	}

	public int[] getEdgeColour() {
		int[] rgb = { edgeColour.getRed(), edgeColour.getGreen(), edgeColour.getBlue() };
		return rgb;
	}

	public int[] getLabelColour() {
		int[] rgb = { labelColour.getRed(), labelColour.getGreen(), labelColour.getBlue() };
		return rgb;
	}

	public double getEdgeRadius() {
		return edgeRadius;
	}

	public double getEdgeThickness() {
		return edgeThickness;
	}

	public double getEdgeOpacity() {
		return edgeOpacity;
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
		reload();
	}

}
