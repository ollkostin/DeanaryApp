package ru.kostin.rsubd.deanary.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.DateStringConverter;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;
import ru.kostin.rsubd.deanary.persistence.model.control.Control;
import ru.kostin.rsubd.deanary.persistence.model.control.ControlType;
import ru.kostin.rsubd.deanary.service.*;
import ru.kostin.rsubd.deanary.service.dto.ControlDTO;
import ru.kostin.rsubd.deanary.service.dto.DisciplineDTO;
import ru.kostin.rsubd.deanary.service.dto.LecturerDTO;
import ru.kostin.rsubd.deanary.service.dto.StudentGroupDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ControlComponent {
    private ViewService viewService;
    private ControlService controlService;
    private DisciplineService disciplineService;
    private LecturerService lecturerService;
    private StudentGroupService groupService;
    private ObservableList<String> controlTypes, groups, disciplines, lecturers;
    private Scene controlListScene;
    private TableView<ControlDTO> table;

    @Autowired
    public ControlComponent(ViewService viewService, ControlService controlService,
                            DisciplineService disciplineService, LecturerService lecturerService,
                            StudentGroupService groupService) {
        this.viewService = viewService;
        this.controlService = controlService;
        this.disciplineService = disciplineService;
        this.lecturerService = lecturerService;
        this.groupService = groupService;
        final VBox vBox = new VBox();
        final HBox hBox = new HBox();
        final AnchorPane anchorPane = new AnchorPane();
        vBox.setPrefWidth(ViewService.WIDTH);
        vBox.setPrefHeight(ViewService.HEIGHT);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        hBox.setAlignment(Pos.CENTER);
        controlListScene = new Scene(vBox);
        final Button goToPreviousSceneButton = new Button("Назад");
        final Button addButton = new Button("Добавить");
        final Button changeButton = new Button("Изменить");
        final Button deleteButton = new Button("Удалить");
        buildTable();
        final ButtonBar bar = new ButtonBar();
        bar.getButtons().addAll(goToPreviousSceneButton, addButton, changeButton, deleteButton);
        anchorPane.getChildren().add(table);
        hBox.getChildren().addAll(bar);
        vBox.getChildren().addAll(anchorPane, hBox);
        goToPreviousSceneButton.setOnAction(viewService::showPreviousScene);
        addButton.setOnAction(e -> viewService.showPaneDialog("Добавить", buildControlPane(null)));
        deleteButton.setOnAction(event -> {
            ControlDTO dto = table.getSelectionModel().getSelectedItem();
            if (dto != null) {
                controlService.delete(dto.getId());
                table.getItems().remove(dto);
            } else {
                viewService.showError("Не выбран элемент");
            }
        });
        changeButton.setOnAction(event -> {
            ControlDTO dto = table.getSelectionModel().getSelectedItem();
            if (dto == null) {
                viewService.showError("Не выбран элемент");
            } else {
                viewService.showPaneDialog("Добавить", buildControlPane(dto));
            }
        });
    }

    public void fillTable() {
        ObservableList<ControlDTO> controlList = FXCollections.observableList(controlService.fetch());
        table.setItems(controlList);
        controlTypes = FXCollections.observableArrayList(ControlType.EXAM.name(), ControlType.TEST.name());
        groups = FXCollections.observableList(
                groupService
                        .fetch()
                        .stream()
                        .map(StudentGroupDTO::getName)
                        .collect(Collectors.toList())
        );
        disciplines = FXCollections.observableList(
                disciplineService
                        .fetch()
                        .stream()
                        .map(DisciplineDTO::getName)
                        .collect(Collectors.toList())
        );
        lecturers = FXCollections.observableList(
                lecturerService
                        .fetch()
                        .stream()
                        .map(LecturerDTO::getFullName)
                        .collect(Collectors.toList())
        );
    }

    private void buildTable() {
        table = new TableView<>();
        table.setPrefWidth(ViewService.WIDTH);
        table.setPrefHeight(ViewService.HEIGHT);
        final TableColumn<ControlDTO, String> groupColumn = new TableColumn<>("Группа");
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        groupColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        final TableColumn<ControlDTO, String> disciplineColumn = new TableColumn<>("Дисциплина");
        disciplineColumn.setCellValueFactory(new PropertyValueFactory<>("discipline"));
        disciplineColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        final TableColumn<ControlDTO, String> lecturerColumn = new TableColumn<>("Экзаменатор");
        lecturerColumn.setCellValueFactory(new PropertyValueFactory<>("lecturer"));
        lecturerColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        final TableColumn<ControlDTO, String> controlTypeColumn = new TableColumn<>("Тип контроля");
        controlTypeColumn.setCellValueFactory(new PropertyValueFactory<>("controlType"));
        controlTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        final TableColumn<ControlDTO, Date> dateColumn = new TableColumn<>("Дата");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));
        table.getColumns().addAll(groupColumn, disciplineColumn, lecturerColumn, controlTypeColumn, dateColumn);
    }

    private VBox buildControlPane(ControlDTO controlDTO) {
        VBox newControlVBox = new VBox();
        final ComboBox<String> groupBox = new ComboBox<>(groups);
        final ComboBox<String> lecturerBox = new ComboBox<>(lecturers);
        final ComboBox<String> disciplineBox = new ComboBox<>(disciplines);
        final ComboBox<String> controlTypeBox = new ComboBox<>(controlTypes);
        final DatePicker datePicker = controlDTO == null ? new DatePicker(LocalDate.now())
                : new DatePicker(controlDTO.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        final Button saveButton = new Button("Сохранить");
        if (controlDTO == null) {
            groupBox.getSelectionModel().selectFirst();
            lecturerBox.getSelectionModel().selectFirst();
            controlTypeBox.getSelectionModel().selectFirst();
            disciplineBox.getSelectionModel().selectFirst();
        } else {
            groupBox.getSelectionModel().select(controlDTO.getGroup());
            lecturerBox.getSelectionModel().select(controlDTO.getLecturer());
            controlTypeBox.getSelectionModel().select(controlDTO.getControlType());
            disciplineBox.getSelectionModel().select(controlDTO.getDiscipline());
        }
        newControlVBox.getChildren().addAll(groupBox, disciplineBox, lecturerBox, controlTypeBox, datePicker, saveButton);
        saveButton.setOnAction(e -> {
            ControlDTO dto = Optional.ofNullable(controlDTO).orElse(new ControlDTO());
            dto.setGroup(groupBox.getSelectionModel().getSelectedItem());
            dto.setDiscipline(disciplineBox.getSelectionModel().getSelectedItem());
            dto.setLecturer(lecturerBox.getSelectionModel().getSelectedItem());
            dto.setControlType(controlTypeBox.getSelectionModel().getSelectedItem());
            LocalDate localDate = datePicker.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            dto.setDate(date);
            try {
                Control control;
                if (controlDTO == null) {
                    control = controlService.save(dto);
                } else {
                    control = controlService.edit(dto);
                }
                dto.setId(control.getId());
                if (controlDTO == null) {
                    table.getItems().add(dto);
                }
                table.refresh();
            } catch (DataIntegrityViolationException ex) {
                viewService.showError("Уже существует!");
            } catch (JpaSystemException ex) {
                viewService.showError(((GenericJDBCException) ex.getCause()).getSQLException().getMessage());
            } catch (Exception ex) {
                viewService.showError(ex.getMessage());
            }
        });
        return newControlVBox;
    }

    public Scene getControlListScene() {
        return controlListScene;
    }
}
