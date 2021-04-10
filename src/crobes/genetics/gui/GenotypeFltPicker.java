package crobes.genetics.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class GenotypeFltPicker extends Stage
{
    private GenotypeFlt _genotypeFlt;
    private boolean _cancelled = true;
    public boolean cancelled() {
        return _cancelled;
    }
    private ObservableList<Double> genotypeList;
    private Spinner<Double> spnGenotype;
    private ListView<Double> lstGenotype;
    private EventHandler<ActionEvent> evtAdd;
    EventHandler<ActionEvent> evtDelete;

    public GenotypeFltPicker(GenotypeFlt genotypeFlt, double width, double height) {
        _genotypeFlt = genotypeFlt;

        BorderPane root = new BorderPane();
        VBox vbxEditor = new VBox();

        //gene name label
        Label lblName = new Label(_genotypeFlt.geneName);
        VBox.setVgrow(lblName, Priority.NEVER);

        //spinner bar with add and delete
        HBox hbxSpin = new HBox();

        spnGenotype = new Spinner<Double>();
        SpinnerValueFactory<Double> svf = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999, 1, 1);
        svf.setConverter(new DoubleStringConverter());
        spnGenotype.setValueFactory(svf);
        spnGenotype.setEditable(true);
        spnGenotype.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER)
                    evtAdd.handle(null);
            }
        });
        TextFormatter<Double> textFormatter = new TextFormatter<Double>(svf.getConverter(), svf.getValue());
        spnGenotype.getEditor().setTextFormatter(textFormatter);
        svf.valueProperty().bindBidirectional(textFormatter.valueProperty());
        spnGenotype.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(spnGenotype, Priority.ALWAYS);

        evtAdd = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                genotypeList.add(spnGenotype.getValue());
                copyListToGenotype();
            }
        };
        Button btnAdd = new Button("+");
        HBox.setHgrow(btnAdd, Priority.NEVER);
        btnAdd.setOnAction(evtAdd);

        evtDelete = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = lstGenotype.getSelectionModel().getSelectedIndex();
                if(index > -1) {
                    genotypeList.remove(index);
                    copyListToGenotype();
                }//end if
            }
        };
        Button btnDelete = new Button("-");
        HBox.setHgrow(btnDelete, Priority.NEVER);
        btnDelete.setOnAction(evtDelete);

        hbxSpin.getChildren().addAll(spnGenotype, btnAdd, btnDelete);
        VBox.setVgrow(hbxSpin, Priority.NEVER);

        //genotype list
        genotypeList = FXCollections.observableArrayList();
        if(_genotypeFlt.genotype != null) {
            for(float f: _genotypeFlt.genotype) {
                genotypeList.add((double) f);
            }//end for each
        }//end if
        lstGenotype = new ListView<>(genotypeList);
        lstGenotype.setMaxHeight(Double.MAX_VALUE);
        lstGenotype.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() > 1) {
                    evtDelete.handle(null);
                }//end if
            }
        });
        VBox.setVgrow(lstGenotype, Priority.ALWAYS);

        //OK/Cancel buttons
        FlowPane paneOKCancel = new FlowPane();
        paneOKCancel.setAlignment(Pos.CENTER_RIGHT);

        Button btnOK = new Button("OK");
        btnOK.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _cancelled = false;
                close();
            }
        });

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _cancelled = true;
                close();
            }
        });

        paneOKCancel.getChildren().addAll(btnOK, btnCancel);
        VBox.setVgrow(paneOKCancel, Priority.NEVER);

        vbxEditor.getChildren().addAll(lblName, hbxSpin, lstGenotype, paneOKCancel);

        root.setCenter(vbxEditor);

        Scene scene = new Scene(root, width, height);
        setScene(scene);
    }

    public static final class GenotypeFlt
    {
        public String geneName;
        public float[] genotype;
    }

    public static boolean editFltGenotype(GenotypeFlt genotypeFlt, Stage parent) {
        GenotypeFltPicker edit = new GenotypeFltPicker(genotypeFlt, 300, 400);

        edit.initOwner(parent);
        edit.initModality(Modality.APPLICATION_MODAL);
        edit.showAndWait();
        genotypeFlt = edit._genotypeFlt;

        return edit.cancelled();
    }

    private void copyListToGenotype() {
        _genotypeFlt.genotype = new float[genotypeList.size()];
        for(int i = 0; i < genotypeList.size(); i++) {
            double d = genotypeList.get(i);
            _genotypeFlt.genotype[i] = (float) d;
        }//end for i
    }
}
