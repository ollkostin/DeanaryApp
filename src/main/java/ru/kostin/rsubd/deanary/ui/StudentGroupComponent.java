package ru.kostin.rsubd.deanary.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import ru.kostin.rsubd.deanary.service.StudentGroupService;
import ru.kostin.rsubd.deanary.service.ViewService;
import ru.kostin.rsubd.deanary.service.dto.DisciplinePercentageDTO;
import ru.kostin.rsubd.deanary.service.dto.StudentGroupDTO;

import java.util.Optional;

@Component
public class StudentGroupComponent {
    private static final String WRONG_FIELDS = "Название группы должно быть от 1 до 10 символов и соответствовать паттерну  - " +
            "Латинская буква и число (1-4 цифры).\n" +
            "Значение курса от 1 до 6.\nКоличество студентов - от 0 до 35\n" +
            "Сочетание (название,курс) должно быть уникальным";
    private ViewService viewService;
    private StudentGroupService studentGroupService;
    private Scene scene;
    private TableView<StudentGroupDTO> table;

    @Autowired
    public StudentGroupComponent(ViewService viewService,
                                 StudentGroupService studentGroupService) {
        this.viewService = viewService;
        this.studentGroupService = studentGroupService;
        VBox vBox = new VBox();
        AnchorPane anchorPane = new AnchorPane();
        HBox hBox = new HBox();
        vBox.setPrefWidth(ViewService.WIDTH);
        vBox.setPrefHeight(ViewService.HEIGHT);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        hBox.setAlignment(Pos.CENTER);
        scene = new Scene(vBox);
        Button goToPreviousSceneButton = new Button("Назад");
        Button addButton = new Button("Добавить");
        Button changeButton = new Button("Изменить");
        Button deleteButton = new Button("Удалить");
        ButtonBar bar = new ButtonBar();
        bar.getButtons().addAll(goToPreviousSceneButton, addButton, changeButton, deleteButton);
        buildTable();
        anchorPane.getChildren().add(table);
        hBox.getChildren().addAll(bar);
        vBox.getChildren().addAll(anchorPane, hBox);
        goToPreviousSceneButton.setOnAction(viewService::showPreviousScene);
        addButton.setOnAction(event -> viewService.showPaneDialog("Добавить", buildGroupVBox(null)));
        changeButton.setOnAction(event -> {
            StudentGroupDTO dto = table.getSelectionModel().getSelectedItem();
            if (dto == null) {
                viewService.showError("Не выбран элемент");
            } else {
                viewService.showPaneDialog("Изменить", buildGroupVBox(dto));
            }
        });
        deleteButton.setOnAction(e -> {
            StudentGroupDTO dto = table.getSelectionModel().getSelectedItem();
            if (dto == null) {
                viewService.showError("Не выбран элемент");
            } else {
                studentGroupService.delete(dto.getId());
                table.getItems().remove(dto);
            }
        });
    }

    public void fillTable() {
        ObservableList<StudentGroupDTO> groups = FXCollections.observableList(studentGroupService.fetch());
        table.setItems(groups);
    }

    private void buildTable() {
        table = new TableView<>();
        table.setPrefWidth(ViewService.WIDTH);
        table.setPrefHeight(ViewService.HEIGHT);
        TableColumn<StudentGroupDTO, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        TableColumn<StudentGroupDTO, Integer> courseColumn = new TableColumn<>("Курс");
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        courseColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        TableColumn<StudentGroupDTO, Integer> studAmountColumn = new TableColumn<>("Кол-во студентов");
        studAmountColumn.setCellValueFactory(new PropertyValueFactory<>("studAmount"));
        studAmountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        TableColumn<StudentGroupDTO, Integer> hoursAmountColumn = new TableColumn<>("Кол-во часов");
        hoursAmountColumn.setCellValueFactory(new PropertyValueFactory<>("hoursAmount"));
        TableColumn sessionLengthColumn = new TableColumn("Длительность сессии");
        TableColumn<StudentGroupDTO, Integer> winterSessionLengthColumn = new TableColumn<>("Зимняя");
        TableColumn<StudentGroupDTO, Integer> summerSessionLengthColumn = new TableColumn<>("Летняя");
        winterSessionLengthColumn.setCellValueFactory(new PropertyValueFactory<>("winterSessionLength"));
        summerSessionLengthColumn.setCellValueFactory(new PropertyValueFactory<>("summerSessionLength"));
        sessionLengthColumn.getColumns()
                .addAll(winterSessionLengthColumn, summerSessionLengthColumn);
        TableColumn disciplinePercentageColumn = new TableColumn<>();
        disciplinePercentageColumn.setCellFactory(showDisciplinePercentageColumnCellFactory());
        table.getColumns().addAll(nameColumn, courseColumn, studAmountColumn, hoursAmountColumn, sessionLengthColumn, disciplinePercentageColumn);
    }

    private VBox buildGroupVBox(StudentGroupDTO group) {
        VBox groupVBox = new VBox();
        TextField nameField = group == null ? new TextField() : new TextField(group.getName());
        nameField.setPromptText("Название");
        TextField courseField = group == null ? new TextField() : new TextField(group.getCourse().toString());
        courseField.setPromptText("Курс");
        TextField studAmountField = group == null ? new TextField() : new TextField(group.getStudAmount().toString());
        studAmountField.setPromptText("Количество студентов");
        Button saveButton = new Button("Сохранить");
        groupVBox.getChildren().addAll(nameField, courseField, studAmountField, saveButton);
        saveButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                Integer course = Integer.parseUnsignedInt(courseField.getText());
                Integer studAmount = Integer.parseUnsignedInt(studAmountField.getText());
                if (!validateName(name)
                        || course < 0 || course > 6
                        || studAmount < 0 || studAmount > 35) {
                    viewService.showError(WRONG_FIELDS);
                } else {
                    StudentGroupDTO dto = Optional.ofNullable(group).orElse(new StudentGroupDTO());
                    dto.setName(name);
                    dto.setCourse(course);
                    dto.setStudAmount(studAmount);
                    dto.setId(studentGroupService.save(dto).getId());
                    if (group == null) {
                        table.getItems().add(dto);
                    }
                    table.refresh();
                }
            } catch (NumberFormatException | DataIntegrityViolationException ex) {
                viewService.showError(WRONG_FIELDS);
            } finally {
                nameField.setText("");
                courseField.setText("");
                studAmountField.setText("");
            }
        });
        return groupVBox;
    }

    public Scene getScene() {
        return scene;
    }

    private Callback<TableColumn<StudentGroupDTO, Void>, TableCell<StudentGroupDTO, Void>> showDisciplinePercentageColumnCellFactory() {
        return new Callback<TableColumn<StudentGroupDTO, Void>, TableCell<StudentGroupDTO, Void>>() {
            @Override
            public TableCell<StudentGroupDTO, Void> call(final TableColumn<StudentGroupDTO, Void> param) {
                return new TableCell<StudentGroupDTO, Void>() {
                    private String title = "Процент дисциплин";
                    private final Button btn = new Button(title);

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            StudentGroupDTO group = getTableView().getItems().get(getIndex());
                            VBox vBox = new VBox();
                            TableView<DisciplinePercentageDTO> disciplineTable = buildDisciplinePercentageTable();
                            disciplineTable.setItems(
                                    FXCollections.observableList(studentGroupService.getGroupDisciplinePercentage(group.getId()))
                            );
                            Stage secondStage = new Stage();
                            secondStage.setTitle(title);
                            vBox.getChildren().add(disciplineTable);
                            Scene membersScene = new Scene(vBox);
                            secondStage.setScene(membersScene);
                            secondStage.show();
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
    }

    private TableView<DisciplinePercentageDTO> buildDisciplinePercentageTable() {
        TableView<DisciplinePercentageDTO> tableView = new TableView<>();
        TableColumn<DisciplinePercentageDTO, String> nameColumn = new TableColumn<>("Дисциплина");
        TableColumn<DisciplinePercentageDTO, Double> percentColumn = new TableColumn<>("Процент");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        percentColumn.setCellValueFactory(new PropertyValueFactory<>("percent"));
        tableView.getColumns().addAll(nameColumn, percentColumn);
        return tableView;
    }

    private boolean validateName(String name) {
        String pattern = "[a-zA-Z][0-9]{1,4}";
        return name.length() < 10 && name.length() > 2 && name.matches(pattern);
    }
}
