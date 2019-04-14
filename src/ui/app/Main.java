package ui.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.JPanel;

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
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	@FXML
	private BorderPane mainPane;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		
		stage.setTitle("Hello World");
		
	    
		Parent root = FXMLLoader.load(getClass().getResource("mainView.fxml"));
		
      	
      	
      	
      	
      	//root.getChildren().add(box);
      	stage.setScene(new Scene(root, 1080, 720));
      	stage.show();
      	

		
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
            File file = new File(getClass().getResource("Test.gexf").toURI());
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
        layout.setOptimalDistance(200f);
         
        for (int i = 0; i < 100 && layout.canAlgo(); i++) {
           layout.goAlgo();
        }
        layout.endAlgo();
        
        //Preview configuration
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);       
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.BLACK));
        previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 50);
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

       // frame.setSize(800, 420);
        
        //Wait for the frame to be visible before painting, or the result drawing will be strange
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                previewSketch.resetZoom();
            }
        });
        
       
        frame.setVisible(true);   
        SwingNode swing = new SwingNode();
        
        swing.setContent(frame);
        mainPane.setCenter(swing);
    }
	
	
	
	
	

}
