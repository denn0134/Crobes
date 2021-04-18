package crobes.genetics.gui;

import crobes.genetics.genomics.GenomeGene;
import crobes.genetics.genomics.GenomeString;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class HeritableStringGeneEditor extends GeneEditor
{
    private GenomeString _geneString;
    private Button btnDomain;

    public HeritableStringGeneEditor(GenomeGene gene, Sequencer sequencer) {
        super(gene, sequencer);

        _geneString = (GenomeString) gene.geneValue;

        btnDomain = new Button("Domain");
        btnDomain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DomainEditorString.DomainString domainString = new DomainEditorString.DomainString();
                domainString.domain = null;
                if(_geneString.domain() != null)
                   domainString.domain = _geneString.domain().clone();

                if(!DomainEditorString.editStringDomain(domainString,_sequencer)) {
                    _geneString.domain(domainString.domain);
                }//end if
            }
        });
        hbxRangeHierarchy.getChildren().addAll(btnDomain);

        hbxGenoType.setFillGenotype(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if((_geneString.genoType() != null) &&
                        (_geneString.genoType().length > 0)) {
                    StringBuilder sb = new StringBuilder();
                    String[] gt = _geneString.genoType();

                    sb.append(gt[0]);
                    for(int i = 1; i < gt.length; i++) {
                        sb.append(" | ");
                        sb.append(gt[i]);
                    }//end for i

                    hbxGenoType.setGenotypeDisplay(sb.toString());
                }//end if
                else {
                    hbxGenoType.setGenotypeDisplay("Not set");
                }//end else
            }
        });
        hbxGenoType.processGenotype();
        hbxGenoType.setEditEvent(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GenotypeStringPicker.GenotypeString genotypeString = new GenotypeStringPicker.GenotypeString();

                genotypeString.name = _genomeGene.name;

                genotypeString.domain = null;
                if(_geneString.domain() != null)
                    genotypeString.domain = _geneString.domain().clone();

                genotypeString.genotype = null;
                if(_geneString.genoType() != null)
                    genotypeString.genotype = _geneString.genoType().clone();

                if(!GenotypeStringPicker.editStringGenotype(genotypeString, _sequencer)) {
                    _geneString.genoType(genotypeString.genotype);
                    hbxGenoType.processGenotype();
                }
            }
        });
    }

    @Override
    public void setControlStates(boolean readOnly) {
        super.setControlStates(readOnly);

        btnDomain.setDisable(readOnly);
        if(btnDomain.isDisabled())
            btnDomain.setStyle("-fx-opacity: 1; -fx-text-fill: black; -fx-background-color: white;");
    }
}
