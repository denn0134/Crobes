package crobes.genetics.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class GenotypeIntPicker extends Stage
{
    private GenotypeInt _genotypeInt;
    private boolean _cancelled = true;
    public boolean cancelled() {
        return _cancelled;
    }
    private ObservableList<Integer> genotypeList;
    private Spinner<Integer> spnGenotype;
    private ListView<Integer> lstGenotype;
    private EventHandler<ActionEvent> evtAdd;
    EventHandler<ActionEvent> evtDelete;

    public GenotypeIntPicker(GenotypeInt genotypeInt,
                             double width,
                             double height) {
        _genotypeInt = genotypeInt;

        BorderPane root = new BorderPane();
        VBox vbxEditor = new VBox();

        //gene name label
        Label lblName = new Label(_genotypeInt.geneName);
        VBox.setVgrow(lblName, Priority.NEVER);

        //spinner bar with add and delete
        HBox hbxSpin = new HBox();

        spnGenotype = new Spinner<Integer>();
        SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999, 1, 1);
        svf.setConverter(new IntegerStringConverter());
        spnGenotype.setValueFactory(svf);
        spnGenotype.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                evtAdd.handle(null);
            }
        });
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

        vbxEditor.getChildren().addAll(lblName, hbxSpin, lstGenotype, paneOKCancel);

        root.setCenter(vbxEditor);

        Scene scene = new Scene(root, width, height);
        setScene(scene);
    }

    public static final class GenotypeInt
    {
        public String geneName;
        public int[] genotype;
    }

    public static boolean editIntGenotype(GenotypeInt genotypeInt, Stage parent ) {
        GenotypeIntPicker edit = new GenotypeIntPicker(genotypeInt, 300, 400);

        edit.initOwner(parent);
        edit.initModality(Modality.APPLICATION_MODAL);
        edit.showAndWait();
        genotypeInt = edit._genotypeInt;

        return edit.cancelled();
    }

    private void copyListToGenotype() {
        _genotypeInt.genotype = new int[genotypeList.size()];
        for(int i = 0; i < genotypeList.size(); i++) {
            _genotypeInt.genotype[i] = genotypeList.get(i);
        }//end for i
    }
}
