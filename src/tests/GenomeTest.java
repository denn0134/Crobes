package tests;

import crobes.genetics.genePools.*;
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

        System.out.println("GenePool Types");
        for(int i = 0; i < Genomics.genePools.count(); i++) {
            String gps = Genomics.genePools.getGenePools()[i];
            Genomics.GenePoolInfo info = Genomics.genePools.getInfo(gps);
            System.out.println(gps);
            System.out.println(info.displayName);
            System.out.println(info.description);
            System.out.println();
        }//end for i

        System.out.println(Genomics.genePools.getInfo(LifeCycle.class.getSimpleName()).displayName);
        for(int i = 0; i < Genomics.lifeCycles.count(); i++) {
            String lcs = Genomics.lifeCycles.getGenePools()[i];
            Genomics.GenePoolInfo info = Genomics.lifeCycles.getInfo(lcs);
            System.out.println(lcs);
            System.out.println(info.displayName);
            System.out.println(info.description);
            System.out.println(info.taxanomicName);
            System.out.println();
        }//end for i

        System.out.println(Genomics.genePools.getInfo(Metabolism.class.getSimpleName()).displayName);
        for(int i = 0; i < Genomics.metabolisms.count(); i++) {
            String mbs = Genomics.metabolisms.getGenePools()[i];
            Genomics.GenePoolInfo info = Genomics.metabolisms.getInfo(mbs);
            System.out.println(mbs);
            System.out.println(info.displayName);
            System.out.println(info.description);
            System.out.println(info.taxanomicName);
            System.out.println();
        }//end for i

        System.out.println(Genomics.genePools.getInfo(Motility.class.getSimpleName()).displayName);
        for(int i = 0; i < Genomics.motilities.count(); i++) {
            String mts = Genomics.motilities.getGenePools()[i];
            Genomics.GenePoolInfo info = Genomics.motilities.getInfo(mts);
            System.out.println(mts);
            System.out.println(info.displayName);
            System.out.println(info.description);
            System.out.println(info.taxanomicName);
            System.out.println();
        }//end for i

        System.out.println(Genomics.genePools.getInfo(Renderer.class.getSimpleName()).displayName);
        for(int i = 0; i < Genomics.renderers.count(); i++) {
            String rds = Genomics.renderers.getGenePools()[i];
            Genomics.GenePoolInfo info = Genomics.renderers.getInfo(rds);
            System.out.println(rds);
            System.out.println(info.displayName);
            System.out.println(info.description);
            System.out.println(info.taxanomicName);
            System.out.println();
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

                genome.lifeCycle().genePool = SimpleLifeCycle.class.getSimpleName();
                genome.motility().genePool = ImmobileMotilty.class.getSimpleName();

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
