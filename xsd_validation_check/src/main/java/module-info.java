module com.staniskhan {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens com.staniskhan to javafx.fxml;
    exports com.staniskhan;
}
