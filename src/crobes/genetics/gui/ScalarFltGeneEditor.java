package crobes.genetics.gui;

import crobes.genetics.genomics.GenomeFlt;
import crobes.genetics.genomics.GenomeGene;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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

        hbxRangeHierarchy.getChildren().addAll(spnMutationRange);
    }
}
