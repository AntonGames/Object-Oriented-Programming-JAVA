package com.example.lab_2_javafx;

import brains.months_manager;
import brains.report_manager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;

public class first_window_controller extends months_manager implements Initializable{

    @FXML
    public ComboBox<String> month_choice;

    @FXML
    public ComboBox<String> graph_choice;

    @FXML
    public Button calculate_button;

    @FXML
    public TextField wanted_sum_field;

    @FXML
    public TextField year_field;

    @FXML
    public TextField percentage_field;

    @FXML
    public void calculate_button(){
        super.total_months = get_total_months();

        super.remaining_amount_a = Integer.parseInt(wanted_sum_field.getText());
        super.percentage_a = Double.parseDouble(percentage_field.getText());

        super.remaining_amount_l = Integer.parseInt(wanted_sum_field.getText());
        super.percentage_l = Double.parseDouble(percentage_field.getText());

        switch (graph_choice.getSelectionModel().getSelectedItem()){
            case "Anuiteto" -> calculate_by_annuity();
            case "Linijinis" -> calculate_by_linear();
        }

        Stage table_stage = new Stage();
        Stage graph_stage = new Stage();
        try {
            FXMLLoader fxmlLoader_table = new FXMLLoader();
            fxmlLoader_table.setLocation(getClass().getResource("table_window.fxml"));
            Scene scene_window = new Scene(fxmlLoader_table.load());
            table_stage.setTitle("Būsto paskolos skaičiuoklė (lab_2)");
            table_stage.setScene(scene_window);
            table_stage.setResizable(false);
            table_stage.show();

            table_window_controller children_table = fxmlLoader_table.getController();

            switch (graph_choice.getSelectionModel().getSelectedItem()){
                case "Anuiteto" -> children_table.init_months(months_annuity, get_total_months());
                case "Linijinis" -> children_table.init_months(months_linear, get_total_months());
            }

            FXMLLoader fxmlLoader_graph = new FXMLLoader();
            fxmlLoader_graph.setLocation(getClass().getResource("graph_window.fxml"));
            Scene scene_graph = new Scene(fxmlLoader_graph.load());
            graph_stage.setTitle("Būsto paskolos skaičiuoklė (lab_2)");
            graph_stage.setScene(scene_graph);
            graph_stage.setResizable(false);
            graph_stage.show();

            graph_window_controller children_graph = fxmlLoader_graph.getController();
            children_graph.get_total_months(get_total_months());
            children_graph.init_months(months_annuity, months_linear);

            Stage current_stage = (Stage) calculate_button.getScene().getWindow();
            current_stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void calculate_by_annuity(){
        create_months(get_total_months());
        report_manager r_m = new report_manager(months_annuity);
        r_m.write_message();
    }

    public void calculate_by_linear(){
        create_months(get_total_months());
        report_manager r_m = new report_manager(months_linear);
        r_m.write_message();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        month_choice.getItems().addAll(
                "Sausis",
                "Vasaris",
                "Kovas",
                "Balandis",
                "Gegužė",
                "Birželis",
                "Liepa",
                "Rugpjūtis",
                "Rugsėjis",
                "Spalis",
                "lapkritis",
                "Gruodis");

        graph_choice.getItems().addAll("Anuiteto", "Linijinis");
    }

    public int get_total_months(){  // funkcija grazina kiek menesiu bus mokama suma
        LocalDate current_date = LocalDate.now();

        int current_year = current_date.getYear();
        Month current_month = current_date.getMonth();
        int current_month_nr=0;

        int months=0;

        switch(current_month){
            case JANUARY -> current_month_nr = 1;
            case FEBRUARY -> current_month_nr = 2;
            case MARCH -> current_month_nr = 3;
            case APRIL -> current_month_nr = 4;
            case MAY -> current_month_nr = 5;
            case JUNE -> current_month_nr = 6;
            case JULY -> current_month_nr = 7;
            case AUGUST -> current_month_nr = 8;
            case SEPTEMBER -> current_month_nr = 9;
            case OCTOBER -> current_month_nr = 10;
            case NOVEMBER -> current_month_nr = 11;
            case DECEMBER -> current_month_nr = 12;
        }

        int count_to_add = 0;

        int current_month_copy = current_month_nr;

        while (current_month_copy != 12){
            count_to_add++;
            current_month_copy++;
        }

        switch (month_choice.getSelectionModel().getSelectedItem()){
            case "Sausis" -> months = 1;
            case "Vasaris" -> months = 2;
            case "Kovas" -> months = 3;
            case "Balandis" -> months = 4;
            case "Gegužė" -> months = 5;
            case "Birželis" -> months = 6;
            case "Liepa" -> months = 7;
            case "Rugpjūtis" -> months = 8;
            case "Rugsėjis" -> months = 9;
            case "Spalis" -> months = 10;
            case "Lapkritis" -> months = 11;
            case "Gruodis" -> months = 12;
        }

        if (String.valueOf(current_year).equals(year_field.getText()))
            return months - current_month_nr;
        else if (String.valueOf(current_year + 1).equals(year_field.getText()))
            return count_to_add + months;
        else
            return (Integer.parseInt(year_field.getText()) - current_year) * 12 + count_to_add + months - 12;
    }
}