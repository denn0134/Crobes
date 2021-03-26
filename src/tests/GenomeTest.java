package tests;

import crobes.genetics.genomics.Genome;
import crobes.genetics.genomics.Genomics;
import crobes.genetics.gui.Sequencer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class GenomeTest extends Application
{
    public static void main(String[] args) {
        Genomics.initializeGenomics();

        for(int i = 0; i < Genomics.genePools.count(); i++) {
            System.out.println(Genomics.genePools.getGenePools()[i]);
        }//end for i

        for(int i = 0; i < Genomics.lifeCycles.count(); i++) {
            System.out.println(Genomics.lifeCycles.getLifeCycles()[i]);
        }//end for i

        for(int i = 0; i < Genomics.metabolisms.count(); i++) {
            System.out.println(Genomics.metabolisms.getMetabolisms()[i]);
        }//end for i

        for(int i = 0; i < Genomics.motilities.count(); i++) {
            System.out.println(Genomics.motilities.getMotilities()[i]);
        }//end for i

        for(int i = 0; i < Genomics.renderers.count(); i++) {
            System.out.println(Genomics.renderers.getRenderers()[i]);
        }//end for i

        launch(args);
    }

    private BorderPane root;
    private Button btnGo;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new BorderPane();
        FlowPane pane = new FlowPane();
        root.setCenter(pane);

        btnGo = new Button("GO");
        pane.getChildren().add(btnGo);

        btnGo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Genome genome = new Genome();

                genome = Sequencer.sequenceGenome(genome, true, primaryStage);

                if(genome != null)
                    System.out.println(genome.toString());
            }
        });

        Scene scene = new Scene(root, 200, 200);
        primaryStage.setTitle("Genome Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
