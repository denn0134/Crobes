package crobes;

import crobes.core.*;
import crobes.core.factors.Flow;
import crobes.core.factors.gui.FlowEditor;
import crobes.genetics.genomics.Genome;
import crobes.genetics.genomics.Genomics;
import crobes.genetics.gui.Sequencer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.util.Timer;
import java.util.TimerTask;

/***
 * Main GUI form for the Crobes application.  Contains the "main" mmethod.
 */
public class Microscope extends Application
{
    //panels
    private VBox root;

    //buttons, edits, combos, etc.
    TextArea txtLocation;
    TextArea txtCrobeDetail;

    private Timer tmrClock;

    //microscope objects
    private World world;
    private Lens lens;
    WebView lensPane;

    private int _radix;

    /***
     * The font name for rendering the world within the microscope.
     */
    public static final String FONT_NAME = "monospace";
    private static final int FONT_WIDTH_CONSTANT = 27;
    private static final int FONT_HEIGHT_CONSTANT = 63;
    private static final double FONT_WIDTH_COEFFICIENT = 0.6;
    private static final double FONT_HEIGHT_COEFFICIENT = 1.13;
    private static final int MIN_SCALE = 1;
    private static final int MAX_SCALE = 14;
    private static final long DELAY_SLOW = 25000;
    private static final long DELAY_MEDIUM = 5000;
    private static final long DELAY_FAST = 1000;

    private static String RESOURCE_PREFIX = "";

    /***
     * Gets the resource identifier for a given resource name.  This is a work around
     * for an issue which I do not understand where when running the application in
     * IntelliJ Idea the resources are resolving correctly, but in Eclipse for some
     * reason a slash("/") must be prepended in order to get it to resolve ocrrectly.
     * @param resourceName  The name of the resource.
     * @return Returns the ID for the resource based on the command line parameters.
     */
    public static String getResourceId(String resourceName) {
        return RESOURCE_PREFIX + resourceName;
    }

    /***
     * The "main" method for the Crobes application.  Used for starting up the
     * application.
     * @param args <p>Space delimited list of command line arguments for running
     *             the application.  The command line arguments are as follows:</p>
     *             <ul>
     *                  <li>
     *                      <p>radix: This integer value is used during world generation to determine how large the world will be.</p>
     *                  </li>
     *                  <li>
     *                      <p>mutationRate: This integer value represents how likely mutations are to happen; essentially the chance of a mutation occurring is functionally equivalent to 1 in m where m = mutationRate.</p>
     *                  </li>
     *                  <li>
     *                      <p>resourcePrefix: Optional prefix for telling Microscope where to find resources.  This is needed to add a leading forward slash("/") in Eclipse.</p>
     *                  </li>
     *             </ul>
     */
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
            Crobe.MUTATION_RATE = Integer.parseInt(list.get(1));
            if(list.size() > 2)
                RESOURCE_PREFIX = list.get(2);
        }//end if
    }

    @Override
    public void start(Stage primaryStage) {
        root = new VBox();

        HBox hbxTop = new HBox();
        initTopPane(hbxTop);
        VBox.setVgrow(hbxTop, Priority.ALWAYS);

        HBox hbxBottom = new HBox();
        initBottomPane(hbxBottom);
        VBox.setVgrow(hbxBottom, Priority.NEVER);

        root.getChildren().addAll(hbxTop, hbxBottom);

        ChangeListener<Number> stageResize = (observable, oldValue, newValue) -> {
            refreshLens();
        };
        primaryStage.widthProperty().addListener(stageResize);
        primaryStage.heightProperty().addListener(stageResize);

        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setTitle("Microbescope");
        primaryStage.setScene(scene);
        primaryStage.show();

        world.resetElements();
        refreshLens();
    }

    private void initTopPane(HBox pane) {
        //set the background to Tan
        BackgroundFill fill = new BackgroundFill(Color.TAN, CornerRadii.EMPTY, Insets.EMPTY);
        pane.setBackground(new Background(fill));

        //control panel
        VBox vbxControls = new VBox();
        HBox.setHgrow(vbxControls, Priority.NEVER);
        buildControls(vbxControls);

        //microscope lens
        BorderPane pnlScope = new BorderPane();
        HBox.setHgrow(pnlScope, Priority.ALWAYS);
        buildScope(pnlScope);

        pane.getChildren().addAll(vbxControls, pnlScope);
    }
    private void initBottomPane(HBox pane) {
        //set the background to Beige
        BackgroundFill fill = new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY);
        pane.setBackground(new Background(fill));

        //location report
        txtLocation = new TextArea();
        txtLocation.setMaxWidth(200);
        txtLocation.setEditable(false);
        txtLocation.setFont(Font.font("monospace", 14));
        HBox.setHgrow(txtLocation, Priority.NEVER);

        //crobe detail
        txtCrobeDetail = new TextArea();
        txtCrobeDetail.setEditable(false);
        txtCrobeDetail.setFont(Font.font("monospace", 14));
        HBox.setHgrow(txtCrobeDetail, Priority.ALWAYS);

        pane.getChildren().addAll(txtLocation, txtCrobeDetail);
    }

    private void buildControls(VBox pane) {
        //toolbar for simulation speed
        FlowPane pnlSpeedToolbar = new FlowPane();
        pnlSpeedToolbar.setMaxSize(200, 20);
        VBox.setVgrow(pnlSpeedToolbar, Priority.NEVER);
        buildSpeedBar(pnlSpeedToolbar);

        //controls for the environment
        VBox vbxEnvironment = new VBox();
        VBox.setVgrow(vbxEnvironment, Priority.ALWAYS);
        buildEnvironmentControls(vbxEnvironment);

        //pan and zoom controls
        GridPane pnlPanZoom = new GridPane();
        pnlPanZoom.setMaxSize(200, 200);
        VBox.setVgrow(pnlPanZoom, Priority.NEVER);
        buildPanZoomControls(pnlPanZoom);

        //controls for crobe interaction
        VBox vbxInteraction = new VBox();
        VBox.setVgrow(vbxInteraction, Priority.ALWAYS);
        buildInteractionControls(vbxInteraction);

        pane.getChildren().addAll(pnlSpeedToolbar, vbxEnvironment, pnlPanZoom, vbxInteraction);
    }
    private void buildScope(BorderPane pane) {
        //initialize the world
        world = new World(_radix);
        world.environment().setAmbience();
        lens = new Lens(world);
        lens.mode(Lens.Mode.LIGHT);

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

        pane.setCenter(lensPane);
    }
    private void buildSpeedBar(FlowPane pane) {
        ToggleGroup tglSpeed = new ToggleGroup();

        //pause button
        RadioButton btnPause = new RadioButton();
        setToggleButtonSkin(btnPause);
        setButtonIcon(btnPause, "pause.png");
        btnPause.setTooltip(new Tooltip("Pause simulation"));
        btnPause.setToggleGroup(tglSpeed);
        btnPause.setSelected(true);
        btnPause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stopTimer();
            }
        });

        //step button
        Button btnStep = new Button();
        setButtonIcon(btnStep, "step.png");
        btnStep.setTooltip(new Tooltip("Advance one day"));
        btnStep.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                advance();
            }
        });

        //slow button
        RadioButton btnSlow = new RadioButton();
        setToggleButtonSkin(btnSlow);
        setButtonIcon(btnSlow, "slow.png");
        btnSlow.setTooltip(new Tooltip("Slow speed"));
        btnSlow.setToggleGroup(tglSpeed);
        btnSlow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stopTimer();
                startTimer(DELAY_SLOW);
            }
        });

        //medium button
        RadioButton btnMedium = new RadioButton();
        setToggleButtonSkin(btnMedium);
        setButtonIcon(btnMedium, "medium.png");
        btnMedium.setTooltip(new Tooltip("Medium speed"));
        btnMedium.setToggleGroup(tglSpeed);
        btnMedium.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stopTimer();
                startTimer(DELAY_MEDIUM);
            }
        });

        //fast button
        RadioButton btnFast = new RadioButton();
        setToggleButtonSkin(btnFast);
        setButtonIcon(btnFast, "fast.png");
        btnFast.setTooltip(new Tooltip("Fast speed"));
        btnFast.setToggleGroup(tglSpeed);
        btnFast.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stopTimer();
                startTimer(DELAY_FAST);
            }
        });

        pane.getChildren().addAll(btnPause, btnStep, btnSlow, btnMedium, btnFast);
    }
    private void buildEnvironmentControls(VBox pane) {
        //environment mode
        Label lblEvironmentMode = new Label("Environment Mode");

        ChoiceBox<String> cmbMode = new ChoiceBox<>();
        for(Lens.Mode mode : Lens.Mode.values()) {
            cmbMode.getItems().add(mode.name());
        }//end for mode
        cmbMode.setMaxWidth(Double.MAX_VALUE);
        cmbMode.setValue(Lens.Mode.LIGHT.name());
        ChangeListener<String> cmbModeChange = (observable, oldValue, newValue) -> {
            if(!newValue.equalsIgnoreCase(oldValue)) {
                lens.mode(Lens.Mode.valueOf(newValue));
                refreshLens();
            }//end if
        };
        cmbMode.valueProperty().addListener(cmbModeChange);

        Button btnFlow = new Button("Add Flow");
        btnFlow.setMaxWidth(Double.MAX_VALUE);
        btnFlow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Location location = lens.selection();
                if (location != null) {
                    FlowEditor.FlowInfo info = FlowEditor.getFlowInfo((Stage) root.getScene().getWindow());

                    if (info != null) {
                        Flow flow = new Flow(world, info.rise, info.run, info.widthCoefficient, info.speed);
                        flow.location(location);
                        world.factors().add(flow);
                    }//end if
                }//end if
            }
        });

        pane.getChildren().addAll(lblEvironmentMode, cmbMode, btnFlow);
    }
    private void buildPanZoomControls(GridPane pane) {
        //pan and zoom buttons
        pane.getColumnConstraints().add(createColumn(100.0 / 3.0, true));
        pane.getColumnConstraints().add(createColumn(100.0 / 6.0, true));
        pane.getColumnConstraints().add(createColumn(100.0 / 6.0, true));
        pane.getColumnConstraints().add(createColumn(100.0 / 3.0, true));

        for(int i = 0; i < 3; i++) {
            pane.getRowConstraints().add(createRow(100.0 / 3.0, true));
        }//end for i

        Button btnPanUp = createButton("˄", true);
        pane.add(btnPanUp, 1, 0, 2, 1);
        Button btnPanLeft = createButton("<", true);
        pane.add(btnPanLeft, 0, 1, 1, 1);
        Button btnZoomIn = createButton("+", true);
        pane.add(btnZoomIn, 1, 1, 1, 1);
        Button btnZoomOut = createButton("-", true);
        pane.add(btnZoomOut, 2, 1, 1, 1);
        Button btnPanRight = createButton(">", true);
        pane.add(btnPanRight, 3, 1, 1, 1);
        Button btnPanDown = createButton("˅", true);
        pane.add(btnPanDown, 1, 2, 2, 1);

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
    }
    private void buildInteractionControls(VBox pane) {
        HBox hbxAdd = new HBox();

        TextField txtDesignation = new TextField();
        HBox.setHgrow(txtDesignation, Priority.ALWAYS);

        Button btnIncubate = new Button("Incubate");
        btnIncubate.setMinWidth(80);
        HBox.setHgrow(btnIncubate, Priority.NEVER);
        btnIncubate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(lens.selection() == null) {
                    //no location selected
                    return;
                }//end if

                if(lens.selection().crobe() != null) {
                    //selected location already has a crobe
                    return;
                }//end if

                Genome genome = Sequencer.sequenceGenome(new Genome(), true, (Stage) root.getScene().getWindow());
                if(genome != null) {
                    String designation = txtDesignation.getText();
                    txtDesignation.setText("");

                    Genomics.randomizeGenome(genome);
                    Crobe c = Genomics.incubateCrobe(genome, designation);
                    c.world(world);
                    world.crobes().add(c);
                    c.position(lens.selection().point());
                    txtCrobeDetail.setText(c.toString());
                    refreshLens();
                }//end if
            }
        });

        Button btnInspect = new Button("Inspect");
        btnInspect.setMaxWidth(Double.MAX_VALUE);
        btnInspect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(lens.selection() != null) {
                    Crobe crobe = lens.selection().crobe();
                    if(crobe != null) {
                        Sequencer.sequenceCrobe(crobe, false, (Stage) root.getScene().getWindow());
                    }//end if
                }//end if
            }
        });

        hbxAdd.getChildren().addAll(txtDesignation, btnIncubate);
        pane.getChildren().addAll(hbxAdd, btnInspect);
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
    private void setToggleButtonSkin(RadioButton rb) {
        rb.getStyleClass().remove("radio-button");
        rb.getStyleClass().add("toggle-button");
    }
    private void setButtonIcon(Labeled button, String iconResource) {
        Image img = new Image(getClass().getResourceAsStream(getResourceId(iconResource)));
        button.setGraphic(new ImageView(img));
    }


    private double calcWidthIncrement() {
        return lens.fontSize() * FONT_WIDTH_COEFFICIENT;
    }
    private int calcHeightIncrement() {
        return (int)(Math.round(lens.fontSize() * FONT_HEIGHT_COEFFICIENT));
    }

    private void setSelectedLocation(Location location) {
        String locText = "";
        String crobeText = "";
        if(location != null) {
            locText = location.toString();

            if(location.crobe() != null) {
                crobeText = location.crobe().toString();
            }//end if
        }//end if

        txtLocation.setText(locText);
        txtCrobeDetail.setText(crobeText);
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

        setSelectedLocation(lens.selection());
    }

    private void advance() {
        //advances one day in the world
        //tell all crobes to live for a day
        //and process reproduction
        world.crobes().live();
        world.crobes().reproduce();

        //remove any dead crobes from the colony
        world.crobes().purge();

        //reset the elements in all Locations
        world.resetElements();

        //generate any random drift
        world.brownianMotion();

        //update all environmental factors
        world.updateFactors();

        //process any drift accumulated
        world.processDrift();

        //rerender everything
        refreshLens();
    }
    private void stopTimer() {
        if(tmrClock != null) {
            tmrClock.purge();
            tmrClock.cancel();
            tmrClock = null;
        }//end if
    }
    private void startTimer(long delay) {
        tmrClock = new Timer(true);
        tmrClock.schedule(new AdvanceTask(), delay, delay);
    }

    private class AdvanceTask extends TimerTask
    {
        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    advance();
                }
            });
        }
    }
}
