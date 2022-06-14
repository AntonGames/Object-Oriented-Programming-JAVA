package com.main.lab_3;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public class a_group extends group_functions implements extract_create {

    public static final int STUDENTS_LIMITS = 50;

    public a_student[] students = new a_student[STUDENTS_LIMITS];

    int stud_id = 0;
    int current_id;

    public String name_group;

    Pane pane;
    TableView<Object> tv;
    Label name;
    TextField name_field;
    Label surname;
    TextField surname_field;
    Label year;
    TextField year_field;
    Label attendance;
    TextField days_attended;

    Button edit_student_btn;
    Button delete_student_btn;

    Label filter_from;
    TextField from_field;
    Label filter_to;
    TextField to_field;
    Button filter_btn;

    TableColumn<Object, Object> index_column = new TableColumn<>("Nr.");
    TableColumn<Object, Object> name_column = new TableColumn<>("Vardas, pavardė");
    TableColumn<Object, Object> year_column = new TableColumn<>("Gimimo metai");
    TableColumn<Object, Object> attendance_column = new TableColumn<>("Lankomumas");

    ObservableList<a_student> full_list = FXCollections.observableArrayList();
    ObservableList<a_student> filtered_list = FXCollections.observableArrayList();

    public a_group(Tab tab){
        graphics_init();
        tab.setContent(pane);

        index_column.setCellValueFactory(new PropertyValueFactory<>("index"));
        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        year_column.setCellValueFactory(new PropertyValueFactory<>("year"));
        attendance_column.setCellValueFactory(new PropertyValueFactory<>("attendance_string"));

        tv.getColumns().addAll(index_column, name_column, year_column, attendance_column);
    }

    void graphics_init(){
        pane = new Pane();
        pane.setPrefWidth(600);
        pane.setPrefHeight(488);
        pane.setMinWidth(0);
        pane.setMinHeight(0);
        pane.setMaxWidth(Region.USE_COMPUTED_SIZE);
        pane.setMaxHeight(Region.USE_COMPUTED_SIZE);

        tv = new TableView<>();
        tv.setPrefWidth(600);
        tv.setPrefHeight(250);
        tv.setLayoutY(-2);
        pane.getChildren().add(tv);

        name = new Label();
        name.setText("Vardas: ");
        name.setFont(Font.font("Calibri", 17));
        name.setPrefWidth(90);
        name.setPrefHeight(25);
        name.setMaxWidth(Region.USE_COMPUTED_SIZE);
        name.setMaxHeight(Region.USE_COMPUTED_SIZE);
        name.setLayoutX(11);
        name.setLayoutY(288);
        pane.getChildren().add(name);

        name_field = new TextField();
        name_field.setMinWidth(Region.USE_COMPUTED_SIZE);
        name_field.setMinHeight(Region.USE_COMPUTED_SIZE);
        name_field.setPrefWidth(95);
        name_field.setPrefHeight(25);
        name_field.setMaxWidth(Region.USE_COMPUTED_SIZE);
        name_field.setMaxHeight(Region.USE_COMPUTED_SIZE);
        name_field.setLayoutX(66);
        name_field.setLayoutY(288);
        name_field.setId("student_name");
        pane.getChildren().add(name_field);

        surname = new Label();
        surname.setText("Pavardė: ");
        surname.setFont(Font.font("Calibri", 17));
        surname.setPrefWidth(90);
        surname.setPrefHeight(25);
        surname.setMinWidth(Region.USE_COMPUTED_SIZE);
        surname.setMinHeight(Region.USE_COMPUTED_SIZE);
        surname.setMaxWidth(Region.USE_COMPUTED_SIZE);
        surname.setMaxHeight(Region.USE_COMPUTED_SIZE);
        surname.setLayoutX(168);
        surname.setLayoutY(288);
        pane.getChildren().add(surname);

        surname_field = new TextField();
        surname_field.setMinWidth(Region.USE_COMPUTED_SIZE);
        surname_field.setMinHeight(Region.USE_COMPUTED_SIZE);
        surname_field.setPrefWidth(120);
        surname_field.setPrefHeight(25);
        surname_field.setMaxWidth(Region.USE_COMPUTED_SIZE);
        surname_field.setMaxHeight(Region.USE_COMPUTED_SIZE);
        surname_field.setLayoutX(235);
        surname_field.setLayoutY(288);
        pane.getChildren().add(surname_field);

        year = new Label();
        year.setText("Gimimo metai: ");
        year.setFont(Font.font("Calibri", 17));
        year.setMinWidth(Region.USE_COMPUTED_SIZE);
        year.setMinHeight(Region.USE_COMPUTED_SIZE);
        year.setPrefWidth(116);
        year.setPrefHeight(25);
        year.maxWidth(Region.USE_COMPUTED_SIZE);
        year.maxHeight(Region.USE_COMPUTED_SIZE);
        year.setLayoutX(361);
        year.setLayoutY(288);
        pane.getChildren().add(year);

        year_field = new TextField();
        year_field.setMinWidth(Region.USE_COMPUTED_SIZE);
        year_field.setMinHeight(Region.USE_COMPUTED_SIZE);
        year_field.setPrefWidth(120);
        year_field.setPrefHeight(25);
        year_field.setMaxWidth(Region.USE_COMPUTED_SIZE);
        year_field.setMaxHeight(Region.USE_COMPUTED_SIZE);
        year_field.setLayoutX(470);
        year_field.setLayoutY(288);
        pane.getChildren().add(year_field);

        attendance = new Label();
        attendance.setText("Lankomumas: ");
        attendance.setFont(Font.font("Calibri", 17));
        attendance.setMinWidth(Region.USE_COMPUTED_SIZE);
        attendance.setMinHeight(Region.USE_COMPUTED_SIZE);
        attendance.setPrefWidth(104);
        attendance.setPrefHeight(25);
        attendance.setMaxWidth(Region.USE_COMPUTED_SIZE);
        attendance.setMaxHeight(Region.USE_COMPUTED_SIZE);
        attendance.setLayoutX(14);
        attendance.setLayoutY(255);
        pane.getChildren().add(attendance);

        days_attended = new TextField();
        days_attended.setMinWidth(Region.USE_COMPUTED_SIZE);
        days_attended.setMinHeight(Region.USE_COMPUTED_SIZE);
        days_attended.setPrefWidth(467);
        days_attended.setPrefHeight(25);
        days_attended.setMaxWidth(Region.USE_COMPUTED_SIZE);
        days_attended.setMaxHeight(Region.USE_COMPUTED_SIZE);
        days_attended.setLayoutX(118);
        days_attended.setLayoutY(255);
        pane.getChildren().add(days_attended);

        edit_student_btn = new Button("Redaguoti");
        edit_student_btn.minWidth(Region.USE_COMPUTED_SIZE);
        edit_student_btn.minHeight(Region.USE_COMPUTED_SIZE);
        edit_student_btn.prefWidth(200);
        edit_student_btn.prefHeight(25);
        edit_student_btn.maxWidth(Region.USE_COMPUTED_SIZE);
        edit_student_btn.maxHeight(Region.USE_COMPUTED_SIZE);
        edit_student_btn.setLayoutX(14);
        edit_student_btn.setLayoutY(319);
        edit_student_btn.setOnAction(actionEvent -> {
            edit_student_data(tv.getSelectionModel().getSelectedIndex());
        });
        pane.getChildren().add(edit_student_btn);

        delete_student_btn = new Button("Ištrinti");
        delete_student_btn.minWidth(Region.USE_COMPUTED_SIZE);
        delete_student_btn.minHeight(Region.USE_COMPUTED_SIZE);
        delete_student_btn.prefWidth(200);
        delete_student_btn.prefHeight(25);
        delete_student_btn.maxWidth(Region.USE_COMPUTED_SIZE);
        delete_student_btn.maxHeight(Region.USE_COMPUTED_SIZE);
        delete_student_btn.setLayoutX(90);
        delete_student_btn.setLayoutY(319);
        delete_student_btn.setOnAction(actionEvent -> {
            tv.getItems().remove(tv.getSelectionModel().getSelectedItem());
            stud_id--;
        });
        pane.getChildren().add(delete_student_btn);

        filter_from = new Label();
        filter_from.setText("Nuo:");
        filter_from.setFont(Font.font("Calibri", 17));
        filter_from.setMinWidth(Region.USE_COMPUTED_SIZE);
        filter_from.setMinHeight(Region.USE_COMPUTED_SIZE);
        filter_from.setPrefWidth(104);
        filter_from.setPrefHeight(25);
        filter_from.setMaxWidth(Region.USE_COMPUTED_SIZE);
        filter_from.setMaxHeight(Region.USE_COMPUTED_SIZE);
        filter_from.setLayoutX(365);
        filter_from.setLayoutY(319);
        pane.getChildren().add(filter_from);

        from_field = new TextField();
        from_field.setMinWidth(Region.USE_COMPUTED_SIZE);
        from_field.setMinHeight(Region.USE_COMPUTED_SIZE);
        from_field.setPrefWidth(30);
        from_field.setPrefHeight(25);
        from_field.setMaxWidth(Region.USE_COMPUTED_SIZE);
        from_field.setMaxHeight(Region.USE_COMPUTED_SIZE);
        from_field.setLayoutX(410);
        from_field.setLayoutY(319);
        pane.getChildren().add(from_field);

        filter_to = new Label();
        filter_to.setText("Iki:");
        filter_to.setFont(Font.font("Calibri", 17));
        filter_to.setMinWidth(Region.USE_COMPUTED_SIZE);
        filter_to.setMinHeight(Region.USE_COMPUTED_SIZE);
        filter_to.setPrefWidth(104);
        filter_to.setPrefHeight(25);
        filter_to.setMaxWidth(Region.USE_COMPUTED_SIZE);
        filter_to.setMaxHeight(Region.USE_COMPUTED_SIZE);
        filter_to.setLayoutX(450);
        filter_to.setLayoutY(319);
        pane.getChildren().add(filter_to);

        to_field = new TextField();
        to_field.setMinWidth(Region.USE_COMPUTED_SIZE);
        to_field.setMinHeight(Region.USE_COMPUTED_SIZE);
        to_field.setPrefWidth(30);
        to_field.setPrefHeight(25);
        to_field.setMaxWidth(Region.USE_COMPUTED_SIZE);
        to_field.setMaxHeight(Region.USE_COMPUTED_SIZE);
        to_field.setLayoutX(480);
        to_field.setLayoutY(319);
        pane.getChildren().add(to_field);

        filter_btn = new Button("Filtruoti");
        filter_btn.minWidth(Region.USE_COMPUTED_SIZE);
        filter_btn.minHeight(Region.USE_COMPUTED_SIZE);
        filter_btn.prefWidth(200);
        filter_btn.prefHeight(25);
        filter_btn.maxWidth(Region.USE_COMPUTED_SIZE);
        filter_btn.maxHeight(Region.USE_COMPUTED_SIZE);
        filter_btn.setLayoutX(532);
        filter_btn.setLayoutY(319);
        filter_btn.setOnAction(actionEvent -> {
            if (Objects.equals(from_field.getText(), "") && Objects.equals(to_field.getText(), ""))
                remove_filter();
            else
                filter_manager();
        });
        pane.getChildren().add(filter_btn);
    }

    public void edit_student_data(int student_index) {
        name_field.setText(students[student_index].name2);
        surname_field.setText(students[student_index].surname);
        year_field.setText(students[student_index].year);
        days_attended.setText(students[student_index].attendance_string);
        current_id = student_index;
}

    public void edit_student_manager(){
        students[current_id].name = name_field.getText() + " " + surname_field.getText();
        students[current_id].year = year_field.getText();
        students[current_id].attendance_string = days_attended.getText();

        students[current_id].fill_attendance();
        System.out.println(Arrays.toString(students[current_id].attendance));

        tv.getItems().set(current_id, students[current_id]);
        name_field.setText("");
        surname_field.setText("");
        year_field.setText("");
        days_attended.setText("");
    }

    @Override
    public void file_extractor(String file_path) throws IOException {
        File file = new File(file_path);
        FileReader f_reader = new FileReader(file);
        BufferedReader b_reader = new BufferedReader(f_reader);
        String line;
        while ((line = b_reader.readLine()) != null){
            String[] s = string_splitting(line, ',');
            create_custom_student(s[0], s[1], s[2]);
        }
        b_reader.close();
    }

    @Override
    public void file_writter() throws IOException {
        File csv_file = new File("data/" + name_group + ".csv");
        PrintWriter f_writer = new PrintWriter(csv_file);
        StringBuilder s_builder = new StringBuilder();
        for (int i=0; i<stud_id; i++){
            s_builder.append(students[i].name);
            s_builder.append(",");
            s_builder.append(students[i].year);
            s_builder.append(",");
            s_builder.append(students[i].attendance_string);
            s_builder.append(",");
            f_writer.println(s_builder);
            s_builder.setLength(0);
        }
        f_writer.close();
    }

    public void create_custom_student(String name, String year_of_birth, String attendance) {   // redaguojama
        students[stud_id] = new a_student(name, year_of_birth, stud_id+1);

        tv.getItems().add(students[stud_id]);

        students[stud_id].name2 = name_field.getText();
        students[stud_id].surname = surname_field.getText();
        students[stud_id].attendance_string = attendance;

        students[stud_id].fill_attendance();
        System.out.println(Arrays.toString(students[stud_id].attendance));

        stud_id++;

        name_field.setText("");
        surname_field.setText("");
        year_field.setText("");
        days_attended.setText("");
    }

    void filter_manager() {
        if (Objects.equals(from_field.getText(), to_field.getText())){  // filter by one day
            full_list.clear();
            for (int i=0; i<stud_id; i++){
                full_list.add(students[i]);
                for (int j=0; j<31; j++){
                    if (students[i].attendance[j] == Integer.parseInt(from_field.getText())){
                        filtered_list.add(students[i]);
                        System.out.println(students[i].getName());
                    }
                }
            }

            tv.getItems().clear();
            tv.getItems().addAll(filtered_list);

        } else{ // filter by a period
            full_list.clear();
            int period = Integer.parseInt(to_field.getText()) - Integer.parseInt(from_field.getText());
            int period_day = Integer.parseInt(from_field.getText());
            for (int i=0; i<stud_id; i++){
                full_list.add(students[i]);
                for (int j=0; j<31; j++){

                    for (int e=0; e<period; e++){
                        if (students[i].attendance[j] == period_day){
                            filtered_list.add(students[i]);
                            System.out.println(students[i].getName());
                            period_day++;
                        }
                    }
                }
            }
            tv.getItems().clear();
            tv.getItems().addAll(filtered_list);
        }
    }

    @Override
    void remove_filter() {
        filtered_list.clear();
        tv.getItems().clear();
        tv.getItems().addAll(full_list);
    }

    public void create_student() {
        students[stud_id] = new a_student(name_field.getText() + " " + surname_field.getText(), year_field.getText(), stud_id+1);
        tv.getItems().add(students[stud_id]);

        students[stud_id].name2 = name_field.getText();
        students[stud_id].surname = surname_field.getText();
        students[stud_id].attendance_string = days_attended.getText();

        students[stud_id].fill_attendance();
        System.out.println(Arrays.toString(students[stud_id].attendance));

        stud_id++;

        name_field.setText("");
        surname_field.setText("");
        year_field.setText("");
        days_attended.setText("");
    }
}