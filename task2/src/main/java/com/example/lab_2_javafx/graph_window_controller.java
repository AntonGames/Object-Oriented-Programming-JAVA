package com.example.lab_2_javafx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

public class graph_window_controller implements Initializable {

    a_month[] months_anuity;
    a_month[] months_linear;

    int months_count;

    XYChart.Series anuity;
    XYChart.Series linear;

    @FXML
    LineChart the_chart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        the_chart.setTitle("Anuiteto ir linijinio mokėjimo kitimai");

        anuity  = new XYChart.Series();
        anuity.setName("Anuiteto");

        linear = new XYChart.Series();
        linear.setName("Linijinis");

        the_chart.getData().addAll(anuity);
        the_chart.getData().addAll(linear);
    }

    public void init_months(a_month[] months_anuity, a_month[] months_linear){
        this.months_anuity = months_anuity;
        this.months_linear = months_linear;

        for (int i=0; i<months_count; i++) {
            anuity.getData().add(new XYChart.Data(String.valueOf(months_anuity[i].id), months_anuity[i].awp));
            linear.getData().add(new XYChart.Data(String.valueOf(months_linear[i].id), months_linear[i].awp));
        }
        the_chart.getXAxis().setTickLabelsVisible(false);
        the_chart.getXAxis().setOpacity(0);
    }

    public void get_total_months(int total_months){
        months_count = total_months;
    }
}
