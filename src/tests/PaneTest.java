package tests;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class PaneTest extends Application
{
    private static final String HTML_TXT = "<span style='font-family: courier; font-size: 32; color:red'>_________1_________2_________3_________4_________5_________6</span><br><span style='font-family: courier; font-size: 32; color:red'>123456789012345678901234567890123456789012345678901234567890</span>";
    private static final String HTML_FMT_TEST = "<span style='font-family: %1$s; font-size: %2$d'><span style='background-color: %3%s; color: %4$s'>%5$s</span>";
    private static final String FONT_FXSYS = "monospace";

    private static final String SPAN_FONT = "<span style='font-family: %1$s; font-size: %2$d'>";
    private static final String SPAN_STYLE = "<span style='color: %1$s; background-color: %2$s'>";
    private static final String SPAN_END = "</span>";
    private static final String LINE_BREAK = "<br>";
    private static final String B_COLOR = "white";
    private static final String B_COLOR_S = "black";
    private static final String F_COLOR = "red";
    private static final String F_COLOR_S = "yellow";
    private Point selection = new Point(-1, -1);

    private BorderPane root;
    private WebView pane;
    private TextField txtPaneWidth;
    private Button btnPaneWidth;
    private TextField txtPaneHeight;
    private Button btnPaneHeight;
    private TextField txtScreenWidth;
    private Button btnScreenWidth;
    private TextField txtScreenHeight;
    private Button btnScreenHeight;
    private TextField txtFontSize;
    private Button btnFontSize;
    private TextField txtFormWidth;
    private TextField txtFormHeight;
    private TextField txtPixelPoint;
    private TextField txtCellPoint;

    private int font_size = 32;
    private String font_color = "red";
    private long pane_width;
    private long pane_height;
    private long form_width;
    private long form_height;
    private long screen_width = 32;
    private long screen_height = 14;

    private long width_constant = 27;
    private long height_constant = 63;
    private double fontWidthCoefficient = 0.6;
    private double fontHeightCoefficient = 1.13;

    private Timer timer;

    public static void main(String[] args) {
        launch(args);
    }

    private void update() {
        if(pane_width == 0)
            pane_width = Math.round(pane.getWidth());
        txtPaneWidth.setText("" + pane_width);
        if(pane_height == 0)
            pane_height = Math.round(pane.getHeight());
        txtPaneHeight.setText("" + pane_height);
        txtScreenWidth.setText("" + screen_width);
        txtScreenHeight.setText("" + screen_height);
        txtFontSize.setText("" + font_size);
        if(form_width == 0)
          form_width = Math.round(root.getWidth());
        txtFormWidth.setText("" + form_width);
        if(form_height == 0)
            form_height = Math.round(root.getHeight());
        txtFormHeight.setText("" + form_height);
        //pane.getEngine().loadContent(String.format(HTML_FMT_TEST, FONT_FXSYS, font_size, font_color, font_color, generateText()));
        String cont = generateSelectableText();
        pane.getEngine().loadContent(cont);
    }

    private double widthIncrement() {
        return (font_size * fontWidthCoefficient);
    }
    private long heightIncrement() {
        return Math.round(font_size * fontHeightCoefficient);
    }

    private void screenToPane() {
        //use the screen size to calculate the pane size
        pane_width = width_constant + Math.round(screen_width * widthIncrement());
        pane_height = height_constant + (screen_height * heightIncrement());
    }
    private void paneToScreen() {
        //use the pane size to calculate the screen size
        screen_width = Math.round((pane_width - width_constant) / widthIncrement());
        screen_height = (pane_height - height_constant) / heightIncrement();
    }

    private String generateWidthText() {
        StringBuilder sb = new StringBuilder();

        for(int i = 1; i <= screen_width; i++) {
            sb.append(i % 10);
        }//end for i

        return sb.toString();
    }
    private String generateWidthMeter() {
        StringBuilder sb = new StringBuilder();

        for(int i = 1; i <= screen_width; i++) {
            if((i % 10) == 0)
                sb.append(i / 10);
            else
                sb.append("_");
        }//end for i

        sb.append("<br>");
        sb.append(generateWidthText());

        return sb.toString();
    }
    private String generateHeightText(int height) {
        if(height < 10)
            return "0" + height;
        else
            return "" + height;
    }
    private String generateText() {
        StringBuilder sb = new StringBuilder();

        if(screen_height == 0) {
            //do not generate any text
        }//end if
        else if(screen_height == 1) {
            sb.append(generateWidthText());
        }//end else if
        else if(screen_height == 2) {
            sb.append(generateWidthMeter());
        }//end else if
        else {
            sb.append(generateWidthMeter());
            sb.append("<br>");

            for(int i = 2; i < screen_height; i++) {
                sb.append(generateHeightText(i + 1));
                sb.append("<br>");
            }//end for i
        }//end else

        return sb.toString();
    }

    private String getCell(int x, int y) {
        String result = "&nbsp;";

        if((y % 10) == 0) {
            if((x % 10) == 0)
                result = "O";
            else if((x % 2) == 0)
                result = "" + (y / 10);
        }//end if
        else if((x % 10) == (y % 10)) {
            result = "" + (x % 10);
        }//end else if
        else if(((x % 10) == 0) && ((y % 2) == 0)) {
            result = "" + (x / 10);
        }//end else if
        else if((x % 2) == (y % 2)) {
            result = "·";
        }//end else if

        return result;
    }
    private String generateSelectableText() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format(SPAN_FONT, FONT_FXSYS, font_size));

        for(int i = 0; i < screen_height; i++) {
            int Y = i + 1;
            sb.append(String.format(SPAN_STYLE, F_COLOR, B_COLOR));
            for(int j = 0; j < screen_width; j++) {
                int X = j + 1;

                //first check if this is selected
                if((selection.x == X) && (selection.y == Y)) {
                    sb.append(SPAN_END);
                    sb.append(String.format(SPAN_STYLE, F_COLOR_S, B_COLOR_S));

                    sb.append(getCell(X, Y));

                    if(X != (screen_width - 1)) {
                        sb.append(SPAN_END);
                        sb.append(String.format(SPAN_STYLE, F_COLOR, B_COLOR));
                    }//end if
                }//end if
                else {
                    sb.append(getCell(X, Y));
                }//end else
            }//end for j
            sb.append(SPAN_END);
            sb.append(LINE_BREAK);
        }//end for i

        sb.append(SPAN_END);

        return sb.toString();
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new BorderPane();
        FlowPane leftPane = new FlowPane();

        VBox vbLbl = new VBox();
        vbLbl.setSpacing(10);
        vbLbl.getChildren().addAll(new Label("Pane Width"), new Label("Pane Height"), new Label("Screen Width"), new Label("Screen Height"), new Label("Font Size"), new Label("Form Width"), new Label("Form Height"), new Label("Pixel"), new Label("Cell"));

        VBox vbTxt = new VBox();
        txtPaneWidth = new TextField();
        txtPaneHeight = new TextField();
        txtScreenWidth = new TextField();
        txtScreenHeight = new TextField();
        txtFontSize = new TextField();
        txtFormWidth = new TextField();
        txtFormHeight = new TextField();
        txtPixelPoint = new TextField();
        txtCellPoint = new TextField();
        vbTxt.getChildren().addAll(txtPaneWidth, txtPaneHeight, txtScreenWidth, txtScreenHeight, txtFontSize, txtFormWidth, txtFormHeight, txtPixelPoint, txtCellPoint);

        VBox vbBtn = new VBox();
        btnPaneWidth = new Button("Set");
        btnPaneHeight = new Button("Set");
        btnScreenWidth = new Button("Set");
        btnScreenHeight = new Button("Set");
        btnFontSize = new Button("Set");
        vbBtn.getChildren().addAll(btnPaneWidth, btnPaneHeight, btnScreenWidth, btnScreenHeight, btnFontSize);

        leftPane.getChildren().addAll(vbLbl, vbTxt, vbBtn);

        pane = new WebView();

        root.setLeft(leftPane);
        root.setCenter(pane);

        update();

        ChangeListener<Number> stageResize = (observable, oldValue, newValue) -> {
            if(observable == primaryStage.widthProperty()) {
                form_width = Math.round((Double)newValue);
                pane_width = form_width - 400;
            }//end if
            update();
        };//end resize listener
        primaryStage.widthProperty().addListener(stageResize);
        primaryStage.heightProperty().addListener(stageResize);

        btnPaneWidth.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double width = Double.parseDouble(txtPaneWidth.getText());
                pane_width = Math.round(width);
                width += 400;
                primaryStage.setWidth(width);
                form_width = Math.round(width);
                paneToScreen();
                update();
            }
        });
        btnPaneHeight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double height = Double.parseDouble(txtPaneHeight.getText());
                pane_height = Math.round(height);
                primaryStage.setHeight(height);
                form_height = pane_height;
                paneToScreen();
                update();
            }
        });
        btnScreenWidth.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double width = Double.parseDouble(txtScreenWidth.getText());
                screen_width = Math.round(width);
                screenToPane();
                form_width = pane_width + 400;
                primaryStage.setWidth(form_width);
                update();
            }
        });
        btnScreenHeight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double height = Double.parseDouble(txtScreenHeight.getText());
                screen_height = Math.round(height);
                screenToPane();
                form_height = pane_height;
                primaryStage.setHeight(form_height);
                update();
            }
        });
        btnFontSize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                font_size = Integer.parseInt(txtFontSize.getText());
                update();
            }
        });

        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x, y;
                x = event.getX();
                y = event.getY();

                int wBase = (int)Math.round(Math.floor(font_size / 4 * 2.5)) + 7;
                int hBase = (int)Math.round(font_size / 4 * 1.5) * 3 + 7;

                int xCell = (int)Math.round(Math.floor((x - wBase) / widthIncrement())) + 2;
                int yCell = (int)Math.round(Math.floor((y - hBase) / heightIncrement())) + 2;

                txtPixelPoint.setText(String.format("( %1$.2f , %2$.2f )", x , y));
                txtCellPoint.setText(String.format("( %1$d , %2$d )", xCell, yCell));

                if((selection.x == xCell) && (selection.y == yCell)) {
                    selection.x = -1;
                    selection.y = -1;
                }//end if
                else {
                    selection.x = xCell;
                    selection.y = yCell;
                }//end else

                update();
            }
        });
        pane.getEngine().getLoadWorker().stateProperty().addListener(
                (observable, oldState, newState) -> {
                    if(newState != Worker.State.SUCCEEDED) {
                        return;
                    }//end if

                    Document doc = pane.getEngine().getDocument();
                    Element body = (Element) doc.getElementsByTagName("body").item(0);
                    String style = body.getAttribute("style");
                    body.setAttribute("style", "cursor: crosshair;" + style);
                });

        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null,"Timer");
            }
        }, 10000, 10000);


        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("HTML Testing");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
