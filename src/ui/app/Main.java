package ui.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.JPanel;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
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
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Hello World");
		SwingNode swing = new SwingNode();
		script(swing);
      	Pane root = new Pane();
      	BorderPane box = new BorderPane();
      //	box.setMinWidth(500);
      //	box.setMinHeight(500);
      	// box.getChildren().add(swing);
      	box.setLeft(new TextField("Test"));
      	box.setCenter(swing);
      	FlowPane fp = new FlowPane();
      	fp.getChildren().add(new TextField("Bottom Text"));
      	box.setBottom(fp);
      	//root.getChildren().add(box);
      	stage.setScene(new Scene(box, 1080, 720));
      	stage.show();
      	

		
	}
	
	public void script(SwingNode swing) {
        //Init a project - and therefore a workspace
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        //Import file
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        Container container;
        try {
            File file = new File(getClass().getResource("hello.gexf").toURI());
            container = importController.importFile(file);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        //Append imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), workspace);

        //Preview configuration
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.BLACK));
        previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
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
        swing.setContent(frame);
    }
	
	
	
	
	

}
