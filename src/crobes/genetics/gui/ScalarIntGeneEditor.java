package crobes.genetics.gui;

import crobes.genetics.genomics.GenomeGene;
import crobes.genetics.genomics.GenomeInt;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

        hbxGenoType.setFillGenotype(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if((_geneInt.genoType() != null) &&
                        (_geneInt.genoType().length > 0)) {
                    StringBuilder sb = new StringBuilder();
                    int[] gt = _geneInt.genoType();

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
                GenotypeIntPicker.GenotypeInt genotypeInt = new GenotypeIntPicker.GenotypeInt();

                genotypeInt.geneName = _genomeGene.name;
                genotypeInt.genotype = null;

                if(_geneInt.genoType() != null)
                    genotypeInt.genotype = _geneInt.genoType().clone();

                if(!GenotypeIntPicker.editIntGenotype(genotypeInt, _sequencer)) {
                    _geneInt.genoType(genotypeInt.genotype);
                    hbxGenoType.processGenotype();
                }//end if
            }
        });
    }
}
