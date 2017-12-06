package ru.kostin.rsubd.deanary.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kostin.rsubd.deanary.service.LecturerService;
import ru.kostin.rsubd.deanary.service.ViewService;
import ru.kostin.rsubd.deanary.service.dto.LecturerDTO;

@Component
public class LecturerComponent {
    private ViewService viewService;
    private LecturerService lecturerService;
    private Scene scene;
    private TableView<LecturerDTO> table;

    @Autowired
    public LecturerComponent(ViewService viewService, LecturerService lecturerService) {
        this.viewService = viewService;
        this.lecturerService = lecturerService;
        final Button goToPreviousSceneButton = new Button("Назад");
        final Button addButton = new Button("Добавить");
        final Button deleteButton = new Button("Удалить");
        final Button getLecturersWithMaxStudentCountButton = new Button("Преподаватели, принимающие экзамены у наибольшего числа студентов");
        final VBox vBox = new VBox();
        final AnchorPane anchorPane = new AnchorPane();
        final HBox hBox = new HBox();
        vBox.setPrefWidth(ViewService.WIDTH);
        vBox.setPrefHeight(ViewService.HEIGHT);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        hBox.setAlignment(Pos.CENTER);
        scene = new Scene(vBox);
        buildTable();
        final ButtonBar bar = new ButtonBar();
        bar.getButtons().addAll(goToPreviousSceneButton, addButton, deleteButton, getLecturersWithMaxStudentCountButton);
        anchorPane.getChildren().addAll(table);
        hBox.getChildren().addAll(bar);
        vBox.getChildren().addAll(anchorPane, hBox);
        goToPreviousSceneButton.setOnAction(viewService::showPreviousScene);
        addButton.setOnAction(e -> {
            LecturerDTO lecturerDTO = new LecturerDTO();
            lecturerDTO.setFullName("имя лектора");
            table.getItems().add(lecturerDTO);
        });
        deleteButton.setOnAction(e -> {
            LecturerDTO lecturerDTO = table.getSelectionModel().getSelectedItem();
            lecturerService.delete(lecturerDTO.getId());
            table.getItems().remove(lecturerDTO);
        });
        getLecturersWithMaxStudentCountButton.setOnAction(e -> {
            VBox max = new VBox();
            TableView<LecturerDTO> tableView = new TableView<>();
            TableColumn<LecturerDTO, String> fullNameColumn = new TableColumn<>("Полное имя");
            fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            fullNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            tableView.getColumns().add(fullNameColumn);
            tableView.setItems(
                    FXCollections.observableList(lecturerService.getLecturersWithMaxStudentCount())
            );
            Stage secondStage = new Stage();
            secondStage.setTitle("Преподаватели, принимающие экзамены у наибольшего числа студентов");
            max.getChildren().add(tableView);
            Scene membersScene = new Scene(max);
            secondStage.setScene(membersScene);
            secondStage.show();
        });
    }

    private void buildTable() {
        table = new TableView<>();
        table.setEditable(true);
        table.setPrefWidth(ViewService.WIDTH );
        table.setPrefHeight(ViewService.HEIGHT);
        TableColumn<LecturerDTO, String> fullNameColumn = new TableColumn<>("Полное имя");
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        fullNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        table.getColumns().addAll(fullNameColumn);
        fullNameColumn.setOnEditCommit(event -> {
            LecturerDTO lecturerDTO = (LecturerDTO) ((TableColumn.CellEditEvent) event).getRowValue();
            String newName = (String) ((TableColumn.CellEditEvent) event).getNewValue();
            if (newName.length() > 255) {
                viewService.showError("Слишком длинное имя!");
            } else {
                lecturerDTO.setFullName(newName);
                lecturerDTO.setId(lecturerService.save(lecturerDTO));
            }
            table.refresh();
        });
    }

    public void fillTable() {
        ObservableList<LecturerDTO> people = FXCollections.observableArrayList(lecturerService.fetch());
        table.setItems(people);
    }

    public Scene getScene() {
        return scene;
    }
}
