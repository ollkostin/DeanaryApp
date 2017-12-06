package ru.kostin.rsubd.deanary.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kostin.rsubd.deanary.service.ViewService;

@Component
public class MenuComponent {
    private Scene scene;
    private ViewService viewService;

    @Autowired
    public MenuComponent(ViewService viewService) {
        this.viewService = viewService;
        final VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);
        vBox.setPrefWidth(ViewService.WIDTH);
        vBox.setPrefHeight(ViewService.HEIGHT);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        scene = new Scene(vBox);
        Text title = new Text("Меню");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        final Button showLecturerListButton = new Button("Лекторы");
        showLecturerListButton.setOnAction(viewService::showLecturerList);
        final Button showStudentGroupListButton = new Button("Группы");
        showStudentGroupListButton.setOnAction(viewService::showStudentGroupList);
        final Button showDisciplineListButton = new Button("Дисциплины");
        showDisciplineListButton.setOnAction(viewService::showDisciplineList);
        final Button showControlListButton = new Button("Контроль");
        showControlListButton.setOnAction(viewService::showControlList);
        vBox.getChildren().addAll(title, showLecturerListButton, showStudentGroupListButton, showDisciplineListButton, showControlListButton);
    }


    public Scene getScene() {
        return scene;
    }
}
