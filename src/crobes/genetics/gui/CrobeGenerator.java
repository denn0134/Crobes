package crobes.genetics.gui;

import crobes.core.Crobe;
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

public class CrobeGenerator extends Stage
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

    /***
     * Show the Crobe Generator form and generates a new
     * Crobe based on the users genetic configuration
     * choices.  This action can be cancelled.
     * @param parentStage
     * @return Returns a new Crobe if the user configures
     *         the genetics and opts to create it, returns
     *         null if the user cancels the action.
     */
    public static Crobe createCrobe(Stage parentStage) {
        Crobe crobe = null;

        Stage crobeDialog = new CrobeGenerator();

        crobeDialog.initOwner(parentStage);
        crobeDialog.initModality(Modality.APPLICATION_MODAL);
        crobeDialog.showAndWait();

        return crobe;
    }

    private BorderPane root;
    private VBox leftPane;
    private BorderPane rightPane;
    private FlowPane bottomPane;

    public CrobeGenerator() {
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

        root.setLeft(leftPane);
        root.setRight(rightPane);
        root.setBottom(bottomPane);

        Scene scene = new Scene(root, 800, 400);
        setScene(scene);
    }
}
