package crobes.genetics.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;

public class DomainEditorString extends Stage
{
    private boolean _cancelled = false;
    public boolean cancelled() {
        return _cancelled;
    }

    BorderPane root;
    ObservableList<String> domainList;
    private DomainString _domain;

    public DomainEditorString(double width, double height) {
        root = new BorderPane();

        //top pane - controls
        HBox topPane = new HBox();

        TextField txtAdd = new TextField();
        txtAdd.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(txtAdd, Priority.ALWAYS);

        Button btnAdd = new Button("+");
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!txtAdd.getText().isEmpty()) {
                    String s = txtAdd.getText();
                    txtAdd.setText("");
                    txtAdd.requestFocus();

                    if(!domainList.contains(s)) {
                        domainList.add(s);
                    }//end if

                    copyListToDomain();
                }//end if
            }
        });

        Button btnDelete = new Button("-");

        Button btnUp = new Button("˄");

        Button btnDown = new Button("˅");

        topPane.getChildren().addAll(txtAdd, btnAdd, btnDelete, btnUp, btnDown);

        //middle pane - strings list
        HBox midPane = new HBox();

        domainList = FXCollections.observableArrayList();
        if(_domain != null) {
            for(String s: _domain.domain)
                domainList.add(s);
        }//end if

        ListView<String> lstStrings = new ListView<String>(domainList);
        lstStrings.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lstStrings, Priority.ALWAYS);

        midPane.getChildren().addAll(lstStrings);

        //bottom pane - OK/Cancel
        FlowPane bottomPane = new FlowPane();
        bottomPane.setAlignment(Pos.CENTER_RIGHT);

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

        bottomPane.getChildren().addAll(btnOK, btnCancel);

        root.setTop(topPane);
        root.setCenter(midPane);
        root.setBottom(bottomPane);

        Scene scene = new Scene(root, width, height);
        setScene(scene);
    }

    public static class DomainString
    {
        public String[] domain;
    }

    public static boolean editStringDomain(DomainString domain, Stage parent) {
        DomainEditorString edit = new DomainEditorString(350, 400);

        edit._domain = domain;
        edit.initOwner(parent);
        edit.initModality(Modality.WINDOW_MODAL);
        edit.showAndWait();
        domain = edit._domain;

        return edit.cancelled();
    }

    private void copyListToDomain() {
        _domain.domain = new String[domainList.size()];
        for(int i = 0; i < domainList.size(); i++ ) {
            _domain.domain[i] = domainList.get(i);
        }//end for i
    }
}
