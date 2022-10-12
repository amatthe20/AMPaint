module com.example.ampaint {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires org.junit.jupiter.api;


    opens com.example.ampaint to javafx.fxml;
    exports com.example.ampaint;
}