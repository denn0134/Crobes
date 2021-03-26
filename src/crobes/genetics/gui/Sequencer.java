package crobes.genetics.gui;

import crobes.core.Crobe;
import crobes.genetics.genomics.Genome;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Sequencer extends Stage
{
    //GUI constants
    public static final double GENEPOOL_BASE_HEIGHT = 10;
    public static final double GENEPOOL_DESC_HEIGHT = 20;
    public static final double GENEPOOL_GENES_HEIGHT = 50;

    public static final double GENE_PICKER_WIDTH = 775;
    public static final double GENE_NAME_WIDTH = 150;
    public static final double GENE_MT_WIDTH = 150;
    public static final double GENE_DOM_WIDTH = 75;
    public static final double GENE_GENOTYPE_WIDTH = 400;

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
    private VBox leftPane;
    private BorderPane rightPane;
    private FlowPane bottomPane;

    public Sequencer(Genome genome) {
        _genome = genome;

        root = new BorderPane();

        leftPane = new VBox();
        Label lblTaxa = new Label("Taxonomic name");

        TextField txtTaxa = new TextField();
        txtTaxa.setEditable(false);

        //ListView<GenePoolPicker> lstPools = new ListView<GenePoolPicker>();

        leftPane.getChildren().addAll(lblTaxa, txtTaxa);

        rightPane = new BorderPane();

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

        root.setLeft(leftPane);
        root.setRight(rightPane);
        root.setBottom(bottomPane);

        Scene scene = new Scene(root, 800, 400);
        setScene(scene);
    }
}
