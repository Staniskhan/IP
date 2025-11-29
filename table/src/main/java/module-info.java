module com.staniskhan {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.staniskhan to javafx.fxml;
    exports com.staniskhan;
}
