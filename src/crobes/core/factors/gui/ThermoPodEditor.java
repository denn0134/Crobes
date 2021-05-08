package crobes.core.factors.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/***
 * GUI form for creating ThermoPods.
 */
public class ThermoPodEditor extends PodEditor
{
    private ThermoPodInfo tpInfo() {
        return (ThermoPodInfo) _info;
    }

    public ThermoPodEditor() {
        super(PODEDITOR_WIDTH, PODEDITOR_HEIGHT);

        _info = new ThermoPodInfo();
    }

    @Override
    protected void buildChildPane() {
        VBox vbxThermo = new VBox();

        Label lblThermoPod = new Label("ThermoPod");
        lblThermoPod.setMaxWidth(Double.MAX_VALUE);
        lblThermoPod.setAlignment(Pos.CENTER);
        VBox.setVgrow(lblThermoPod, Priority.ALWAYS);

        //thermal value
        HBox hbxThermal = new HBox();
        VBox.setVgrow(hbxThermal, Priority.NEVER);

        Label lblThermal = new Label("Thermal Value");
        lblThermal.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblThermal, Priority.ALWAYS);

        Spinner<Integer> spnThermal = new Spinner<Integer>();
        SpinnerValueFactory<Integer> tvf = new SpinnerValueFactory.IntegerSpinnerValueFactory(-4, 4, 0, 1);
        spnThermal.setValueFactory(tvf);
        tvf.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                tpInfo().thermalValue = newValue;
            }
        });

        HBox.setHgrow(spnThermal, Priority.NEVER);

        hbxThermal.getChildren().addAll(lblThermal, spnThermal);

        //range
        HBox hbxRange = new HBox();
        VBox.setVgrow(hbxRange, Priority.NEVER);

        Label lblRange = new Label("Range");
        lblRange.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblRange, Priority.ALWAYS);

        Spinner<Integer> spnRange = new Spinner<Integer>();
        SpinnerValueFactory<Integer> rvf = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1, 1);
        spnRange.setValueFactory(rvf);
        rvf.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                tpInfo().range = newValue;
            }
        });
        HBox.setHgrow(spnRange, Priority.NEVER);

        hbxRange.getChildren().addAll(lblRange, spnRange);

        vbxThermo.getChildren().addAll(lblThermoPod, hbxThermal, hbxRange);

        bpnChild.setCenter(vbxThermo);
    }

    /***
     * Class for transporting data about the
     * configuration of a ThermoPod.
     */
    public static class ThermoPodInfo extends PodInfo
    {
        public int thermalValue;
        public int range;
    }

    public static ThermoPodInfo getPodInfo(Stage parentStage) {
        ThermoPodEditor edit = new ThermoPodEditor();
        return (ThermoPodInfo) PodEditor.getPodInfo(edit, parentStage);
    }
}
