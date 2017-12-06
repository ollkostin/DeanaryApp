package ru.kostin.rsubd.deanary.service;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kostin.rsubd.deanary.ui.*;

@Service
public class ViewService {
    public final static int WIDTH = 800, HEIGHT = 600;
    private static Stage primaryStage;
    private Scene previousScene;

    private MenuComponent menuComponent;
    private LecturerComponent lecturerComponent;
    private StudentGroupComponent studentGroupComponent;
    private DisciplineComponent disciplineComponent;
    private ControlComponent controlComponent;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        ViewService.primaryStage = primaryStage;
    }

    public void showPreviousScene(ActionEvent event) {
        primaryStage.setScene(previousScene);
        previousScene = null;
    }

    public void showMenu(ActionEvent event) {
        previousScene = primaryStage.getScene();
        primaryStage.setScene(menuComponent.getScene());
    }

    public void showLecturerList(ActionEvent event) {
        previousScene = primaryStage.getScene();
        lecturerComponent.fillTable();
        primaryStage.setScene(lecturerComponent.getScene());
    }

    public void showStudentGroupList(ActionEvent event) {
        previousScene = primaryStage.getScene();
        studentGroupComponent.fillTable();
        primaryStage.setScene(studentGroupComponent.getScene());
    }

    public void showDisciplineList(ActionEvent event) {
        previousScene = primaryStage.getScene();
        disciplineComponent.fillTable();
        primaryStage.setScene(disciplineComponent.getScene());
    }

    public void showControlList(ActionEvent event) {
        previousScene = primaryStage.getScene();
        controlComponent.fillTable();
        primaryStage.setScene(controlComponent.getControlListScene());
    }

    public void showError(String error) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка");
        Label label = new Label("Ошибка:");
        TextArea textArea = new TextArea(error);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane expContent = new GridPane();
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    public void showPaneDialog(String title, VBox pane) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(title);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.CLOSE);
        alert.getDialogPane().setContent(pane);
        alert.showAndWait();
    }

    @Autowired
    public void setLecturerComponent(LecturerComponent lecturerComponent) {
        this.lecturerComponent = lecturerComponent;
    }

    @Autowired
    public void setStudentGroupComponent(StudentGroupComponent studentGroupComponent) {
        this.studentGroupComponent = studentGroupComponent;
    }

    @Autowired
    public void setDisciplineComponent(DisciplineComponent disciplineComponent) {
        this.disciplineComponent = disciplineComponent;
    }

    @Autowired
    public void setControlComponent(ControlComponent controlComponent) {
        this.controlComponent = controlComponent;
    }

    @Autowired
    public void setMenuComponent(MenuComponent menuComponent) {
        this.menuComponent = menuComponent;
    }
}
