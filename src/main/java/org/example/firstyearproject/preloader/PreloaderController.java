package org.example.firstyearproject.preloader;

import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.firstyearproject.App;

import java.io.File;

public class PreloaderController {

    public PreloaderController(PreloaderView preloaderView, Stage primaryStage)
    {
        openButtonClicked(preloaderView);
        fileButtonClicked(preloaderView, primaryStage);
        defaultFileClicked(preloaderView);
        customFileClicked(preloaderView);
    }
    void openButtonClicked(PreloaderView preloaderView){
        App app = new App();
        preloaderView.open.setOnAction(event->
        {
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        System.out.println("start loading");
                        app.loadMap(preloaderView.url);
                        System.out.println("Done loading");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                }
            };

            task.setOnSucceeded(eventSuccess -> {
                try {
                    app.startMap(preloaderView.primaryStage, preloaderView.url);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            Thread thread = new Thread(task);
            thread.start();

            ProgressView view = new ProgressView(preloaderView.primaryStage);
        });
    }

    void fileButtonClicked(PreloaderView preloaderView, Stage primaryStage){
        preloaderView.fileButton.setOnAction(e->
        {
            FileChooser fileWindow = new FileChooser();
            fileWindow.setInitialDirectory(new File("./data"));
            File newFile = fileWindow.showOpenDialog(primaryStage);
            preloaderView.fileBox.setText(newFile.getAbsolutePath());
            preloaderView.url = newFile.getAbsolutePath();
        });
    }

    void defaultFileClicked(PreloaderView preloaderView){
        preloaderView.defaultFile.setOnAction(e ->{
            preloaderView.url = "data/bornholm.osm";
            preloaderView.fileBox.setText(preloaderView.url);
        });
    }

    void customFileClicked(PreloaderView preloaderView){
        preloaderView.customFile.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            preloaderView.fileButton.setDisable(!newValue);
            preloaderView.fileBox.setDisable(!newValue);
        });
    }
}
