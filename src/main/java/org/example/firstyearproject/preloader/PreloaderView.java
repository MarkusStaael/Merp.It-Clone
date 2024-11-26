package org.example.firstyearproject.preloader;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PreloaderView {
    Stage primaryStage;
    Scene preloaderScene;
    Button open, fileButton;
    RadioButton defaultFile, customFile;
    TextField fileBox;
    String url;

    public PreloaderView(Stage stage) {
        this.url = "data/bornholm.osm.obj";
        this.defaultFile = new RadioButton("Use default file for map info");
        this.customFile = new RadioButton("Use custom file for map info");
        this.fileButton = new Button("Choose file");
        this.fileBox = new TextField(url);
        this.open = new Button("Open the Map");
        this.primaryStage = stage;

        setupUI();
    }

    private Text createDescriptionText(){
        Text about = new Text();
        about.setText("Welcome to Mapping.it. This is a program that can view, and search through, a map. You can choose which" +
                " map to display by clicking the 'use custom file for map info', or directly load the default map of Denmark, by simply" +
                " clicking the 'Open Map'.");
        about.setFont(Font.font("SansSerif", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        about.setWrappingWidth(600);
        return about;
    }

    private Text createTitle(){
        Text title = new Text("Mapping.it");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 35));
        return title;
    }

    private void setupUI(){
        primaryStage.setTitle("Preloader");

        Text title = createTitle();
        Text about = createDescriptionText();

        defaultFile.setSelected(true);
        ToggleGroup fileToggleGroup = new ToggleGroup();
        defaultFile.setToggleGroup(fileToggleGroup);
        customFile.setToggleGroup(fileToggleGroup);

        fileButton.setDisable(true);
        fileBox.setEditable(false);
        fileBox.setDisable(true);
        fileBox.setMouseTransparent(false);
        fileBox.setMaxWidth(100);

        VBox pane = new VBox();
        HBox filePane = new HBox();
        HBox openBox = new HBox();

        Region spacingRegion = new Region();
        Region radiospacing = new Region();
        Region customspacing = new Region();
        Region openRegion = new Region();

        spacingRegion.setPrefHeight(80);
        radiospacing.setPrefHeight(20);
        customspacing.setPrefHeight(10);
        openRegion.setPrefWidth(500);

        filePane.getChildren().addAll(fileBox, fileButton);
        HBox.setMargin(fileBox, new Insets(10, 2, 10, 10));
        HBox.setMargin(fileButton, new Insets(10, 10, 10, 2));

        openBox.getChildren().addAll(openRegion, open);

        pane.setPadding(new Insets(50, 0, 0, 100));
        pane.getChildren().addAll(title, about, spacingRegion, defaultFile, radiospacing, customFile, customspacing, filePane, openBox);

        preloaderScene = new Scene(pane, 800, 400);
        primaryStage.setScene(preloaderScene);
        primaryStage.show();
    }
}
