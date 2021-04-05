package crobes.genetics.gui;

import crobes.genetics.genomics.GenomeGene;
import crobes.genetics.genomics.GenomeInt;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.converter.IntegerStringConverter;

public class ScalarIntGeneEditor extends GeneEditor
{
    private GenomeInt _geneInt;

    Spinner<Integer> spnMutationRange;

    public ScalarIntGeneEditor(GenomeGene gene, Sequencer sequencer) {
        super(gene, sequencer);

        _geneInt = (GenomeInt) gene.geneValue;

        spnMutationRange = new Spinner<Integer>();
        SpinnerValueFactory<Integer> vf = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999, 0, 1);
        vf.setConverter(new IntegerStringConverter());
        vf.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                _geneInt.mutationRange(newValue);
            }
        });
        spnMutationRange.setValueFactory(vf);

        hbxRangeHierarchy.getChildren().addAll(spnMutationRange);
    }
}
