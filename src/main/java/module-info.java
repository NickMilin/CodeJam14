module ca.mcjammers.codejam14 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ca.mcjammers.codejam14 to javafx.fxml;
    exports ca.mcjammers.codejam14;
}