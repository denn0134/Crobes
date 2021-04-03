package tests;

import crobes.core.CrobeEnums;
import crobes.genetics.genePools.*;
import crobes.genetics.genomics.Genome;
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
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

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

        System.out.println("Genes");
        for(int i = 0; i < Genomics.genes.count(); i++) {
            String gns = Genomics.genes.getGenes()[i];
            Genomics.GeneInfo info = Genomics.genes.getInfo(gns);

            if(info != null) {
                System.out.println(gns);
                for(CrobeEnums.MutationType mt: info.allowedMutations) {
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

        Spinner<Integer> spnEdit = new Spinner<Integer>();
        SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999, 1, 1);
        svf.setConverter(new IntegerStringConverter());
        spnEdit.setEditable(true);
        spnEdit.setValueFactory(svf);

        Spinner<Double> spnDbl = new Spinner<Double>();
        SpinnerValueFactory<Double> dvf = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999, 1, 1);
        dvf.setConverter(new DoubleStringConverter());
        spnDbl.setEditable(true);
        spnDbl.setValueFactory(dvf);

        pane.getChildren().addAll(btnGo, spnEdit, spnDbl);

        btnGo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Genome genome = new Genome();

                genome.lifeCycle().genePool = SimpleLifeCycle.class.getSimpleName();
                genome.metabolism().genePool = BasePhotoMetabolism.class.getSimpleName();
                genome.motility().genePool = ImmobileMotilty.class.getSimpleName();
                genome.renderer().genePool = SimpleVisageRenderer.class.getSimpleName();

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
