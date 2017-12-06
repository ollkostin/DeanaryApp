package ru.kostin.rsubd.deanary;

import javafx.application.Preloader;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.kostin.rsubd.deanary.service.ViewService;

@SpringBootApplication
@SuppressWarnings("restriction")
public class App extends AbstractJavaFxApplicationSupport {
    @Value("${app.ui.title:Example App}")
    private String windowTitle;

    @Autowired
    private ViewService viewService;

    @Override
    public void start(Stage stage) throws Exception {
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));
        viewService.setPrimaryStage(stage);
        stage.setTitle(windowTitle);
        viewService.showMenu(null);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launchApp(App.class, args);
    }

}