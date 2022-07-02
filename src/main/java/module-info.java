module com.example.discordfull {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.discordfull to javafx.fxml;
    exports com.example.discordfull;
}