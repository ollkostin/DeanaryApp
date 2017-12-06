package ru.kostin.rsubd.deanary;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@SuppressWarnings("restriction")
public class AppPreloader extends Preloader {
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Scene scene = new Scene(new ProgressIndicator(-1), 100, 100);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification pn) {
        if (pn instanceof StateChangeNotification) {
            stage.hide();
        }
    }
}

