package crobes.genetics.gui;

import crobes.core.CrobeEnums;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GenotypeEnumPicker extends Stage
{
    private GenotypeEnum _genotypeEnum;
    private boolean _cancelled = true;
    public boolean cancelled() {
        return _cancelled;
    }
    ObservableList<String> domainList;
    ListView<String> lstDomain;
    ObservableList<String> genotypeList;
    ListView<String> lstGenotype;

    public GenotypeEnumPicker(GenotypeEnum genotypeEnum,
                              double width,
                              double height) {
        _genotypeEnum = genotypeEnum;

        BorderPane root = new BorderPane();

        //top - name label and buttons
        HBox hbxTop = new HBox();

        Label lblName = new Label(_genotypeEnum.name);
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
        if(_genotypeEnum.domain != null) {
            for(Enum e: _genotypeEnum.domain)
                domainList.add(e.name());
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
        if(_genotypeEnum.genotype != null) {
            for(Enum e: _genotypeEnum.genotype)
                genotypeList.add(e.name());
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

    public static final class GenotypeEnum
    {
        public String name;
        public String enumName;
        public Enum[] domain;
        public Enum[] genotype;
    }

    public static boolean editStringGenotype(GenotypeEnum genotypeEnum, Stage parent) {
        GenotypeEnumPicker edit = new GenotypeEnumPicker(genotypeEnum, 300, 400);

        edit.initOwner(parent);
        edit.initModality(Modality.APPLICATION_MODAL);
        edit.showAndWait();
        genotypeEnum = edit._genotypeEnum;

        return edit.cancelled();
    }

    private void copyListToGenotype() {
        _genotypeEnum.genotype = new Enum[genotypeList.size()];
        for(int i = 0; i < genotypeList.size(); i++) {
            _genotypeEnum.genotype[i] = CrobeEnums.getEnum(_genotypeEnum.enumName, genotypeList.get(i));
        }//end for i
    }
}
