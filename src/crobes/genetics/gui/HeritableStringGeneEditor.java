package crobes.genetics.gui;

import crobes.genetics.genomics.GenomeGene;
import crobes.genetics.genomics.GenomeString;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class HeritableStringGeneEditor extends GeneEditor
{
    private GenomeString _geneString;

    public HeritableStringGeneEditor(GenomeGene gene, Sequencer sequencer) {
        super(gene, sequencer);

        _geneString = (GenomeString) gene.geneValue;

        Button btnDomain = new Button("Domain");
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
    }
}
