module Discord1 {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;


    opens discord.discord1 to javafx.fxml;
    exports discord.discord1;
}