package com.example.lab_2_javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class table_window_controller implements Initializable {

    public a_month[] months;
    int months_count;

    TableColumn<Object, Object> index_column = new TableColumn<>("Nr.");
    TableColumn<Object, Object> remaining_amount_column = new TableColumn<>("Paskolos likutis");
    TableColumn<Object, Object> credit_column = new TableColumn<>("Kreditas");
    TableColumn<Object, Object> interest_column = new TableColumn<>("Palūkanos");
    TableColumn<Object, Object> payment_column = new TableColumn<>("Bendra mėn. įmoka");

    @FXML
    TableView<Object> the_table;

    @FXML
    TextField filter_from_field;

    @FXML
    TextField filter_to_field;

    @FXML
    TextField postpone_from_field;

    @FXML
    TextField postpone_to_field;

    @FXML
    TextField postpone_percentage;

    ObservableList<a_month> full_list = FXCollections.observableArrayList();
    ObservableList<a_month> filtered_list = FXCollections.observableArrayList();

    @FXML
    public void postpone_manager(){

        for (int i=0; i<months_count; i++){
            // monthly payments will be postponed
            if (months[i].id >= Integer.parseInt(postpone_from_field.getText()) && months[i].id <= Integer.parseInt(postpone_to_field.getText())){
                months[i].awp = months[i-1].r * (months[i].p / 100);
                months[i].awp = round(months[i].awp, 2);
                months[i].i = 0;
                months[i].c = 0;
                the_table.getItems().set(i, months[i]);
            }
        }

    }

    @FXML
    public void filter_manager(){
        if (Objects.equals(filter_from_field.getText(), "") && Objects.equals(filter_to_field.getText(), "")){
            filtered_list.clear();
            the_table.getItems().clear();
            the_table.getItems().addAll(full_list);
        } else {
            full_list.clear();
            for (int i = 0; i < months_count; i++) {
                full_list.add(months[i]);
                if (months[i].id >= Integer.parseInt(filter_from_field.getText()) && months[i].id <= Integer.parseInt(filter_to_field.getText())) {
                    filtered_list.add(months[i]);
                }
            }
            the_table.getItems().clear();
            the_table.getItems().addAll(filtered_list);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {    // sukuriama lenteles stulpeliai
        index_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        remaining_amount_column.setCellValueFactory(new PropertyValueFactory<>("r"));
        credit_column.setCellValueFactory(new PropertyValueFactory<>("c"));
        interest_column.setCellValueFactory(new PropertyValueFactory<>("i"));
        payment_column.setCellValueFactory(new PropertyValueFactory<>("awp"));

        the_table.getColumns().addAll(index_column, remaining_amount_column, credit_column, interest_column, payment_column);
    }

    public void init_months(a_month[] months, int months_count){    // initializuojamas menesis
        this.months = new a_month[months_count];
        this.months_count = months_count;

        System.arraycopy(months, 0, this.months, 0, months_count);

        System.out.println(months_count + " months are ready to be added to the table.");

        for (int i=0; i<months_count; i++){
            the_table.getItems().add(this.months[i]);
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}