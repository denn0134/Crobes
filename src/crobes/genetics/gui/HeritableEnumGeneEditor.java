package crobes.genetics.gui;

import crobes.genetics.genomics.GenomeEnum;
import crobes.genetics.genomics.GenomeGene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class HeritableEnumGeneEditor extends GeneEditor
{
    private GenomeEnum _geneEnum;

    public HeritableEnumGeneEditor(GenomeGene gene,
                                   Sequencer sequencer) {
        super(gene, sequencer);

        _geneEnum = (GenomeEnum) gene.geneValue;

        Button btnDomain = new Button("Domain");
        btnDomain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DomainEditorEnum.DomainEnum domainEnum = new DomainEditorEnum.DomainEnum();
                domainEnum.enumClass = _geneEnum.enumClass();
                domainEnum.domain = null;
                if(_geneEnum.domain() != null)
                    domainEnum.domain = _geneEnum.domain().clone();

                if(!DomainEditorEnum.editEnumDomain(domainEnum, _sequencer)) {
                    _geneEnum.domain(domainEnum.domain);
                }//end if
            }
        });
        hbxRangeHierarchy.getChildren().addAll(btnDomain);

        hbxGenoType.setFillGenotype(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if((_geneEnum.genoType() != null) &&
                        (_geneEnum.genoType().length > 0)) {
                    StringBuilder sb = new StringBuilder();
                    Enum[] gt = _geneEnum.genoType();

                    sb.append(gt[0]);
                    for(int i = 1; i < gt.length; i++) {
                        sb.append(" | ");
                        sb.append(gt[i].name());
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
                GenotypeEnumPicker.GenotypeEnum genotypeEnum = new GenotypeEnumPicker.GenotypeEnum();

                genotypeEnum.name = _genomeGene.name;
                genotypeEnum.enumName = _geneEnum.enumClass().getSimpleName();

                genotypeEnum.domain = null;
                if(_geneEnum.domain() != null)
                    genotypeEnum.domain = _geneEnum.domain().clone();

                genotypeEnum.genotype = null;
                if(_geneEnum.genoType() != null)
                    genotypeEnum.genotype = _geneEnum.genoType().clone();

                if(!GenotypeEnumPicker.editStringGenotype(genotypeEnum, _sequencer)) {
                    _geneEnum.genoType(genotypeEnum.genotype);
                    hbxGenoType.processGenotype();
                }//end if
            }
        });
    }
}
