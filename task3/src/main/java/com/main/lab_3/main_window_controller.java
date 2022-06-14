package com.main.lab_3;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class main_window_controller {

    a_group[] groups = new a_group[10];
    int group_id = 0;

    @FXML
    private TabPane tab_pane;

    @FXML
    public Button edit_student_btn;

    @FXML
    public Button save_student_btn;

    @FXML
    public TextField new_name_group;

    @FXML
    private void close_tab(){
        tab_pane.getTabs().remove(tab_pane.getSelectionModel().getSelectedIndex());
    }

    @FXML
    public void create_tab(){
        int groups_count = tab_pane.getTabs().size() + 1;
        Tab tab = new Tab(groups_count + " Grupė");
        tab_pane.getTabs().add(tab);

        groups[group_id] = new a_group(tab);
        groups[group_id].name_group = tab.getText();
        group_id++;
    }

    @FXML
    public void add_a_student(){
        groups[tab_pane.getSelectionModel().getSelectedIndex()].create_student();
    }

    @FXML
    private void editing_student(){
        groups[tab_pane.getSelectionModel().getSelectedIndex()].edit_student_manager();
    }

    @FXML
    private void file_extractor() throws IOException {
        Stage stage = (Stage) edit_student_btn.getScene().getWindow();
        FileChooser f_chooser = new FileChooser();
        f_chooser.setTitle("Pasirinkite studentų failą");
        //f_chooser.showOpenDialog(stage);
        File file = f_chooser.showOpenDialog(stage);
        System.out.println("Selected file: " + file.getAbsolutePath());
        groups[tab_pane.getSelectionModel().getSelectedIndex()].file_extractor(file.getAbsolutePath());
    }

    @FXML
    private void file_writter() throws IOException {
        groups[tab_pane.getSelectionModel().getSelectedIndex()].file_writter();
    }
}