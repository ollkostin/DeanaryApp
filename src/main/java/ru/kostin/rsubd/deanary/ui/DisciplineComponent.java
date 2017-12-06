package ru.kostin.rsubd.deanary.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import ru.kostin.rsubd.deanary.service.DisciplineService;
import ru.kostin.rsubd.deanary.service.ViewService;
import ru.kostin.rsubd.deanary.service.dto.DisciplineDTO;

import java.util.Optional;


@Component
public class DisciplineComponent {
    private static final String WRONG_FIELDS = "Название от 0 до 255 символов\nКоличество часов от 0 до 200\nНазвание уникально в категории";
    private ViewService viewService;
    private DisciplineService disciplineService;
    private ObservableList<String> categories;
    private Scene scene;
    private TableView<DisciplineDTO> table;

    @Autowired
    public DisciplineComponent(ViewService viewService, DisciplineService disciplineService) {
        this.viewService = viewService;
        this.disciplineService = disciplineService;
        categories = FXCollections.observableList(disciplineService.getCategories());
        final VBox vBox = new VBox();
        final AnchorPane anchorPane = new AnchorPane();
        final HBox hBox = new HBox();
        vBox.setPrefWidth(ViewService.WIDTH);
        vBox.setPrefHeight(ViewService.HEIGHT);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        hBox.setAlignment(Pos.CENTER);
        scene = new Scene(vBox);
        final Button goToPreviousSceneButton = new Button("Назад");
        final Button addButton = new Button("Добавить");
        final Button changeButton = new Button("Изменить");
        final Button deleteButton = new Button("Удалить");
        buildTable();
        ButtonBar bar = new ButtonBar();
        bar.getButtons().addAll(goToPreviousSceneButton, addButton, changeButton, deleteButton);
        anchorPane.getChildren().addAll(table);
        hBox.getChildren().addAll(bar);
        vBox.getChildren().addAll(anchorPane, hBox);
        goToPreviousSceneButton.setOnAction(viewService::showPreviousScene);
        addButton.setOnAction(e -> viewService.showPaneDialog("Добавить", buildDisciplinePane(null)));
        changeButton.setOnAction(e -> {
            DisciplineDTO dto = table.getSelectionModel().getSelectedItem();
            if (dto == null) {
                viewService.showError("Не выбран элемент");
            } else {
                viewService.showPaneDialog("Изменить", buildDisciplinePane(dto));
            }
        });
    }

    public void fillTable() {
        ObservableList<DisciplineDTO> disciplines = FXCollections.observableArrayList(disciplineService.fetch());
        table.setItems(disciplines);
    }

    private void buildTable() {
        table = new TableView<>();
        table.setPrefWidth(ViewService.WIDTH);
        table.setPrefHeight(ViewService.HEIGHT);
        final TableColumn<DisciplineDTO, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        final TableColumn<DisciplineDTO, String> categoryColumn = new TableColumn<>("Категория");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.setCellFactory(ComboBoxTableCell.forTableColumn(categories));
        final TableColumn<DisciplineDTO, Integer> hoursAmountColumn = new TableColumn<>("Кол-во часов");
        hoursAmountColumn.setCellValueFactory(new PropertyValueFactory<>("hoursAmount"));
        hoursAmountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        table.getColumns().addAll(nameColumn, categoryColumn, hoursAmountColumn);
        nameColumn.setOnEditCommit(e -> {
            DisciplineDTO dto = (DisciplineDTO) ((TableColumn.CellEditEvent) e).getRowValue();
            String newName = (String) ((TableColumn.CellEditEvent) e).getNewValue();
            if (newName.length() > 255) {
                viewService.showError(WRONG_FIELDS);
            } else {
                dto.setName(newName);
                disciplineService.save(dto);
            }
            table.refresh();
        });
        categoryColumn.setOnEditCommit(e -> {
            DisciplineDTO dto = (DisciplineDTO) ((TableColumn.CellEditEvent) e).getRowValue();
            String newCategory = (String) ((TableColumn.CellEditEvent) e).getNewValue();
            dto.setCategory(newCategory);
            try {
                disciplineService.save(dto);
            } catch (Exception ex) {
                viewService.showError(ex.getMessage());
            }
            table.refresh();
        });
        hoursAmountColumn.setOnEditCommit(e -> {
            DisciplineDTO dto = (DisciplineDTO) ((TableColumn.CellEditEvent) e).getRowValue();
            Integer newHours = (Integer) ((TableColumn.CellEditEvent) e).getNewValue();
            if (newHours < 0 || newHours > 200) {
                viewService.showError(WRONG_FIELDS);
            } else {
                dto.setHoursAmount(newHours);
                disciplineService.save(dto);
            }
            table.refresh();
        });
    }

    private VBox buildDisciplinePane(DisciplineDTO discipline) {
        VBox newDisciplinePane = new VBox();
        final TextField nameField = discipline == null ? new TextField() : new TextField(discipline.getName());
        nameField.setPromptText("Название");
        final ComboBox<String> categoryComboBox = new ComboBox<>(categories);
        final TextField hoursAmountField = discipline == null ? new TextField() : new TextField(discipline.getHoursAmount().toString());
        hoursAmountField.setPromptText("Кол-во часов");
        final Button saveButton = new Button("Сохранить");
        if (discipline == null) {
            categoryComboBox.getSelectionModel().selectFirst();
        } else {
            categoryComboBox.getSelectionModel().select(discipline.getCategory());
        }
        newDisciplinePane.getChildren().addAll(nameField, categoryComboBox, hoursAmountField, saveButton);
        saveButton.setOnAction(event -> {
            try {
                String name = nameField.getText();
                Integer hoursAmount = Integer.parseUnsignedInt(hoursAmountField.getText());
                String category = categoryComboBox.getSelectionModel().getSelectedItem();
                if (name.length() < 1 || name.length() > 255 || hoursAmount > 200 || hoursAmount < 1) {
                    viewService.showError(WRONG_FIELDS);
                } else {
                    DisciplineDTO dto = Optional.ofNullable(discipline).orElse(new DisciplineDTO());
                    dto.setName(name);
                    dto.setCategory(category);
                    dto.setHoursAmount(hoursAmount);
                    dto.setId(disciplineService.save(dto).getId());
                    if (discipline == null) {
                        table.getItems().add(dto);
                    }
                    table.refresh();
                }
            } catch (NumberFormatException | DataIntegrityViolationException ex) {
                viewService.showError(WRONG_FIELDS);
            } finally {
                nameField.setText("");
                hoursAmountField.setText("");
            }
        });
        return newDisciplinePane;
    }

    public Scene getScene() {
        return scene;
    }
}
