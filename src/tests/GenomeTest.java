package tests;

import crobes.genetics.gui.Sequencer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class GenomeTest extends Application
{
    public static void main(String[] args) {
        launch(args);
    }

    private BorderPane root;
    private Button btnGo;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new BorderPane();
        FlowPane pane = new FlowPane();
        root.setCenter(pane);

        btnGo = new Button("GO");
        pane.getChildren().add(btnGo);

        btnGo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Sequencer.createCrobe(primaryStage);
            }
        });

        Scene scene = new Scene(root, 200, 200);
        primaryStage.setTitle("Genome Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
