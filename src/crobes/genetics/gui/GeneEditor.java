package crobes.genetics.gui;

import crobes.core.CrobeEnums;
import crobes.genetics.genomics.GenomeGene;
import crobes.genetics.genomics.Genomics;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class GeneEditor extends GridPane
{
    private GenomeGene _genomeGene;
    public GenomeGene genomeGene() {
        return _genomeGene;
    }

    TextField txtGeneName;
    CheckBox chkRandom;
    ComboBox<String> cmbMutationType;
    HBox hbxRangeHierarchy;
    HBox hbxGenoType;

    private String _genePool;
    public String genePool() {
        return _genePool;
    }
    public void genePool(String genePool) {
        _genePool = genePool;
    }

    public String name() {
        return txtGeneName.getText();
    }
    public void name(String name) {
        txtGeneName.setText(name);
    }

    public GeneEditor(GenomeGene gene) {
        super();

        _genomeGene = gene;
        Genomics.GeneInfo info = Genomics.getGeneInfo(_genomeGene.geneType);

        setPrefHeight(40.0);

        getColumnConstraints().add(new ColumnConstraints(Sequencer.GENE_NAME_WIDTH, Sequencer.GENE_NAME_WIDTH, Double.MAX_VALUE, Priority.ALWAYS, HPos.CENTER, true));
        getColumnConstraints().add(new ColumnConstraints(Sequencer.GENE_RAND_WIDTH, Sequencer.GENE_RAND_WIDTH, Sequencer.GENE_RAND_WIDTH, Priority.NEVER, HPos.CENTER, true));
        getColumnConstraints().add(new ColumnConstraints(Sequencer.GENE_MT_WIDTH, Sequencer.GENE_MT_WIDTH, Sequencer.GENE_MT_WIDTH, Priority.NEVER, HPos.CENTER, true));
        getColumnConstraints().add(new ColumnConstraints(Sequencer.GENE_DOM_WIDTH, Sequencer.GENE_DOM_WIDTH, Sequencer.GENE_DOM_WIDTH, Priority.NEVER, HPos.CENTER, true));
        getColumnConstraints().add(new ColumnConstraints(Sequencer.GENE_GENOTYPE_WIDTH, Sequencer.GENE_GENOTYPE_WIDTH, Sequencer.GENE_GENOTYPE_WIDTH, Priority.NEVER, HPos.CENTER, true));

        txtGeneName = new TextField(_genomeGene.name);
        txtGeneName.setEditable(false);
        add(txtGeneName, 0, 0);

        chkRandom = new CheckBox();
        chkRandom.setSelected(false);
        add(chkRandom, 1, 0);

        ObservableList<String> mtList = FXCollections.observableArrayList();
        for(int i = 0; i < info.allowedMutations.length; i++) {
            mtList.add(info.allowedMutations[i].name());
        }//end for i
        cmbMutationType = new ComboBox<String>(mtList);
        cmbMutationType.setMaxWidth(Double.MAX_VALUE);
        add(cmbMutationType, 2, 0);

        hbxRangeHierarchy = new HBox();
        add(hbxRangeHierarchy, 3, 0);

        hbxGenoType = new HBox();
        add(hbxGenoType, 4, 0);
    }
}
