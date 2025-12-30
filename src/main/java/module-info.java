module com.example.foodorderingjdbcproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    // requires mysql.connector.j; // Removed this line

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.foodorderingjdbcproject to javafx.fxml;
    exports com.example.foodorderingjdbcproject;
}