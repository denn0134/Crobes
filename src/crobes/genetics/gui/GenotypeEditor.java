package crobes.genetics.gui;

import crobes.genetics.genomics.GenomeValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class GenotypeEditor extends HBox
{
    private TextField txtGenotype;
    private Button btnGenotype;

    private EventHandler<ActionEvent> _evtFillGenotype;
    public void setFillGenotype(EventHandler<ActionEvent> event) {
        _evtFillGenotype = event;
    }
    public void setEditEvent(EventHandler<ActionEvent> event) {
        btnGenotype.setOnAction(event);
    }

    public GenotypeEditor() {
        txtGenotype = new TextField();
        txtGenotype.setEditable(false);
        txtGenotype.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(txtGenotype, Priority.ALWAYS);

        btnGenotype = new Button("Edit");
        HBox.setHgrow(btnGenotype, Priority.NEVER);

        getChildren().addAll(txtGenotype, btnGenotype);
    }

    public void setGenotypeDisplay(String genotype) {
        txtGenotype.setText(genotype);
    }

    public void processGenotype() {
        if(_evtFillGenotype != null) {
            _evtFillGenotype.handle(null);
        }//end if
    }

    public void setControlState(boolean readOnly) {
        btnGenotype.setDisable(readOnly);
        if(btnGenotype.isDisabled())
            btnGenotype.setStyle("-fx-opacity: 1; -fx-text-fill: black; -fx-background-color: white;");
    }
}
