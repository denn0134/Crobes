package crobes.core.factors.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class FlowEditor extends Stage
{
    private BorderPane root;
    private Label lblAngle;
    private Button btnOK;
    private Button btnCancel;
    private Canvas cnvAngle;

    private static final int CANVAS_SIZE = 200;
    private static final int GAUGE_DIAMETER = 170;
    private static final int ARROW_WIDTH = 20;
    private static final int ARROW_LENGTH = 150;
    private static final int[] INTERVAL_RISE = {0,1,1,2,1,3,2,4,1,4,2,3,1,2,1,1};
    private static final int[] INTERVAL_RUN =  {1,4,2,3,1,2,1,1,0,-1,-1,-2,-1,-3,-2,-4};
    private static Point2D GCENTER = new Point2D(CANVAS_SIZE / 2.0, CANVAS_SIZE / 2.0);

    private ArrayList<Point2D> _arrow_pts;
    private ArrayList<AngleInterval> _intervals;

    private boolean _cancelled;
    public boolean cancelled() {
        return _cancelled;
    }

    private FlowInfo _info;
    public FlowInfo info() {
        return _info;
    }

    public FlowEditor(double width, double height) {
        root = new BorderPane();
        //gCenter = new Point2D(CANVAS_SIZE / 2.0, CANVAS_SIZE / 2.0);

        _cancelled = true;

        _info = new FlowInfo();
        _info.rise = 0;
        _info.run = 1;
        _info.widthCoefficient = 1;
        _info.speed = 1;

        //build the arrow gauge
        {
            //set the points for the arrow polygon
            int arrowButt, arrowTip, arrowHead,
                    arrowTop, arrowBottom;
            arrowButt = (CANVAS_SIZE - ARROW_LENGTH) / 2;
            arrowTip = CANVAS_SIZE - arrowButt;
            arrowHead = 3 * ARROW_WIDTH / 2;
            arrowTop = CANVAS_SIZE / 2 - ARROW_WIDTH / 2;
            arrowBottom = CANVAS_SIZE / 2 + ARROW_WIDTH / 2;

            _arrow_pts = new ArrayList<Point2D>();
            _arrow_pts.add(new Point2D(arrowButt, arrowTop));
            _arrow_pts.add(new Point2D(arrowTip - arrowHead,
                    arrowTop));
            _arrow_pts.add(new Point2D(arrowTip - arrowHead,
                    CANVAS_SIZE / 2 - arrowHead / 2));
            _arrow_pts.add(new Point2D(arrowTip, CANVAS_SIZE / 2));
            _arrow_pts.add(new Point2D(arrowTip - arrowHead,
                    CANVAS_SIZE / 2 + arrowHead / 2));
            _arrow_pts.add(new Point2D(arrowTip - arrowHead,
                    arrowBottom));
            _arrow_pts.add(new Point2D(arrowButt, arrowBottom));

            //set the angel interval points
            _intervals = new ArrayList<AngleInterval>();
            Point2D intervalBase = new Point2D(arrowTip, CANVAS_SIZE / 2);

            for (int i = 0; i < INTERVAL_RISE.length; i++) {
                _intervals.add(new AngleInterval(INTERVAL_RISE[i], INTERVAL_RUN[i], intervalBase));
                _intervals.add(new AngleInterval(INTERVAL_RISE[i] * -1, INTERVAL_RUN[i] * -1, intervalBase));
            }//end for i
        }//end build arrow gauge

        //configuration controls
        VBox vbxTop = new VBox();

        BorderPane pnlAngle = new BorderPane();
        VBox.setVgrow(pnlAngle, Priority.ALWAYS);

        cnvAngle = new Canvas();
        cnvAngle.setHeight(CANVAS_SIZE);
        cnvAngle.setWidth(CANVAS_SIZE);
        cnvAngle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Point2D click = new Point2D(event.getX(), event.getY());
                if (click.distance(GCENTER) <= GAUGE_DIAMETER / 2) {
                    AngleInterval closest = getAngleInterval(click);

                    _info.rise = closest.rise;
                    _info.run = closest.run;

                    drawAngle();
                }//end if
            }
        });
        pnlAngle.setCenter(cnvAngle);

        lblAngle = new Label();
        lblAngle.setMaxWidth(Double.MAX_VALUE);
        lblAngle.setAlignment(Pos.CENTER);
        VBox.setVgrow(lblAngle, Priority.NEVER);

        HBox hbxWidth = new HBox();
        VBox.setVgrow(hbxWidth, Priority.NEVER);

        Label lblWidth = new Label("Half-Width");
        lblWidth.setPrefWidth(100);
        HBox.setHgrow(lblWidth, Priority.NEVER);

        Spinner<Integer> spnWidth = new Spinner<Integer>();
        SpinnerValueFactory<Integer> wvf = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1, 1);
        wvf.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                _info.widthCoefficient = newValue;
            }
        });
        spnWidth.setValueFactory(wvf);
        spnWidth.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(spnWidth, Priority.ALWAYS);

        hbxWidth.getChildren().addAll(lblWidth, spnWidth);

        HBox hbxSpeed = new HBox();
        VBox.setVgrow(hbxSpeed, Priority.NEVER);

        Label lblSpeed = new Label("Speed");
        lblSpeed.setPrefWidth(100);
        HBox.setHgrow(lblSpeed, Priority.NEVER);

        Spinner<Integer> spnSpeed = new Spinner<Integer>();
        SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4, 1, 1);
        svf.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                _info.speed = newValue;
            }
        });
        spnSpeed.setValueFactory(svf);
        spnSpeed.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(spnSpeed, Priority.ALWAYS);

        hbxSpeed.getChildren().addAll(lblSpeed, spnSpeed);

        vbxTop.getChildren().addAll(pnlAngle, lblAngle, hbxWidth, hbxSpeed);

        //ok and cancel buttons
        FlowPane pnlBottom = new FlowPane();
        pnlBottom.setAlignment(Pos.CENTER_RIGHT);

        btnOK = new Button("OK");
        btnOK.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _cancelled = false;
                close();
            }
        });

        btnCancel = new Button("Cancel");
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _cancelled = true;
                close();
            }
        });

        pnlBottom.getChildren().addAll(btnOK, btnCancel);

        drawAngle();

        root.setCenter(vbxTop);
        root.setBottom(pnlBottom);

        Scene scene = new Scene(root, width, height );
        setScene(scene);
    }

    private void drawAngle() {
        lblAngle.setText(String.format("Rise/Run: %0$d/%2$d", _info.rise, _info.run));

        GraphicsContext gc = cnvAngle.getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0,0, CANVAS_SIZE, CANVAS_SIZE);

        gc.setFill(Color.CORNSILK);
        gc.fillOval((CANVAS_SIZE - GAUGE_DIAMETER) / 2,
                (CANVAS_SIZE - GAUGE_DIAMETER) / 2,
                GAUGE_DIAMETER, GAUGE_DIAMETER);

        gc.setFill(Color.BLUE);

        double angle = getRotationAngle(_info.rise, _info.run);
        Rotate rotate = new Rotate(angle, GCENTER.getX(), GCENTER.getY());
        ArrayList<Point2D> rot_pts = new ArrayList<Point2D>();
        for (int i = 0; i < _arrow_pts.size(); i++) {
            Point2D ap = _arrow_pts.get(i);
            rot_pts.add(rotate.transform(ap.getX(), ap.getY()));
        }//end for i

        double[] xVals = new double[7];
        double[] yVals = new double[7];

        for (int i = 0; i < rot_pts.size(); i++) {
            xVals[i] = rot_pts.get(i).getX();
            yVals[i] = rot_pts.get(i).getY();
        }//end for i
        gc.fillPolygon(xVals, yVals, _arrow_pts.size());
    }

    private static double getRotationAngle(int rise, int run) {
        double hyp = Math.sqrt(Math.pow(rise, 2.0) + Math.pow(run, 2.0));
        double angle = Math.abs(Math.asin(rise / hyp));

        //modify the angle based on quadrant
        if (rise < 0) {
            if (run < 0)
                angle += Math.PI;
            else
                angle = 2 * Math.PI - angle;
        }//end else if
        else {
            if (run < 0)
                angle = Math.PI - angle;
        }//end else

        return Math.toDegrees(angle);
    }

    private AngleInterval getAngleInterval(Point2D point) {
        AngleInterval result = _intervals.get(0);
        double dist = point.distance(result.point);

        for (int i = 1; i < _intervals.size(); i++) {
            AngleInterval check = _intervals.get(i);
            double checkDist = point.distance(check.point);
            if (checkDist < dist) {
                result = check;
                dist = checkDist;
            }//end if
        }//end for i

        return result;
    }

    private static String quotedStr(String str) {
        return "\"" + str + "\"";
    }
    private static String point2DtoJson(Point2D p) {
        StringBuilder sb = new StringBuilder();

        sb.append("{");

        sb.append(quotedStr("x"));
        sb.append(": ");
        sb.append(p.getX());
        sb.append(", ");

        sb.append(quotedStr("y"));
        sb.append(": ");
        sb.append(p.getY());

        sb.append("}");

        return sb.toString();
    }
    private String angleIntervalListToJson() {
        StringBuilder sb = new StringBuilder();

        sb.append("[");

        for (int i = 0; i < _intervals.size(); i++) {
            if (i > 0) sb.append(", ");

            sb.append(_intervals.get(i).toJson());
        }//end for i

        sb.append("]");

        return sb.toString();
    }

    /***
     * Class for transporting Flow properties
     */
    public static final class FlowInfo
    {
        public int rise;
        public int run;
        public int widthCoefficient;
        public int speed;
    }

    private static final class AngleInterval
    {
        int rise;
        int run;
        double angle;
        Point2D point;

        public AngleInterval(int rs, int rn, Point2D base) {
            rise = rs;
            run = rn;
            angle = getRotationAngle(rise, run);

            Rotate r = new Rotate(angle, GCENTER.getX(), GCENTER.getY());
            point = r.transform(base.getX(), base.getY());
        }

        public String toJson() {
            StringBuilder sb = new StringBuilder();

            sb.append("{");

            sb.append(quotedStr("rise"));
            sb.append(": ");
            sb.append(rise);
            sb.append(", ");

            sb.append(quotedStr("run"));
            sb.append(": ");
            sb.append(run);
            sb.append(", ");

            sb.append(quotedStr("angle"));
            sb.append(": ");
            sb.append(angle);
            sb.append(", ");

            sb.append(quotedStr("point"));
            sb.append(": ");
            sb.append(point2DtoJson(point));

            sb.append("}");

            return sb.toString();
        }
    }

    /***
     * Gets properties to be used in creating a Flow object
     * by presenting the user with a GUI dialog.
     * @return Returns the user selected properties for creating
     *         a Flow object.
     */
    public static FlowInfo getFlowInfo(Stage parentStage) {
        FlowInfo result = null;

        FlowEditor edit = new FlowEditor(300, 310);
        edit.initOwner(parentStage);
        edit.initModality(Modality.APPLICATION_MODAL);

        edit.showAndWait();
        if (!edit.cancelled())
            result = edit.info();

        return result;
    }
}
