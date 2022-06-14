module com.main.lab_3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.main.lab_3 to javafx.fxml;
    exports com.main.lab_3;
}