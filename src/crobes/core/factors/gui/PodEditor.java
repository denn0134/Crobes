package crobes.core.factors.gui;

import crobes.core.CrobeEnums;
import crobes.core.World;
import crobes.core.factors.Pod;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/***
 * GUI form for creating Pod Factors.
 */
public abstract class PodEditor extends Stage
{
    protected static final double PODEDITOR_WIDTH = 500;
    protected static final double PODEDITOR_HEIGHT = 300;
    protected static final double BASE_FIELD_WIDTH = 130;

    protected BorderPane root;
    protected BorderPane bpnChild;
    protected ToggleGroup tglDirection;

    private boolean _cancelled;
    protected PodInfo _info;

    /***
     * Cancel/OK state of the dialog.
     * @return Returns true if the user clicked the
     * Cancel button; false if they clicked the OK
     * button.
     */
    public boolean cancelled() {
        return _cancelled;
    }
    /***
     * Configuration information from the dialog.
     * @return Returns the PodInfo.
     */
    public PodInfo info() {
        return _info;
    }

    public PodEditor(double width, double height) {
        root = new BorderPane();

        VBox vbxControls = new VBox();

        HBox hbxPod = new HBox();
        VBox.setVgrow(hbxPod, Priority.NEVER);

        VBox vbxBase = new VBox();
        HBox.setHgrow(vbxBase, Priority.ALWAYS);

        //time to live
        HBox hbxTime = new HBox();
        VBox.setVgrow(hbxTime, Priority.NEVER);

        Label lblTime = new Label("Life (infinite)");
        lblTime.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblTime, Priority.ALWAYS);

        CheckBox chkInfinite = new CheckBox();
        chkInfinite.setPrefWidth(20);
        chkInfinite.setAlignment(Pos.CENTER);
        HBox.setHgrow(chkInfinite, Priority.NEVER);

        Spinner<Integer> spnTimeToLive = new Spinner<Integer>();
        spnTimeToLive.setPrefWidth(BASE_FIELD_WIDTH);
        SpinnerValueFactory<Integer> tvf = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1, 1);
        spnTimeToLive.setValueFactory(tvf);
        tvf.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                _info.timeToLive = newValue;
            }
        });
        HBox.setHgrow(spnTimeToLive, Priority.NEVER);

        chkInfinite.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                spnTimeToLive.setDisable(chkInfinite.isSelected());

                if (chkInfinite.isSelected())
                    _info.timeToLive = -1;
                else
                    _info.timeToLive = spnTimeToLive.getValue();
            }
        });

        hbxTime.getChildren().addAll(lblTime, chkInfinite, spnTimeToLive);

        //size
        HBox hbxSize = new HBox();
        VBox.setVgrow(hbxSize, Priority.NEVER);

        Label lblSize = new Label("Size");
        lblSize.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblSize, Priority.ALWAYS);

        ChoiceBox<String> chbSize = new ChoiceBox<String>();
        for (Pod.PodSize ps: Pod.PodSize.values())
            chbSize.getItems().add(ps.name());
        chbSize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _info.size = Pod.PodSize.valueOf(chbSize.getValue());
            }
        });
        chbSize.setMaxWidth(Double.MAX_VALUE);
        chbSize.setPrefWidth(BASE_FIELD_WIDTH);

        hbxSize.getChildren().addAll(lblSize, chbSize);

        //motility
        HBox hbxMotility = new HBox();
        VBox.setVgrow(hbxMotility, Priority.NEVER);

        Label lblMotility = new Label("Motility");
        lblMotility.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblMotility, Priority.ALWAYS);

        ChoiceBox<String> chbMotility = new ChoiceBox<String>();
        for (CrobeEnums.MotilityType mt: CrobeEnums.MotilityType.values())
            chbMotility.getItems().add(mt.name());
        chbMotility.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _info.motility = CrobeEnums.MotilityType.valueOf(chbMotility.getValue());
            }
        });
        chbMotility.setMaxWidth(Double.MAX_VALUE);
        chbMotility.setPrefWidth(BASE_FIELD_WIDTH);

        hbxMotility.getChildren().addAll(lblMotility, chbMotility);

        //motion
        HBox hbxMotion = new HBox();
        VBox.setVgrow(hbxMotion, Priority.NEVER);

        Label lblMotion = new Label("Motion");
        lblMotion.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblMotion, Priority.ALWAYS);

        ChoiceBox<String> chbMotion = new ChoiceBox<String>();
        for (Pod.PodMotion pm: Pod.PodMotion.values())
            chbMotion.getItems().add(pm.name());
        chbMotion.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _info.motion = Pod.PodMotion.valueOf(chbMotion.getValue());
            }
        });
        chbMotion.setMaxWidth(Double.MAX_VALUE);
        chbMotion.setPrefWidth(BASE_FIELD_WIDTH);

        hbxMotion.getChildren().addAll(lblMotion, chbMotion);

        //speed
        HBox hbxSpeed = new HBox();
        VBox.setVgrow(hbxSpeed, Priority.NEVER);

        Label lblSpeed = new Label("Speed");
        lblSpeed.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblSpeed, Priority.ALWAYS);

        Spinner<Integer> spnSpeed = new Spinner<Integer>();
        spnSpeed.setPrefWidth(BASE_FIELD_WIDTH);
        SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999, 1, 1);
        spnSpeed.setValueFactory(svf);
        svf.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                _info.speed = newValue;
            }
        });
        HBox.setHgrow(spnSpeed, Priority.NEVER);

        hbxSpeed.getChildren().addAll(lblSpeed, spnSpeed);

        vbxBase.getChildren().addAll(hbxTime, hbxSize,
                hbxMotility, hbxMotion, hbxSpeed);

        //direction
        VBox vbxDirection = new VBox();
        HBox.setHgrow(vbxDirection, Priority.NEVER);

        Label lblDirection = new Label("Direction");
        lblDirection.setMaxWidth(Double.MAX_VALUE);
        lblDirection.setPrefHeight(64);
        lblDirection.setAlignment(Pos.CENTER);
        VBox.setVgrow(lblDirection, Priority.ALWAYS);

        GridPane grdDirection = new GridPane();
        tglDirection = new ToggleGroup();
        VBox.setVgrow(grdDirection, Priority.ALWAYS);
        grdDirection.getColumnConstraints().add(new ColumnConstraints(75));
        grdDirection.getColumnConstraints().add(new ColumnConstraints(75));
        grdDirection.getColumnConstraints().add(new ColumnConstraints(75));

        grdDirection.getRowConstraints().add(new RowConstraints());
        grdDirection.getRowConstraints().add(new RowConstraints());
        grdDirection.getRowConstraints().add(new RowConstraints());

        RadioButton rbtUpLeft = new RadioButton();
        setToggleButtonSkin(rbtUpLeft);
        setButtonIcon(rbtUpLeft, "upleft.png");
        grdDirection.add(rbtUpLeft, 0, 0);
        RadioButton rbtUp = new RadioButton();
        setToggleButtonSkin(rbtUp);
        setButtonIcon(rbtUp, "up.png");
        grdDirection.add(rbtUp, 1, 0);
        RadioButton rbtUpRight = new RadioButton();
        setToggleButtonSkin(rbtUpRight);
        setButtonIcon(rbtUpRight, "upright.png");
        grdDirection.add(rbtUpRight, 2, 0);
        RadioButton rbtLeft = new RadioButton();
        setToggleButtonSkin(rbtLeft);
        setButtonIcon(rbtLeft, "left.png");
        grdDirection.add(rbtLeft, 0, 1);
        RadioButton rbtRandom = new RadioButton();
        setToggleButtonSkin(rbtRandom);
        setButtonIcon(rbtRandom, "random.png");
        grdDirection.add(rbtRandom, 1, 1);
        RadioButton rbtRight = new RadioButton();
        setToggleButtonSkin(rbtRight);
        setButtonIcon(rbtRight, "right.png");
        grdDirection.add(rbtRight, 2, 1);
        RadioButton rbtDownLeft = new RadioButton();
        setToggleButtonSkin(rbtDownLeft);
        setButtonIcon(rbtDownLeft, "downleft.png");
        grdDirection.add(rbtDownLeft, 0, 2);
        RadioButton rbtDown = new RadioButton();
        setToggleButtonSkin(rbtDown);
        setButtonIcon(rbtDown, "down.png");
        grdDirection.add(rbtDown, 1, 2);
        RadioButton rbtDownRight = new RadioButton();
        setToggleButtonSkin(rbtDownRight);
        setButtonIcon(rbtDownRight, "downright.png");
        grdDirection.add(rbtDownRight, 2, 2);

        tglDirection.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue == rbtUpLeft)
                    _info.direction = World.Direction.UPLEFT;
                else if (newValue == rbtUp)
                    _info.direction = World.Direction.UP;
                else if (newValue == rbtUpRight)
                    _info.direction = World.Direction.UPRIGHT;
                else if (newValue == rbtLeft)
                    _info.direction = World.Direction.LEFT;
                else if (newValue == rbtRandom)
                    _info.direction = World.Direction.RANDOM;
                else if (newValue == rbtRight)
                    _info.direction = World.Direction.RIGHT;
                else if (newValue == rbtDownLeft)
                    _info.direction = World.Direction.DOWNLEFT;
                else if (newValue == rbtDown)
                    _info.direction = World.Direction.DOWN;
                else if (newValue == rbtDownRight)
                    _info.direction = World.Direction.DOWNRIGHT;
                else
                    _info.direction = World.Direction.NONE;
            }
        });

        vbxDirection.getChildren().addAll(lblDirection, grdDirection);

        hbxPod.getChildren().addAll(vbxBase, vbxDirection);

        //child class properties
        bpnChild = new BorderPane();
        VBox.setVgrow(bpnChild, Priority.ALWAYS);
        buildChildPane();

        vbxControls.getChildren().addAll(hbxPod, bpnChild);

        FlowPane fpnOKCancel = new FlowPane();
        fpnOKCancel.setAlignment(Pos.CENTER_RIGHT);

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

        fpnOKCancel.getChildren().addAll(btnOK, btnCancel);

        root.setCenter(vbxControls);
        root.setBottom(fpnOKCancel);

        Scene scene = new Scene(root, width, height);
        setScene(scene);
    }

    protected abstract void buildChildPane();
    protected void setToggleButtonSkin(RadioButton rb) {
        rb.setMaxWidth(Double.MAX_VALUE);
        rb.setToggleGroup(tglDirection);
        rb.getStyleClass().remove("radio-button");
        rb.getStyleClass().add("toggle-button");
    }
    protected void setButtonIcon(Labeled btn, String iconResource) {
        Image img = new Image(getClass().getResourceAsStream(iconResource));
        btn.setGraphic(new ImageView(img));
    }

    /***
     * Class for transporting data about the
     * configuration of a Pod.
     */
    public static class PodInfo
    {
        public int timeToLive;
        public Pod.PodSize size;
        public CrobeEnums.MotilityType motility;
        public Pod.PodMotion motion;
        public int speed;
        public World.Direction direction;
    }

    public static PodInfo getPodInfo(PodEditor editor, Stage parentStage) {
        PodInfo result = null;

        editor.initOwner(parentStage);
        editor.initModality(Modality.APPLICATION_MODAL);
        editor.showAndWait();

        if (!editor.cancelled())
            result = editor.info();

        return result;
    }
}
