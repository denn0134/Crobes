package crobes.genetics.gui;

import crobes.genetics.genomics.GenomeFlt;
import crobes.genetics.genomics.GenomeGene;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.DoubleStringConverter;

public class ScalarFltGeneEditor extends GeneEditor
{
    private GenomeFlt _geneFlt;
    Spinner<Double> spnMutationRange;

    public ScalarFltGeneEditor(GenomeGene gene, Sequencer sequencer) {
        super(gene, sequencer);

        _geneFlt = (GenomeFlt) gene.geneValue;

        spnMutationRange = new Spinner<Double>();
        SpinnerValueFactory<Double> vf = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999, 0, 1);
        vf.setConverter(new DoubleStringConverter());
        vf.valueProperty().addListener(new ChangeListener<Double>() {
            @Override
            public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
                double dRng = newValue;
                _geneFlt.mutationRange((float) dRng);
            }
        });
        spnMutationRange.setValueFactory(vf);
        spnMutationRange.setEditable(true);
        TextFormatter<Double> textFormatter = new TextFormatter<Double>(vf.getConverter(), vf.getValue());
        spnMutationRange.getEditor().setTextFormatter(textFormatter);
        vf.valueProperty().bindBidirectional(textFormatter.valueProperty());

        hbxRangeHierarchy.getChildren().addAll(spnMutationRange);

        hbxGenoType.setFillGenotype(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if((_geneFlt.genoType() != null) &&
                        (_geneFlt.genoType().length > 0)) {
                    StringBuilder sb = new StringBuilder();
                    float[] gt = _geneFlt.genoType();

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
                GenotypeFltPicker.GenotypeFlt genotypeFlt = new GenotypeFltPicker.GenotypeFlt();

                genotypeFlt.geneName = _genomeGene.name;
                genotypeFlt.genotype = null;

                if(_geneFlt.genoType() != null)
                    genotypeFlt.genotype = _geneFlt.genoType().clone();

                if(!GenotypeFltPicker.editFltGenotype(genotypeFlt, _sequencer)) {
                    _geneFlt.genoType(genotypeFlt.genotype);
                    hbxGenoType.processGenotype();
                }//end if
            }
        });
    }

    @Override
    public void setControlStates(boolean readOnly) {
        super.setControlStates(readOnly);

        spnMutationRange.setDisable(readOnly);
        if(spnMutationRange.isDisabled())
            spnMutationRange.setStyle("-fx-opacity: 1; -fx-text-fill: black; -fx-background-color: white;");
    }
}
