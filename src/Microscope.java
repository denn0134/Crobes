import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.util.List;

public class Microscope extends Application
{
    //panels
    private BorderPane root;
    private BorderPane pnlBottom;
    private BorderPane pnlTop;
    private GridPane pnlControls;
    private BorderPane pnlScope;
    private BorderPane pnlLocation;

    //buttons, edits, combos, etc.
    private Button btnZoomIn;
    private Button btnZoomOut;
    private Button btnPanUp;
    private Button btnPanDown;
    private Button btnPanLeft;
    private Button btnPanRight;
    private ChoiceBox<String> cmbMode;
    private TextArea txtLocation;

    //microscope objects
    private World world;
    private Lens lens;
    WebView lensPane;

    private int _radix;

    public static final String FONT_NAME = "monospace";
    private static final int FONT_WIDTH_CONSTANT = 27;
    private static final int FONT_HEIGHT_CONSTANT = 63;
    private static final double FONT_WIDTH_COEFFICIENT = 0.6;
    private static final double FONT_HEIGHT_COEFFICIENT = 1.13;
    private static final int MIN_SCALE = 1;
    private static final int MAX_SCALE = 14;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();

        Parameters p = getParameters();
        List<String> list = p.getUnnamed();
        if((list != null) &&
                (list.size() > 0)) {
            _radix = Integer.parseInt(list.get(0));
        }//end if
    }

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();

        //set up the bottom pane for the info panel
        configureBottomPane();
        root.setBottom(pnlBottom);

        //set up the top pane for the controls and the scope
        configureTopPane();
        root.setCenter(pnlTop);

        //set up the scope pane including the world and lens
        world = new World(_radix);
        world.environment().setAmbience();
        lens = new Lens(world);
        lens.mode(Lens.Mode.LIGHT);
        cmbMode.setValue(lens.mode().name());

        ChangeListener<Number> stageResize = (observable, oldValue, newValue) -> {
            refreshLens();
        };
        primaryStage.widthProperty().addListener(stageResize);
        primaryStage.heightProperty().addListener(stageResize);

        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setTitle("Microbescope");
        primaryStage.setScene(scene);
        primaryStage.show();

        world.processEnvironment();
        refreshLens();
    }

    private void configureBottomPane() {
        pnlBottom = new BorderPane();
        pnlBottom.setPrefSize(0, 200);
        BackgroundFill fill = new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY);
        pnlBottom.setBackground(new Background(fill));

        pnlLocation = new BorderPane();
        pnlLocation.setPrefSize(200, 0);

        txtLocation = new TextArea();
        txtLocation.setEditable(false);
        txtLocation.setFont(Font.font("monospace", 14));
        pnlLocation.setCenter(txtLocation);

        pnlBottom.setLeft(pnlLocation);
    }
    private void configureTopPane() {
        pnlTop = new BorderPane();
        BackgroundFill fill = new BackgroundFill(Color.TAN, CornerRadii.EMPTY, Insets.EMPTY);
        pnlTop.setBackground(new Background(fill));

        //set up the left pane for the controls
        configureControlPane();
        pnlTop.setLeft(pnlControls);

        //set up the right pane for the microscope viewer
        configureScopePane();
        pnlTop.setCenter(pnlScope);
    }

    private Button createButton(String text, boolean fill) {
        Button result = new Button(text);
        result.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return result;
    }
    private ColumnConstraints createColumn(double pctWidth, boolean fill) {
        ColumnConstraints result = new ColumnConstraints();
        result.setPercentWidth(pctWidth);
        result.setFillWidth(fill);
        return result;
    }
    private RowConstraints createRow(double pctHeight, boolean fill) {
        RowConstraints result = new RowConstraints();
        result.setPercentHeight(pctHeight);
        result.setFillHeight(fill);
        return result;
    }

    private void configureControlPane() {
        pnlControls = new GridPane();
        pnlControls.setPrefSize(200, 0);

        for(int i = 0; i < 3; i++) {
            pnlControls.getRowConstraints().add(createRow(100.0 / 3.0, false));
        }//end for i

        BorderPane ctrPaneTop = new BorderPane();
        GridPane ctrPaneMid = new GridPane();
        BorderPane ctrPaneBot = new BorderPane();

        //middle pane - pan and zoom buttons
        ctrPaneMid.getColumnConstraints().add(createColumn(100.0 / 3.0, true));
        ctrPaneMid.getColumnConstraints().add(createColumn(100.0 / 6.0, true));
        ctrPaneMid.getColumnConstraints().add(createColumn(100.0 / 6.0, true));
        ctrPaneMid.getColumnConstraints().add(createColumn(100.0 / 3.0, true));

        for(int i = 0; i < 3; i++) {
            ctrPaneMid.getRowConstraints().add(createRow(100.0 / 3.0, true));
        }//end for i

        btnPanUp = createButton("UP", true);
        ctrPaneMid.add(btnPanUp, 1, 0, 2, 1);
        btnPanLeft = createButton("<", true);
        ctrPaneMid.add(btnPanLeft, 0, 1, 1, 1);
        btnZoomIn = createButton("+", true);
        ctrPaneMid.add(btnZoomIn, 1, 1, 1, 1);
        btnZoomOut = createButton("-", true);
        ctrPaneMid.add(btnZoomOut, 2, 1, 1, 1);
        btnPanRight = createButton(">", true);
        ctrPaneMid.add(btnPanRight, 3, 1, 1, 1);
        btnPanDown = createButton("Down", true);
        ctrPaneMid.add(btnPanDown, 1, 2, 2, 1);

        pnlControls.add(ctrPaneTop, 0, 0);
        pnlControls.add(ctrPaneMid, 0, 1);
        pnlControls.add(ctrPaneBot, 0, 2);

        btnZoomIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(lens.scale() < MAX_SCALE)
                    lens.scale(lens.scale() + 1);

                refreshLens();
            }
        });
        btnZoomOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(lens.scale() > 1)
                    lens.scale(lens.scale() - 1);

                refreshLens();
            }
        });

        EventHandler<ActionEvent> panClick = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean horiz;
                int dir;
                Button btn = (Button)event.getSource();
                if((btn == btnPanUp) || (btn == btnPanDown)) {
                    horiz = false;
                    if(btn == btnPanUp)
                        dir = -1;
                    else
                        dir = 1;
                }//end if
                else {
                    horiz = true;
                    if(btn == btnPanLeft)
                        dir = -1;
                    else
                        dir = 1;
                }//end else

                int span = (horiz) ? lens.width() : lens.height();
                int max = (horiz) ? world.environment().width() : world.environment().height();
                int delta = span / 4;
                Point center = lens.center();

                int pos = (horiz) ? center.x : center.y;
                pos += (delta * dir);
                if(pos < 0)
                    pos = 0;
                if(pos >= max)
                    pos = max - 1;

                if(horiz)
                    center.x = pos;
                else
                    center.y = pos;

                lens.center(center);
                refreshLens();
            }
        };
        btnPanUp.setOnAction(panClick);
        btnPanDown.setOnAction(panClick);
        btnPanLeft.setOnAction(panClick);
        btnPanRight.setOnAction(panClick);

        //bottom pane - mode dropdown
        VBox vb = new VBox();

        Label label = new Label("Environment Mode");

        cmbMode = new ChoiceBox<>();
        for(Lens.Mode mode : Lens.Mode.values()) {
            cmbMode.getItems().add(mode.name());
        }//end for mode

        ChangeListener<String> cmbModeChange = (observable, oldValue, newValue) -> {
            if(!newValue.equalsIgnoreCase(oldValue)) {
                lens.mode(Lens.Mode.valueOf(newValue));
                refreshLens();
            }//end if
        };
        cmbMode.valueProperty().addListener(cmbModeChange);

        vb.getChildren().addAll(label, cmbMode);
        ctrPaneBot.setCenter(vb);
    }
    private void configureScopePane() {
        pnlScope = new BorderPane();
        lensPane = new WebView();

        //set the cursor on the WebView to be a crosshair
        lensPane.getEngine().getLoadWorker().stateProperty().addListener(
                (observable, oldState, newState) -> {
                    if(newState != Worker.State.SUCCEEDED) {
                        return;
                    }//end if

                    Document doc = lensPane.getEngine().getDocument();
                    Element body = (Element)doc.getElementsByTagName("body").item(0);
                    String style = body.getAttribute("style");
                    body.setAttribute("style", "cursor: crosshair;" + style);
                });
        //set the selection event
        lensPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //get the x,y clicked
                double x = event.getX();
                double y = event.getY();

                //convert the screen coordinates to a mixel
                int wBase = (int)Math.round(Math.floor(lens.fontSize() / 4 * 2.5)) + 7;
                int hBase = (int)Math.round(lens.fontSize() / 4 * 1.5) * 3 + 7;

                int mixelX = (int)Math.round(Math.floor((x - wBase) / calcWidthIncrement())) + 1;
                int mixelY = (int)Math.round(Math.floor((y - hBase) / calcHeightIncrement())) + 2;

                //adjust for the lens origin
                mixelX += lens.origin().x - 1;
                mixelY += lens.origin().y - 1;

                //set the mixel location as selected
                Location mixel = world.getLocation(mixelX, mixelY);
                if(lens.selection() == null) {
                    if(mixel != null) {
                        lens.selection(mixel);
                        mixel.selected(true);
                    }//end if
                }//end if
                else {
                    //deselect the current selection
                    Location current = lens.selection();
                    current.selected(false);
                    lens.selection(null);

                    if((mixel != null) && (mixel != current)) {
                        lens.selection(mixel);
                        mixel.selected(true);
                    }//end if
                }//end else

                setSelectedLocation(lens.selection());
                refreshLens();
            }
        });

        pnlScope.setCenter(lensPane);
    }

    private double calcWidthIncrement() {
        return lens.fontSize() * FONT_WIDTH_COEFFICIENT;
    }
    private int calcHeightIncrement() {
        return (int)(Math.round(lens.fontSize() * FONT_HEIGHT_COEFFICIENT));
    }

    private void setSelectedLocation(Location location) {
        String locText = "";
        if(location != null) {
            locText = location.toString();
        }//end if

        txtLocation.setText(locText);
    }

    private void resizeLens() {
        //set the lens width and height based on
        //the size of the lensPane
        int lensWidth, lensHeight;
        int paneWidth = (int)Math.round(lensPane.getWidth());
        int paneHeight = (int)Math.round(lensPane.getHeight());

        lensWidth = (int)(Math.round((paneWidth - FONT_WIDTH_CONSTANT) / calcWidthIncrement()));
        lensHeight = (paneHeight - FONT_HEIGHT_CONSTANT) / calcHeightIncrement();

        lens.width(lensWidth);
        lens.height(lensHeight);
    }
    private void refreshLens() {
        resizeLens();
        lens.renderWorld();
        String content = lens.flush();
        lensPane.getEngine().loadContent(content);
    }
}
