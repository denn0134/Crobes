package crobes.genetics.gui;

import crobes.genetics.genomics.Genome;
import crobes.genetics.genomics.GenomePool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class GenePoolPicker extends VBox
{
    private Sequencer _sequencer;
    private GenomePool _genomePool;
    public GenomePool genomePool() {
        return _genomePool;
    }

    private ListView<GenePoolPicker> _listView = null;
    public void setListView(ListView<GenePoolPicker> list) {
        _listView = list;
    }

    private Label lblType;
    private ComboBox<String> cmbClass;

    public GenePoolPicker(String poolType,
                          String[] classes,
                          Sequencer sequencer,
                          GenomePool genomePool) {
        _sequencer = sequencer;
        _genomePool = genomePool;
        ObservableList<String> list = FXCollections.observableArrayList(classes);

        setFillWidth(true);

        lblType = new Label(poolType);
        cmbClass = new ComboBox<String>(list);
        cmbClass.setMaxWidth(Double.MAX_VALUE);

        cmbClass.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setSelected();
            }
        });
        cmbClass.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _genomePool.genePool = cmbClass.getValue();
                _sequencer.updateTaxonomy();
            }
        });

        cmbClass.getSelectionModel().select(_genomePool.genePool);

        getChildren().addAll(lblType, cmbClass);
    }

    private void setSelected() {
        if(_listView != null) {
            _listView.getSelectionModel().select(this);
        }//end if
    }
    public void setcontrolState(boolean readOnly) {
        cmbClass.setDisable(readOnly);
        if(readOnly)
            cmbClass.setStyle("-fx-opacity: 1; -fx-text-fill: black; -fx-background-color: white;");
    }
}
