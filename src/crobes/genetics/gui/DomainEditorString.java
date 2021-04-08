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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DomainEditorString extends Stage
{
    private boolean _cancelled = true;
    public boolean cancelled() {
        return _cancelled;
    }

    BorderPane root;
    ObservableList<String> domainList;
    private DomainString _domain;
    ListView<String> lstStrings;
    TextField txtAdd;

    public DomainEditorString(DomainString domain,
                              double width,
                              double height) {
        _domain = domain;
        root = new BorderPane();

        //top pane - controls
        HBox topPane = new HBox();

        txtAdd = new TextField();
        txtAdd.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(txtAdd, Priority.ALWAYS);
        txtAdd.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER) {
                    callAdd();
                }//end if
            }
        });

        Button btnAdd = new Button("+");
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                callAdd();
            }
        });

        EventHandler<ActionEvent> evtDelete = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = lstStrings.getSelectionModel().getSelectedIndex();

                if(index > -1) {
                    domainList.remove(index);
                    copyListToDomain();
                }//end if
            }
        };
        Button btnDelete = new Button("-");
        btnDelete.setOnAction(evtDelete);

        Button btnUp = new Button("˄");
        btnUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = lstStrings.getSelectionModel().getSelectedIndex();

                if(index > 0) {
                    String s = domainList.get(index);
                    domainList.remove(index);
                    domainList.add(index - 1, s);
                    lstStrings.getSelectionModel().select(index - 1);
                    copyListToDomain();
                }//end if
            }
        });

        Button btnDown = new Button("˅");
        btnDown.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = lstStrings.getSelectionModel().getSelectedIndex();

                if((index > -1) && (index != domainList.size() - 1)) {
                    String s = domainList.get(index);
                    domainList.remove(index);
                    domainList.add(index + 1, s);
                    lstStrings.getSelectionModel().select(index + 1);
                    copyListToDomain();
                }//end if
            }
        });

        topPane.getChildren().addAll(txtAdd, btnAdd, btnDelete, btnUp, btnDown);

        //middle pane - strings list
        HBox midPane = new HBox();

        domainList = FXCollections.observableArrayList();
        if(_domain != null) {
            if(_domain.domain != null) {
                for (String s : _domain.domain)
                    domainList.add(s);
            }//end if
        }//end if

        lstStrings = new ListView<String>(domainList);
        lstStrings.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lstStrings, Priority.ALWAYS);
        lstStrings.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() > 1) {
                    evtDelete.handle(null);
                }//end if
            }
        });

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

    public static final class DomainString
    {
        public String[] domain;
    }

    public static boolean editStringDomain(DomainString domain, Stage parent) {
        DomainEditorString edit = new DomainEditorString(domain, 350, 400);

        edit.initOwner(parent);
        edit.initModality(Modality.WINDOW_MODAL);
        edit.showAndWait();
        domain = edit._domain;

        return edit.cancelled();
    }

    private void callAdd() {
        if(!txtAdd.getText().isEmpty()) {
            String s = txtAdd.getText();
            txtAdd.setText("");
            txtAdd.requestFocus();

            if(!domainList.contains(s)) {
                domainList.add(s);
                copyListToDomain();
            }//end if
        }//end if
    }
    private void copyListToDomain() {
        _domain.domain = new String[domainList.size()];
        for(int i = 0; i < domainList.size(); i++ ) {
            _domain.domain[i] = domainList.get(i);
        }//end for i
    }
}
