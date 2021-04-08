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
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DomainEditorEnum extends Stage
{
    private boolean _cancelled = true;
    public boolean concelled() {
        return _cancelled;
    }

    BorderPane root;
    private DomainEnum _domain;
    ObservableList<String> enumList;
    ListView<String> lstEnums;
    ObservableList<String> domainList;
    ListView<String> lstDomain;

    public DomainEditorEnum(DomainEnum domain,
                            double width,
                            double height) {
        _domain = domain;
        root = new BorderPane();

        //top pane - buttons and labels
        VBox topPane = new VBox();
        HBox topButtonPane = new HBox();

        EventHandler<ActionEvent> evtRemove = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = lstDomain.getSelectionModel().getSelectedIndex();

                if(index > -1) {
                    domainList.remove(index);
                    copyListToDomain();
                }//end if
            }
        };
        Button btnRemove = new Button("<");
        btnRemove.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnRemove, Priority.ALWAYS);
        btnRemove.setOnAction(evtRemove);

        EventHandler<ActionEvent> evtAdd = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = lstEnums.getSelectionModel().getSelectedIndex();

                if((index > -1) && (!domainList.contains(enumList.get(index)))) {
                    domainList.add(enumList.get(index));
                    copyListToDomain();
                }//end if
            }
        };
        Button btnAdd = new Button(">");
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnAdd, Priority.ALWAYS);
        btnAdd.setOnAction(evtAdd);

        Button btnUp = new Button("˄");
        btnUp.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnUp, Priority.ALWAYS);
        btnUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = lstDomain.getSelectionModel().getSelectedIndex();

                if(index > 0) {
                    String s = domainList.get(index);
                    domainList.remove(index);
                    domainList.add(index - 1, s);
                    lstDomain.getSelectionModel().select(index - 1);
                    copyListToDomain();
                }//end if
            }
        });

        Button btnDown = new Button("˅");
        btnDown.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnDown, Priority.ALWAYS);
        btnDown.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = lstDomain.getSelectionModel().getSelectedIndex();

                if((index > -1) && (index != domainList.size() - 1)) {
                    String s = domainList.get(index);
                    domainList.remove(index);
                    domainList.add(index + 1, s);
                    lstDomain.getSelectionModel().select(index + 1);
                    copyListToDomain();
                }//end if
            }
        });

        topButtonPane.getChildren().addAll(btnRemove, btnAdd, btnUp, btnDown);

        HBox topLabelPane = new HBox();

        Label lblEnum = new Label(_domain.enumClass.getSimpleName());
        lblEnum.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblEnum, Priority.ALWAYS);
        Label lblDomain = new Label("Domain");
        lblDomain.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblDomain, Priority.ALWAYS);

        topLabelPane.getChildren().addAll(lblEnum, lblDomain);

        topPane.getChildren().addAll(topButtonPane, topLabelPane);

        //mid pane - list views
        HBox midPane = new HBox();

        enumList = FXCollections.observableArrayList();
        if((_domain != null) && (_domain.enumClass != null)) {
            for (Enum e : _domain.enumClass.getEnumConstants()) {
                enumList.add(e.name());
            }//end for each
        }//end if

        lstEnums = new ListView<String>(enumList);
        lstEnums.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lstEnums, Priority.ALWAYS);
        lstEnums.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() > 1) {
                    int index = lstEnums.getSelectionModel().getSelectedIndex();
                    if(index > -1) {
                        evtAdd.handle(null);
                    }//end if
                }//end if
            }
        });

        domainList = FXCollections.observableArrayList();
        if(_domain != null) {
            if(_domain.domain != null) {
                for(Enum e: _domain.domain)
                    domainList.add(e.name());
            }//end if
        }//end if

        lstDomain = new ListView<String>(domainList);
        lstDomain.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lstDomain, Priority.ALWAYS);
        lstDomain.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() > 1) {
                    int index = lstDomain.getSelectionModel().getSelectedIndex();
                    if(index > -1) {
                        evtRemove.handle(null);
                    }//end if
                }//end if
            }
        });

        midPane.getChildren().addAll(lstEnums, lstDomain);

        //bottom pane - OK and Cancel buttons
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

    public static final class DomainEnum
    {
        public Enum[] domain;
        public Class<? extends Enum> enumClass;
    }

    public static boolean editEnumDomain(DomainEnum domain,
                                         Stage parent) {
        DomainEditorEnum edit = new DomainEditorEnum(domain, 400, 500);

        edit.initOwner(parent);
        edit.initModality(Modality.WINDOW_MODAL);
        edit.showAndWait();
        domain = edit._domain;

        return edit.concelled();
    }

    private void copyListToDomain() {
        _domain.domain = new Enum[domainList.size()];
        for(int i = 0; i < domainList.size(); i++) {
            _domain.domain[i] = CrobeEnums.getEnum(_domain.enumClass.getSimpleName(), domainList.get(i));
        }//end for i
    }
}
