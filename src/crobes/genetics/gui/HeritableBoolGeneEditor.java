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

        hbxGenoType.setFillGenotype(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if((_geneBool.genoType() != null) &&
                        (_geneBool.genoType().length > 0)) {
                    StringBuilder sb = new StringBuilder();
                    boolean[] gt = _geneBool.genoType();

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
                GenotypeBoolPicker.GenotypeBool genotypeBool = new GenotypeBoolPicker.GenotypeBool();

                genotypeBool.geneName = _genomeGene.name;
                genotypeBool.genotype = null;
                if(_geneBool.genoType() != null)
                    genotypeBool.genotype = _geneBool.genoType().clone();

                if(!GenotypeBoolPicker.editBoolGenotype(genotypeBool, _sequencer)) {
                    _geneBool.genoType(genotypeBool.genotype);
                    hbxGenoType.processGenotype();
                }//end if
            }
        });
    }

    private String getCaption() {
        StringBuilder sb = new StringBuilder();

        if(_geneBool.domain() != null) {
            sb.append(_geneBool.domain()[0] ? "T":"F");
            sb.append(" : ");
            sb.append(_geneBool.domain()[1] ? "T":"F");
        }//end if
        else {
            sb.append("Dom");
        }//end else

        return sb.toString();
    }

    @Override
    public void setControlStates(boolean readOnly) {
        super.setControlStates(readOnly);

        btnDomain.setDisable(readOnly);
        if(btnDomain.isDisabled())
            btnDomain.setStyle("-fx-opacity: 1; -fx-text-fill: black; -fx-background-color: white;");
    }
}
