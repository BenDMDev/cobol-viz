package main.java.ui.controllers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JPanel;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
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
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import main.java.graphs.Graph;
import main.java.graphs.GraphWriter;
import main.java.parsers.Parser;
import main.java.parsers.cobol.COBOLParser;
import main.java.scanners.Scanner;
import main.java.scanners.SourceFile;
import main.java.trees.ParseTreeNode;
import main.java.trees.visitors.cobol.COBOLVisitor;
import main.java.ui.models.PreviewSketch;

public class FileChooserController {

	
	@FXML 
	private BorderPane mainPane;
	
	@FXML
	private AnchorPane anchorPane;
	
	String fileName; 
	
	final FileChooser chooser = new FileChooser();
	
	
	@FXML
	private void setText() {
		
		File file = chooser.showOpenDialog(null);
		fileName = file.getName();
		if(file != null) {
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(file));
				SourceFile source = new SourceFile(reader);
				Scanner s = new Scanner(source);
				Parser p = new COBOLParser(s);
				
				s.scan();
				ParseTreeNode pt = p.parse(s.getCurrentToken());
				COBOLVisitor visitor = new COBOLVisitor();
				pt.accept(visitor);
				Graph g = visitor.getGraph();
				GraphWriter writer = new GraphWriter(g);
				writer.generate();
				writer.write("test.gexf");
				script();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void printMouseXY(MouseEvent e) {
		System.out.println(e.getX() + " : " + e.getY());
	}
	
	
	public void script() {
        //Init a project - and therefore a workspace
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        //Import file
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        Container container;
       
        
        try {
        	File file = chooser.showOpenDialog(null);
            container = importController.importFile(file);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }         
        
       
        
        //Append imported data to GraphAPI
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
        
        for(Node n : graph.getNodes()) {
        	System.out.println(n.x() + " : " + n.y());
        }
    
        
        
        //Preview configuration
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.BLACK));
        previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 50);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, 5);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 10);
        previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.WHITE);

        

        
        //New Processing target, get the PApplet
        G2DTarget target = (G2DTarget) previewController.getRenderTarget(RenderTarget.G2D_TARGET);
        final PreviewSketch previewSketch = new PreviewSketch(target);
        previewController.refreshPreview();

        //Add the applet to a JFrame and display
        JPanel frame = new JPanel();
        frame.setLayout(new BorderLayout());

        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(previewSketch, BorderLayout.CENTER);

        frame.setSize(800, 420);
        
        //Wait for the frame to be visible before painting, or the result drawing will be strange
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                previewSketch.resetZoom();
            }
        });
        
        
        
       
        frame.setVisible(true);
        // AnchorPane aPane = new AnchorPane();
        SwingNode swing = new SwingNode();        
        swing.setContent(frame);
        anchorPane.getChildren().add(swing);
        AnchorPane.setBottomAnchor(swing, 0.0d);
        AnchorPane.setTopAnchor(swing, 0.0d);
        AnchorPane.setRightAnchor(swing, 0.0d);
        AnchorPane.setLeftAnchor(swing, 0.0d);
      
       
    }
	
	
}
