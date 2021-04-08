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
    }
}
