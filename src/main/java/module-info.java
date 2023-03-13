module com.example.spiel_laurinwassmann {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.spiel_laurinwassmann to javafx.fxml;
    exports com.example.spiel_laurinwassmann;
}