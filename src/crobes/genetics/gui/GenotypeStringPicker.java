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
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GenotypeStringPicker extends Stage
{
    private GenotypeString _genotypeString;
    private boolean _cancelled = true;
    public boolean cancelled() {
        return _cancelled;
    }
    ObservableList<String> domainList;
    ListView<String> lstDomain;
    ObservableList<String> genotypeList;
    ListView<String> lstGenotype;

    public GenotypeStringPicker(GenotypeString genotypeString,
                                double width,
                                double height) {
        _genotypeString = genotypeString;

        BorderPane root = new BorderPane();

        //top - name label and buttons
        HBox hbxTop = new HBox();

        Label lblName = new Label(_genotypeString.name);
        lblName.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblName, Priority.ALWAYS);

        EventHandler<ActionEvent> evtAdd = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = lstDomain.getSelectionModel().getSelectedIndex();
                if(index > -1) {
                    genotypeList.add(domainList.get(index));
                    copyListToGenotype();
                }//end if
            }
        };
        Button btnAdd = new Button(">");
        btnAdd.setOnAction(evtAdd);
        HBox.setHgrow(btnAdd, Priority.NEVER);

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
        Button btnDelete = new Button("<");
        btnDelete.setOnAction(evtDelete);
        HBox.setHgrow(btnDelete, Priority.NEVER);

        hbxTop.getChildren().addAll(lblName, btnDelete, btnAdd);

        //mid - listviews
        HBox hbxMid = new HBox();

        domainList = FXCollections.observableArrayList();
        if(_genotypeString.domain != null) {
            for(String s: _genotypeString.domain)
                domainList.add(s);
        }//end if
        lstDomain = new ListView<String>(domainList);
        lstDomain.setMaxWidth(Double.MAX_VALUE);
        lstDomain.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() > 1)
                    evtAdd.handle(null);
            }
        });
        HBox.setHgrow(lstDomain, Priority.ALWAYS);

        genotypeList = FXCollections.observableArrayList();
        if(_genotypeString.genotype != null) {
            for(String s: _genotypeString.genotype)
                genotypeList.add(s);
        }//end if
        lstGenotype = new ListView<String>(genotypeList);
        lstGenotype.setMaxWidth(Double.MAX_VALUE);
        lstGenotype.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() > 1)
                    evtDelete.handle(null);
            }
        });
        HBox.setHgrow(lstGenotype, Priority.ALWAYS);

        hbxMid.getChildren().addAll(lstDomain, lstGenotype);

        //bottom - OK / Cancel
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

        root.setTop(hbxTop);
        root.setCenter(hbxMid);
        root.setBottom(flwOKCancel);

        Scene scene = new Scene(root, width, height);
        setScene(scene);
    }

    public static final class GenotypeString
    {
        public String name;
        public String[] domain;
        public String[] genotype;
    }

    public static boolean editStringGenotype(GenotypeString genotypeString, Stage parent) {
        GenotypeStringPicker edit = new GenotypeStringPicker(genotypeString, 300, 400);

        edit.initOwner(parent);
        edit.initModality(Modality.APPLICATION_MODAL);
        edit.showAndWait();
        genotypeString = edit._genotypeString;

        return edit.cancelled();
    }

    private void copyListToGenotype() {
        _genotypeString.genotype = new String[genotypeList.size()];
        for(int i = 0; i < genotypeList.size(); i++) {
            _genotypeString.genotype[i] = genotypeList.get(i);
        }//end for i
    }
}
