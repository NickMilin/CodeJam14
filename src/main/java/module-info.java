module ca.mcjammers.codejam14 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires json.simple;


    opens ca.mcjammers.codejam14 to javafx.fxml;
    exports ca.mcjammers.codejam14;
}