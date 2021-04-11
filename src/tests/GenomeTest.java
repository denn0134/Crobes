package tests;

import crobes.core.CrobeConstants;
import crobes.core.CrobeEnums;
import crobes.genetics.genePools.*;
import crobes.genetics.genomics.Genome;
import crobes.genetics.genomics.GenomeEnum;
import crobes.genetics.genomics.Genomics;
import crobes.genetics.gui.Sequencer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class GenomeTest extends Application
{
    public static void main(String[] args) {
        Genomics.initializeGenomics();

        if(false) {
            System.out.println("GenePool Types");
            for (int i = 0; i < Genomics.genePools.count(); i++) {
                String gps = Genomics.genePools.getGenePools()[i];
                Genomics.GenePoolInfo info = Genomics.genePools.getInfo(gps);
                System.out.println(gps);
                System.out.println(info.displayName);
                System.out.println(info.description);
                System.out.println();
            }//end for i

            System.out.println(Genomics.genePools.getInfo(LifeCycle.class.getSimpleName()).displayName);
            for (int i = 0; i < Genomics.lifeCycles.count(); i++) {
                String lcs = Genomics.lifeCycles.getGenePools()[i];
                Genomics.GenePoolInfo info = Genomics.lifeCycles.getInfo(lcs);
                System.out.println(lcs);
                System.out.println(info.displayName);
                System.out.println(info.description);
                System.out.println(info.taxanomicName);
                System.out.println();
            }//end for i

            System.out.println(Genomics.genePools.getInfo(Metabolism.class.getSimpleName()).displayName);
            for (int i = 0; i < Genomics.metabolisms.count(); i++) {
                String mbs = Genomics.metabolisms.getGenePools()[i];
                Genomics.GenePoolInfo info = Genomics.metabolisms.getInfo(mbs);
                System.out.println(mbs);
                System.out.println(info.displayName);
                System.out.println(info.description);
                System.out.println(info.taxanomicName);
                System.out.println();
            }//end for i

            System.out.println(Genomics.genePools.getInfo(Motility.class.getSimpleName()).displayName);
            for (int i = 0; i < Genomics.motilities.count(); i++) {
                String mts = Genomics.motilities.getGenePools()[i];
                Genomics.GenePoolInfo info = Genomics.motilities.getInfo(mts);
                System.out.println(mts);
                System.out.println(info.displayName);
                System.out.println(info.description);
                System.out.println(info.taxanomicName);
                System.out.println();
            }//end for i

            System.out.println(Genomics.genePools.getInfo(Renderer.class.getSimpleName()).displayName);
            for (int i = 0; i < Genomics.renderers.count(); i++) {
                String rds = Genomics.renderers.getGenePools()[i];
                Genomics.GenePoolInfo info = Genomics.renderers.getInfo(rds);
                System.out.println(rds);
                System.out.println(info.displayName);
                System.out.println(info.description);
                System.out.println(info.taxanomicName);
                System.out.println();
            }//end for i

            System.out.println("Genes");
            for (int i = 0; i < Genomics.genes.count(); i++) {
                String gns = Genomics.genes.getGenes()[i];
                Genomics.GeneInfo info = Genomics.genes.getInfo(gns);

                if (info != null) {
                    System.out.println(gns);
                    for (CrobeEnums.MutationType mt : info.allowedMutations) {
                        System.out.println(mt.name());
                    }//end for each
                    System.out.println(info.geneEditorClass.getSimpleName());
                    System.out.println(info.genomeValueClass.getSimpleName());
                }//end if
                else {
                    System.out.println(gns + " was not registered");
                    System.out.println();
                }//end else
            }///end for i
        }//end if

        launch(args);
    }

    private BorderPane root;
    private Button btnGo;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new BorderPane();

        VBox pane = new VBox();

        Button btnConfig = new Button("Config");
        btnConfig.setMaxWidth(Double.MAX_VALUE);
        btnConfig.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                configGenome(primaryStage);
            }
        });

        Button btnInspect = new Button("Inspect");
        btnInspect.setMaxWidth(Double.MAX_VALUE);
        btnInspect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                inspectGenome(primaryStage);
            }
        });

        Button btnRand = new Button("Randomizer Test");
        btnRand.setMaxWidth(Double.MAX_VALUE);
        btnRand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Genomics.GenePoolInfo infoLc = Genomics.getGenePoolInfo(SimpleLifeCycle.class.getSimpleName());
                Genomics.GenePoolInfo infoMb = Genomics.getGenePoolInfo(BasePhotoMetabolism.class.getSimpleName());
                Genomics.GenePoolInfo infoMt = Genomics.getGenePoolInfo(ImmobileMotilty.class.getSimpleName());
                Genomics.GenePoolInfo infoRd = Genomics.getGenePoolInfo(SimpleVisageRenderer.class.getSimpleName());

                System.out.println("LifeCycle");
                System.out.println(infoLc.randomizer.toString());
                System.out.println("Metabolism");
                System.out.println(infoMb.randomizer.toString());
                System.out.println("Motility");
                System.out.println(infoMt.randomizer.toString());
                System.out.println("Renderer");
                System.out.println(infoRd.randomizer.toString());

            }
        });

        pane.getChildren().addAll(btnConfig, btnInspect, btnRand);

        root.setCenter(pane);

        Scene scene = new Scene(root, 200, 200);
        primaryStage.setTitle("Genome Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void configGenome(Stage parent) {
        Genome genome = new Genome();
        Genome result = Sequencer.sequenceGenome(genome, true, parent);

        if(result != null)
            System.out.println(genome.toString());
        else
            System.out.println("Cancelled by user");
    }

    private void inspectGenome(Stage parent) {

    }
}
