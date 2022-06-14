module com.example.lab_2_javafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lab_2_javafx to javafx.fxml;
    exports com.example.lab_2_javafx;
}