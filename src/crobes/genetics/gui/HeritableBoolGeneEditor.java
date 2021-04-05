package crobes.genetics.gui;

import crobes.genetics.genomics.GenomeBool;
import crobes.genetics.genomics.GenomeGene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class HeritableBoolGeneEditor extends GeneEditor
{
    private GenomeBool _geneBool;
    Button btnDomain;

    public HeritableBoolGeneEditor(GenomeGene gene, Sequencer sequencer) {
        super(gene, sequencer);

        _geneBool = (GenomeBool) gene.geneValue;

        btnDomain = new Button(getCaption());
        btnDomain.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnDomain, Priority.ALWAYS);
        btnDomain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean[] b;
                if(_geneBool.domain() == null) {
                    //initialize to true/false
                    b = new boolean[2];
                    b[0] = true;
                    b[1] = false;
                }//end if
                else {
                    //swap the values
                    b = _geneBool.domain();
                    boolean swap = b[0];
                    b[0] = b[1];
                    b[1] = swap;
                }//end else

                _geneBool.domain(b);
                btnDomain.setText(getCaption());
            }
        });

        hbxRangeHierarchy.getChildren().addAll(btnDomain);
    }

    private String getCaption() {
        StringBuilder sb = new StringBuilder();

        if(_geneBool.domain() != null) {
            sb.append(_geneBool.domain()[0] ? "T":"F");
            sb.append(" : ");
            sb.append(_geneBool.domain()[1] ? "T":"F");
        }//end if

        return sb.toString();
    }
}
