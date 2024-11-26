package org.example.firstyearproject.preloader;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ProgressView {

    public Stage primaryStage;
    public ProgressView(Stage stage)
    {
        primaryStage = stage;
        primaryStage.setTitle("Preloader");

        VBox pane = new VBox();
        Text title = new Text("Loading Map...");
        title.setFont(Font.font(50));
        Text wait = new Text("Please Wait");
        wait.setFont(Font.font(50));
        pane.setPadding(new Insets(50, 0, 0, 100));
        pane.getChildren().addAll(title, wait);

        primaryStage.setScene(new Scene(pane, 800, 400));
        primaryStage.show();
    }
}
