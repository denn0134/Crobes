package crobes.genetics.gui;

import crobes.genetics.genomics.GenomePool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GenePoolPicker extends VBox
{
    private GenomePool _genomePool;
    public GenomePool genomePool() {
        return _genomePool;
    }

    private Label lblType;
    private ComboBox cmbClass;

    public GenePoolPicker(String poolType, String[] classes, GenomePool genomePool) {
        _genomePool = genomePool;
        ObservableList<String> list = FXCollections.observableArrayList(classes);

        setFillWidth(true);

        lblType = new Label(poolType);
        cmbClass = new ComboBox<String>(list);
        cmbClass.setMaxWidth(Double.MAX_VALUE);

        getChildren().addAll(lblType, cmbClass);
    }
}
