package crobes.genetics.gui;

import crobes.core.Crobe;
import crobes.genetics.genePools.*;
import crobes.genetics.genomics.Genome;
import crobes.genetics.genomics.GenomePool;
import crobes.genetics.genomics.Genomics;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sequencer extends Stage
{
    //GUI constants
    public static final double GENEPOOL_BASE_HEIGHT = 10;
    public static final double GENEPOOL_DESC_HEIGHT = 20;
    public static final double GENEPOOL_GENES_HEIGHT = 50;

    public static final double GENE_PICKER_WIDTH = 300;
    public static final double GENE_NAME_WIDTH = 150;
    public static final double GENE_RAND_WIDTH = 50;
    public static final double GENE_MT_WIDTH = 150;
    public static final double GENE_DOM_WIDTH = 75;
    public static final double GENE_GENOTYPE_WIDTH = 400;
    public static final double GENE_WIDTH_MODIFIER = 20;

    private double getSequencerWidth() {
        return GENE_PICKER_WIDTH + GENE_NAME_WIDTH +
                GENE_RAND_WIDTH + GENE_MT_WIDTH +
                GENE_DOM_WIDTH + GENE_GENOTYPE_WIDTH + 20;
    }

    private Genome _genome;
    public Genome genome() {
        return _genome;
    }
    public void genome(Genome genome) {
        _genome = genome;
    }

    private boolean _cancelled;
    public boolean cancelled() {
        return _cancelled;
    }

    /***
     * Shows a Genome within an editor form to allow
     * the user to examine/modify the genomic configuration.
     * The form can allow editing or be set to read only
     * mode.  If in read only mode the method will
     * return null.
     * @param genome The Genome object to view/edit.
     * @param allowEdit Whether to allow editing.
     * @param parentStage Parent stage for the modal form.
     * @return Returns the edited Genome object; returns
     *         null if in read only mode.
     */
    public static Genome sequenceGenome(Genome genome, boolean allowEdit, Stage parentStage) {
        Genome result = null;

        Sequencer centrifuge = new Sequencer(genome);

        centrifuge.initOwner(parentStage);
        centrifuge.initModality(Modality.APPLICATION_MODAL);

        centrifuge.showAndWait();

        if(!centrifuge.cancelled())
            result = centrifuge.genome();

        return result;
    }

    private BorderPane root;
    private HBox topPane;
    private VBox leftPane;
    private BorderPane rightPane;
    private FlowPane bottomPane;
    private TextField txtTaxa;
    private GenePoolEditor poolEditor;

    public Sequencer(Genome genome) {
        _genome = genome;
        _cancelled = true;

        root = new BorderPane();

        //top pane - taxanomic name
        topPane = new HBox();
        topPane.setSpacing(5);

        Label lblTaxa = new Label("Taxonomy");

        txtTaxa = new TextField();
        txtTaxa.setEditable(false);
        HBox.setHgrow(txtTaxa, Priority.ALWAYS);

        topPane.getChildren().addAll(lblTaxa, txtTaxa);

        //left pane - list of genePool pickers for each
        //genePool interface
        leftPane = new VBox();
        leftPane.setMinWidth(GENE_PICKER_WIDTH);
        leftPane.setPrefWidth(GENE_PICKER_WIDTH);
        leftPane.setMaxWidth(GENE_PICKER_WIDTH);

        Label lblGenePools = new Label("Gene Pools");

        ObservableList<GenePoolPicker> gpl = FXCollections.observableArrayList();

        gpl.add(new GenePoolPicker(Genomics.genePools.getInfo(LifeCycle.class.getSimpleName()).displayName,
                Genomics.lifeCycles.getGenePools(),
                this,
                _genome.lifeCycle()));
        gpl.add(new GenePoolPicker(Genomics.genePools.getInfo(Metabolism.class.getSimpleName()).displayName,
                Genomics.metabolisms.getGenePools(),
                this,
                _genome.metabolism()));
        gpl.add(new GenePoolPicker(Genomics.genePools.getInfo(Motility.class.getSimpleName()).displayName,
                Genomics.motilities.getGenePools(),
                this,
                _genome.motility()));
        gpl.add(new GenePoolPicker(Genomics.genePools.getInfo(Renderer.class.getSimpleName()).displayName,
                Genomics.renderers.getGenePools(),
                this,
                _genome.renderer()));

        ListView genePoolList = new ListView(gpl);

        for(GenePoolPicker gpp: gpl) {
            gpp.setListView(genePoolList);
        }//end for each

        genePoolList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<GenePoolPicker>() {
            @Override
            public void changed(ObservableValue<? extends GenePoolPicker> observable, GenePoolPicker oldValue, GenePoolPicker newValue) {
                poolEditor.setGenePool(newValue.genomePool());
            }
        });

        updateTaxonomy();

        leftPane.getChildren().addAll(lblGenePools, genePoolList);

        //right pane - gene pool editor
        rightPane = new BorderPane();

        poolEditor = new GenePoolEditor();
        rightPane.setCenter(poolEditor);

        //bottom pane - OK and cancel buttons
        bottomPane = new FlowPane();
        bottomPane.setAlignment(Pos.CENTER_RIGHT);
        Button btnOk = new Button("OK");
        Button btnCancel = new Button("Cancel");
        bottomPane.getChildren().addAll(btnOk, btnCancel);

        btnOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _cancelled = false;
                close();
            }
        });
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _cancelled = true;
                close();
            }
        });

        root.setTop(topPane);
        root.setLeft(leftPane);
        root.setCenter(rightPane);
        root.setBottom(bottomPane);

        Scene scene = new Scene(root, getSequencerWidth(), 400);
        setScene(scene);
        setMinWidth(scene.getWidth() + 2 * GENE_WIDTH_MODIFIER);
    }

    public void updateTaxonomy() {
        txtTaxa.setText(_genome.getTaxonomy());
    }
}
