package crobes.genetics.gui;


import crobes.genetics.genomics.GenomeGene;
import crobes.genetics.genomics.GenomePool;
import crobes.genetics.genomics.Genomics;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;


public class GenePoolEditor extends VBox
{
    private GenomePool _genomePool;

    Label lblName;
    TextArea txtDescription;
    Label lblGene;
    Label lblRandom;
    Label lblMutationType;
    Label lblDomainRange;
    Label lblGenoType;
    ObservableList<GeneEditor> geneList;
    ListView<GeneEditor> lstGenes;

    public GenePoolEditor() {
        //set up the genepool controls
        lblName = new Label();
        lblName.setPrefHeight(Sequencer.GENEPOOL_BASE_HEIGHT);
        setVgrow(lblName, Priority.NEVER);

        txtDescription = new TextArea();
        txtDescription.setPrefHeight(Sequencer.GENEPOOL_DESC_HEIGHT);
        txtDescription.setEditable(false);
        setVgrow(txtDescription, Priority.NEVER);

        //set up the gene editor title banner
        HBox hbxBanner = new HBox();
        hbxBanner.setPrefHeight(Sequencer.GENEPOOL_BASE_HEIGHT);
        hbxBanner.setPrefWidth(Sequencer.GENE_PICKER_WIDTH);
        setVgrow(hbxBanner, Priority.NEVER);

        lblGene = new Label("Gene");
        lblGene.setPrefWidth(Sequencer.GENE_NAME_WIDTH);
        lblGene.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblGene, Priority.ALWAYS);

        lblRandom = new Label("Rand");
        lblRandom.setPrefWidth(Sequencer.GENE_RAND_WIDTH);
        lblRandom.setMaxWidth(Sequencer.GENE_RAND_WIDTH);
        HBox.setHgrow(lblRandom, Priority.NEVER);

        lblMutationType = new Label("Mutation Type");
        lblMutationType.setPrefWidth(Sequencer.GENE_MT_WIDTH);
        lblMutationType.setMaxWidth(Sequencer.GENE_MT_WIDTH);
        HBox.setHgrow(lblMutationType, Priority.NEVER);

        lblDomainRange = new Label("Dom/Rng");
        lblDomainRange.setPrefWidth(Sequencer.GENE_DOM_WIDTH);
        lblDomainRange.setMaxWidth(Sequencer.GENE_DOM_WIDTH);
        HBox.setHgrow(lblMutationType, Priority.NEVER);

        lblGenoType = new Label("Genotype");
        lblGenoType.setPrefWidth(Sequencer.GENE_GENOTYPE_WIDTH);
        lblGenoType.setMaxWidth(Sequencer.GENE_GENOTYPE_WIDTH);
        HBox.setHgrow(lblGenoType, Priority.NEVER);

        hbxBanner.getChildren().addAll(lblGene, lblRandom,
                lblMutationType, lblDomainRange, lblGenoType);

        //set up the gene list
        geneList = FXCollections.observableArrayList();
        lstGenes = new ListView<>(geneList);

        //add the components to the editor
        getChildren().addAll(lblName, txtDescription, hbxBanner, lstGenes);
    }

    public void setGenePool(GenomePool pool) {
        _genomePool = pool;
        Genomics.GenePoolInfo info = Genomics.getGenePoolInfo(_genomePool.genePool);

        if((_genomePool == null) || (info == null)) {
            lblName.setText("");
            txtDescription.setText("");

            setGenes(null);
        }//end if
        else {
            lblName.setText(info.displayName);
            txtDescription.setText(info.description);

            setGenes(_genomePool.genes());
        }//end else
    }

    private void setGenes(ArrayList<GenomeGene> genes) {
        geneList.clear();

        if(genes != null) {
            for(GenomeGene gene: genes) {
                geneList.add(new GeneEditor(gene));
                lstGenes.refresh();
            }//end for each
        }//end if
    }
}
