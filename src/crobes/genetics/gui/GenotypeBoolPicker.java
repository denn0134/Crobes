package crobes.genetics.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GenotypeBoolPicker extends Stage
{
    private GenotypeBool _genotypeBool;
    private boolean _cancelled = true;
    public boolean cancelled() {
        return _cancelled;
    }
    private ObservableList<Boolean> genotypeList;
    private ListView<Boolean> lstGenotype;
    private EventHandler<ActionEvent> evtAdd;
    EventHandler<ActionEvent> evtDelete;

    public GenotypeBoolPicker(GenotypeBool genotypeBool, double width, double height) {
        _genotypeBool = genotypeBool;
        BorderPane root = new BorderPane();

        VBox vbxEditor = new VBox();

        //name label
        Label lblName = new Label(_genotypeBool.geneName);
        VBox.setVgrow(lblName, Priority.NEVER);

        //button bar
        HBox hbxButtons = new HBox();

        Button btnTrue = new Button("True");
        btnTrue.setMaxWidth(Double.MAX_VALUE);
        btnTrue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                genotypeList.add(true);
                copyListToGenotype();
            }
        });
        HBox.setHgrow(btnTrue, Priority.ALWAYS);

        Button btnFalse = new Button("False");
        btnFalse.setMaxWidth(Double.MAX_VALUE);
        btnFalse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                genotypeList.add(false);
                copyListToGenotype();
            }
        });
        HBox.setHgrow(btnFalse, Priority.ALWAYS);

        EventHandler<ActionEvent> evtDelete = new EventHandler<ActionEvent>() {
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
        btnDelete.setOnAction(evtDelete);
        HBox.setHgrow(btnDelete, Priority.NEVER);

        hbxButtons.getChildren().addAll(btnTrue, btnFalse, btnDelete);

        //listview
        genotypeList = FXCollections.observableArrayList();
        if(_genotypeBool.genotype != null) {
            for(boolean b: _genotypeBool.genotype) {
                genotypeList.add(b);
            }//end for each
        }//end if
        lstGenotype = new ListView<Boolean>(genotypeList);
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

        //OK / Cancel
        FlowPane flwOKCancel = new FlowPane();
        flwOKCancel.setAlignment(Pos.CENTER_RIGHT);

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

        flwOKCancel.getChildren().addAll(btnOK, btnCancel);
        VBox.setVgrow(flwOKCancel, Priority.NEVER);

        vbxEditor.getChildren().addAll(lblName, hbxButtons, lstGenotype, flwOKCancel);

        root.setCenter(vbxEditor);

        Scene scene = new Scene(root, width, height);
        setScene(scene);
    }

    public static final class GenotypeBool
    {
        public String geneName;
        public boolean[] genotype;
    }

    public static boolean editBoolGenotype(GenotypeBool genotypeBool, Stage parent) {
        GenotypeBoolPicker edit = new GenotypeBoolPicker(genotypeBool, 300, 400);

        edit.initOwner(parent);
        edit.initModality(Modality.APPLICATION_MODAL);
        edit.showAndWait();
        genotypeBool = edit._genotypeBool;

        return edit.cancelled();
    }

    private void copyListToGenotype() {
        _genotypeBool.genotype = new boolean[genotypeList.size()];
        for(int i = 0; i < genotypeList.size(); i++) {
            _genotypeBool.genotype[i] = genotypeList.get(i);
        }//end for i
    }
}
